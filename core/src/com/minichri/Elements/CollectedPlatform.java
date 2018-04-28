package com.minichri.Elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.entity.TextureObject;
import com.minichri.helpers.GameInfo;
import com.minichri.helpers.TileType;

public class CollectedPlatform extends TextureObject{

    private TileType tileType;
    private boolean markedAsDeleted;

    public CollectedPlatform(World world, Vector2 pos, TileType tileType) {
        super(world, pos, DEFAULT_DYNAMIC_BODYDEF, DEFAULT_STATIC_FIXTUREDEF, new TextureRegion(tileType.getItemTexture()));

        this.tileType = tileType;
        body.getFixtureList().get(0).setSensor(true);
        body.setUserData(this);
        body.setGravityScale(0);
    }

    public void render(SpriteBatch batch, float delta, Vector2 playerPos) {
        //super.render(batch, delta);

        this.body.setLinearVelocity(new Vector2(playerPos).sub(this.body.getPosition()).scl(6));

        Vector2 pos = body.getPosition();
        float width = texture.getRegionWidth() * GameInfo.PPM;
        float height = texture.getRegionHeight() * GameInfo.PPM;
        batch.draw(texture, pos.x - width/2, pos.y - height/2, width / 2f, height / 2f, width, height, 1, 1, body.getAngle());
    }

    public void markedAsDeleted(){
        this.markedAsDeleted = true;
    }

    public boolean isMarkedAsDeleted() {
        return markedAsDeleted;
    }
}
