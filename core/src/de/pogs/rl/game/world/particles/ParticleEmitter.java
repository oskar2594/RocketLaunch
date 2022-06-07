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

/**
 * Particle Quelle
 */
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

    /**
     * Particle Quelle erstellen
     * 
     * @param x X - Koordinate
     * @param y Y - Koordinate
     * @param count Maximale Anzahl an Particle. "-1" für unendlich viele
     * @param perFrame Anzahl an Particle pro Frame. Zahlen kleiner null für Wahrscheinlichkeiten
     * @param texture Textur des Particles
     * @param minAngle Minimaler Ausströmwinkel
     * @param maxAngle Maximaler Ausströmwinkel
     * @param minSpeed Minmale Ausströmgeschwindigkeit
     * @param maxSpeed Maximale Ausströmgeschwindigkeit
     * @param minSize Minimale Größe
     * @param maxSize Maximale Größe
     * @param alpha Transparenz
     * @param duration Dauer bis Löschen
     * @param timeAlpha Zeit ab der die Transparenz abnehmen soll
     * @param timeSize Zeit ab der die Größe abnehmen soll
     * @param once Soll nur einmal die maximale Particlegröße erreicht werden?
     */
    public ParticleEmitter(int x, int y, int count, float perFrame, Texture texture, float minAngle,
            float maxAngle, float minSpeed, float maxSpeed, float minSize, float maxSize,
            float alpha, float duration, float timeAlpha, float timeSize, boolean once) {
        this.position = new Vector2(x, y);
        this.particles = new LinkedList<Particle>();
        this.texture = texture;
        this.particleSettings = new float[] {minAngle, maxAngle, minSpeed, maxSpeed, minSize,
                maxSize, alpha, duration, timeAlpha, timeSize};
        this.count = count;
        this.perFrame = perFrame;
        this.once = once;
    }

    public void update(float delta) {
        LinkedList<Particle> addParticles = new LinkedList<Particle>();
        LinkedList<Particle> remParticles = new LinkedList<Particle>();
        // Particle Quelle löschen, wenn nicht mehr gebruacht
        if ((willDie && this.particles.size() == 0) || (isAttached && !attachedEntity.isAlive())) {
            isDead = true;
            this.dispose();
            return;
        }
        // Particle angebrachtes Entitie oder Sprite heften
        if (isAttached) {
            angle = attachedTo.getRotation() - 90;
            Vector2 relativeOffset =
                    new Vector2((float) (offset * Math.cos(Math.toRadians(angle + offsetAngle))),
                            (float) (offset * Math.sin(Math.toRadians(angle + offsetAngle))));
            if (attachedEntity == null) {
                position = new Vector2(attachedTo.getX() + relativeOffset.getX(),
                        attachedTo.getY() + relativeOffset.getY());
            } else {
                Vector2 pos = attachedEntity.getPosition();
                position = new Vector2(pos.getX() + relativeOffset.getX(),
                        pos.getY() + relativeOffset.getY());
            }
        }
        // Bei Bedarf neue Particle generieren
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
                // texture, position, velocity, angle, minAngle, maxAngle, minSpeed, maxSpeed,
                // startSize, size, alpha, duration, timeAlpha, timeSize
                Particle newParticle =
                        new Particle(texture, position, velocity, angle, particleSettings[0],
                                particleSettings[1], particleSettings[2], particleSettings[3],
                                particleSettings[4], particleSettings[5], particleSettings[6],
                                particleSettings[7], particleSettings[8], particleSettings[9]);
                addParticles.add(newParticle);
            }
        } else if (this.particles.size() >= this.count && this.once) {
            this.isActive = false;
            this.willDie = true;
        }
        // Alte Particle löschen
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

    /**
     * Particle Quelle an Sprite knüpfen
     * 
     * @param sprite Sprite, an den geknüpft werden soll
     * @param offset Abstand
     * @param angle Winkel des Abstandes
     */
    public void attach(Sprite sprite, float offset, float angle) {
        isAttached = true;
        attachedTo = sprite;
        this.offset = offset;
        this.offsetAngle = angle;
    }

    /**
     * Particle Quelle an Sprite & dazugehöriges Entity knüpfen
     * 
     * @param sprite Sprite, an den geknüpft werden soll
     * @param offset Abstand
     * @param angle Winkel des Abstandes
     * @param attachedEntity Entity, an das geknüft werden soll
     */
    public void attach(Sprite sprite, float offset, float angle, AbstractEntity attachedEntity) {
        isAttached = true;
        attachedTo = sprite;
        this.offset = offset;
        this.offsetAngle = angle;
        this.attachedEntity = attachedEntity;
    }

    /**
     * Grundbewegung der Particle Quelle anpassen
     * 
     * @param vel Bewegung (Velocity)
     */
    public void updateVelocity(Vector2 vel) {
        this.velocity = vel;
    }

    /**
     * Particle Quelle (de)aktivieren
     * 
     * @param active Aktiv?
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Particle Quelle töten Keine neue Particle erzeugen
     */
    public void setDead() {
        isDead = true;
    }

    /**
     * Ist Particle Quelle tot?
     * 
     * @return Particle Quelle tot: Ja / Nein
     */
    public boolean getDead() {
        return isDead;
    }
}
