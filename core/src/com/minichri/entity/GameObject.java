package com.minichri.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameObject {

    /** The BodyDef used for something like tiles */
    public static BodyDef DEFAULT_STATIC_BODYDEF = createDefaultStaticBodyDef();
    /** The FixtureDef used for something like tiles */
    public static FixtureDef DEFAULT_STATIC_FIXTUREDEF = createDefaultStaticFixtureDef();
    /** The FixtureDef used for something like a player     */
    public static FixtureDef PLAYER__FIXTUREDEF = createPlayerFixtureDef();
    /** The BodyDef used for something like a player     */
    public static BodyDef PLAYER_BODYDEF = createPlayerBodyDef();

    protected final Body body;

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
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;

        return fixtureDef;
    }

    /** The default fixturedef for players */
    private static FixtureDef createPlayerFixtureDef(){
        float cornerSize = 0.043f;
        float width = Player.WIDTH/2f;
        float widthShort = Player.WIDTH/2f - cornerSize;
        float height = Player.HEIGHT/2f;
        float heightShort = Player.HEIGHT/2f - cornerSize;
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[] {
                new Vector2(-width, height),
                new Vector2(width, height),
                new Vector2(width, -heightShort),
                new Vector2(0, -height),
                new Vector2(-width, -heightShort),
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0;

        return fixtureDef;

    }


    /** The BodyDef used for something like players */
    private static BodyDef createPlayerBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        return bodyDef;
    }


    public Body getBody() {
        return body;
    }
}
