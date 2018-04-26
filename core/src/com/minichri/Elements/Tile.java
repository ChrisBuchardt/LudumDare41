package com.minichri.Elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.entity.GameObject;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.TileType;

public class Tile extends TextureObject {

    private World world;
    private TileType tileType;
    private float x;
    private float y;


    public Tile(World world, TileType tileType, float x, float y) {
        super(world, new Vector2(x, y), GameObject.DEFAULT_STATIC_BODYDEF, GameObject.DEFAULT_STATIC_FIXTUREDEF, new TextureRegion(new Texture("tiles/dirt.png")));
        this.world = world; //TODO input argument
        this.tileType = tileType;
        this.x = x; this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
