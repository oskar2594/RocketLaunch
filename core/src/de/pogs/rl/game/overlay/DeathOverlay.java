package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.menu.Button;

public class DeathOverlay extends Overlay {

    private Button testButton;

    public DeathOverlay() {
        super();
        testButton = new Button(0, -200, 300, 20, Color.WHITE, Color.BLACK, Color.WHITE, "DEAD");
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(SpriteBatch batch) {
        // TODO Auto-generated method stub
        testButton.render(batch);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        //DARK BACKGROUND
        shapeRenderer.setColor(new Color(0x000000b0));
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        testButton.shapeRender(shapeRenderer);

    }

    @Override
    public void update(float delta) {
        testButton.update(delta);
        if(testButton.isClicked()) {
            GameScreen.INSTANCE.paused = true;
        } else {
            GameScreen.INSTANCE.paused = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }



}
