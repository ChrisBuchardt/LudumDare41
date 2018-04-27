package com.minichri;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyboardController implements InputProcessor{


    public boolean a, s, d, w, leftClick, rightClick, space;


    @Override
    public boolean keyDown(int keycode) {

        boolean keyProcessed = false;
        switch (keycode){
            case Input.Keys.SPACE:
                space = true;
                keyProcessed = true;
                break;
            case Input.Keys.W:
                w = true;
                keyProcessed = true;
                break;
            case Input.Keys.A:
                a = true;
                keyProcessed = true;
                break;
            case Input.Keys.S:
                s = true;
                keyProcessed = true;
                break;
            case Input.Keys.D:
                d = true;
                keyProcessed = true;
        }
        return keyProcessed;
    }
    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;
        switch (keycode){
            case Input.Keys.SPACE:
                space = false;
                keyProcessed = true;
                break;
            case Input.Keys.W:
                w = false;
                keyProcessed = true;
                break;
            case Input.Keys.A:
                a = false;
                keyProcessed = true;
                break;
            case Input.Keys.D:
                d = false;
                keyProcessed = true;
                break;
            case Input.Keys.S:
                s = false;
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    //Mouse click

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean clickProcessed = false;
        if(button == 0){
            leftClick = true;
            clickProcessed = true;
        }else if(button == 1){
            rightClick = true;
            clickProcessed = true;
        }
        return clickProcessed;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean clickProcessed = false;
        if(button == 0){
            leftClick = false;
            clickProcessed = true;
        }else if(button == 1){
            rightClick = false;
            clickProcessed = true;
        }
        return clickProcessed;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
