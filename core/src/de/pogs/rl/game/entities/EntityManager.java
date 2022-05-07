package de.pogs.rl.game.entities;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.utils.SpecialMath.Vector2;

public class EntityManager {
    private LinkedList<AbstractEntity> entities = new LinkedList<AbstractEntity>();
    private LinkedList<AbstractEntity> entityQueue = new LinkedList<AbstractEntity>();

    public EntityManager() {
    }

    public void addEntity(AbstractEntity newEntity) {
        entityQueue.add(newEntity);
    }

    public void addEntities(Collection<AbstractEntity> newEntities) {
        entityQueue.addAll(newEntities);
    }

    public LinkedList<AbstractEntity> getCollidingEntities(AbstractEntity entity) {
        return getCollidingEntities(entity, entity.getRadius());
    }
    public LinkedList<AbstractEntity> getCollidingEntities(AbstractEntity entity, float radius) {
        LinkedList<AbstractEntity> result = new LinkedList<AbstractEntity>();
        for (AbstractEntity entityChecked : entities) {
            if (entityChecked.getPosition().dst(entity.getPosition()) <= (radius + entityChecked.getRadius()) && !entityChecked.equals(entity)) {
                result.add(entityChecked);
            }
        }
        return result;
    }

    public void update(float delta, Vector2 playerPosition, int updateDistance2) {
        for (AbstractEntity entity : entities) {
            if (entity.getPosition().dst2(playerPosition) < updateDistance2)
                entity.update(delta);
        }
        if (entityQueue.size() != 0) {
            flush();
        }
        entities.removeIf((entity) -> !entity.getAlive());
    }

    public void render(SpriteBatch batch, Vector2 playerPosition, int renderDistance2) {
        for (AbstractEntity entity : entities) {
            if (entity.getPosition().dst2(playerPosition) < renderDistance2)
                entity.render(batch);
        }
    }

    public void flush() {
        entities.addAll(entityQueue);
        while (entityQueue.size() > 0) {
            entityQueue.remove();
        }
        entities.sort((e1, e2) -> e1.getRenderPriority() - e2.getRenderPriority());
    }

    public void removeOutOfRange(Vector2 pos, float range2) {
        entities.removeIf((entity) -> entity.getPosition().dst2(pos) > range2);
    }
}
