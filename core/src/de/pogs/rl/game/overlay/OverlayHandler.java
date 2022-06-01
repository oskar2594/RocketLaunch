package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import de.pogs.rl.game.GameScreen;

public class OverlayHandler {

    private Overlay currentOverlay;
    private ShapeRenderer shapeRenderer;

    public OverlayHandler() {

    }

    public void setOverlay(Overlay overlay) {
        if (currentOverlay != null)
            currentOverlay.clear();
        currentOverlay = overlay;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

    }

    public void render(SpriteBatch batch) {
        if (currentOverlay == null)
            return;
        currentOverlay.render(batch);
    }

    public void shapeRender(Matrix4 combined) {
        if (currentOverlay == null)
            return;
        shapeRenderer.setProjectionMatrix(combined);
        shapeRenderer.begin();
        currentOverlay.renderShape(shapeRenderer);
        shapeRenderer.end();

    }

    public void update(float delta) {
        if (currentOverlay == null)
            return;
        currentOverlay.update(delta);
    }

    public void resize(int width, int height) {
        if (currentOverlay == null)
            return;
        currentOverlay.resize(width, height);
    }

}
