package de.pogs.rl.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.utils.SpecialMath;

public class Bullet extends AbstractEntity {
    private Texture texture = new Texture(Gdx.files.internal("bucket.png"));
    private Sprite sprite;

    private float scale = 0.2f;
    private float speed;
    private float angle;
    public Vector2 position = new Vector2(0, 0);

    public Bullet(float posX, float posY, float angle, float speed) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position.set(posX, posY);
        this.angle = angle;
        this.speed = speed;
        sprite.setRotation(this.angle);
    }
    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        updatePosition(delta);
        sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - sprite.getHeight() / 2);
    }

    private void updatePosition(float delta) {
        position = position.add(SpecialMath.angleToVector(angle).scl(delta * speed));
    }

}
