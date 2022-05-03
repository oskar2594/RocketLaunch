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
        super(Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
        this.zoom = 1f;
    }

    public void resize(int width, int height) {
        this.viewportHeight = height;
        this.viewportWidth = width;
    }

    public void move() {
        this.position.set(GameScreen.INSTANCE.player.getPosition().x,
                GameScreen.INSTANCE.player.getPosition().y, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.zoom += 0.01f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.zoom -= 0.01f;
        }

        if (this.zoom < 0)
            this.zoom = 0;

            GameScreen.INSTANCE.resize((int)(Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom), (int)(Gdx.graphics.getHeight() * GameScreen.INSTANCE.camera.zoom));
        // this.position.x = GameScreen.INSTANCE.player.getPosition().x;
        // this.position.y = GameScreen.INSTANCE.player.getPosition().y;
    }

}