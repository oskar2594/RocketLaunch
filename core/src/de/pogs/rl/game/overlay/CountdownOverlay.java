package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

/**
 * Countdown
 */
public class CountdownOverlay extends Overlay {
    private BitmapFont font;
    private long start;
    private int duration;
    private float alpha = 1f;
    private int pastTime;
    private int leftTime;
    
    public CountdownOverlay(int duration) {
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) (Gdx.graphics.getWidth() * 0.2));
        this.duration = duration;
        this.start = TimeUtils.millis();
    }

    @Override
    public void render(SpriteBatch batch) {
        if (leftTime == -1) {
            GameScreen.setPaused(false);
            GameScreen.getOverlayHandler().setOverlay((null));
            return;
        }

        font.draw(batch, "" + leftTime, -Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 5,
                Gdx.graphics.getWidth(), Align.center, false);

    }

    @Override
    public void update(float delta) {
        pastTime = (int) Math.floor((TimeUtils.millis() - start) / 1000);
        leftTime = duration - pastTime;
        alpha = 1f - ((TimeUtils.millis() - start) / 1000f) / duration;

    }

    @Override
    public void clear() {}

    @Override
    public void resize(int width, int height) {
        font = RocketLauncher.getAssetHelper().getFont("superstar", (int) (width * 0.2));
    }


    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0, 0, 0, 0.69f * alpha));
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
