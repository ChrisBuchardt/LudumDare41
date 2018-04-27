package com.minichri.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.minichri.helpers.GameInfo;

public class AnimatedObject extends GameObject implements RenderableObject {

    private Animation[] animation;
    private Texture texture;

    private final int NUMBER_OF_FRAMES;
    private final float ANIMATION_SPEED;

    private boolean isAnimationDirectionForward;
    private int currentFrame;
    private float stateTime;

    public AnimatedObject(World world, Vector2 pos, BodyDef bodyDef, FixtureDef fixtureDef, Texture texture, int numberOfFrames) {
        this( world, pos, bodyDef, fixtureDef, texture, numberOfFrames, 0.2f);
    }

    public AnimatedObject(World world, Vector2 pos, BodyDef bodyDef, FixtureDef fixtureDef, Texture texture, int numberOfFrames, float animationSpeed) {
        super(world, pos, bodyDef, fixtureDef);

        this.NUMBER_OF_FRAMES = numberOfFrames;
        this.ANIMATION_SPEED = animationSpeed;

        this.isAnimationDirectionForward = true;
        this.texture = texture;
        this.currentFrame = 0;
        this.stateTime = 0f;

        setUpAnimation();
    }

    /** Sets up the animation for the the object. */
    private void setUpAnimation(){

        this.animation = new Animation[NUMBER_OF_FRAMES];
        TextureRegion[][] resourceTextureSheet = TextureRegion.split(texture, GameInfo.TILE_SIZE, GameInfo.TILE_SIZE);

        for(int i = 0; i < NUMBER_OF_FRAMES; i++){
            animation[i] = new com.badlogic.gdx.graphics.g2d.Animation(ANIMATION_SPEED, resourceTextureSheet[0][i]);
        }
    }

    /** Render the AnimationObject */
    @Override
    public void render(SpriteBatch batch, float delta){
        stateTime += delta;
        updateFrame();

        Vector2 pos = body.getPosition();
        float width = getCurrentFrame().getRegionWidth() * GameInfo.PPM;
        float height = getCurrentFrame().getRegionHeight() * GameInfo.PPM;

        batch.draw(getCurrentFrame(), pos.x - width/2, pos.y - height/2, width / 2f, height / 2f, width, height, 1, 1, body.getAngle());;
        //batch.draw(getCurrentFrame(), body.getPosition().x - getCurrentFrame().getRegionWidth()/2f, body.getPosition().y - getCurrentFrame().getRegionHeight()/2f);
    }

    /** Updates the current frame number */
    private void updateFrame() {

        if(stateTime > ANIMATION_SPEED){ //Has enough time passed to switch frame?

            if(isAnimationDirectionForward){

                if(currentFrame+1 == NUMBER_OF_FRAMES){ //Have we reached the end of animation?
                    currentFrame--;
                    isAnimationDirectionForward = false;
                }else
                    currentFrame++;

            }else{ //Going backwards in frames
                if(currentFrame == 0){ //Have we reached the end of animation?
                    currentFrame++;
                    isAnimationDirectionForward = true;
                }else
                    currentFrame--;
            }

            stateTime -= ANIMATION_SPEED;
        }
    }

    public TextureRegion getCurrentFrame(){
        return (TextureRegion) animation[currentFrame].getKeyFrame(stateTime, true); //TODO THIS MIGHT BE A BUG
    }
}
