package com.minichri.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.entity.Player;

public class ContactManager implements ContactListener {

    public static final String FEET = "Feet";
    public static final String TILE = "Tile";

    private World world;

    public static boolean isFeetOnGround = false;
    public static int feetCollisions = 0;

    public ContactManager(World world){
        this.world = world;
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
            Object other = fa.getUserData() instanceof Player ? fb : fa;

            //Collision with tile
            if(other instanceof Tile) {

            }

            //Collision with resource
            if(other instanceof Resource) {

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
