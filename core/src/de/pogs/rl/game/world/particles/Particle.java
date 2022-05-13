package de.pogs.rl.game.world.particles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.utils.SpecialMath.Vector2;

public class Particle {

    public boolean isDead = false;

    private Vector2 position;
    private Vector2 velocity;
    private Sprite sprite;
    private float speed;
    private float size;
    private float timeSize;
    private float duration;
    private float timeAlpha;
    private float alpha;
    private float time;
    private float angle;
    private float offsetAngle = 0;
    private float textureCorrectionAngle = 0;

    public Particle(Texture texture, Vector2 source, Vector2 velocity, float offsetAngle,
            float minAngle, float maxAngle, float minSpeed, float maxSpeed, float minSize,
            float maxSize, float alpha, float duration, float timeAlpha, float timeSize) {
        sprite = new Sprite(texture);
        sprite.setPosition(source.getX(), source.getY());
        sprite.setAlpha(0);
        this.size = getSize(minSize, maxSize);
        if (texture.getWidth() >= texture.getHeight()) {
            sprite.setSize(texture.getWidth() / texture.getHeight() * size, size);
            this.textureCorrectionAngle = 0;
        } else {
            sprite.setSize(size, texture.getHeight() / texture.getWidth() * size);
            this.textureCorrectionAngle = 90;
        }
        this.timeSize = timeSize;
        this.offsetAngle = offsetAngle;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.angle = getAngle(minAngle, maxAngle);
        this.velocity = velocity;
        sprite.setRotation(this.angle + offsetAngle + textureCorrectionAngle);
        this.position = new Vector2(source.getX(), source.getY());
        this.speed = getSpeed(minSpeed, maxSpeed);
        this.duration = duration;
        this.timeAlpha = timeAlpha;
        this.alpha = alpha;
        this.time = 0;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(float delta) {
        time += delta;
        position = position.add(angleToVector(angle + offsetAngle).mul(delta * speed))
                .add(velocity.mul(-.001f));
        if (time >= duration) {
            isDead = true;
            return;
        }
        if (time > timeAlpha) {
            float alp = alpha - alpha * (time - timeAlpha / duration - timeAlpha);
            if (alp > 1)
                alp = 0;
            sprite.setAlpha(alp);
        } else {
            sprite.setAlpha(alpha);
        }
        if (time <= timeSize) {
            sprite.setScale(time / timeSize);
        }
        // float currentSize = (time / duration) * (size - startSize + 1) + startSize;
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        // System.out.println(time);
    }

    private Vector2 angleToVector(float angle) {
        return new Vector2((float) Math.cos(Math.toRadians(angle)),
                (float) Math.sin(Math.toRadians(angle)));
    }

    public void dispose() {
    }

    public float getAngle(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }

    public float getSpeed(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }

    public float getSize(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }

}
