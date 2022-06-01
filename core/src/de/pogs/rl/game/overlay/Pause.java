package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Pause extends Overlay{
    

    public Pause() {
        super();
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        //DARK BACKGROUND
        shapeRenderer.setColor(new Color(0x000000b0));
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void render(SpriteBatch batch) {
        
        
    }

    @Override
    public void resize(int width, int height) {
        
        
    }

}
