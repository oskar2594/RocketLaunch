package de.pogs.rl.game.world.entities;

import java.util.LinkedList;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.CameraShake;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Asteroid extends AbstractEntity {
    private int level;
    private Vector2 velocity;
    private Texture texture = RocketLauncher.getAssetHelper().getImage("asteroid");
    private Sprite sprite = new Sprite(texture);
    private static final int baseSize = 5;
    private static Random random = new Random();

    private float force;
    private static final float baseForce = 100;
    private static final float dps = 3;
    private float mass = 10;
    private LinkedList<Asteroid> collided = new LinkedList<Asteroid>();

    public Asteroid(Vector2 position, int level, Vector2 velocity) {
        this.position = position;
        this.level = level;
        this.velocity = velocity;
        force = baseForce * (float) Math.pow(1.5, this.level);
        radius = baseSize * (float) Math.pow(1.5, this.level) / 2;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setSize(baseSize * (float) Math.pow(1.5, this.level),
                baseSize * (float) Math.pow(1.5, this.level));
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
    }

    @Override
    public void update(float delta) {
        velocity = velocity.add(forceAdded);
        forceAdded = Vector2.zero;
        position = position.add(velocity.mul(delta));
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);

        for (AbstractEntity entity : EntityManager.get().getCollidingEntities(this)) {
            if (entity instanceof ImpulseEntity) {
                ImpulseEntity impulseEntity = (ImpulseEntity) entity;
                Vector2 v1 = velocity;
                Vector2 v2 = impulseEntity.getVelocity();
                Vector2 x1 = position;
                Vector2 x2 = impulseEntity.getPosition();
                float m1 = mass;
                float m2 = impulseEntity.getMass();
                Vector2 v1_new = v1.sub(x1.sub(x2)
                        .mul(2 * m2 / (m1 + m2) * v1.sub(v2).dot(x1.sub(x2)) / x1.dst2(x2)));
                Vector2 v2_new = v2.sub(x2.sub(x1)
                        .mul(2 * m1 / (m2 + m1) * v2.sub(v1).dot(x2.sub(x1)) / x2.dst2(x1)));
                velocity = v1_new;
                impulseEntity.setVelocity(v2_new);
                position = position.add(position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
                if(entity instanceof Player) {
                    CameraShake.makeShake(2f, 50);
                }
                System.out.println(position.dst(x2));
            }
            if (entity instanceof Asteroid) {
                Asteroid other = (Asteroid) entity;
                if (!collided.contains(other)) {
                    Vector2 v1 = velocity;
                    Vector2 v2 = other.getVelocity();
                    Vector2 x1 = position;
                    Vector2 x2 = other.getPosition();
                    float m1 = mass;
                    float m2 = other.getMass();
                    Vector2 v1_new = v1.sub(x1.sub(x2)
                            .mul(2 * m2 / (m1 + m2) * v1.sub(v2).dot(x1.sub(x2)) / x1.dst2(x2)));
                    Vector2 v2_new = v2.sub(x2.sub(x1)
                            .mul(2 * m1 / (m2 + m1) * v2.sub(v1).dot(x2.sub(x1)) / x2.dst2(x1)));
                    velocity = v1_new;
                    other.setVelocity(v2_new);
                    other.addCollided(this);
                    
                    position = position.add(position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
                }
            }
        }
        collided.clear();
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getMass() {
        return mass;
    }

    public void addCollided(Asteroid other) {
        collided.add(other);
    }
}
