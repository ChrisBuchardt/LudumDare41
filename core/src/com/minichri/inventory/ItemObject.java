package com.minichri.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.GameObject;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.TileType;

public class ItemObject extends TextureObject {

    private TileType type;

    public ItemObject(World world, Vector2 pos, TileType type) {
        super(world, pos, GameObject.DEFAULT_STATIC_BODYDEF, GameObject.DEFAULT_STATIC_FIXTUREDEF, new TextureRegion(new Texture("tiles/dirt.png")));
        this.type = type;
    }

    public TileType getType() {
        return type;
    }
}
