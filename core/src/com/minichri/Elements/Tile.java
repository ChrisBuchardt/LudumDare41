package com.minichri.Elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.TileType;

public class Tile extends Sprite {

    private World world;
    private Body body;
    private TileType tileType;


    public Tile(World world, TileType tileType, float x, float y) {
        super(new Texture("tiles/dirt.png"));
        this.world = world; //TODO input argument
        this.tileType = tileType;
        setPosition(x, y);
        createBody();
    }

    private void createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(((getX() + getWidth() / 2) / GameInfo.PPM), (getY() + getHeight() / 2) / GameInfo.PPM);

        //Add the body to the world
        body = world.createBody(bodyDef);

        //Collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / GameInfo.PPM, (getHeight() / 2) / GameInfo.PPM);

        //Contains mass and such
        FixtureDef fixtureDef = new FixtureDef(); //
        fixtureDef.shape = shape;
        fixtureDef.density = 1;

        Fixture fixture = body.createFixture(fixtureDef);
        //fixture.setUserData(id); //Can we called on contact //TODO set id!

        shape.dispose(); //It is no longer needed/used
    }
}
