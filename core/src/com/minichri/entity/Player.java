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
import com.minichri.physics.PlayerFeetContactListener;

import java.util.SortedMap;

public class Player extends TextureObject {

    public static final float WIDTH = 0.8f;
    public static final float HEIGHT = 0.8f;

    private static final float FEET_WIDTH = WIDTH - 0.05f;
    private static final float FEET_HEIGHT = 0.2f;
    private static final float FEET_Y_OFFSET = -.54f;
    private static final float MAX_X_VEL = 6f;
    private static final float JUMP_FORCE = 10f;
    private static final float JUMP_FORCE_IN_AIR = 7.5f;
    private static final float WALK_SPEED = 6f;
    private static final float AIR_WALK_FORCE = 0.3f;

    private static TextureRegion playerTexLeft = new TextureRegion(new Texture("player/player_left.png"), 0, 0, 16, 16);
    private static TextureRegion playerTexRight = new TextureRegion(new Texture("player/player_right.png"), 0, 0, 16, 16);

    // Inventory singleton
    private static Inventory _inventory;
    public static Inventory getInventory() {
        if (_inventory == null) _inventory = new Inventory();
        return _inventory;
    }

    private boolean isMidAir = false;
    private boolean isCrouched = false;
    private boolean hasJumped = false;
    private Body feet;

    public Player(World world, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_DYNAMIC_BODYDEF,GameObject.DEFAULT_DYNAMIC_FIXTUREDEF, playerTexLeft);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FEET_WIDTH/2f, FEET_HEIGHT/2f);

        FixtureDef feetDef = new FixtureDef();
        feetDef.shape = shape;
        feetDef.density = 0;
        feetDef.friction = 1;
        feetDef.restitution = 0;
        feetDef.isSensor = false;
        feet = new GameObject(world, new Vector2(pos.x, pos.y + FEET_Y_OFFSET), GameObject.DEFAULT_DYNAMIC_BODYDEF,feetDef).getBody();
        feet.setUserData(PlayerFeetContactListener.FEET);
        feet.setGravityScale(0);
        body.setLinearDamping(0);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {

        Vector2 vel = body.getLinearVelocity();

        isMidAir = !(PlayerFeetContactListener.feetCollisions > 0 && Math.abs(vel.y) <= 1e-2);

        if (!isMidAir) hasJumped = false;
        if (!hasJumped && (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
            vel.y = isMidAir ? JUMP_FORCE_IN_AIR : JUMP_FORCE;
            isMidAir = true;
            hasJumped = true;
        }

        // Movement
        int dir = Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : Gdx.input.isKeyPressed(Input.Keys.A) ? -1 : 0;
        if (!isMidAir) {
            // Grounded
            vel.x = WALK_SPEED * dir;
        } else {
            // Mid air
            vel.add(AIR_WALK_FORCE * dir, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) isCrouched = true;
        else isCrouched = false;

        // Restrict vel x
        float restrictVelX = Math.min(Math.max(-MAX_X_VEL, vel.x), MAX_X_VEL);
        vel.x = restrictVelX;

        // Apply new vel
        body.setLinearVelocity(vel);

        // Move feet
        feet.setTransform(new Vector2(body.getPosition()).add(0, FEET_Y_OFFSET), 0);

        updateTextre(dir);
        super.render(batch, delta);
    }

    public void updateTextre(int moveDirection) {
        // Changes the player texture based on movement
        if (moveDirection < 0)
            texture = playerTexLeft;
        else if (moveDirection > 0)
            texture = playerTexRight;
    }

    public Vector2 getBodyPos(){
         return body.getPosition();
    }
}
