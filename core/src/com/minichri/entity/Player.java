package com.minichri.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.World.GameMap;
import com.minichri.helpers.TileType;
import com.minichri.inventory.Inventory;
import com.minichri.inventory.Item;
import com.minichri.physics.ContactManager;

import java.util.ArrayList;
import java.util.Collections;


public class Player extends TextureObject {

    private static final int PIXEL_WIDTH = 16;
    private static final int PIXEL_HEIGHT = 21;

    public static final float WIDTH = 0.56f;
    public static final float HEIGHT = 1.35f;

    private static final float FEET_WIDTH = WIDTH - 0.05f;
    private static final float FEET_HEIGHT = 0.2f;
    private static final float FEET_Y_OFFSET = -.85f;
    private static final float MAX_X_VEL = 6f;
    private static final float JUMP_FORCE = 11.4f;
    private static final float JUMP_FORCE_IN_AIR = 9f;
    private static final float WALK_SPEED = 6f;
    private static final float AIR_WALK_FORCE = 0.3f;


    private ArrayList<Tile> playerPlacedTiles;
    private ArrayList<Tile> queue;

    private static final float SPAWN_TIMER = 2f;

    private static TextureRegion playerTexLeft = new TextureRegion(new Texture("player/player_left.png"), 0, 0, PIXEL_WIDTH, PIXEL_HEIGHT);
    private static TextureRegion playerTexRight = new TextureRegion(new Texture("player/player_right.png"), 0, 0, PIXEL_WIDTH, PIXEL_HEIGHT);
    private static TextureRegion playerShip = new TextureRegion(new Texture("escape_pod.png"), 0, 0, PIXEL_WIDTH*2, PIXEL_HEIGHT*2);

    // Inventory singleton
    private static Inventory _inventory;
    public static Inventory getInventory() {
        if (_inventory == null) _inventory = new Inventory();
        return _inventory;
    }

    private World world;
    private boolean isMidAir = false;
    private boolean isCrouched = false;
    private boolean hasJumped = false;
    private float maxRange = 5f;
    private float minRange = 1.6f;
    private Vector2 placeVector = new Vector2(0,0);
    private Texture preview;

    private boolean isPodLanding = true;
    private boolean isDead = false;
    private float deathTimer = 0;
    private float timePassed = 0;
    private Vector2 podPosition;
    private Vector2 spawnPosition;

    private Body feet;

    public Player(World world, Vector2 pos) {
        super(world, pos, GameObject.DEFAULT_DYNAMIC_BODYDEF, GameObject.DEFAULT_DYNAMIC_FIXTUREDEF, playerTexLeft);

        playerPlacedTiles = new ArrayList<>();
        queue = new ArrayList<>();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FEET_WIDTH/2f, FEET_HEIGHT/2f);

        this.world = world;
        FixtureDef feetDef = new FixtureDef();
        feetDef.shape = shape;
        feetDef.density = 0;
        feetDef.friction = 1;
        feetDef.restitution = 0;
        feetDef.isSensor = false;
        feet = new GameObject(world, new Vector2(pos.x, pos.y + FEET_Y_OFFSET), GameObject.DEFAULT_DYNAMIC_BODYDEF,feetDef).getBody();
        feet.setUserData(ContactManager.FEET);
        feet.setGravityScale(0);
        body.setLinearDamping(0);
        body.setUserData(this);

        podPosition = new Vector2(pos);
    }

    public void render(GameMap map, World world, Vector3 mousePos, KeyboardController controller, SpriteBatch batch, float delta) {

        //adds Player spawned tiles to the array
        if (queue.size()>0) playerPlacedTiles.addAll(queue);
        queue.removeAll(queue);

        for(RenderableObject renderableObject : playerPlacedTiles)
                renderableObject.render(batch, delta);

        timePassed += delta;

        //Draws player Ship. Needs to be here to be drawn in the right layer
        batch.draw(playerShip, podPosition.x, podPosition.y, 4, 4);

        if (isPodLanding) {
            spacePodLanding(delta);
        } else {
            if (!isDead) {
                movement(map, controller, batch, delta);
                blockPlacing(batch, map, controller, mousePos);
            } else {
                resolveDeath(batch, delta);
            }

            super.render(batch, delta);
        }
    }

    private void spacePodLanding(float delta) {
        if (timePassed < SPAWN_TIMER) {
            podPosition = new Vector2(body.getPosition().x - 2f, body.getPosition().y - 2f);
        } else {
            isPodLanding = false;
            spawnPosition = new Vector2(body.getPosition());
        }
    }

    private void blockPlacing(SpriteBatch batch, GameMap map, KeyboardController controller, Vector3 mousePos) {
        //Spawn blocks at the click
        if (getInventory().getSelectedItem()!=null){
            placeVector.x = Math.round(mousePos.x);
            placeVector.y = Math.round(mousePos.y);
            float distance = new Vector2(placeVector).sub(body.getPosition()).len();
            if (distance<maxRange && distance>minRange){
                preview = getInventory().getSelectedItem().getType().getBlockTexture();
                batch.draw(getInventory().getSelectedItem().getType().getBlockTexture(),placeVector.x-0.5f,placeVector.y-0.5f,1, 1);
                if (controller.leftClick){
                    if (!map.isTileOcccipied((int)placeVector.x, (int)placeVector.y)) {
                        TileType type = getInventory().getSelectedItem().getType();
                        queue.add(new Tile(world, type, placeVector));
                        getInventory().remove(getInventory().getSelectedSlot());
                    }
                }
            }
        }
    }

    private void movement(GameMap map, KeyboardController controller, SpriteBatch batch, float delta) {
        Vector2 vel = body.getLinearVelocity();

        isMidAir = !(ContactManager.feetCollisions > 0 && Math.abs(vel.y) <= 1e-2);

        // Jump
        if (!isMidAir) hasJumped = false;
        if (!hasJumped && (controller.w || controller.space)) {
            vel.y = isMidAir ? JUMP_FORCE_IN_AIR : JUMP_FORCE;
            isMidAir = true;
            hasJumped = true;
        }

        // Movement
        int dir = controller.d ? 1 : controller.a ? -1 : 0;
        if (!isMidAir) {
            // Grounded
            vel.x = WALK_SPEED * dir;
        } else {
            // Mid air
            vel.add(AIR_WALK_FORCE * dir, 0);
        }

        // Q-collect
        if(controller.q){

            //Get number of empty item slots
            int emptySlots = getInventory().slotsLeft();

            //Any free slots?
            if(emptySlots != 0){

                //Create dist from player ordered list
                Collections.sort(playerPlacedTiles, (a, b) -> {

                    Vector2 vectorA = ((GameObject)a).body.getPosition();
                    Vector2 vectorB = ((GameObject)b).body.getPosition();

                    float distToA = Vector2.dst(vectorA.x, vectorA.y, getBodyPos().x, getBodyPos().y);
                    float distToB = Vector2.dst(vectorB.x, vectorB.y, getBodyPos().x, getBodyPos().y);

                    float comparison = distToA - distToB;

                    if(comparison > 0)
                        return 1;
                    else if(comparison < 0)
                        return -1;
                    else
                        return 0;
                });

                ArrayList<Tile> playerPlacedCopy = new ArrayList(playerPlacedTiles);

                //Remove till the found number of elements and add them to inv
                int collectCount = Math.min(getPlayerPlacedTiles().size(), emptySlots);
                for(int i = 0; i < collectCount; i ++){

                    Tile involvedTile = playerPlacedCopy.get(i);

                    //Add element to inventory
                    getInventory().add(new Item(involvedTile.getTileType()));

                    //Remove and destroy from world
                    world.destroyBody(playerPlacedTiles.get(playerPlacedTiles.indexOf(playerPlacedCopy.get(i))).getBody());
                    getPlayerPlacedTiles().remove(playerPlacedTiles.indexOf(playerPlacedCopy.get(i)));
                }
            }
        }

        isCrouched = controller.s;

        // Restrict vel x
        vel.x = Math.min(Math.max(-MAX_X_VEL, vel.x), MAX_X_VEL);

        // Apply new vel
        body.setLinearVelocity(vel);

        // Move feet
        feet.setTransform(new Vector2(body.getPosition()).add(0, FEET_Y_OFFSET), 0);

        updateTexture(dir);
    }

    private void updateTexture(int moveDirection) {
        // Changes the player texture based on movement
        if (moveDirection < 0)
            texture = playerTexLeft;
        else if (moveDirection > 0)
            texture = playerTexRight;
    }

    public void resolveDeath(SpriteBatch batch, float delta) {
        deathTimer += delta;
        if (deathTimer < 0.23f) batch.setColor(1, 0, 0, 1);
        else if (deathTimer < 1f) batch.setColor(0, 0, 0, 1);
        else {
            body.setTransform(spawnPosition, 0);
            isDead = false;
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void kill() {
        if (!isDead) {
            isDead = true;
            deathTimer = 0;
        }
    }

    public Vector2 getBodyPos(){
         return body.getPosition();
    }

    public ArrayList<Tile> getPlayerPlacedTiles() {
        return playerPlacedTiles;
    }

    public ArrayList<Tile> getQueue() {
        return queue;
    }
}
