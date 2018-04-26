package com.minichri.desktop.helpers;

import com.badlogic.gdx.graphics.Color;

public enum TileType {
    WHITE_SPACE(Constants.WHITE_SPACE_COLOR),
    GROUND(Constants.GROUND_COLOR);

    private Color color;

    TileType(Color color){
        this.color = color;
    }

    /** Takes a color and returns a matching type. Returns null of non matches.
     *  @param color a color to be matched.
     *  @return a tileType based on the color given. Returns null if non matches. */
    public static TileType getTypeFromColor(Color color){

        if(color.equals(TileType.WHITE_SPACE.getColor()))
            return WHITE_SPACE;
        if(color.equals(TileType.GROUND.getColor()))
            return GROUND;
        else
            return null;
    }

    public Color getColor() {
        return color;
    }

    /** Color constants. */
    private static class Constants{
        static final Color WHITE_SPACE_COLOR = Color.valueOf("#"); //TODO
        static final Color GROUND_COLOR = Color.valueOf("#"); //TODO
    }
}
