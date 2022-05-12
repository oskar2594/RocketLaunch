package de.pogs.rl.game.world.spawners;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.pogs.rl.game.entities.AbstractEntity;
import de.pogs.rl.game.entities.SimpleEnemy;
import de.pogs.rl.game.world.AbstractSpawner;

public class SimpleSpawner extends AbstractSpawner {
    Random random = new Random();

    public LinkedList<AbstractEntity> spawn(ArrayList<Integer> chunk) {
        LinkedList<AbstractEntity> list = new LinkedList<AbstractEntity>();
        if (random.nextDouble() < 0.01) {
            int count = random.nextInt(5) + 1;
            for (int i = 0; i<count; i++) {
                list.add(new SimpleEnemy((float) (chunk.get(0) + random.nextDouble() * 200), (float) (chunk.get(1) + random.nextDouble() * 200)));
            }
        }
        return list;
    }
}
