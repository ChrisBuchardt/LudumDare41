package com.minichri.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.minichri.Elements.CollectedPlatform;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.World.GameMap;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.PlayerAnimationController;
import com.minichri.helpers.TileType;
import com.minichri.inventory.Inventory;
import com.minichri.inventory.Item;
import com.minichri.physics.ContactManager;

import java.util.ArrayList;
import java.util.Collections;


public class Player extends TextureObject {

    public static final float WIDTH = 0.56f;
    public static final float HEIGHT = 1.35f;

    private static final float FEET_WIDTH = WIDTH - 0.05f;
    private static final float FEET_HEIGHT = 0.23f;
    private static final float FEET_Y_OFFSET = -.85f;
    private static final float MAX_X_VEL = 6f;
    private static final float JUMP_FORCE = 11.4f;
    private static final float JUMP_FORCE_IN_AIR = 9f;
    private static final float WALK_SPEED = 6f;
    private static final float AIR_WALK_FORCE = 0.3f;

    private static final float MAX_RANGE = 5f;
    private static final float MIN_RANGE = 1.6f;
    private static final float GUN_X_OFFSET = 0.53f;
    private static final float SPAWN_TIMER = 2f;
    private static final float DEATH_TIMER_RED = 0.24f;
    private static final float RESPAWN_TIMER = 2.2f;

    private ArrayList<Tile> playerPlacedTiles;
    private ArrayList<Tile> queue;

    private static Texture playerTexture = new Texture("player/player.png");
    private static TextureRegion playerShip = new TextureRegion(new Texture("escape_pod.png"), 0, 0, 32, 42);
    private static Texture aimTexture = new Texture("player/aim.png");
    private static Texture rangeTexture = new Texture("player/range_indicator.png");
    private static Texture laserTexture = new Texture("player/laser.png");

    // Inventory singleton
    private static Inventory _inventory;
    public static Inventory getInventory() {
        if (_inventory == null) _inventory = new Inventory();
        return _inventory;
    }

    private World world;
    private int lookingDir = 1;
    private boolean isMidAir = false;
    private boolean isCrouched = false;
    private boolean hasJumped = false;
    private PlayerAnimationController animController;
    private Vector2 placeVector = new Vector2(0,0);
    private Vector2 gunPosition = new Vector2(0, 0);

    private boolean isPodLanding = true;
    private boolean isDead = false;
    private float deathTimer = 0;
    private float timePassed = 0;
    private Vector2 podPosition;
    private Vector2 spawnPosition;
    private GameMap map;

    private Sound placementSound;
    private Sound deathSound;
    private Sound qCollectSound;
    private Sound bounceSound;
    private Music walkingOnIce;
    private Sound walkingSound;
    private Sound pickupSound;
    private Sound voidSound;


    private boolean onIce;


    private Body feet;

    public Player(World world, Vector2 pos) {
        super(world, pos, createPlayerBodyDef(), createPlayerFixtureDef(), null);
        animController = new PlayerAnimationController(this, playerTexture);

        playerPlacedTiles = new ArrayList<>();
        queue = new ArrayList<>();


        onIce = false;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(FEET_WIDTH/2f, FEET_HEIGHT/2f);

        placementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/placeBlockSound.wav"));
        qCollectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/qSound.wav"));
        walkingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/running_Sound.wav"));
        walkingOnIce = Gdx.audio.newMusic(Gdx.files.internal("sounds/ice_Sound.wav"));
        bounceSound  = Gdx.audio.newSound(Gdx.files.internal("sounds/bounce_sound.mp3"));
        voidSound = Gdx.audio.newSound(Gdx.files.internal("sounds/wilhelm.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/death_Sound.mp3"));
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/pickup dak.wav"));

        this.world = world;
        FixtureDef feetDef = new FixtureDef();
        feetDef.shape = shape;
        feetDef.density = 0;
        feetDef.friction = 1;
        feetDef.restitution = 0;
        feetDef.isSensor = false;
        feet = new GameObject(world, new Vector2(pos.x, pos.y + FEET_Y_OFFSET), createPlayerBodyDef(), feetDef).getBody();
        feet.setUserData(ContactManager.FEET);
        feet.setGravityScale(0);
        body.setLinearDamping(0);
        body.setUserData(this);

        podPosition = new Vector2(pos);
    }

    public void render(GameMap map, World world, Vector3 mousePos, KeyboardController controller, SpriteBatch batch, float delta) {
        this.map = map;

        //adds Player spawned tiles to the array
        if (queue.size()>0) playerPlacedTiles.addAll(queue);
        queue.removeAll(queue);

        for(RenderObject renderableObject : playerPlacedTiles)
                renderableObject.render(batch, delta);

        timePassed += delta;

        //Draws player Ship. Needs to be here to be drawn in the right layer
        batch.draw(playerShip, podPosition.x, podPosition.y, 4, 4);

        if (isPodLanding) {
            spacePodLanding(delta);
        } else {
            if (!isDead) {
                movement(map, controller, batch, delta);
                qCollect(controller, map);
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
        if (getInventory().getSelectedItem() != null && !isMidAir) {
            placeVector.x = Math.round(mousePos.x);
            placeVector.y = Math.round(mousePos.y);
            float distance = new Vector2(placeVector).sub(body.getPosition()).len();
            if (MIN_RANGE < distance && distance < MAX_RANGE) {
                int x = (int)placeVector.x;
                int y = (int)placeVector.y;
                if (!map.isTileOcccipied(x, y)) {
                    TileType type = getInventory().getSelectedItem().getType();
                    batch.draw(type.getAimTexture(), placeVector.x - 0.5f, placeVector.y - 0.5f, 1, 1);
                    if (controller.leftClick){
                        placementSound.play();
                        queue.add(new Tile(world, type, placeVector));
                        getInventory().remove(getInventory().getSelectedSlot());
                        getInventory().focus();
                        showLaser(batch, placeVector.x - 0.5f, placeVector.y - 0.5f);
                    }
                }
            }
            // Show range
            Vector2 bodyPos = body.getPosition();
            int lowX = (int)(bodyPos.x - MAX_RANGE);
            int highX= (int)(bodyPos.x + MAX_RANGE);
            int lowY= (int)(bodyPos.y - MAX_RANGE);
            int highY = (int)(bodyPos.y + MAX_RANGE);
            for (int x = lowX; x <= highX; x++) {
                for (int y = lowY; y <= highY; y++) {
                    distance = new Vector2(x, y).sub(bodyPos).len();
                    if (MIN_RANGE < distance && distance < MAX_RANGE) {
                        if (!map.isTileOcccipied(x, y)) {
                            batch.draw(rangeTexture, x - 0.5f, y - 0.5f, 1, 1);
                        }
                    }
                }
            }
        }
    }

    private void showLaser(SpriteBatch batch, float x, float y) {
        Vector2 diff = new Vector2(gunPosition).sub(x, y);
        Vector2 middle = new Vector2(gunPosition).add(new Vector2(diff).scl(-0.5f));
        batch.draw(new TextureRegion(laserTexture), middle.x, middle.y, 8f * GameInfo.PPM, 8f * GameInfo.PPM, 16f * GameInfo.PPM, 16f * GameInfo.PPM, diff.len(), 0.2f, diff.angle());
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
        lookingDir = dir == 0 ? lookingDir : dir;
        if (onIce && !isMidAir){
            //Sliding on ice
            if (!walkingOnIce.isPlaying())walkingOnIce.play();
               body.applyForceToCenter(WALK_SPEED*dir,0,true);
        }else if (!onIce && !isMidAir) {
            // Grounded

            vel.x = WALK_SPEED * dir;

        } else  {
            // Mid air
            vel.add(AIR_WALK_FORCE * dir, 0);
        }

        isCrouched = controller.s;

        // Restrict vel x
        vel.x = Math.min(Math.max(-MAX_X_VEL, vel.x), MAX_X_VEL);

        // Apply new vel
        body.setLinearVelocity(vel);

        // Move feet
        feet.setTransform(new Vector2(body.getPosition()).add(0, FEET_Y_OFFSET), 0);
        feet.setLinearVelocity(vel);
        gunPosition.set(body.getPosition()).add(lookingDir * GUN_X_OFFSET, 0).add(-0.45f, -0.65f);

        texture = animController.getTexture(dir, isMidAir, delta);
    }

    /** When q is pressed, collect as many nearby tiles as you can hold in your inventory. */
    private void qCollect(KeyboardController controller, GameMap map){
        if(controller.q){

            //Get number of empty item slots
            int emptySlots = getInventory().slotsLeft();

            //Any free slots?
            if (emptySlots != 0) {

                //Create dist from player ordered list
                Collections.sort(playerPlacedTiles, (a, b) -> {

                    Vector2 vectorA = a.body.getPosition();
                    Vector2 vectorB = b.body.getPosition();

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

                    //Create collectedPlatform for collection animation
                    map.addCollectedObject(new CollectedPlatform(world, involvedTile.getBody().getPosition(), involvedTile.getTileType()));

                    //Add element to inventory
                    getInventory().add(new Item(involvedTile.getTileType()));

                    //Remove and destroy from world
                    world.destroyBody(playerPlacedTiles.get(playerPlacedTiles.indexOf(playerPlacedCopy.get(i))).getBody());
                    getPlayerPlacedTiles().remove(playerPlacedTiles.indexOf(playerPlacedCopy.get(i)));

                    //Play sound
                    //qCollectSound.play();
                    long qCollectSoundsID = qCollectSound.play(0.5f);
                    qCollectSound.setPitch(qCollectSoundsID, 3);
                }
            }
        }
    }

    public void resolveDeath(SpriteBatch batch, float delta) {
        deathTimer += delta;
        if (deathTimer < DEATH_TIMER_RED) batch.setColor(1, 0, 0, 1);
        else if (deathTimer < RESPAWN_TIMER) batch.setColor(0, 0, 0, 1);
        else {
            body.setTransform(spawnPosition, 0);
            isDead = false;
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void kill() {
        if (!isDead) {
            if (map.isPlayerOutOfBounds()){
                voidSound.play();
            }else {
                deathSound.play();
            }

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

    public void slide(boolean b) {
        onIce = b;
    }

    public boolean isCrouched() {
        return isCrouched;
    }

    public void bounce(Body other) {
        if (!isCrouched) {
            if (Math.round(other.getPosition().x) == Math.round(body.getPosition().x) && body.getLinearVelocity().y < 1)
                bounceSound.stop();
                bounceSound.play();
                body.setLinearVelocity(body.getLinearVelocity().x, -body.getLinearVelocity().y);
        }
    }

    public void playPickupSound() {
        pickupSound.play();
    }
}
