package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends AbstractEntity {
    private Sprite sprite;
    public Player(Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(20, 20);
    }   
    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
