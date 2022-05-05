package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.GdxNativesLoader;

/**
 * RocketCamera
 */
public class RocketCamera extends OrthographicCamera {

    public RocketCamera() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.zoom = 1f;
    }

    public void resize(int width, int height) {
        this.viewportHeight = height;
        this.viewportWidth = width;
    }

    public void move() {
        this.position.set(GameScreen.INSTANCE.player.getPosition().x,
                GameScreen.INSTANCE.player.getPosition().y, 0);

        float playerSpeed = GameScreen.INSTANCE.player.getSpeed();
        float maxSpeed = GameScreen.INSTANCE.player.getMaxSpeed();
        float zoom = (float) easeInOut((playerSpeed / maxSpeed)) * 0.18f + 0.9f;
        this.zoom = Math.min(zoom, 1.1f);
        GameScreen.INSTANCE.resizeZoom((int) (Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom),
                (int) (Gdx.graphics.getHeight() * GameScreen.INSTANCE.camera.zoom));
    }

    private double easeInOut(double number) {
        if (number < 0.5) {
            return 4 * Math.pow(number, 3);
        } else {
            return (number - 1) * (2 * number - 2) * (2 * number - 2) + 1;
        }

    }

}