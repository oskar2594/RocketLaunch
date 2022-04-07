package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractEntity {
    protected Vector2 position = new Vector2(0, 0);
    public void update(float delta) {};
    public abstract void render(SpriteBatch batch);
    public Vector2 getPosition() {
        return position;
    }
}
