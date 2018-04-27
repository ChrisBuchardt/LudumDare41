package com.minichri.physics;

import com.badlogic.gdx.physics.box2d.*;

public class GameContactListener implements ContactListener {
    private World world;

    public GameContactListener(World world){
        this.world = world;
    }



    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        

        if(fa.getBody().getType()== BodyDef.BodyType.DynamicBody){

            fa.getBody().setUserData("Grounded");

            return;
        }else if(fb.getBody().getUserData() == BodyDef.BodyType.DynamicBody){

            fb.getBody().setUserData("Grounded");
            return;

        }else{
            if (fa.getBody().getUserData()=="");

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
