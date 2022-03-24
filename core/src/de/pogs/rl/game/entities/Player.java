package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends AbstractEntity {
    private float speed = 100;
    private Sprite sprite;
    public Player(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(-80, -440);
        sprite.setScale(0.2f);
    }   
    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
    @Override
    public void update(float delta, Input input) {
        if (input.isKeyPressed(Keys.LEFT)) sprite.setPosition(sprite.getX() - speed * delta, sprite.getY());
        
        if (input.isKeyPressed(Keys.RIGHT)) sprite.setPosition(sprite.getX() + speed * delta, sprite.getY());
    }

}
