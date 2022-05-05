package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends AbstractEntity {
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("rakete");
    private Sprite sprite;

    private float scale = 0.2f;

    private float angle = 0;
    private float aimedAngle = 0;
    private float angle_response = 2;

    private float speed = 100;
    private float bulletSpeed = 500;
    private double shotCooldown = 200;
    private double lastBulletTime = TimeUtils.millis();

    private float armor = 100;
    private float health = 100;
    private float maxArmor = 100;
    private float maxHealth = 100;

    private float acceleration = 1000;

    private float breakCoeff = 0.5f;

    float bulletDamage = 10;

    Vector2 velocity = new Vector2(0, 0);

    public Player() {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position = new Vector2(0, 0);
        renderPriority = 1;
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        updateAimedAngle();
        updateAngle(delta);
        updatePosition(delta);
        updateVelocity(delta);

        sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - sprite.getHeight() / 2);
        sprite.setRotation(angle);
        shoot();
    }

    private void shoot() {
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            if ((TimeUtils.millis() - lastBulletTime) >= shotCooldown) {
                Bullet bullet = new Bullet(position.x, position.y, this,
                        bulletDamage, velocity.add(SpecialMath.angleToVector(angle).mul(bulletSpeed)), angle);
                bullet.update(0);
                GameScreen.INSTANCE.entityManager
                        .addEntity(bullet);
                lastBulletTime = TimeUtils.millis();

            }
        }
    }

    private void updateAimedAngle() {

        aimedAngle = (float) Math
                .toDegrees((float) (Math.atan(mouseXfromPlayer()
                        / mouseYfromPlayer())));
        if (mouseXfromPlayer() > 0 && mouseYfromPlayer() > 0) {
            aimedAngle = -180 + aimedAngle;
        }
        if (mouseXfromPlayer() < 0 && mouseYfromPlayer() > 0) {
            aimedAngle = 180 + aimedAngle;
        }

    }

    private float mouseXfromPlayer() {
        return Gdx.input.getX() - (float) (Gdx.graphics.getWidth() / 2);
    }

    private float mouseYfromPlayer() {
        return (float) (Gdx.input.getY() - (float) (Gdx.graphics.getHeight() / 2));
    }

    private void updateAngle(float delta) {

        angle = angle + (SpecialMath.angleDifferenceSmaller(aimedAngle, angle, 360)) * delta * angle_response;
        angle = SpecialMath.modulus(angle + 180, 360) - 180;
    }

    private void updatePosition(float delta) {
        // position = position.add(SpecialMath.angleToVector(this.angle).mul(delta * speed));
        position = position.add(velocity.mul(delta));
    }

    private void updateVelocity(float delta) {
        if (Gdx.input.isTouched()) {
            velocity = velocity.add(SpecialMath.angleToVector(this.angle).mul(delta * acceleration));
        }
        velocity = velocity.sub(velocity.mul(breakCoeff * delta));

    }
    public float getHealth() {
        return health;
    }

    public float getArmor() {
        return armor;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getMaxArmor() {
        return maxArmor;
    }

    @Override
    public void addDamage(float damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }
}
