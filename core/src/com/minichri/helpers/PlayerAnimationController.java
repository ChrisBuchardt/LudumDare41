package com.minichri.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.minichri.entity.Player;

public class PlayerAnimationController {

    private static final float FRAME_DURATION = 0.1f;
    private static final int FRAME_WIDTH = 16;
    private static final int FRAME_HEIGHT = 21;

    private Player player;
    private Texture texture;

    private TextureRegion[][] frames;
    private int lastDirNonNegative = 1;
    private int currentFrame = 0;
    private float timer = 0;

    public PlayerAnimationController(Player player, Texture texture) {
        this.player = player;
        this.texture = texture;

        frames = new TextureRegion[2][2];
        frames[0][0] = new TextureRegion(texture, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        frames[0][1] = new TextureRegion(texture, FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
        frames[1][0] = new TextureRegion(texture, 0, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
        frames[1][1] = new TextureRegion(texture, FRAME_WIDTH, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
    }

    public TextureRegion getTexture(int dir, boolean midAir, float delta) {
        if (dir == 0) {
            currentFrame = midAir ? 1 : 0;
            timer = 0;
        } else {
            timer += delta;
            lastDirNonNegative = dir == 1 ? 1 : 0;
            if (timer > FRAME_DURATION) {
                timer -= FRAME_DURATION;
                currentFrame = (currentFrame + 1) % 2;
            }
        }
        return frames[lastDirNonNegative][currentFrame];
    }
}
