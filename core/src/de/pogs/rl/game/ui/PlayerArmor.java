package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerArmor extends HUDComponent {

    private float width;
    private float height;

    public float progress = 0f;
    private float currProg = 0f;
    private float progress_response = 0.1f;

    public PlayerArmor() {
        super();
        progress = 0.15f;
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void update(float delta) {
        position.set(HUDUtils.getBottomCenter().x - width / 2, HUDUtils.getBottomCenter().y + height * 1.5f);
        updateAngle();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.3);
        this.height = (float) (this.width * 0.07);
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

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0x26262691));
        shapeRenderer.rect(position.x, position.y, width, height);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(position.x + width * 0.01f, position.y + width * 0.01f,
                (width - width * 0.02f) * currProg, height - width * 0.02f);
    }

    public void setProg(float prog) {
        progress = prog;
    }
}
