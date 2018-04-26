package com.minichri.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.Tile;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.TileType;

import java.util.ArrayList;

public class MapLoader {

    private ArrayList<Tile> tilesList;

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

        //Goes through all pixels, analyses and converts the colored pixel to tiles
        for(int y = 0; y < levelPixmap.getHeight(); y++) {
            for (int x = 0; x < levelPixmap.getWidth(); x++) {

                //Get a color
                Color color = new Color();
                Color.argb8888ToColor(color, levelPixmap.getPixel(x, y));

                //Check if color matches a type
                TileType currentTileType = TileType.getTypeFromColor(color);

                //Create an element if color was found
                if(currentTileType == TileType.WHITE_SPACE){ //Do nothing
                    continue;
                }else if(currentTileType != null){ //Add tile based on tileType

                    //TileType aboveTileType = ;
                    //TileType leftTileType = ;
                    //TileType rightTileType = ;


                    //boolean isTileAboveTheSame = ;//Is the block above the same as this?
                    //boolean isTileLeftTheSame = ;//Is the block to the left the same?
                    //boolean isTileRightTheSame = ;//Is the block to the right the same?



                    this.tilesList.add(new Tile(world, TileType.TilePlacementType.MIDDEL, currentTileType, x * GameInfo.TILE_SIZE, (levelPixmap.getHeight() - y) * GameInfo.TILE_SIZE)); //TODO Add arguments
                    //this.tilesList.add(new Tile(world, currentTileType, x * GameInfo.TILE_SIZE, (levelPixmap.getHeight() - y) * GameInfo.TILE_SIZE - GameInfo.TILE_SIZE)); //TODO Add arguments

                }
            }
        }

        //Dispose data
        levelTexture.dispose();
        tempData.disposePixmap();
        levelPixmap.dispose();
    }

    public ArrayList<Tile> getTilesList() {
        return tilesList;
    }
}
