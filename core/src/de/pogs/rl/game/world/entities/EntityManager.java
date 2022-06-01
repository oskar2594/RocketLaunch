package de.pogs.rl.game.world.entities;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.utils.SpecialMath.Vector2;


/**
 * Verwaltet Entitäten.
 */
public class EntityManager {
    private static EntityManager instance = new EntityManager();
    private LinkedList<AbstractEntity> entities = new LinkedList<AbstractEntity>();
    private LinkedList<AbstractEntity> entityQueue = new LinkedList<AbstractEntity>();

    private EntityManager() {}
    public static EntityManager get() {
        return instance;
    }

    /**
     * Fügt neue Entität der Warteschlange hinzu.
     * 
     * @param newEntity Die hinzuzufügende Entität.
     */
    public void addEntity(AbstractEntity newEntity) {
        entityQueue.add(newEntity);
    }

    /**
     * Fügt mehrere neue Entitäten der Warteschlange hinzu.
     * 
     * @param newEntities Die hinzuzufügenden Entitäten.
     */
    public void addEntities(Collection<AbstractEntity> newEntities) {
        entityQueue.addAll(newEntities);
    }

    /**
     * Gibt Entitäten zurück, die mit der Angegebenen ihrem Radius und deren Radius nach
     * kollidieren.
     * 
     * @param entity Die zu überprüfende Entität.
     * @return Kollidierende Entitäten, ausgenommen der Angegebenen.
     */
    public LinkedList<AbstractEntity> getCollidingEntities(AbstractEntity entity) {
        return getCollidingEntities(entity, entity.getRadius());
    }

    /**
     * Gibt Entitäten zurück, die mit der Angegebenen ihrem Radius und einem spezifizierten Radius
     * um die Angegebene nach kollidieren.
     * 
     * @param entity Die zu überprüfende Entität.
     * @param radius Der Radius um die zu überprüfende Entität.
     * @return Kollidierende Entitäten, ausgenommen der Angegebenen.
     */
    public LinkedList<AbstractEntity> getCollidingEntities(AbstractEntity entity, float radius) {
        LinkedList<AbstractEntity> result = new LinkedList<AbstractEntity>();
        for (AbstractEntity entityChecked : entities) {
            if (entityChecked.getPosition()
                    .dst(entity.getPosition()) <= (radius + entityChecked.getRadius())
                    && !entityChecked.equals(entity)) {
                result.add(entityChecked);
            }
        }
        return result;
    }

    /**
     * Führt einen Simulationsschritt für alle Entitäten in Simulationsweite durch.
     * @param delta Zeit, die seit dem letzten Simulationsschritt vergangen ist.
     * @param playerPosition Position des Spielers, von der aus die Simulationsweite gemessen wird.
     * @param updateDistance2 Quadrat der Simulationsweite, in der simuliert werden soll.
     */
    public void update(float delta, Vector2 playerPosition, int updateDistance2, int removeDistance2) {
        
        entities.stream().filter(entity -> !entity.getAlive()).forEach(entity -> entity.dispose());
        entities.removeIf((entity) -> !entity.getAlive());

        removeOutOfRange(playerPosition, removeDistance2);
        for (AbstractEntity entity : entities) {
            if (entity.getPosition().dst2(playerPosition) < updateDistance2)
                entity.update(delta);
        }
        if (entityQueue.size() != 0) {
            flush();
        }
    }

    /**
     * Zeichnet die Entitäten in Sichtweite.
     * @param batch Geöffneter SpriteBatch auf dem gezeichnet wird.
     * @param playerPosition Positon des Spielers, von der aus die Sichtweite gemessen wird.
     * @param renderDistance2 Quadrat der Entfernung, in der Entitäten gezeichnet werden sollen.
     */
    public void render(SpriteBatch batch, Vector2 playerPosition, int renderDistance2) {
        for (AbstractEntity entity : entities) {
            if (entity.getPosition().dst2(playerPosition) < renderDistance2)
                entity.render(batch);
        }
    }

    /**
     * Reiht die Warteschlange in die Liste der Entitäten ein.
     */
    public void flush() {
        entities.addAll(entityQueue);
        entityQueue.clear();
        entities.sort((e1, e2) -> e1.getRenderPriority() - e2.getRenderPriority());
    }

    /**
     * Entfernt alle Entitäten, die zu weit entfernt sind.
     * @param pos Position, von der aus die Entfernung gemessen wird.
     * @param range2 Quadrat der Entfernung, ab der Entitäten entfernt werden.
     */
    public void removeOutOfRange(Vector2 pos, float range2) {
        entities.stream().filter(e -> e.getPosition().dst2(pos) > range2).forEach(e -> e.dispose());
        entities.removeIf((entity) -> entity.getPosition().dst2(pos) > range2);
    }

    public LinkedList<AbstractEntity> getEntities() {
        return new LinkedList<AbstractEntity>(entities);
    }
    public void dispose() {}
}
