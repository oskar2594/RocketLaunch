package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.PlayerStats;

public class Level extends HUDComponent {

    private float width;
    private float height;

    private BitmapFont levelFont;
    private BitmapFont smallFont;

    private GlyphLayout lLayout;
    private GlyphLayout sLayout;

    private String levelString = "";
    private String expString = "";

    Level() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.1);
        this.height = (float) (this.width);
        updateFonts();
    }

    public void updateFonts() {
        lLayout = new GlyphLayout();
        sLayout = new GlyphLayout();

        levelFont = RocketLauncher.INSTANCE.assetHelper.getFont("superstar",
                (int) Math.ceil(this.width * 0.75));

        smallFont = RocketLauncher.INSTANCE.assetHelper.getFont("superstar",
                (int) Math.ceil(this.width * 0.2));
    }


    @Override
    public void update(float delta) {
        position.set(HUDUtils.getBottomLeft().x, HUDUtils.getBottomLeft().y);
        float level = PlayerStats.getLevel();
        levelString = Integer.toString((int) Math.floor(level));
        expString = PlayerStats.getAccessExp() + "/" + PlayerStats.getNeedExp() + " EXP";

    }

    @Override
    public void render(SpriteBatch batch) {
        lLayout.setText(levelFont, levelString, Color.BLACK, width, Align.center, false);
        sLayout.setText(smallFont, "LVL", Color.BLACK, width, Align.center, false);
        levelFont.draw(batch, levelString, position.x,
                position.y + lLayout.height + sLayout.height * 1.5f, width, Align.left, false);
        smallFont.draw(batch, "LVL", position.x + lLayout.width * 1.1f,
                position.y + sLayout.height * 2.5f, width, Align.left, false);
        sLayout.setText(smallFont, expString, Color.BLACK, width, Align.center, false);
        smallFont.draw(batch, expString, position.x,
                position.y + sLayout.height, width, Align.left, false);
        // lvlFont.draw(batch, "LVL", position.x - width / 2,
        // position.y - levelFont.getCapHeight() / 2 - lvlFont.getCapHeight() / 2, width,
        // Align.center, false);

        // lvlFont.draw(batch, PlayerStats.getAccessExp() + "/" + PlayerStats.getNeedExp(),
        // position.x - width / 2, position.y - levelFont.getCapHeight() / 2
        // - lvlFont.getCapHeight() - expFont.getCapHeight(),
        // width, Align.center, false);
    }

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        // float percentage = PlayerStats.getCurrentLevelPercentage();

        // shapeRenderer.setColor(new Color(0x2626261d));
        // partialCircle(shapeRenderer, position.x, position.y, radius, 0, 360, 2, radius / 25);
        // shapeRenderer.setColor(Color.WHITE);
        // partialCircle(shapeRenderer, position.x, position.y, radius, 0, percentage * 360, 2,
        // radius / 15);
    }

    // private void partialCircle(ShapeRenderer shapeRenderer, float x, float y, float radius,
    // float start, float degrees, float space, float width) {
    // for (float i = start; i < degrees; i += space) {
    // int seg = 10;
    // if (i >= degrees - space || i == 0) {
    // seg = 20;
    // }
    // shapeRenderer.circle(x + Math.round(Math.cos(Math.toRadians(i)) * radius),
    // y + Math.round(Math.sin(Math.toRadians(i)) * radius), width, seg);
    // }
    // }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
