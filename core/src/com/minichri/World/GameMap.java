package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minichri.Elements.Resource;
import com.minichri.entity.GameObject;
import com.minichri.entity.Player;
import com.minichri.entity.RenderableObject;
import java.util.ArrayList;

public class GameMap {

    private ArrayList<RenderableObject> gameObjects;
    private ArrayList<RenderableObject> removeQueue = new ArrayList<>();
    private Player player;
    private World world;

    /** The constructor loads the map. */
    public GameMap(World world){

        this.world = world;

        //Load the map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel04.png", world);
        gameObjects = ml.getTilesList();
        player = ml.getPlayer();
    }

    /** Renders objects from the game map. */
    public void render(SpriteBatch spriteBatch, float delta){

        processRemoveQueue();

        for(RenderableObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        getPlayer().render(spriteBatch, delta);
    }

    /** Removes the objects listed in the removequeue from the gameObjects list */
    private void processRemoveQueue(){
        if(removeQueue.size() != 0){
            for(RenderableObject removeQueueObject : new ArrayList<>(removeQueue)){
                gameObjects.remove(removeQueueObject);
                world.destroyBody(((GameObject) removeQueueObject).getBody());
                removeQueue.remove(removeQueueObject);
            }
        }
    }

    /** Adds an object to the removeQueue*/
    public void addToRemoveResource(Resource resource){

        //Find the resource in the map
        for(RenderableObject renderableObject : gameObjects){
            if(renderableObject == resource){
                this.removeQueue.add(renderableObject); //Add to remove queue
            }
        }
    }

    public Player getPlayer(){
        return this.player;
    }
}
