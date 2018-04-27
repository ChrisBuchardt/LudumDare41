package com.minichri.Elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.TileType;

public class DirectionalTile extends Tile {

    private TileType.TileTextureDirection placementType;


    public DirectionalTile(World world, TileType.TileTextureDirection placementType, TileType tileType, Vector2 pos) {
        super(world, tileType, pos);

        this.placementType = placementType;
        setDirectionalTextures();
        //setDirectionalTextures02();
    }

    private void setDirectionalTextures02(){


    }

    private void setDirectionalTextures(){

        TextureRegion[] regions = new TextureRegion[4];

        regions[0] = new TextureRegion(texture, 16, 16, GameInfo.TILE_SIZE, GameInfo.TILE_SIZE); //Under
        regions[1] = new TextureRegion(texture, 16, 0, GameInfo.TILE_SIZE, GameInfo.TILE_SIZE); //Middle
        regions[2] = new TextureRegion(texture, 0, 16, GameInfo.TILE_SIZE, GameInfo.TILE_SIZE); //Left
        regions[3] = new TextureRegion(texture, 0, 0, GameInfo.TILE_SIZE, GameInfo.TILE_SIZE); //Right

        switch (placementType){
            case UNDER: this.texture = regions[0];
                    break;
            case MIDDLE: this.texture = regions[1];
                    break;
            case LEFT: this.texture = regions[2];
                    break;
            case RIGHT: this.texture = regions[3];
                    break;
        }
    }
}
