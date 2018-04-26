package com.minichri.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameObject {

    protected Body body;

    public GameObject(World world, Vector2 pos, BodyDef.BodyType bodyType) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = false;
        bodyDef.position.set(pos);
        bodyDef.type = bodyType;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;

        body.createFixture(fixtureDef);
        body.setUserData(this);
    }
}
