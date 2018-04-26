package com.minichri;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.minichri.screens.GameScreen;

public class MainGame extends Game {
	SpriteBatch batch;
	private GameScreen gameScreen;


	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		this.setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();

	}
	
	@Override
	public void dispose () {

	}
}
