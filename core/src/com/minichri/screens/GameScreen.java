package com.minichri.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.minichri.MainGame;
import com.minichri.World.MapLoader;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.GameInfo;
import com.minichri.physics.GameContactListener;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private MainGame game;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private InputProcessor inputProcessor;
    private World world;
    private Stage stage;
    private Box2DDebugRenderer debugRenderer;

    private ArrayList<TextureObject> gameMap;
    private int playerIndex;

    public GameScreen(MainGame game) {
        this.game = game;
        this.world = new World(new Vector2(0, -9.8f), true); //Creating the world with gravity
        world.setContactListener(new GameContactListener(world));

        this.spriteBatch = new SpriteBatch();

        this.camera = new OrthographicCamera(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.camera.zoom = GameInfo.ZOOM;
        this.camera.update();

        stage = new IngameStage(new FitViewport(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT));

        spriteBatch.setProjectionMatrix(camera.combined);

        //Load map
        MapLoader ml = new MapLoader();
        ml.loadLevelFromImage("level/testLevel04.png", world);
        gameMap = ml.getTilesList();
        playerIndex = ml.getPlayerIndex();

        debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {

        camera.position.x = gameMap.get(playerIndex).getBody().getPosition().x;
        camera.position.y = gameMap.get(playerIndex).getBody().getPosition().y;
        camera.update();

        world.step(delta,3,3);
        Gdx.gl.glClearColor(0f, 0.5f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.begin();

        //Render map
        for(TextureObject textureObject : gameMap)
            textureObject.render(spriteBatch);

        gameMap.get(playerIndex).render(spriteBatch);

        spriteBatch.end();

        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.begin();
        stage.getCamera().update();
        stage.act(delta);
        stage.draw();
        spriteBatch.end();



        spriteBatch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world,camera.combined);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
    }
}
