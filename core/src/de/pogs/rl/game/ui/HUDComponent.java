package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class HUDComponent {
    protected Sprite sprite;
    protected Vector2 position;

    public HUDComponent() {
        position = new Vector2(0, 0);
        sprite = new Sprite();
        resize(HUD.getWidth(), HUD.getHeight());
    }

    public abstract void resize(float width, float height);

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public abstract void shapeRender(ShapeRenderer shapeRenderer);

    public abstract void dispose();
}
