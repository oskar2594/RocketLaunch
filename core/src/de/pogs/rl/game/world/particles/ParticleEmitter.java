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
package de.pogs.rl.game.world.particles;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.game.world.entities.AbstractEntity;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class ParticleEmitter {

    private LinkedList<Particle> particles;
    private Vector2 position;
    private Texture texture;
    private float[] particleSettings;
    private int count;
    private float perFrame;
    private boolean isActive = true;
    private boolean isDead = false;
    private boolean willDie = false;
    private boolean isAttached = false;
    private Sprite attachedTo;
    private float offset;
    private float angle = 0;
    private Vector2 velocity = Vector2.zero;
    private float offsetAngle;
    private boolean once = false;
    private AbstractEntity attachedEntity;

    public ParticleEmitter(int x, int y, int count, float perFrame, Texture texture, float minAngle, float maxAngle,
            float minSpeed, float maxSpeed, float startSize, float size, float alpha, float duration, float timeAlpha,
            float timeSize, boolean once) {
        this.position = new Vector2(x, y);
        this.particles = new LinkedList<Particle>();
        this.texture = texture;
        this.particleSettings = new float[] { minAngle, maxAngle, minSpeed, maxSpeed, startSize, size, alpha, duration,
                timeAlpha, timeSize };
        this.count = count;
        this.perFrame = perFrame;
        this.once = once;
    }

    public void update(float delta) {
        LinkedList<Particle> addParticles = new LinkedList<Particle>();
        LinkedList<Particle> remParticles = new LinkedList<Particle>();
        if ((willDie && this.particles.size() == 0) || (isAttached && !attachedEntity.isAlive())) {
            isDead = true;
            this.dispose();
            return;
        }
        if (isAttached) {
            angle = attachedTo.getRotation() - 90;
            Vector2 relativeOffset = new Vector2((float) (offset * Math.cos(Math.toRadians(angle + offsetAngle))),
                    (float) (offset * Math.sin(Math.toRadians(angle + offsetAngle))));
            if (attachedEntity == null) {
                position = new Vector2(attachedTo.getX() + relativeOffset.getX(), attachedTo.getY() + relativeOffset.getY());
            } else {
                Vector2 pos = attachedEntity.getPosition();
                position = new Vector2(pos.getX() + relativeOffset.getX(), pos.getY() + relativeOffset.getY());
            }
        }
        if (((this.particles.size() < this.count) || (this.count < 0)) && isActive) {
            int genAmount = 0;
            if (this.perFrame < 1) {
                if (Math.random() < this.perFrame) {
                    genAmount = 1;
                }
            } else if (this.count > 0) {
                genAmount = (int) Math.min(this.count - this.particles.size(), perFrame);
            } else {
                genAmount = (int) perFrame;
            }
            for (int i = 0; i < genAmount; i++) {
                Particle newParticle = new Particle(texture, position, velocity, angle, particleSettings[0],
                        particleSettings[1],
                        particleSettings[2], particleSettings[3], particleSettings[4], particleSettings[5],
                        particleSettings[6], particleSettings[7], particleSettings[8], particleSettings[9]);
                addParticles.add(newParticle);
            }
        } else if (this.particles.size() >= this.count && this.once) {
            this.isActive = false;
            this.willDie = true;
        }
        for (Particle particle : particles) {
            if (particle.isDead()) {
                remParticles.add(particle);
            } else {
                particle.update(delta);
            }
        }
        particles.addAll(addParticles);
        particles.removeAll(remParticles);
    }

    private void dispose() {
        this.texture.dispose();
    }

    public void render(SpriteBatch batch) {
        for (Particle particle : particles) {
            if (!particle.isDead())
                particle.render(batch);
        }
    }

    public void attach(Sprite sprite, float offset, float angle) {
        isAttached = true;
        attachedTo = sprite;
        this.offset = offset;
        this.offsetAngle = angle;
    }

    public void attach(Sprite sprite, float offset, float angle, AbstractEntity attachedEntity) {
        isAttached = true;
        attachedTo = sprite;
        this.offset = offset;
        this.offsetAngle = angle;
        this.attachedEntity = attachedEntity;
    }

    public void updateVelocity(Vector2 vel) {
        this.velocity = vel;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean getDead() {
        return isDead;
    }
}
