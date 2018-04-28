package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.Resource;
import com.minichri.Elements.Tile;
import com.minichri.KeyboardController;
import com.minichri.entity.GameObject;
import com.minichri.entity.Player;
import com.minichri.entity.RenderObject;
import com.minichri.screens.GameScreen;

import java.util.ArrayList;

public class GameMap {

    private ArrayList<RenderObject> gameObjects;
    private Tile[][] tilesArray;
    private int mapTileSizeX;
    private int mapTileSizeY;
    private ArrayList<RenderObject> removeQueue = new ArrayList<>();
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
        this.mapTileSizeX = ml.getMapTileSizeX();
        this.mapTileSizeY = ml.getMapTileSizeY();
    }

    /** Renders objects from the game map. */
    public void render(World world,Vector3 mousePos, KeyboardController controller, SpriteBatch spriteBatch, float delta){

        processRemoveQueue();

        for(RenderObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        getPlayer().render(this,world,mousePos,controller, spriteBatch, delta);
    }

    /** Removes the objects listed in the removequeue from the gameObjects list */
    private void processRemoveQueue(){
        if(removeQueue.size() != 0){
            for(RenderObject removeQueueObject : new ArrayList<>(removeQueue)){
                gameObjects.remove(removeQueueObject);
                world.destroyBody(removeQueueObject.getBody());
                removeQueue.remove(removeQueueObject);
            }
        }
    }

    /** Adds an object to the removeQueue*/
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

    /** Places a block in the world (both arrays).
     *  @param tileType the type if the tile you want to set.
     *  @param pos the position of the tile. */
    /*public void setTile(TileType tileType, Vector2 pos){

        Tile tile = new Tile(this.world, tileType, pos);

        int x = (int)(pos.x / GameInfo.TILE_SIZE);
        int y = (int)(pos.y / GameInfo.TILE_SIZE);

        //this.tilesArray[x][y] = tile;
        //this.gameObjects.add(tile);
    }*/
}
