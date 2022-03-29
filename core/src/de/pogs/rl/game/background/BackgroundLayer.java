package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundLayer {
    private Sprite sprite;
    public BackgroundLayer() {
        sprite = new Sprite();
    }

    public void update() {

    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
