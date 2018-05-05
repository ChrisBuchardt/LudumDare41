package com.minichri.Elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.TextureObject;

public class Spikes extends TextureObject {

    private static final BodyDef BODY_DEF = createBodyDef();
    private static final FixtureDef FIXTURE_DEF = createFixtureDef();

    public Spikes(World world, Vector2 pos) {
        super(world, pos, BODY_DEF, FIXTURE_DEF, new TextureRegion(new Texture("tiles/spikes.png")));
        body.setUserData(this);
    }

    /** The BodyDef used for something like tiles */
    private static BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        return bodyDef;
    }

    /** The FixtureDef used for something like tiles */
    private static FixtureDef createFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[] {
                new Vector2(-0.3f, 0.23f),
                new Vector2(0.3f, 0.23f),
                new Vector2(0.45f, -0.5f),
                new Vector2(-0.45f, -0.5f)
        });

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;
        fixtureDef.isSensor = true;

        return fixtureDef;
    }
}
