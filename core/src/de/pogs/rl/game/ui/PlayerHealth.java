package de.pogs.rl.game.ui;

import javax.swing.text.Position;

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

    private BitmapFont font = RocketLauncher.INSTANCE.assetHelper.getFont("roboto");

    public PlayerHealth() {
        super();
        progress = 0.75f;
    }

    @Override
    public void update(float delta) {
        this.progress = GameScreen.INSTANCE.player.getHealth() / GameScreen.INSTANCE.player.getMaxHealth();
        position.set(HUDUtils.getBottomCenter().x - width / 2, HUDUtils.getBottomCenter().y);
        updateProg();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.3);
        this.height = (float) (this.width * 0.07);
    }

    public void updateProg() {
        currProg += (progress - currProg) * progress_response;
        if(currProg > 1) currProg = 1f;
        if(currProg < 0) currProg = 0;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.getData().setScale(50f / 100f);
        font.draw(batch, "test", position.x + 5, position.y + 5);
    }


    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0x26262691));
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x + width * 0.01f, position.y + width * 0.01f,
                (width - width * 0.02f) * currProg, height - width * 0.02f);
    }
}
