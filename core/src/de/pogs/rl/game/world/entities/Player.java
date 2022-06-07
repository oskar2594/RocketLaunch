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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.game.PlayerStats;
import de.pogs.rl.game.world.particles.ParticleEmitter;
import de.pogs.rl.game.world.particles.ParticleUtils;
import de.pogs.rl.utils.InteractionUtils;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

import java.awt.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Die Entität des Spielers
 */
public class Player extends AbstractEntity implements CollisionInterface {
    private Sprite sprite;
    private int size = 50;
    private float angle = 0;
    private float aimedAngle = 0;
    private float angleResponse = 7;
    private float bulletSpeed = 1500;
    private float maxSpeed = 500;
    private double shotCooldown = 200;
    private double lastBulletTime = TimeUtils.millis();
    private float armor = 100;
    private float health = 100;
    private float maxArmor = 100;
    private float maxHealth = 100;
    private double healingTime = 2000;
    private double lastTimeDamaged = 0;
    private float regeneration = 0.5f;
    private float acceleration = 200;
    private float tractionCoeff = 0.5f;
    private boolean isAccelerating = false;
    private boolean wasAccelerating = false;
    private Sound thrustSound;
    private long thrustId;
    private float thrustVolume = 0f;
    private float thrustMaxVolume = 0.5f;
    private boolean thrustisPlaying = false;
    private Sound startSound;
    private long startId;
    private float startVolume = 0.5f;
    private Sound shootSound;
    private float bulletDamage = 10;
    private Vector2 velocity = new Vector2(0, 0);
    private ParticleEmitter dust;
    private ParticleEmitter sparks;
    private ParticleEmitter hot;
    private ParticleEmitter flame;
    private ParticleEmitter overheat;
    public long experiencePoints = 1;
    private float baseTractionCoeff = 0.5f;
    private float mass = 100;

    public Player() {
        texture = RocketLauncher.getAssetHelper().getImage("rakete");
        sprite = new Sprite(texture);
        sprite.setSize(((float) texture.getWidth() / (float) texture.getHeight()) * size, size);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position = Vector2.zero;
        renderPriority = 1;
        radius = size / 3;

        thrustSound = RocketLauncher.getAssetHelper().getSound("thrust");
        thrustId = thrustSound.loop(thrustVolume);
        thrustisPlaying = true;
        thrustSound.setLooping(thrustId, true);

        startSound = RocketLauncher.getAssetHelper().getSound("start");
        shootSound = RocketLauncher.getAssetHelper().getSound("shoot");

        dust = GameScreen.getParticleManager()
                .createEmitter(new ParticleEmitter(0, 0, -1, 10,
                        ParticleUtils.generateParticleTexture(new Color(0x808080)), -10, 10, 150,
                        250, 5, 10, .2f, 1f, 0f, .5f, false));
        dust.attach(this.getSprite(), 15, 0, this);

        flame = GameScreen.getParticleManager()
                .createEmitter(new ParticleEmitter(0, 0, -1, 3,
                        ParticleUtils.generateParticleTexture(new Color(0xd63636)), -10, 10, 150,
                        250, 5, 5, .6f, .5f, 0f, 0, false));
        flame.attach(this.getSprite(), 15, 0, this);

        hot = GameScreen.getParticleManager()
                .createEmitter(new ParticleEmitter(0, 0, -1, 2,
                        ParticleUtils.generateParticleTexture(new Color(0xd9851e)), -10, 10, 150,
                        250, 5, 5, .6f, .3f, 0f, 0, false));
        hot.attach(this.getSprite(), 15, 0, this);

        overheat = GameScreen.getParticleManager()
                .createEmitter(new ParticleEmitter(0, 0, -1, 1,
                        ParticleUtils.generateParticleTexture(new Color(0xffeba8)), -10, 10, 150,
                        250, 5, 5, .6f, .1f, 0f, 0, false));
        overheat.attach(this.getSprite(), 15, 0, this);

        sparks = GameScreen.getParticleManager()
                .createEmitter(new ParticleEmitter(0, 0, -1, 0.5f,
                        RocketLauncher.getAssetHelper().getImage("spark"), -5, 5, 200, 300, 5, 5,
                        .8f, 0.4f, .9f, 0, false));
        sparks.attach(this.getSprite(), 15, 0, this);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        PlayerStats.update();
        updateAimedAngle();
        updateAngle(delta);
        updatePosition(delta);
        updateVelocity(delta);
        regenArmor(delta);
        updateSounds(delta);
        updateParticles();

        updateStats();

        dust.updateVelocity(velocity);
        sparks.updateVelocity(velocity);
        hot.updateVelocity(velocity);
        flame.updateVelocity(velocity);
        overheat.updateVelocity(velocity);

        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        sprite.setRotation(angle);
        shoot();
    }

    private void updateStats() {
        acceleration = 200 + (float) Math.log(PlayerStats.getLevel() + 1);
        angleResponse = 5 + (float) Math.log(PlayerStats.getLevel() + 1) / 10;
        tractionCoeff = baseTractionCoeff / (float) Math.log(PlayerStats.getLevel() + 2);
    }

    private void regenArmor(float delta) {
        if (((TimeUtils.millis() - lastTimeDamaged) > healingTime) && (armor < maxArmor)) {
            armor += delta * regeneration;
        }
    }

    private void updateSounds(float delta) {
        if (thrustVolume < thrustMaxVolume && isAccelerating)
            thrustVolume += delta * 5;
        if (thrustVolume >= 0 && !isAccelerating)
            thrustVolume -= delta * 5;
        thrustVolume = Math.max(Math.min(thrustVolume, thrustMaxVolume), 0);

        if (wasAccelerating != isAccelerating) {
            if (isAccelerating) {
                startId = startSound.play(startVolume);
                startSound.setLooping(startId, false);
            }
        }
        if (thrustVolume > 0 && !thrustisPlaying && !GameScreen.isPaused()) {
            thrustisPlaying = true;
            thrustSound.stop(thrustId);
            thrustId = thrustSound.loop(thrustVolume);
            thrustSound.setLooping(thrustId, true);
        }
        thrustSound.setVolume(thrustId, thrustVolume);
        wasAccelerating = isAccelerating;
    }

    private void shoot() {
        if (Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isKeyPressed(Keys.SPACE)) {
            if ((TimeUtils.millis() - lastBulletTime) >= shotCooldown) {
                Bullet.createBullet(position, this, bulletDamage,
                        velocity.add(SpecialMath.angleToVector(angle).mul(bulletSpeed)),
                        new Color(0xffffff), 20000);
                shootSound.play(1f);
                lastBulletTime = TimeUtils.millis();
            }
        }
    }

    private void updateAimedAngle() {
        aimedAngle = SpecialMath.VectorToAngle(new Vector2(InteractionUtils.mouseXfromPlayer(),
                InteractionUtils.mouseYfromPlayer()));

    }

    private void updateAngle(float delta) {

        angle = angle + (SpecialMath.angleDifferenceSmaller(aimedAngle, angle, 360)) * delta
                * angleResponse;
        angle = SpecialMath.modulus(angle + 180, 360) - 180;
    }

    private void updatePosition(float delta) {
        position = position.add(velocity.mul(delta));
    }

    private void updateVelocity(float delta) {
        if (Gdx.input.isButtonPressed(Buttons.RIGHT) || Gdx.input.isKeyPressed(Keys.M)) {
            isAccelerating = true;
            velocity =
                    velocity.add(SpecialMath.angleToVector(this.angle).mul(delta * acceleration));
        } else {
            isAccelerating = false;
        }
        velocity = velocity.sub(velocity.mul(tractionCoeff * delta));
        if (velocity.magn() > maxSpeed) {
            velocity = velocity.mul(maxSpeed / velocity.magn());
        }

        velocity = velocity.add(forceAdded);
        forceAdded = Vector2.zero;

    }

    private void updateParticles() {
        if (isAccelerating) {
            dust.setActive(true);
            sparks.setActive(true);
            hot.setActive(true);
            flame.setActive(true);
            overheat.setActive(true);
        } else {
            dust.setActive(false);
            sparks.setActive(false);
            hot.setActive(false);
            flame.setActive(false);
            overheat.setActive(false);
        }
    }

    /**
     * Gibt die Lebenspunkt des Spielers zurück.
     * 
     * @return Die aktuellen Lebenspunkte.
     */
    public float getHealth() {
        return health;
    }

    /**
     * Gibt die Rüstungspunkte des Spielers zurück.
     * 
     * @return Die aktuellen Rüstungspunkte.
     */
    public float getArmor() {
        return armor;
    }

    /**
     * Gibt die höchstmöglichen Lebenspunkte zurück.
     * 
     * @return Die maximalen Lebenspunkte.
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gibt Sprite des Spielers zurück.
     * 
     * @return Sprite des Spielers.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Gibt die höchstmöglichen Rüstungspunte zurück.
     * 
     * @return Die maximalen Rüstungspunkte.
     */
    public float getMaxArmor() {
        return maxArmor;
    }

    @Override
    public void addDamage(float damage, AbstractEntity source) {
        armor -= damage;
        if (armor < 0) {
            health += armor;
            armor = 0;
        }
        if (health <= 0) {
            health = 0;
            GameScreen.playerDied();
        }
        lastTimeDamaged = TimeUtils.millis();
    }

    /**
     * Gibt die Höchstgeschwindigkeit zurück.
     * 
     * @return Die Höchstgeschwindigkeit des Spielers.
     */
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Gibt zurück, ob der Spieler momentan beschleunigt.
     * 
     * @return
     */
    public boolean getAccelerating() {
        return isAccelerating;
    }

    @Override
    public void killOtherEvent(AbstractEntity victim) {
        PlayerStats.addExp(25);
    }

    public float getMass() {
        return mass;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Pausiert alle vom Spieler erzeugten Klänge.
     */
    public void pauseSounds() {
        thrustSound.stop();
        thrustSound.stop(thrustId);
        thrustisPlaying = false;
        startSound.stop();
        startSound.stop(startId);
    }

    public void heal(float amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }
}
