package de.pogs.rl.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;

public class Debug extends HUDComponent {
    private int width = 200;
    private int height = 200;
    private BitmapFont font = RocketLauncher.INSTANCE.assetHelper.getFont("roboto");

    Debug() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(float delta) {
        position.set(HUDUtils.getTopLeft().x, HUDUtils.getTopCenter().y);
        
    }

    @Override
    public void render(SpriteBatch batch) {
        font.setColor(Color.WHITE);
        font.getData().setScale(2 / 10f);
        font.draw(batch, "test\ntestttttttttttt", position.x, position.y);
        
    }

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
}
