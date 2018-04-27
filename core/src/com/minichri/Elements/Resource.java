package com.minichri.Elements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.AnimatedObject;
import com.minichri.entity.GameObject;
import com.minichri.helpers.TileType;

public class Resource extends AnimatedObject {

    private static final int RESOURCE_FRAMES = 4;

    public Resource(World world, TileType tileType, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_STATIC_BODYDEF, GameObject.DEFAULT_STATIC_FIXTUREDEF, tileType.getTexture(), RESOURCE_FRAMES);

        body.getFixtureList().get(0).setSensor(true);
        body.setUserData(this);
    }
}
