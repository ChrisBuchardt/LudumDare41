package com.minichri.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minichri.entity.Player;
import com.minichri.helpers.GameInfo;
import com.minichri.inventory.Inventory;
import com.minichri.inventory.InventoryListener;

public class InventorySlotUI extends Image implements InventoryListener {

    private final int index;
    private final Texture slotTexture;
    private final Texture selectedTexture;
    private final Image image;

    public InventorySlotUI(int index, Texture slotTexture, Texture selectedTexture, Image image) {
        super(slotTexture);
        this.index = index;
        this.slotTexture = slotTexture;
        this.selectedTexture = selectedTexture;
        this.image = image;
        Player.getInventory().addListener(this);

        // Force update
        onChange(Player.getInventory());
    }

    @Override
    public void onChange(Inventory inventory) {
        Drawable slotDrawable = new TextureRegionDrawable(new TextureRegion(inventory.getSelectedSlot() == index ? selectedTexture : slotTexture));
        setDrawable(slotDrawable);

        Item item = inventory.get(index);
        Drawable itemDrawable = item == null ? null : new TextureRegionDrawable(new TextureRegion(item.getType().getItemTexture()));
        image.setDrawable(itemDrawable);
    }
}
