package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.CollectedPlatform;
import com.minichri.Elements.Decoration;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.entity.Player;
import com.minichri.entity.RenderObject;
import com.minichri.screens.GameScreen;

import java.util.ArrayList;

public class GameMap {

    private ArrayList<CollectedPlatform> collectedObjects;
    private ArrayList<CollectedPlatform> collectedObjectsRemoveQueue;
    private ArrayList<RenderObject> gameObjects;
    private ArrayList<Decoration> decorationList;
    private Object[][] tilesArray;
    private int mapTileSizeX;
    private int mapTileSizeY;
    private ArrayList<RenderObject> removeQueue = new ArrayList<>();
    private Player player;
    private World world;

    /** The constructor loads the map. */
    public GameMap(GameScreen screen){

        this.world = screen.world;
        this.collectedObjects = new ArrayList<>();
        this.collectedObjectsRemoveQueue = new ArrayList<>();
        this.decorationList = new ArrayList<>();

        //Load the map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/thelevel.png", world);
        this.gameObjects = ml.getTilesList();
        this.tilesArray = ml.getTileArray();
        this.player = ml.getPlayer();
        this.tilesArray = ml.getTileArray();
        this.mapTileSizeX = ml.getMapTileSizeX();
        this.mapTileSizeY = ml.getMapTileSizeY();
        this.decorationList = ml.getDecorationList();
    }

    /** Renders objects from the game map. */
    public void render(World world, Vector3 mousePos, KeyboardController controller, SpriteBatch spriteBatch, float delta){

        processRemoveQueue();

        if(isPlayerOutOfBounds())
            player.kill();

        for(RenderObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        for(CollectedPlatform collectedPlatform : collectedObjects)
            collectedPlatform.render(spriteBatch, delta, player.getBodyPos());

        for(Decoration decoration : decorationList)
            decoration.render(spriteBatch, delta);

        getPlayer().render(this,mousePos,controller, spriteBatch, delta);
    }

    /** Removes the objects listed in the removequeue from the gameObjects list */
    private void processRemoveQueue(){

        //Game objects
        if(removeQueue.size() != 0){
            for(RenderObject removeQueueObject : new ArrayList<>(removeQueue)){
                gameObjects.remove(removeQueueObject);
                world.destroyBody(removeQueueObject.getBody());
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
        for(RenderObject renderableObject : gameObjects){
            if(renderableObject == resource){
                this.removeQueue.add(renderableObject); //Add to remove queue
            }
        }
    }

    public Player getPlayer(){
        return this.player;
    }

    /** Checks player has gone into the void = kill (Out of bounds) */
    public boolean isPlayerOutOfBounds(){

        int playerTilePosX = Math.round(player.getBody().getPosition().x);
        int playerTilePosY = Math.round(player.getBody().getPosition().y);

        //Check game map bounds
        return playerTilePosX < 0 || playerTilePosX > this.mapTileSizeX - 1 || playerTilePosY < 0 || playerTilePosY > this.mapTileSizeY - 1;
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
        //this.tilesArray[x][y] = tile;
        //this.gameObjects.add(tile);
    }



    public Vector2 getMapTileSize() {
        return new Vector2(mapTileSizeX, mapTileSizeY);
    }

    public int getMapTileSizeX() {
        return mapTileSizeX;
    }

    public int getMapTileSizeY() {
        return mapTileSizeY;
    }
}
