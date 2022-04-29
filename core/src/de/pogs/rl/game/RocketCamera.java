package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * RocketCamera
 */
public class RocketCamera extends OrthographicCamera {

    public RocketCamera() {
        super(Gdx.graphics.getWidth() * 1, Gdx.graphics.getHeight() * 1);
        this.zoom = 2;
    }

    public void resize (int width, int height) {
        this.viewportHeight = height;
        this.viewportWidth = width;
    }

    public void move() {
        this.translate(GameScreen.INSTANCE.player.getPosition().x - this.position.x,
                GameScreen.INSTANCE.player.getPosition().y - this.position.y);

                if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    this.zoom += 0.1f;
                } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    this.zoom -= 0.1f;
                }
         // this.position.x = GameScreen.INSTANCE.player.getPosition().x;
        // this.position.y = GameScreen.INSTANCE.player.getPosition().y;
    }


}