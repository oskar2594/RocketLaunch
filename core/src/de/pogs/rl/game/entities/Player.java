package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
    private Sprite sprite;
    private Texture texture;
    public Player() {
        sprite = new Sprite();
        texture =  new Texture(Gdx.files.internal("rakete.png"));
        sprite.setTexture(texture);
    }   
    
    public void render(float delta, SpriteBatch batch) {
        batch.draw(texture, 50, 50, 100, 100, texture.getWidth(), texture.getHeight());
    }
}
