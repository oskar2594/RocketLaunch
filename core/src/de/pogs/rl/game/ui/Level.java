package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Level extends HUDComponent {

    Level() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(float delta) {
        position.set(HUDUtils.getCenter().x, HUDUtils.getCenter().y);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private float d = 0;
    private float c = 1;

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        d += c;
        if (d == 360)
            c = -1;
        if (d == 0)
            c = 1;
        shapeRenderer.setColor(new Color(0x2626261c));
        partialCircle(shapeRenderer, position.x, position.y, 50, 0, 360, .5f, 3);
        shapeRenderer.setColor(Color.WHITE);
        partialCircle(shapeRenderer, position.x, position.y, 50, 0, d, .5f, 5);
    }

    private void partialCircle(ShapeRenderer shapeRenderer, float x, float y, float radius, float start, float degrees,
            float space, float width) {
        for (float i = start; i < degrees; i += space) {
            shapeRenderer.circle(Math.round(Math.cos(Math.toRadians(i)) * radius),
                    Math.round(Math.sin(Math.toRadians(i)) * radius),
                    width);
        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
