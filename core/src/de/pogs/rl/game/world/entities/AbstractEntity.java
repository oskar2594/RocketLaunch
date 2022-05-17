package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.utils.SpecialMath.Vector2;;

/**
 * Eine Entität die in der Spielwelt interagiert.
 */
public abstract class AbstractEntity {
    /**
     * Koordinaten der Entität
     */
    protected Vector2 position = new Vector2(0, 0);

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
    public boolean getAlive() {
        return alive;
    }

    /**
     * Wird beim Löschen der Entität aufgerufen, damit diese zum Beispiel Ressourcen freigeben kann.
     */
    public void dispose() {

    }

    /**
     * Wird aufgerufen, wenn das Entity ein anderes Entity tötet
     */
    public void kill(AbstractEntity victim) {

    }


    /**
     * Wird aufgerufen, wenn das Entity getötet wird.
     */
    public void killed(AbstractEntity killer) {

    }
}
