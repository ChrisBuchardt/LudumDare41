package com.minichri.Elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.RenderableObject;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.TileType;

public class CollectedPlatform extends TextureObject implements RenderableObject{

    private TileType tileType;
    private boolean markedAsDeleted;

    public CollectedPlatform(World world, Vector2 pos, TileType tileType) {
        super(world, pos, DEFAULT_DYNAMIC_BODYDEF, DEFAULT_STATIC_FIXTUREDEF, new TextureRegion(tileType.getBlockTexture()));

        this.tileType = tileType;
        body.getFixtureList().get(0).setSensor(true);
        body.setUserData(this);
        body.setGravityScale(0);
    }

    public void render(SpriteBatch batch, float delta, Vector2 playerPos) {
        super.render(batch, delta);

        this.body.setLinearVelocity(new Vector2(playerPos).sub(this.body.getPosition()).scl(3));
    }

    public void markedAsDeleted(){
        this.markedAsDeleted = true;
    }

    public boolean isMarkedAsDeleted() {
        return markedAsDeleted;
    }
}
