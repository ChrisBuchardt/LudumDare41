package com.minichri.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.minichri.KeyboardController;
import com.minichri.MainGame;
import com.minichri.World.GameMap;
import com.minichri.entity.Player;
import com.minichri.helpers.GameInfo;
import com.minichri.physics.ContactManager;

public class GameScreen implements Screen {

    private MainGame game;
    private SpriteBatch spriteBatch;
    private Texture background;
    private OrthographicCamera camera;
    private Stage stage;
    private GameMap gameMap;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    public Vector3 mousePos;
    public KeyboardController inputProcessor;
    public World world;


    public GameScreen(MainGame game) {



        mousePos = new Vector3 (0,0,0);
        this.inputProcessor = new KeyboardController();
        this.game = game;
        this.world = new World(new Vector2(0, -18f), true); //Creating the world with gravity
        this.gameMap = new GameMap(this); //Load map
        world.setContactListener(new ContactManager(world, gameMap));

        this.spriteBatch = new SpriteBatch();


        //Position of the mouse and camera
        this.camera = new OrthographicCamera(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.camera.zoom = GameInfo.ZOOM;
        this.camera.update();
        camera.position.x = gameMap.getPlayer().getBodyPos().x;
        camera.position.y = gameMap.getPlayer().getBodyPos().y;

        stage = new IngameStage(new FitViewport(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT));
        font = new BitmapFont();
        font.getData().setScale(2,2);
        spriteBatch.setProjectionMatrix(camera.combined);
        background = new Texture("background.png");


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {

        Vector2 playerBodyPos = gameMap.getPlayer().getBody().getPosition();
        Vector2 diff = new Vector2(playerBodyPos.x - camera.position.x, playerBodyPos.y - camera.position.y).scl(0.1f);
        camera.position.x += diff.x;
        camera.position.y += diff.y;

        //Translats the mouse screen position to the world position.
        mousePos.x = Gdx.input.getX();
        mousePos.y = Gdx.input.getY();
        mousePos.z = 0;
        camera.unproject(mousePos);

        camera.update();

        world.step(delta, 3, 3);
        Gdx.gl.glClearColor(0f, 0.0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        float bgX = gameMap.getMapTileSizeX()/2f - background.getWidth()/2f;
        float bgY = gameMap.getMapTileSizeY()/2f - background.getHeight()/2f;
        spriteBatch.draw(background, bgX, bgY);

        gameMap.render(world,mousePos,inputProcessor,spriteBatch, delta);

        spriteBatch.end();

        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.begin();
        stage.getCamera().update();
        stage.act(delta);
        stage.draw();
        spriteBatch.end();

        spriteBatch.begin();
        if (Player.getInventory().getRemainingResources()==0){
            font.draw(spriteBatch,"Congratulations you have gathered all the platforms!\n"+"You completed the game with "+ gameMap.getPlayer().getDeathCounter() + " deaths.",camera.position.x-60+GameInfo.SCREEN_HEIGHT/2,camera.position.y+GameInfo.SCREEN_HEIGHT/2);
        }
        if (gameMap.getPlayer().getPlayerPlacedTiles().size()>0){
            font.draw(spriteBatch,"Press Q to recollect platforms",camera.position.x-60+GameInfo.SCREEN_HEIGHT/2,camera.position.y+GameInfo.SCREEN_HEIGHT/9);
        }
        spriteBatch.end();



        spriteBatch.setProjectionMatrix(camera.combined);

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
        stage.dispose();
    }
}
