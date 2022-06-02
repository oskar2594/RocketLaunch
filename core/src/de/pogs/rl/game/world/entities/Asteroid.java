/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
package de.pogs.rl.game.world.entities;

import java.util.LinkedList;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.CameraShake;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Asteroid extends AbstractEntity {
    private Vector2 velocity;
    private Texture texture = RocketLauncher.getAssetHelper().getImage("asteroid");
    private Sprite sprite = new Sprite(texture);
    private static Random random = new Random();

    private static final float density = 0.5f;
    private static final float damageCoeff = 0.01f;
    private float mass;
    private LinkedList<Asteroid> collided = new LinkedList<Asteroid>();

    public Asteroid(Vector2 position, float mass, Vector2 velocity) {
        this.position = position;
        this.mass = mass;
        radius = (float) Math.pow(mass / density, 1f / 3f);
        this.velocity = velocity;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setSize(radius * 2, radius * 2);
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

        for (AbstractEntity entity : GameScreen.getEntityManager().getCollidingEntities(this)) {
            if (entity instanceof ImpulseEntity) {
                ImpulseEntity impulseEntity = (ImpulseEntity) entity;
                Vector2 v1 = velocity;
                Vector2 v2 = impulseEntity.getVelocity();
                Vector2 x1 = position;
                Vector2 x2 = impulseEntity.getPosition();
                float m1 = mass;
                float m2 = impulseEntity.getMass();
                Vector2 v1_new = v1.sub(x1.sub(x2)
                        .mul((2 * m2 / (m1 + m2)) * v1.sub(v2).dot(x1.sub(x2)) / x1.dst2(x2)));
                Vector2 v2_new = v2.sub(x2.sub(x1)
                        .mul((2 * m1 / (m2 + m1)) * v2.sub(v1).dot(x2.sub(x1)) / x2.dst2(x1)));
                velocity = v1_new;
                impulseEntity.setVelocity(v2_new);
                if (entity instanceof Player) {
                    entity.addDamage(v2.sub(v1).magn() * damageCoeff, this);
                }
                position = position
                        .add(position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
                if (entity instanceof Player) {
                    CameraShake.makeShake(((Player) entity).getSpeed() / 50, 20);
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
                    System.out.println("Colloisipon");
                    position = position.add(
                            position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
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

    @Override
    public void addDamage(float damage, AbstractEntity sender) {
        this.alive = false;
        Vector2 splitVelocity =
                new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).nor().mul(10);
        GameScreen.getEntityManager()
                .addEntity(new Asteroid(position, mass / 2f, velocity.add(splitVelocity)));
        GameScreen.getEntityManager()
                .addEntity(new Asteroid(position, mass / 2f, velocity.add(splitVelocity.mul(-1))));

    }
}
