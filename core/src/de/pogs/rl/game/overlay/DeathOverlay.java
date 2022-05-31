package de.pogs.rl.game.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.pogs.rl.utils.menu.Button;

public class DeathOverlay extends Overlay {

    private Button testButton;

    public DeathOverlay() {
        super();
        testButton = new Button(0, 0, 100, 100, Color.WHITE, Color.BLACK, Color.WHITE, "DEAD");
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
        // TODO Auto-generated method stub
        testButton.shapeRender(shapeRenderer);
        
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        testButton.update(delta);
    }



}
