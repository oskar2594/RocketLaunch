package de.pogs.rl.game.world.generation;

import java.util.ArrayList;
import java.util.Collection;
import de.pogs.rl.game.world.entities.AbstractEntity;

/**
 * Abstrakte Klasse für Spawner
 */
public abstract class AbstractSpawner {
    /**
     * Wird für jeden neu bespawnten Chunk aufgerufen 
     * @param chunk Die x- und y-Koordinaten des Chunks
     * @return Die gespawnten Entities.
     */
    public abstract Collection<AbstractEntity> spawn(ArrayList<Integer> chunk);
}
