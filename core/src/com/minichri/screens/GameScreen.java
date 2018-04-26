package com.minichri.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.Elements.Tile;
import com.minichri.MainGame;
import com.minichri.World.MapLoader;
import com.minichri.entity.Player;
import com.minichri.helpers.GameInfo;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private MainGame game;
    private Player player;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Texture img;
    private Texture img2;
    private InputProcessor inputProcessor;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    private ArrayList<Tile> gameMap; //TODO MIKKEL



    public  GameScreen(MainGame game){
        this.game = game;
        this.world = new World(new Vector2(0,-9.8f), true); //Creating the world with gravity

        // Temp images for testing screen/camera
        img = new Texture("tiles/dirt.png");
        img2 = new Texture("tiles/grass.png");

        player = new Player(world,new Vector2(1,1), BodyDef.BodyType.DynamicBody);
        this.spriteBatch = new SpriteBatch();

        this.camera = new OrthographicCamera(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.camera.zoom = GameInfo.ZOOM;
        this.camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);

        //Load map //TODO MIKKEL
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel01.png", world);
        gameMap = ml.getTilesList();
        debugRenderer = new Box2DDebugRenderer();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);

    }

    @Override
    public void render(float delta) {
        world.step(delta,3,3);
        Gdx.gl.glClearColor(0f, 0.5f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.begin();
        spriteBatch.draw(img2,0,img.getHeight());
        spriteBatch.draw(img,0,0);

        //TODO MIKKEL
        for(Tile tile : gameMap)
            tile.render(spriteBatch);

        player.render(spriteBatch);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world,camera.combined);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        img.dispose();

    }
}
