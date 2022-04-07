package de.pogs.rl.game.entities;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityManager {
    private LinkedList<AbstractEntity> entities = new LinkedList<AbstractEntity>();

    public EntityManager() {
    }

    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
    }

    public LinkedList<AbstractEntity> getCollidingEntities(AbstractEntity entity, float radius) {
        LinkedList<AbstractEntity> result = new LinkedList<AbstractEntity>();
        for (AbstractEntity entityChecked : entities) {
            if (entityChecked.getPosition().dst(entity.getPosition()) <= radius && ! entityChecked.equals(entity)) {
                result.add(entityChecked);
            }
        }
        return result;
    }

    public void update(float delta) {
        for (AbstractEntity entity : entities) {
            entity.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (AbstractEntity entity : entities) {
            entity.render(batch);
        }
    }
}
