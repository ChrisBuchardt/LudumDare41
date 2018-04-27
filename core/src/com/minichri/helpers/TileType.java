package com.minichri.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum TileType {
    PLAYER(Constants.PLAYER_COLOR, false, null),
    WHITE_SPACE(Constants.WHITE_SPACE_COLOR, false, null),
    GROUND(Constants.GROUND_COLOR, true, "tiles/groundTiles.png");

    private Color color;
    private Texture texture;
    private boolean isDirectionalTile;

    TileType(Color color, boolean isDirectionalTile, String pathToTexture){
        this.color = color;
        this.isDirectionalTile = isDirectionalTile;

        if(pathToTexture != null)
            this.texture = new Texture(pathToTexture);
    }

    /** Takes a color and returns a matching type. Returns null of non matches.
     *  @param color a color to be matched.
     *  @return a tileType based on the color given. Returns null if non matches. */
    public static TileType getTypeFromColor(Color color){

        if(color.equals(TileType.WHITE_SPACE.getColor()))
            return WHITE_SPACE;
        if(color.equals(TileType.GROUND.getColor()))
            return GROUND;
        if(color.equals(TileType.PLAYER.getColor()))
            return PLAYER;
        else
            return null;
    }

    public Texture getTexture() {
        return texture;
    }

    public Color getColor() {
        return color;
    }

    public boolean isDirectionalTile() {
        return isDirectionalTile;
    }

    /** Color constants. */
    private static class Constants{
        static final Color PLAYER_COLOR = Color.valueOf("#26ffff00");
        static final Color WHITE_SPACE_COLOR = Color.valueOf("#FFFFFFFF");
        static final Color GROUND_COLOR = Color.valueOf("#0000FF00");
    }

    /** An enum describing the tiles placement related to its surroundings */
    public enum TileTextureDirection {
        LEFT, MIDDLE, RIGHT, UNDER;
    }
}
