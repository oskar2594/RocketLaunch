package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class PlayerHealth extends HUDComponent {

    private float width;
    private float height;

    public float progress = 0f;
    private float currProg = 0f;
    private float progress_response = 0.1f;

    private BitmapFont font;

    public PlayerHealth() {
        super();
        progress = 0.75f;
    }

    @Override
    public void update(float delta) {
        this.progress = GameScreen.getPlayer().getHealth() / GameScreen.getPlayer().getMaxHealth();
        position.set(HUDUtils.getBottomCenter().x - width / 2, HUDUtils.getBottomCenter().y);
        updateProg();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.32);
        this.height = (float) (this.width * 0.07);
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) Math.ceil(this.height * 0.7));
    }

    public void updateProg() {
        currProg += (progress - currProg) * progress_response;
        if (currProg > 1)
            currProg = 1f;
        if (currProg < 0)
            currProg = 0;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.draw(batch,
                Math.round(GameScreen.getPlayer().getHealth()) + " | "
                        + Math.round(GameScreen.getPlayer().getMaxHealth()),
                position.x + width * 0.02f,
                position.y + height - ((height - (font.getXHeight())) / 2));
    }

    public Color color1 = new Color(0xc74234ff);
    public Color color2 = new Color(0xc92626ff);

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0x26262691));
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x + width * 0.01f, position.y + width * 0.01f,
                (width - width * 0.02f) * currProg, height - width * 0.02f, color1, color2, color2,
                color1);
    }

}
