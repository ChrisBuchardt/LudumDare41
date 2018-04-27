package com.minichri.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.Player;
import com.minichri.entity.RenderableObject;
import java.util.ArrayList;

public class GameMap {

    private ArrayList<RenderableObject> gameObjects;
    private int playerIndex;

    /** The constructor loads the map. */
    public GameMap(World world){

        //Load the map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel04.png", world);
        gameObjects = ml.getTilesList();
        playerIndex = ml.getPlayerIndex();
    }

    /** Renders objects from the game map. */
    public void render(SpriteBatch spriteBatch, float delta){

        for(RenderableObject renderableObject : gameObjects)
            renderableObject.render(spriteBatch, delta);

        getPlayer().render(spriteBatch, delta);
    }

    public Player getPlayer(){
        return (Player) gameObjects.get(playerIndex);
    }
}
