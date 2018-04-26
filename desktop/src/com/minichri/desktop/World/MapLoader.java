package com.minichri.desktop.World;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.desktop.Elements.GroundTile;
import com.minichri.desktop.helpers.TileType;

import java.util.ArrayList;

public class MapLoader {

    private ArrayList<GroundTile> tilesList;

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
                TileType tileType = TileType.getTypeFromColor(color);

                //Create an element if color was found
                if(tileType == TileType.WHITE_SPACE){ //Do nothing
                    continue;
                }else if(tileType != null){

                    this.tilesList.add(new GroundTile()); //TODO Add arguments
                }
            }
        }

        //Dispose data
        levelTexture.dispose();
        tempData.disposePixmap();
        levelPixmap.dispose();
    }

    public ArrayList<GroundTile> getTilesList() {
        return tilesList;
    }
}
