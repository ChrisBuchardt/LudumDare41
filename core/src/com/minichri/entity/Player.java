package com.minichri.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public class Player extends TextureObject {


    private static TextureRegion playerTexLeft = new TextureRegion(new Texture("player/player_left.png"),0,0,16,16);

    public Player(World world, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_DYNAMIC_BODYDEF,GameObject.DEFAULT_DYNAMIC_FIXTUREDEF,playerTexLeft);


    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        //TEMPORATY
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            body.applyForceToCenter(0,1000000,true);
        }
    }

    public Vector2 getBodyPos(){
         return body.getPosition();
    }
}
