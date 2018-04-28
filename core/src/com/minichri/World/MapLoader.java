package com.minichri.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.*;
import com.minichri.entity.Player;
import com.minichri.entity.RenderObject;
import com.minichri.helpers.TileType;

import java.util.ArrayList;

public class MapLoader {

    private Tile[][] tileArray;
    private ArrayList<RenderObject> tilesList;
    private ArrayList<Decoration> decorationList;
    private Player player;
    private World world;
    private int mapTileSizeX;
    private int mapTileSizeY;
    private int numberOfResources;

    /** Loads an image.
     * @param levelImageLocation a path to a level image.
     * @param world the world where the elements will be spawned in. */
    public void loadLevelFromImage(String levelImageLocation, World world){

        this.world = world;
        tilesList = new ArrayList<>();
        this.decorationList = new ArrayList<>();
        numberOfResources = 0;
        //Load map image and create pixmap containing the pixels
        Texture levelTexture = new Texture(levelImageLocation);
        TextureData tempData = levelTexture.getTextureData();
        tempData.prepare();
        Pixmap levelPixmap = levelTexture.getTextureData().consumePixmap();

        //Save map size
        this.mapTileSizeX = levelPixmap.getWidth();
        this.mapTileSizeY = levelPixmap.getHeight();

        //Init tile array
        this.tileArray = new Tile[mapTileSizeX][mapTileSizeY];

        //Coordinates
        Vector2 currentTilePos;

        //Goes through all pixels, analyses and converts the colored pixel to tiles
        for(int y = 0; y < levelPixmap.getHeight(); y++) {
            for (int x = 0; x < levelPixmap.getWidth(); x++) {

                //Calculate tiles coordinates
                currentTilePos = new Vector2(x, levelPixmap.getHeight() - (y + 1));

                //Get a color
                Color color = new Color();
                Color.rgba8888ToColor(color, levelPixmap.getPixel(x, y));

                //Check if color matches a type
                TileType currentTileType = TileType.getTypeFromColor(color);

                //Create an element if color was found

                if (currentTileType == TileType.WHITE_SPACE || currentTileType == null) { //White-space = do nothing

                    tileArray[x][this.mapTileSizeY - (y + 1)] = null;

                } else if (currentTileType == TileType.PLAYER) {

                    this.player = new Player(world, currentTilePos);

                } else if (currentTileType.isResourceTile()) { //Resources

                    this.tilesList.add(new Resource(world, currentTileType, currentTilePos));
                    numberOfResources++;

                } else if (currentTileType == TileType.SPIKES) {

                    RenderObject spike = new Spikes(world, currentTilePos);
                    this.tilesList.add(spike);

                } else if(currentTileType == TileType.PLANT || currentTileType == TileType.MULTIPLEPLANTS || currentTileType == TileType.RANDOMDECORATION) {

                    this.decorationList.add(new Decoration(currentTileType, currentTilePos));

                } else { //Add tile based on tileType

                    Tile tile;

                    if (!currentTileType.isDirectionalTile()) {
                        tile = new Tile(world, currentTileType, currentTilePos);

                    } else { //Is the tile directional?

                        //Get tiletypes of surrounding tiles
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x, y-1));
                        TileType aboveTileType = TileType.getTypeFromColor(color);
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x-1, y));
                        TileType leftTileType = TileType.getTypeFromColor(color);
                        Color.rgba8888ToColor(color, levelPixmap.getPixel(x+1, y));
                        TileType rightTileType = TileType.getTypeFromColor(color);

                        TileType.TextureDirection direction = createDirectionalTile(currentTileType, aboveTileType, leftTileType, rightTileType);
                        tile = new DirectionalTile(world, direction, currentTileType, currentTilePos);
                    }

                    this.tilesList.add(tile);
                    tileArray[x][this.mapTileSizeY - (y + 1)] = tile;
                }
            }
        }

        //Dispose data
        levelTexture.dispose();
        tempData.disposePixmap();
        levelPixmap.dispose();
        Player.getInventory().setTotalResources(numberOfResources);
    }

    private TileType.TextureDirection createDirectionalTile(TileType currentTileType, TileType above, TileType left, TileType right) {

        //Surrounding checks
        boolean isTileAboveTheSame = currentTileType == above;//Is the block above the same as this?
        boolean isTileLeftTheSame = currentTileType == left;//Is the block to the left the same?
        boolean isTileRightTheSame = currentTileType == right;//Is the block to the right the same?

        if (isTileAboveTheSame) { //Under block
            return TileType.TextureDirection.UNDER;
        } else if (above == TileType.WHITE_SPACE) { //Above free

            if (isTileLeftTheSame && isTileRightTheSame) { //Both sides free
                return TileType.TextureDirection.MIDDLE;
            } else if(left == TileType.WHITE_SPACE) { //Left is free
                if(right == TileType.WHITE_SPACE) //Is right also free
                    return TileType.TextureDirection.MIDDLE;
                else
                    return TileType.TextureDirection.LEFT;
            } else if(right == TileType.WHITE_SPACE) { //Right is free
                return TileType.TextureDirection.RIGHT;
            } else {
                return TileType.TextureDirection.MIDDLE;
            }
        } else {
            return TileType.TextureDirection.MIDDLE;
        }
    }

    public ArrayList<RenderObject> getTilesList() {
        return tilesList;
    }

    public ArrayList<Decoration> getDecorationList() {
        return decorationList;
    }

    public Tile[][] getTileArray() {
        return tileArray;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMapTileSizeX() {
        return mapTileSizeX;
    }

    public int getMapTileSizeY() {
        return mapTileSizeY;
    }
}
