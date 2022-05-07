package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class HUDCamera extends OrthographicCamera {

    public HUDCamera() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.zoom = 1f;
    }

    public void resize(int width, int height) {
        this.viewportHeight = height;
        this.viewportWidth = width;
    }

}