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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.utils.SpecialMath.Vector2;;

/**
 * Eine Entität die in der Spielwelt interagiert.
 */
public abstract class AbstractEntity {
    /**
     * Koordinaten der Entität
     */
    protected Vector2 position = Vector2.zero;
    protected Vector2 forceAdded = Vector2.zero;
    protected Texture texture;

    /**
     * Führt einen Simulationsschritt für die Entität durch.
     * 
     * @param delta Die Zeit, die seit dem letzten Simulationsschritt vergangen ist.
     */
    public void update(float delta) {};

    /**
     * Zeichnet die Entität in die Spielwelt.
     * 
     * @param batch geöffnetes SpriteBatch, auf dem die Entität gezeichnet werden soll.
     */
    public void render(SpriteBatch batch) {};

    /**
     * Gibt die Position der Entität zurück.
     * 
     * @return Die Position der Entität.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Entscheidet darüber, wann die Entität im Vergleich zu den anderen gezeichnet wird. Je
     * kleiner, desto eher und dementsprechend weiter unten.
     */
    protected int renderPriority = 0;

    /**
     * Gibt die Renderpriorität zurück.
     * 
     * @return Die Renderpriotität. 
     */
    public int getRenderPriority() {
        return renderPriority;
    }

    /**
     * Fügt der Entität Schaden hinzu, beispielsweise durch Geschosse.
     * 
     * @param damage Die Höhe des Schadens.
     */
    public void addDamage(float damage, AbstractEntity source) {};

    /**
     * Der Radius der Entität bei der Berechnung von Kollisionen.
     */
    protected float radius = 10;

    /**
     * Der Radius der Entität bei der Berechnung von Kollisionen.
     * 
     * @return
     */
    public float getRadius() {
        return radius;
    }

    protected boolean alive = true;

    /**
     * Gibt zurück, ob die Entität getötet wurde und somit gelöscht werden kann.
     * 
     * @return
     */
    public boolean isAlive() {
        return alive;
    }


    /**
     * Wird aufgerufen, wenn das Entity ein anderes Entity tötet
     */
    protected void killOtherEvent(AbstractEntity victim) {

    }


    /**
     * Wird aufgerufen, wenn das Entity getötet wird.
     */
    protected void killSelfEvent(AbstractEntity killer) {

    }

    /**
     * Tötet die Entität
     */
    public void kill(AbstractEntity killer) {
        alive = false;
        killSelfEvent(killer);
        if (killer != null) {
            killer.killOtherEvent(this);
        }
    }


    /**
     * Gibt die Textur der Entität zurück.
     * 
     * @return Die Textur
     */
    public Texture getTexture() {
        return texture;
    }
}
