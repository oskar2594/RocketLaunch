package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.Text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends AbstractEntity {
    private Texture texture = new Texture(Gdx.files.internal("rakete.png"));
    private Sprite sprite;

    private float angle = 0;
    private float aimedAngle = 0;

    public Vector2 position = new Vector2(0, 0);

    public Player() {
        sprite = new Sprite(texture);
        sprite.setScale(0.2f);
        position.set(0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta, Input input) {
        updateAimedAngle();

        sprite.setPosition(position.x - (texture.getWidth() / 2), position.y + texture.getHeight() * sprite.getScaleY() / 2);
        sprite.setRotation(aimedAngle);
        // if (input.isKeyPressed(Keys.LEFT)) sprite.setPosition(sprite.getX() - speed *
        // delta, sprite.getY());

        // if (input.isKeyPressed(Keys.RIGHT)) sprite.setPosition(sprite.getX() + speed
        // * delta, sprite.getY());
    }

    private void updateAimedAngle() {
        if (Gdx.input.isTouched()) {
            aimedAngle = (float) Math.toDegrees((float) (Math.atan((Gdx.input.getX() - (float) (Gdx.graphics.getWidth() / 2))
                    / (float) (Gdx.input.getY() - Gdx.graphics.getHeight()))));

        }
    }

}
