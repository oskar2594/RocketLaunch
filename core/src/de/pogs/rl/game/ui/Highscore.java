package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.PlayerStats;

public class Highscore extends HUDComponent {

    private BitmapFont font;

    private String text = "";

    public Highscore() {
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) (Gdx.graphics.getHeight() * 0.03));
    }

    @Override
    public void resize(float width, float height) {
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) (Gdx.graphics.getHeight() * 0.03));

    }

    @Override
    public void update(float delta) {
        text = "Highscore: " + PlayerStats.getLevelFromExp(PlayerStats.getHighscore()) + " LVL";
    }

    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch, text, -Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - HUD.getBorder(),
                Gdx.graphics.getWidth() - HUD.getBorder(), Align.right, false);
    }

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {}

    @Override
    public void dispose() {}

}
