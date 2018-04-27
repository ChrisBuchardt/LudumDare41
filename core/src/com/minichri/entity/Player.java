package com.minichri.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.helpers.GameInfo;
import com.minichri.inventory.Inventory;

public class Player extends TextureObject {

    private boolean hasJumped;
    private boolean isCrouched;
    private float maxVelocity = 40;
    private float jumpPower = 80;
    // Inventory singleton
    private static Inventory _inventory;
    public static Inventory getInventory() {
        if (_inventory == null) _inventory = new Inventory();
        return _inventory;
    }

    private static TextureRegion playerTexLeft = new TextureRegion(new Texture("player/player_left.png"),0,0,16,16);
    private static TextureRegion playerTexRight = new TextureRegion(new Texture("player/player_right.png"),0,0,16,16);

    public Player(World world, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_DYNAMIC_BODYDEF,GameObject.DEFAULT_DYNAMIC_FIXTUREDEF,playerTexLeft);
        body.setGravityScale(10f);
        body.setLinearDamping(0);
    }

    @Override
    public void render(SpriteBatch batch) {

        //Changes the player texture based on movement.
        if (body.getLinearVelocity().x<0){
            texture = playerTexLeft;
        }else texture = playerTexRight;


        //Checks if the player hsa jumped.
        if (!hasJumped) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                body.setLinearVelocity(body.getLinearVelocity().x*8, jumpPower);
                hasJumped = true;
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
            body.applyForceToCenter(-200,0,true);

        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (body.getLinearVelocity().x>maxVelocity){
                body.setLinearVelocity(maxVelocity,body.getLinearVelocity().y);
            }
            body.applyForceToCenter(200,0,true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            System.out.println("Player is crouched, and can jump again");
            isCrouched = true;
            hasJumped = false;
        }
        else isCrouched = false;

        //Draws the texture the size of the player
        Vector2 pos = body.getPosition();
        float width = GameInfo.TILE_SIZE;
        float height = GameInfo.PLAYER_HEIGHT;
        batch.draw(texture, pos.x - width/2, pos.y - height/2, width / 2f, height / 2f, width, height, 1, 1, body.getAngle());



        //Exits game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))Gdx.app.exit();
    }

    public Vector2 getBodyPos(){
         return body.getPosition();
    }
}
