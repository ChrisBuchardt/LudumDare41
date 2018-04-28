package com.minichri;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.minichri.screens.GameScreen;

public class MainGame extends Game {
	SpriteBatch batch;
	private GameScreen gameScreen;
	Music song1;

	@Override
	public void create () {
		song1 = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Space_Music.wav"));
		song1.setLooping(true);
		song1.play();
		gameScreen = new GameScreen(this);
		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {
		song1.dispose();

	}
}
