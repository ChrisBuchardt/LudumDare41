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
import com.minichri.inventory.Inventory;

import static com.minichri.helpers.GameInfo.UI_SCALE;

public class IngameStage extends Stage {

    private Texture slotTexture = new Texture("ui/inventory_slot.png");
    private Texture slotTextureSelected = new Texture("ui/inventory_slot_selected.png");

    public IngameStage(Viewport viewport) {
        super(viewport);

        OrthographicCamera cam = ((OrthographicCamera)getCamera());

        Table hotbarTable = new Table();
        hotbarTable.setFillParent(true);
        hotbarTable.setDebug(true);
        hotbarTable.bottom();
        addActor(hotbarTable);
        float width = slotTexture.getWidth() * UI_SCALE;
        float height = slotTexture.getHeight() * UI_SCALE;
        Image[] images = new Image[Inventory.SIZE];
        for (int i = 0; i < Inventory.SIZE; i++) {
            images[i] = new Image();
            hotbarTable.add(images[i]).size(width, width).fill();
        }
        hotbarTable.row();
        for (int i = 0; i < Inventory.SIZE; i++) {
            InventorySlotUI slot = new InventorySlotUI(i, slotTexture, slotTextureSelected, images[i]);
            hotbarTable.add(slot).size(width, height).fill();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        slotTexture.dispose();
    }
}
