package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.CollectedPlatform;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.entity.GameObject;
import com.minichri.entity.Player;
import com.minichri.entity.RenderableObject;
import com.minichri.screens.GameScreen;

import java.util.ArrayList;

public class GameMap {

    private ArrayList<CollectedPlatform> collectedObjects;
    private ArrayList<CollectedPlatform> collectedObjectsRemoveQueue;
    private ArrayList<RenderableObject> gameObjects;
    private Tile[][] tilesArray;
    private int mapTileSizeX;
    private int mapTileSizeY;
    private ArrayList<RenderableObject> removeQueue = new ArrayList<>();
    private Player player;
    private World world;

    /** The constructor loads the map. */
    public GameMap(GameScreen screen){

        this.world = screen.world;
        this.collectedObjects = new ArrayList<>();
        this.collectedObjectsRemoveQueue = new ArrayList<>();

        //Load the map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel04.png", world);
        this.gameObjects = ml.getTilesList();
        this.tilesArray = ml.getTileArray();
        this.player = ml.getPlayer();
        this.tilesArray = ml.getTileArray();
        this.mapTileSizeX = ml.getMapTileSizeX();
        this.mapTileSizeY = ml.getMapTileSizeY();
    }

    /** Renders objects from the game map. */
    public void render(World world, Vector3 mousePos, KeyboardController controller, SpriteBatch spriteBatch, float delta){

        processRemoveQueue();

        for(RenderableObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        for(CollectedPlatform collectedPlatform : collectedObjects)
            collectedPlatform.render(spriteBatch, delta, player.getBodyPos());

        getPlayer().render(this,world,mousePos,controller, spriteBatch, delta);
    }

    /** Removes the objects listed in the removequeue from the gameObjects list */
    private void processRemoveQueue(){

        //Game objects
        if(removeQueue.size() != 0){
            for(RenderableObject removeQueueObject : new ArrayList<>(removeQueue)){
                gameObjects.remove(removeQueueObject);
                world.destroyBody(((GameObject) removeQueueObject).getBody());
                removeQueue.remove(removeQueueObject);
            }
        }

        //Collected platforms
        if(collectedObjectsRemoveQueue.size() != 0){
            for(CollectedPlatform collectedPlatform : new ArrayList<>(collectedObjectsRemoveQueue)){
                collectedObjects.remove(collectedPlatform);
                world.destroyBody(collectedPlatform.getBody());
                collectedObjectsRemoveQueue.remove(collectedPlatform);
            }
        }
    }

    /** Adds an Resource object to the removeQueue*/
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

        //Check game map bounds
        if(x < 0 || x > this.mapTileSizeX-1 || y < 0 || y > this.mapTileSizeY-1)
            return true;

        //Check tilesArray /pre placed tiles
        if(this.tilesArray[x][y] != null)
            return true;

        //Check player queue
        for(int i = 0; i < player.getQueue().size(); i++){
            if(player.getQueue().get(i).getBody().getPosition().x == x ||
                    player.getQueue().get(i).getBody().getPosition().y == y)
                return true;
        }

        //Check player placed blocks
        for(int i = 0; i < player.getPlayerPlacedTiles().size(); i++){
            if(player.getPlayerPlacedTiles().get(i).getBody().getPosition().x == x &&
                    player.getPlayerPlacedTiles().get(i).getBody().getPosition().y == y)
                return true;
        }

        return false;
    }

    public void addCollectedObject(CollectedPlatform collectedPlatform){
        collectedObjects.add(collectedPlatform);
    }

    /** Adds collectable object to remove queue*/
    public void addToCollecableRemoveQueue(CollectedPlatform collectedPlatform){

        //Mark the platform as dead
        collectedPlatform.markedAsDeleted();

        //Find the resource in the list
        for(CollectedPlatform currentCollectedPlatform : collectedObjects){
            if(currentCollectedPlatform == collectedPlatform){
                this.collectedObjectsRemoveQueue.add(collectedPlatform); //Add to remove queue
            }
        }
    }
}
