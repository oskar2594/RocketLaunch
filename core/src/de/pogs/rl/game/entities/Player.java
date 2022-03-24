package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.pogs.rl.RocketLauncher;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Entity {
    private Sprite sprite;
    private Texture texture;
    private ShapeRenderer shape;
    public Player(Texture texture) {
        sprite = new Sprite(texture);
        this.texture = texture;
        shape = new ShapeRenderer();
        sprite.setPosition(20, 20);
    }   
    
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
        // batch.draw(texture, 20, 20);


    }
}
