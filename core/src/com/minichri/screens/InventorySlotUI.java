package com.minichri.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.minichri.entity.Player;
import com.minichri.helpers.GameInfo;
import com.minichri.inventory.Inventory;
import com.minichri.inventory.InventoryListener;

public class InventorySlotUI extends Image implements InventoryListener {

    private final int index;
    private final Image image;

    public InventorySlotUI(int index, Texture slotTexture, Image image) {
        super(slotTexture);
        this.index = index;
        this.image = image;
        Player.getInventory().addListener(this);
    }

    @Override
    public void onChange(Inventory inventory) {

    }
}
