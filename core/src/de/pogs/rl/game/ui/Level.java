package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Level extends HUDComponent {

    private float width;
    private float height;
    private float radius;

    private BitmapFont levelFont;
    private BitmapFont lvlFont;
    private String levelString = "test";
    private int stringLength = 2;

    Level() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.1);
        this.height = (float) (this.width * 1);
        this.radius = this.width / 2;
        updateFonts();
    }

    public void updateFonts() {
        if (stringLength < 2)
            stringLength = 2;
        levelFont = RocketLauncher.INSTANCE.assetHelper.getFont("roboto",
                (int) Math.ceil(this.width * 0.5 * 2 / stringLength));

        lvlFont = RocketLauncher.INSTANCE.assetHelper.getFont("roboto",
                (int) Math.ceil(this.width * 0.1));
    }

    private int i = 0;
    private int oldLength = 0;

    @Override
    public void update(float delta) {
        position.set(HUDUtils.getBottomLeft().x + width / 2, HUDUtils.getBottomLeft().y + height / 2);
        i++;
        levelString = "" + i;
        if (oldLength != levelString.length()) {
            stringLength = levelString.length();
            updateFonts();
        }
        oldLength = levelString.length();

    }

    @Override
    public void render(SpriteBatch batch) {
        // font.draw(batch, str, x, y, targetWidth, halign, wrap)
        // font.draw(batch, str, x, y, start, end, targetWidth, halign, wrap, truncate)
        levelFont.draw(batch, levelString, position.x - width / 2,
                position.y + levelFont.getCapHeight() / 2 + lvlFont.getCapHeight() / 2, width,
                Align.center, false);
        lvlFont.draw(batch, "LVL", position.x - width / 2,
                position.y - levelFont.getCapHeight() / 2 - lvlFont.getCapHeight() / 2, width,
                Align.center, false);
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
        shapeRenderer.setColor(new Color(0x2626261d));
        partialCircle(shapeRenderer, position.x, position.y, radius, 0, 360, 2, 3);
        shapeRenderer.setColor(Color.WHITE);
        partialCircle(shapeRenderer, position.x, position.y, radius, 0, d, 2, 5);
    }

    private void partialCircle(ShapeRenderer shapeRenderer, float x, float y, float radius, float start, float degrees,
            float space, float width) {
        for (float i = start; i < degrees; i += space) {
            shapeRenderer.circle(x + Math.round(Math.cos(Math.toRadians(i)) * radius),
                    y + Math.round(Math.sin(Math.toRadians(i)) * radius),
                    width);
        }
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
