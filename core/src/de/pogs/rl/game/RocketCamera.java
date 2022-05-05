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

        // System.out.println(GameScreen.INSTANCE.player.getSpeed());
        float playerSpeed = GameScreen.INSTANCE.player.getSpeed();
        float zoom = (float) ((Math.pow(playerSpeed, 3) + Math.pow(playerSpeed, 2)) * Math.pow(10, -9) + 0.9f);
        this.zoom = Math.min(zoom, 1.1f);
        System.out.println(zoom);
        GameScreen.INSTANCE.resizeZoom((int) (Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom),
                (int) (Gdx.graphics.getHeight() * GameScreen.INSTANCE.camera.zoom));
        // this.position.x = GameScreen.INSTANCE.player.getPosition().x;
        // this.position.y = GameScreen.INSTANCE.player.getPosition().y;
    }

}