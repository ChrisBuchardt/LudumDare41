package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.entity.GameObject;
import com.minichri.entity.Player;
import com.minichri.entity.RenderableObject;
import com.minichri.screens.GameScreen;

import java.util.ArrayList;

public class GameMap {

    private ArrayList<RenderableObject> gameObjects;
    private Tile[][] tilesArray;
    private ArrayList<RenderableObject> removeQueue = new ArrayList<>();
    private Player player;
    private World world;

    /** The constructor loads the map. */
    public GameMap(GameScreen screen){

        this.world = screen.world;

        //Load the map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel04.png", world);
        this.gameObjects = ml.getTilesList();
        this.tilesArray = ml.getTileArray();
        this.player = ml.getPlayer();
        this.tilesArray = ml.getTileArray();
    }

    /** Renders objects from the game map. */
    public void render(World world,Vector3 mousePos, KeyboardController controller, SpriteBatch spriteBatch, float delta){

        processRemoveQueue();

        for(RenderableObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        getPlayer().render(world,mousePos,controller, spriteBatch, delta);
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

    /** Takes a set of tile coordinates and check if the tile is occupied */
    public boolean isTileOcccipied(int x, int y){
        return this.tilesArray[x][y] != null;
    }

    
}
