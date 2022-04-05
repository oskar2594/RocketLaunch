package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractEntity {
    public void update(float delta) {};
    public abstract void render(SpriteBatch batch);
}
