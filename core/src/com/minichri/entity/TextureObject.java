package com.minichri.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.helpers.GameInfo;

public class TextureObject extends GameObject implements RenderableObject {

    protected TextureRegion texture;

    /** An GameObject that always have a textre drawn at the body's position. */
    public TextureObject(World world, Vector2 pos, BodyDef bodyDef, FixtureDef fixtureDef, TextureRegion texture) {
        super(world, pos, bodyDef, fixtureDef);
        this.texture = texture;
    }

    /** Render the TextureObjects texture at the body's position. */
    @Override
    public void render(SpriteBatch batch, float delta) {
        Vector2 pos = body.getPosition();
        float width = texture.getRegionWidth() * GameInfo.PPM;
        float height = texture.getRegionHeight() * GameInfo.PPM;
        batch.draw(texture, pos.x - width/2, pos.y - height/2, width / 2f, height / 2f, width, height, 1, 1, body.getAngle());
    }
}
