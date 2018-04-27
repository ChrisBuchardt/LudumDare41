package com.minichri.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.minichri.Elements.Resource;
import com.minichri.World.GameMap;
import com.minichri.entity.Player;
import com.minichri.inventory.Item;

public class ContactManager implements ContactListener {

    public static final String FEET = "Feet";
    public static final String TILE = "Tile";

    private World world;
    private GameMap gameMap;

    public static int feetCollisions = 0;
    public static boolean playerTouchWall = false;

    public ContactManager(World world, GameMap gameMap){
        this.world = world;
        this.gameMap = gameMap;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody().getUserData() == FEET || fb.getBody().getUserData() == FEET) {
            feetCollisions++;
        }

        //Check for player collison
        if(fa.getBody().getUserData()instanceof Player || fb.getBody().getUserData() instanceof Player){
            Object other = (fa.getBody().getUserData() instanceof Player ? fb : fa).getBody().getUserData();

            //Collision with resource
            if(other instanceof Resource) {
                gameMap.addToRemoveResource((Resource)other);
                Player.getInventory().add(new Item(((Resource) other).getTileType()));
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody().getUserData() == FEET || fb.getBody().getUserData() == FEET) {
            feetCollisions--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
