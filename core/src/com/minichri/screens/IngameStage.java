package com.minichri.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.minichri.helpers.GameInfo;

public class IngameStage extends Stage {

    private Texture hotbarTexture = new Texture("ui/hotbar.png");

    public IngameStage(Viewport viewport) {
        super(viewport);

        OrthographicCamera cam = ((OrthographicCamera)getCamera());

        Image hotbar = new Image(hotbarTexture);
        hotbar.setOrigin(hotbarTexture.getWidth() / 2f, hotbarTexture.getHeight() /2f);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);

        table.bottom();
        table.add(hotbar).size(hotbarTexture.getWidth() * 4, hotbarTexture.getHeight() * 4).fill();

        addActor(table);
    }

    @Override
    public void dispose() {
        super.dispose();
        hotbarTexture.dispose();
    }
}
