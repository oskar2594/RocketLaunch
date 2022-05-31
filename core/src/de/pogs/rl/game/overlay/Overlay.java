package de.pogs.rl.game.overlay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Overlay {


    private boolean isActive = false;

    public Overlay() {
        isActive = true;
    }

    public abstract void clear();

    public abstract void render(SpriteBatch batch);

    public abstract void renderShape(ShapeRenderer shapeRenderer);

    public abstract void update(float delta);
 
}
