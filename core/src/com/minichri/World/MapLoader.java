package com.minichri.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.DirectionalTile;
import com.minichri.Elements.Tile;
import com.minichri.entity.Player;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.TileType;

import java.util.ArrayList;

public class MapLoader {

    private ArrayList<TextureObject> tilesList;
    private int playerIndex;

    /** Loads an image.
     * @param levelImageLocation a path to a level image.
     * @param world the world where the elements will be spawned in. */
    public void loadLevelFromImage(String levelImageLocation, World world){

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
                currentTilePos = new Vector2(x * GameInfo.TILE_SIZE, (levelPixmap.getHeight() - y) * GameInfo.TILE_SIZE);

                //Get a color
                Color color = new Color();
                Color.argb8888ToColor(color, levelPixmap.getPixel(x, y));

                //Check if color matches a type
                TileType currentTileType = TileType.getTypeFromColor(color);


                //Create an element if color was found
                if(currentTileType == TileType.WHITE_SPACE) { //Do nothing
                    continue;
                }
                else if(currentTileType == TileType.PLAYER){
                    this.tilesList.add(new Player(world, currentTilePos));
                    this.playerIndex = tilesList.size()-1;
                }else if(currentTileType != null){ //Add tile based on tileType

                    if(currentTileType.isDirectionalTile()){ //Is the tile directional?

                        //Get tiletypes of surrounding tiles
                        Color.argb8888ToColor(color, levelPixmap.getPixel(x, y-1));
                        TileType aboveTileType = TileType.getTypeFromColor(color);
                        Color.argb8888ToColor(color, levelPixmap.getPixel(x-1, y));
                        TileType leftTileType = TileType.getTypeFromColor(color);
                        Color.argb8888ToColor(color, levelPixmap.getPixel(x+1, y));
                        TileType rightTileType = TileType.getTypeFromColor(color);


                        boolean isTileAboveTheSame = currentTileType == aboveTileType;//Is the block above the same as this?
                        boolean isTileLeftTheSame = currentTileType == leftTileType;//Is the block to the left the same?
                        boolean isTileRightTheSame = currentTileType == rightTileType;//Is the block to the right the same?

                        if(isTileAboveTheSame){ //Under block
                            this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.UNDER, currentTileType, currentTilePos));
                        }else if(aboveTileType == TileType.WHITE_SPACE){ //Above free

                            if(isTileLeftTheSame && isTileRightTheSame){ //Both sides free
                                this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.MIDDLE, currentTileType, currentTilePos));
                            }else if(leftTileType == TileType.WHITE_SPACE){ //Left is free
                                if(rightTileType == TileType.WHITE_SPACE) //Is right also free
                                    this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.MIDDLE, currentTileType, currentTilePos));
                                else
                                    this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.LEFT, currentTileType, currentTilePos));
                            }else if(rightTileType == TileType.WHITE_SPACE){ //Right is free
                                if(leftTileType == TileType.WHITE_SPACE) //Is Left also free
                                    this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.MIDDLE, currentTileType, currentTilePos));
                                else
                                    this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.RIGHT, currentTileType, currentTilePos));
                            }else
                                this.tilesList.add(new DirectionalTile(world, TileType.TileTextureDirection.MIDDLE, currentTileType, currentTilePos)); //TODO maybe something else?
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

    public ArrayList<TextureObject> getTilesList() {
        return tilesList;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
