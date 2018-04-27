package com.minichri.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public enum TileType {

    PLAYER("0026FFFF", null, false),
    WHITE_SPACE("FFFFFFFF", null, false),
    GROUND("000000FF", "tiles/ground_tiles.png", true),
    PLATFORM_BLUE("00FFFFFF", "tiles/platform_blue.png", false),
    PLATFORM_GREEN("00FF21FF", "tiles/platform_green.png", false),
    PLATFORM_PURPLE("FF00DCFF", "tiles/platform_purple.png", false),
    RESOURCE_BLUE("FFFFFFFF", "tiles/platform_resource_blue.png", false),
    RESOURCE_GREEN("FFFFFFFF", "tiles/platform_resource_green.png", false),
    RESOURCE_PURPLE("FFFFFFFF", "tiles/platform_resource_purple.png", false)
    ;

    private Color color;
    private Texture texture;
    private boolean isDirectionalTile;

    TileType(String color, String pathToTexture, boolean isDirectionalTile){
        this.color = Color.valueOf(color);
        this.isDirectionalTile = isDirectionalTile;

        if(pathToTexture != null)
            this.texture = new Texture(pathToTexture);

    }

    /** Takes a color and returns a matching type. Returns null of non matches.
     *  @param color a color to be matched.
     *  @return a tileType based on the color given. Returns null if non matches. */
    public static TileType getTypeFromColor(Color color){

        for (TileType type : TileType.values()) {
            if (type.color.equals(color)) return type;
        }

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

    /** An enum describing the tiles placement related to its surroundings */
    public enum TextureDirection {
        LEFT, MIDDLE, RIGHT, UNDER
    }
}
