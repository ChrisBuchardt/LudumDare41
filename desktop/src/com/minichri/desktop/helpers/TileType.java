package com.minichri.desktop.helpers;

import com.badlogic.gdx.graphics.Color;

public enum TileType {
    WHITE_SPACE(Constants.WHITE_SPACE_COLOR),
    GROUND(Constants.GROUND_COLOR);

    private Color color;

    TileType(Color color){
        this.color = color;
    }

    private static class Constants{
        static final Color WHITE_SPACE_COLOR = Color.valueOf("#"); //TODO
        static final Color GROUND_COLOR = Color.valueOf("#"); //TODO
    }
}
