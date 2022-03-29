package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends AbstractEntity {
    private Texture texture = new Texture(Gdx.files.internal("rakete.png"));
    private Sprite sprite;

    private float scale = 0.2f;

    private float angle = 0;
    private float aimedAngle = 0;
    private float angle_response = 1;

    public Vector2 position = new Vector2(0, 0);

    public Player() {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position.set(0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta, Input input) {
        updateAimedAngle();
        System.out.println( sprite.getWidth());
        sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - sprite.getHeight() / 2);
        sprite.setRotation(aimedAngle);

    }

    private void updateAimedAngle() {

        aimedAngle = (float) Math
                .toDegrees((float) (Math.atan((Gdx.input.getX() - (float) (Gdx.graphics.getWidth() / 2))
                        / (float) (Gdx.input.getY() - Gdx.graphics.getHeight()))));

    }

    private void updateAngle(float delta) {
        angle = angle + (aimedAngle - angle) * delta * angle_response;
    }
}
