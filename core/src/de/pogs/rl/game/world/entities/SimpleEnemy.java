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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import java.awt.Color;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Einfacher Gegner.
 */
public class SimpleEnemy extends AbstractEntity implements CollisionInterface {
    protected float sightRange = (float) Math.pow(500, 2);
    private float haloRange = (float) Math.pow(200, 2);

    private float respectDistance = (float) Math.pow(180, 2);
    protected Sprite sprite;
    private float speed = 100;
    protected float hp = 1;

    private float scale = 0.1f;

    private Vector2 moveDirection =
            new Vector2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).nor();

    protected Vector2 velocity = moveDirection.mul(speed);

    private float repulsionRadius = 50;

    protected float playerAttraction = 100;
    protected float playerRepulsion = 200;

    private float tractionCoeff = 0.1f;

    private Vector2 constAcceleration =
            new Vector2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).nor().mul(10);

    protected float shootingCoeff = 1f;
    protected float bulletDamage = 1;
    protected float bulletSpeed = 500;
    protected Color bulletColor = new Color(0xd46178);

    protected float mass = 100;

    public SimpleEnemy(Vector2 position) {
        this(position, RocketLauncher.getAssetHelper().getImage("enemy1"));
    }

    public SimpleEnemy(Vector2 position, Texture texture) {
        this.radius = 20;
        this.texture = texture;
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.position = position;
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
    }

    public SimpleEnemy(float posX, float posY) {
        this(new Vector2(posX, posY));
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        updateVelocity(delta);



        updatePos(delta);
        shoot(delta);

    }

    protected void shoot(float delta) {

        if (GameScreen.getPlayer().getPosition().dst2(position) < sightRange
                && Math.random() < delta * shootingCoeff) {
            float flightTime = GameScreen.getPlayer().getPosition().dst(position) / bulletSpeed;
            Vector2 playerPosPredicted = GameScreen.getPlayer().getPosition()
                    .add(GameScreen.getPlayer().getVelocity().mul(flightTime));
            Vector2 bulletVelocity = playerPosPredicted.sub(position).nor().mul(bulletSpeed);
            Bullet.createBullet(position, this, bulletDamage, bulletVelocity, bulletColor, 20000);
        }
    }

    private Vector2 repulsion(float delta, AbstractEntity entity) {
        return position.sub(entity.getPosition())
                .mul(20 * delta * speed / (float) Math.pow(position.dst(entity.getPosition()), 3));
    }


    private void updateVelocity(float delta) {
        Vector2 playerPos = GameScreen.getPlayer().getPosition();
        if ((position.dst2(playerPos) > haloRange) && (position.dst2(playerPos) < sightRange)) {
            moveDirection = playerPos.sub(position).nor();
            velocity = velocity.add(moveDirection.mul(playerAttraction * delta));
        } else if (position.dst2(playerPos) < respectDistance) {
            moveDirection = playerPos.sub(position).nor().mul(-1);
            velocity = velocity.add(moveDirection.mul(playerRepulsion * delta));
        }

        for (AbstractEntity entity : GameScreen.getEntityManager().getCollidingEntities(this,
                repulsionRadius)) {
            if (entity instanceof SimpleEnemy) {
                velocity = velocity.add(repulsion(delta, entity));
            }
        }
        if (position.dst2(playerPos) < sightRange) {
            velocity = velocity.sub(velocity.mul(tractionCoeff * delta));
        }
        velocity = velocity.add(constAcceleration.mul(delta));

        velocity = velocity.add(forceAdded);
        forceAdded = Vector2.zero;
    }

    private void updatePos(float delta) {
        position = position.add(velocity.mul(delta));
    }

    @Override
    public void addDamage(float damage, AbstractEntity source) {
        hp -= damage;
        if (hp <= 0) {
            kill(source);
        }
    }

    @Override
    protected void killSelfEvent(AbstractEntity killer) {
        if (killer instanceof Player) {
            GameScreen.getEntityManager().addEntity(new XpOrb(position, 10));
        }
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
}
