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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.CameraShake;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Asteroid extends AbstractEntity {
    private Vector2 velocity;
    private Sprite sprite;

    private Sound rockSound;
    private Sound muffleSound;

    private float mass = 2;
    private static final float density = 0.01f;
    private static final float damageCoeff = 0.03f;
    private LinkedList<Asteroid> collided = new LinkedList<Asteroid>();
    private float hp;
    private static float collectionMass = 10;

    // Die verschiedenen Texturen für die Asteroiden
    private static TextureRegion[][] textureRegion = TextureRegion.split(
            RocketLauncher.getAssetHelper().getImage("asteroids"), 75, 75);

    public Asteroid(Vector2 position, float mass, Vector2 velocity) {
        this.position = position;
        this.mass = mass;
        TextureRegion region = textureRegion[SpecialMath.randint(0, 8)][SpecialMath.randint(0, 1)];
        this.texture = region.getTexture();
        sprite = new Sprite(texture);
        sprite.setRegion(region);

        // Radius einer Kugel mit Masse und Dichte
        radius = (float) Math.pow(mass / density, 1f / 3f);
        hp = 0.1f * mass;
        this.velocity = velocity;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setSize(radius * 2, radius * 2);
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);

        rockSound = RocketLauncher.getAssetHelper().getSound("rockhit");
        muffleSound = RocketLauncher.getAssetHelper().getSound("muffle");
    }

    @Override
    public void update(float delta) {
        velocity = velocity.add(forceAdded);
        forceAdded = Vector2.zero;
        position = position.add(velocity.mul(delta));
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);

        for (AbstractEntity entity : GameScreen.getEntityManager().getCollidingEntities(this)) {
            // Bei kleiner Masse werden die Astroiden vom Spieler aufgenommen und heilen diesen
            if (mass < collectionMass && entity instanceof Player) {
                this.alive = false;
                GameScreen.getPlayer().heal(mass);
                break;
            }

            // Elastische Kollision mit anderen Entitäten, die ImpulseInterface implementieren.
            // Formel für die Kollision: https://en.wikipedia.org/wiki/Elastic_collision

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

                // Entfernt den Asteroiden so weit von der kollidierten Entität, dass diese im
                // nächsten Simulationsschritt nicht nah genug für eine erneute Kollision sind, weil
                // das zu Fehlern führt.
                position = position
                        .add(position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
                // Falls es sich um eine Kollision mit dem Spieler handelt, wird die Kamera
                // geschüttelt und dem Spieler schaden hinzugefügt, welcher proportional zu seiner
                // Geschwindigkeitsänderung durch die Kollision ist.
                if (entity instanceof Player) {
                    entity.addDamage(v2.sub(v2_new).magn() * damageCoeff, this);
                    CameraShake.makeShake(((Player) entity).getSpeed() / 50, 20);
                    playMuffle(0);
                    playSoundBasedOnDistance(rockSound,
                            entity.getPosition().dst(GameScreen.getPlayer().getPosition()));
                } else {
                    playMuffle(entity.getPosition().dst(GameScreen.getPlayer().getPosition()));
                }
            }

            // Kollision mit anderen Asteroiden. Diese wird separat gehandhabt, weil eine Kollision
            // immer nur von einem der Asteroiden durchgeführt werden soll. Das wird durch collided
            // beziehungsweise addCollided bewerkstelligt.
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

                    position = position.add(
                            position.sub(x2).nor().mul(radius + entity.getRadius() - x1.dst(x2)));
                    if (v2_new.dst(Vector2.zero) > 40 || v1_new.dst(Vector2.zero) > 40) {
                        playMuffle(entity.getPosition().dst(GameScreen.getPlayer().getPosition()));
                    }
                }
            }
        }
        collided.clear();
    }

    private void playMuffle(float distance) {
        playSoundBasedOnDistance(muffleSound, distance - 200);
    }

    private void playSoundBasedOnDistance(Sound sound, float distance) {
        if (Math.abs(distance) * 3 > Gdx.graphics.getHeight())
            return;
        sound.play(Math.abs(1 - (2 * distance / Gdx.graphics.getWidth())));
        // sound.play(0.01f);
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
        hp -= damage;
        // Wenn der Asteroid keine Lebenspunkte mehr hat, spaltet er sich in zwei neue mit jeweils
        // kleinerer Masse auf.
        if (hp <= 0) {
            this.alive = false;
            Vector2 splitVelocity =
                    new Vector2((float) Math.random() - 0.5f, (float) Math.random() - 0.5f).nor()
                            .mul(10);
            GameScreen.getEntityManager()
                    .addEntity(new Asteroid(position, mass / 2f, velocity.add(splitVelocity)));
            GameScreen.getEntityManager().addEntity(
                    new Asteroid(position, mass / 2f, velocity.add(splitVelocity.mul(-1))));
        }
    }

    // private void splashEffectSelf() {
    // GameScreen.getParticleManager()
    // .createEmitter(new ParticleEmitter((int) position.getX(), (int) position.getY(), 50,
    // 5,
    // ParticleUtils.generateParticleTexture(ParticleUtils.averageColor(texture)),
    // -180, 180, 10, 150, 1, 5, 1f, 1f, .5f, .1f, true))
    // .updateVelocity(velocity);
    // }
}
