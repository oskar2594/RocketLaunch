package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Debug extends HUDComponent {
    private BitmapFont font = RocketLauncher.INSTANCE.assetHelper.getFont("roboto");
    private String text = "";
    Debug() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(float delta) {
        text = "X: " + Math.round(GameScreen.INSTANCE.camera.position.x) + " | Y: " + Math.round(GameScreen.INSTANCE.camera.position.y) + "\n";
        text += "FPS: " + Gdx.graphics.getFramesPerSecond();
    }

    @Override
    public void render(SpriteBatch batch) {
        position.set(HUDUtils.getTopLeft().x, HUDUtils.getTopLeft().y);
        font.setColor(Color.WHITE);
        font.getData().setScale(2 / 10f);
        font.draw(batch, text, position.x, position.y);
        
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
