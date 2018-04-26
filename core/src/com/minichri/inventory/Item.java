package com.minichri.inventory;

import com.minichri.helpers.TileType;

public class Item {

    private TileType type;

    public Item(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }
}
