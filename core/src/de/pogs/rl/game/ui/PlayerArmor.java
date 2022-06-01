package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class PlayerArmor extends HUDComponent {

    private float width;
    private float height;

    public float progress = 0f;
    private float currProg = 0f;
    private float progress_response = 0.1f;

    private BitmapFont font;

    public PlayerArmor() {
        super();
        progress = 0.15f;
    }

    @Override
    public void render(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.draw(batch, Math.round(GameScreen.INSTANCE.player.getArmor()) + " | " + Math.round(GameScreen.INSTANCE.player.getMaxArmor()), position.x + width * 0.02f, position.y + height - ((height - (font.getCapHeight())) / 2));

    }

    @Override
    public void update(float delta) {
        this.progress = GameScreen.INSTANCE.player.getArmor() / GameScreen.INSTANCE.player.getMaxArmor();
        position.set(HUDUtils.getBottomCenter().x - width / 2, HUDUtils.getBottomCenter().y + height * 1.3f);
        updateAngle();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.32);
        this.height = (float) (this.width * 0.08);
        font = RocketLauncher.INSTANCE.assetHelper.getFont("superstar", (int) Math.ceil(this.height * 0.6));
    }

    public void updateAngle() {
        currProg += (progress - currProg) * progress_response;
        if (currProg > 1)
            currProg = 1f;
        if (currProg < 0)
            currProg = 0;
    }

    @Override
    public void dispose() {

    }

    public Color color1 = new Color(0x349eebff);
    public Color color2 = new Color(0x2164c2ff);

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0x26262691));
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.rect(position.x + width * 0.01f, position.y + width * 0.01f,
                (width - width * 0.02f) * currProg, height - width * 0.02f, color2, color1, color1, color2);
    }

}
