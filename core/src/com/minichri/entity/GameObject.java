package com.minichri.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.helpers.GameInfo;

public class GameObject {

    /** The BodyDef used for something like tiles */
    public static BodyDef DEFAULT_STATIC_BODYDEF = createDefaultStaticBodyDef();
    /** The FixtureDef used for something like tiles */
    public static FixtureDef DEFAULT_STATIC_FIXTUREDEF = createDefaultStaticFixtureDef();
    /** The FixtureDef used for something like a player     */
    public static FixtureDef DEFAULT_DYNAMIC_FIXTUREDEF = createDefaultDynamicFixtureDef();
    /** The BodyDef used for something like a player     */
    public static BodyDef DEFAULT_DYNAMIC_BODYDEF = createDefaultDynamicBodyDef();

    protected Body body;

    public GameObject(World world, Vector2 pos, BodyDef bodyDef, FixtureDef fixtureDef) {

        bodyDef.position.set(pos);
        body = world.createBody(bodyDef);

        body.createFixture(fixtureDef);
        body.setUserData(this);
    }

    /** The BodyDef used for something like tiles */
    private static BodyDef createDefaultStaticBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        return bodyDef;
    }

    /** The FixtureDef used for something like tiles */
    private static FixtureDef createDefaultStaticFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameInfo.TILE_SIZE / 2f, GameInfo.TILE_SIZE / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;

        return fixtureDef;
    }

    /** The default fixturedef for players */
    private static FixtureDef createDefaultDynamicFixtureDef(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameInfo.TILE_SIZE / 2f, GameInfo.PLAYER_HEIGHT / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0;

        return fixtureDef;

    }


    /** The BodyDef used for something like players */
    private static BodyDef createDefaultDynamicBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        return bodyDef;
    }


}