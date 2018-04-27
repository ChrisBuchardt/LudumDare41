package com.minichri.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.helpers.GameInfo;
import com.minichri.inventory.Inventory;

import java.util.SortedMap;

public class Player extends TextureObject {

    public static final float WIDTH = 1;
    public static final float HEIGHT = 1.2f;
    public static final float FEET_WIDTH = WIDTH * 0.8f;
    public static final float FEET_HEIGHT = 0.12f;
    public static final float FEET_Y_OFFSET = -WIDTH/2f;

    private boolean isMidAir;
    private boolean isCrouched;
    private float maxVelocity = 40;
    private float jumpPower = 80;
    private Body feet;

    // Inventory singleton
    private static Inventory _inventory;
    public static Inventory getInventory() {
        if (_inventory == null) _inventory = new Inventory();
        return _inventory;
    }

    private static TextureRegion playerTexLeft = new TextureRegion(new Texture("player/player_left.png"), 0, 0, 16, 16);
    private static TextureRegion playerTexRight = new TextureRegion(new Texture("player/player_right.png"), 0, 0, 16, 16);

    public Player(World world, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_DYNAMIC_BODYDEF,GameObject.DEFAULT_DYNAMIC_FIXTUREDEF,playerTexLeft);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FEET_WIDTH/2f, FEET_HEIGHT/2f);

        FixtureDef feetDef = new FixtureDef();
        feetDef.shape = shape;
        feetDef.density = 0;
        feetDef.friction = 1;
        feetDef.restitution = 0;
        feetDef.isSensor = false;
        feet = new GameObject(world, new Vector2(pos.x, pos.y + FEET_Y_OFFSET), GameObject.DEFAULT_DYNAMIC_BODYDEF,feetDef).getBody();
        feet.setUserData("In Air");
        body.setGravityScale(10f);
        body.setLinearDamping(0);
        body.setUserData("Grounded");
    }

    @Override
    public void render(SpriteBatch batch) {

        isMidAir = feet.getUserData() != "Grounded";


        //Changes the player texture based on movement.
        if (body.getLinearVelocity().x<0){
            texture = playerTexLeft;
        }else texture = playerTexRight;


        //Checks if the player hsa jumped.
        if (!isMidAir) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                body.setLinearVelocity(body.getLinearVelocity().x, jumpPower);
                feet.setUserData("In Air");
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (body.getLinearVelocity().x>maxVelocity){
                body.setLinearVelocity(maxVelocity,body.getLinearVelocity().y);
                }
                else body.applyLinearImpulse(200, 0, 8, 11, true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (body.getLinearVelocity().x<-maxVelocity){
                    body.setLinearVelocity(-maxVelocity,body.getLinearVelocity().y);
                }
                body.applyLinearImpulse(-200, 0, 8, 11, true);
            }
        }else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (body.getLinearVelocity().x<-maxVelocity){
                body.setLinearVelocity(-maxVelocity,body.getLinearVelocity().y);
            }
            body.applyForceToCenter(-2000,0,true);

        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (body.getLinearVelocity().x>maxVelocity){
                body.setLinearVelocity(maxVelocity,body.getLinearVelocity().y);
            }
            body.applyForceToCenter(2000,0,true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            System.out.println(feet.getUserData());
            isCrouched = true;
        }
        else isCrouched = false;

        //Exits game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))body.setTransform(368.0f,176.0f,0);

        super.render(batch);
        feet.setTransform(body.getPosition().sub(0,5),0);
    }

    public Vector2 getBodyPos(){
         return body.getPosition();
    }
}
