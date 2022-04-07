package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * RocketCamera
 */
public class RocketCamera extends OrthographicCamera {

    public RocketCamera() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void move() {
        this.position.x = GameScreen.INSTANCE.player.getPosition().x;
        this.position.y = GameScreen.INSTANCE.player.getPosition().y;
    }

}