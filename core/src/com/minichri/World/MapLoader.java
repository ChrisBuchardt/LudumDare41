package com.minichri.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.DirectionalTile;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.MainGame;
import com.minichri.entity.Player;
import com.minichri.entity.RenderableObject;
import com.minichri.helpers.TileType;
import com.minichri.screens.GameScreen;

import java.util.ArrayList;

public class MapLoader {

    private ArrayList<RenderableObject> tilesList;
    private Player player;
    private World world;

    /** Loads an image.
     * @param levelImageLocation a path to a level image.
     * @param screen the world where the elements will be spawned in. */
    public void loadLevelFromImage(String levelImageLocation, GameScreen screen){

        this.world = screen.world;
        tilesList = new ArrayList<>();

        //Load map image and create pixmap containing the pixels
        Texture levelTexture = new Texture(levelImageLocation);
        TextureData tempData = levelTexture.getTextureData();
        tempData.prepare();
        Pixmap levelPixmap = levelTexture.getTextureData().consumePixmap();

        //Coordinates
        Vector2 currentTilePos;

        //Goes through all pixels, analyses and converts the colored pixel to tiles
        for(int y = 0; y < levelPixmap.getHeight(); y++) {
            for (int x = 0; x < levelPixmap.getWidth(); x++) {

                //Calculate tiles coordinates
                currentTilePos = new Vector2(x, levelPixmap.getHeight() - y);

                //Get a color
                Color color = new Color();
                Color.rgba8888ToColor(color, levelPixmap.getPixel(x, y));

                //Check if color matches a type
                TileType currentTileType = TileType.getTypeFromColor(color);

                //Create an element if color was found
                if(currentTileType == TileType.WHITE_SPACE) { //White-space = do nothing
                    continue;
                } else if(currentTileType == TileType.PLAYER){

                    this.player = new Player(world, currentTilePos);

                }else if(currentTileType.isResourceTile()){ //Resources

                    this.tilesList.add(new Resource(world, currentTileType, currentTilePos));

                }else if(currentTileType != null){ //Add tile based on tileType

                    if(currentTileType.isDirectionalTile()){ //Is the tile directional?

                        //Get tiletypes of surrounding tiles
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x, y-1));
                        TileType aboveTileType = TileType.getTypeFromColor(color);
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x-1, y));
                        TileType leftTileType = TileType.getTypeFromColor(color);
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x+1, y));
                        TileType rightTileType = TileType.getTypeFromColor(color);

                        //Surrounding checks
                        boolean isTileAboveTheSame = currentTileType == aboveTileType;//Is the block above the same as this?
                        boolean isTileLeftTheSame = currentTileType == leftTileType;//Is the block to the left the same?
                        boolean isTileRightTheSame = currentTileType == rightTileType;//Is the block to the right the same?

                        if(isTileAboveTheSame){ //Under block
                            this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.UNDER, currentTileType, currentTilePos));
                        }else if(aboveTileType == TileType.WHITE_SPACE){ //Above free

                            if(isTileLeftTheSame && isTileRightTheSame){ //Both sides free
                                this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.MIDDLE, currentTileType, currentTilePos));
                            }else if(leftTileType == TileType.WHITE_SPACE){ //Left is free
                                if(rightTileType == TileType.WHITE_SPACE) //Is right also free
                                    this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.MIDDLE, currentTileType, currentTilePos));
                                else
                                    this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.LEFT, currentTileType, currentTilePos));
                            }else if(rightTileType == TileType.WHITE_SPACE){ //Right is free
                                this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.RIGHT, currentTileType, currentTilePos));
                            }else
                                this.tilesList.add(new DirectionalTile(world, TileType.TextureDirection.MIDDLE, currentTileType, currentTilePos));
                        }

                    }else{
                        this.tilesList.add(new Tile(world, currentTileType, currentTilePos));
                    }
                }
            }
        }

        //Dispose data
        levelTexture.dispose();
        tempData.disposePixmap();
        levelPixmap.dispose();
    }

    public ArrayList<RenderableObject> getTilesList() {
        return tilesList;
    }

    public Player getPlayer() {
        return player;
    }
}
