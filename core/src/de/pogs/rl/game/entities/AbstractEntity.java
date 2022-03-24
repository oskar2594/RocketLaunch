package de.pogs.rl.game.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractEntity {
    public void update(float delta, Input input) {};
    public abstract void render(SpriteBatch batch);
}
