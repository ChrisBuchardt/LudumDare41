package com.minichri.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.minichri.Elements.Resource;
import com.minichri.entity.Player;

public class PlayerFeetContactListener implements ContactListener {

    public static final String FEET = "Feet";

    private World world;

    public static boolean isFeetOnGround = false;
    public static int feetCollisions = 0;

    public PlayerFeetContactListener(World world){
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getBody().getUserData() == FEET || fb.getBody().getUserData() == FEET) {
            feetCollisions++;
        }

        //if(fa.getBody().getUserData() instanceof Resource || fb.getBody().getUserData() instanceof Resource)
        //    System.out.println("RESOURCE!");


        //Collision with resource
        if(fa.getBody().getUserData() instanceof Resource || fb.getBody().getUserData() instanceof Resource){

            //Check for player collison
            if(fa.getBody().getUserData()instanceof Player || fb.getBody().getUserData() instanceof Player){

                System.out.println("YOU ARE NOW TOUCH A RESOURCE!");
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
