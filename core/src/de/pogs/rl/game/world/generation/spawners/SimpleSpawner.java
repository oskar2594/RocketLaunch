package de.pogs.rl.game.world.generation.spawners;

import java.util.LinkedList;
import java.util.Random;
import de.pogs.rl.game.world.entities.AbstractEntity;
import de.pogs.rl.game.world.entities.SimpleEnemy;
import de.pogs.rl.game.world.generation.AbstractSpawner;
import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Erzeugt SimpleEntities
 */
public class SimpleSpawner extends AbstractSpawner {
    Random random = new Random();

    public LinkedList<AbstractEntity> spawn(Vector2 chunk) {
        LinkedList<AbstractEntity> list = new LinkedList<AbstractEntity>();
        if (random.nextDouble() < 0.01) {
            int count = random.nextInt(5) + 1;
            for (int i = 0; i<count; i++) {
                list.add(new SimpleEnemy((float) (chunk.getX() + random.nextDouble() * 200), (float) (chunk.getY() + random.nextDouble() * 200)));
            }
        }
        return list;
    }
}
