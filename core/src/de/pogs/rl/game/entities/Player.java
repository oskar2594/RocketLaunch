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
    public Player() {
        sprite = new Sprite();
        texture = new Texture(Gdx.files.internal("rakete.png"));
        sprite.setTexture(texture);
        shape = new ShapeRenderer();
    }   
    
    public void render(float delta, SpriteBatch batch) {
        sprite.draw(batch);
        sprite.setPosition(0, 0);
        RocketLauncher.INSTANCE.batch.draw(texture, 64, 64);
        shape.begin(ShapeType.Line);
		shape.line(0,0,50,50);
		shape.end();

        System.out.println(texture.toString());
    }
}
