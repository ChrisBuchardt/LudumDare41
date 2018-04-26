package com.minichri.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.minichri.MainGame;

public class GameScreen implements Screen {

    private MainGame game;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Texture img;
    private Texture img2;
    private InputProcessor inputProcessor;


    public  GameScreen(MainGame game){
        this.game = game;
        // Temp images for testing screen/camera
        img = new Texture("tiles/dirt.png");
        img2 = new Texture("tiles/grass.png");

        this.spriteBatch = new SpriteBatch();

        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(camera.combined);

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputProcessor);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 1f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        spriteBatch.begin();
        spriteBatch.draw(img2,0,img.getHeight());
        spriteBatch.draw(img,0,0);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(camera.combined);

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
