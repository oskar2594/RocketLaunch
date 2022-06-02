package de.pogs.rl.game.world.generation.spawners;

import java.util.LinkedList;
import java.util.Random;
import de.pogs.rl.game.world.entities.AbstractEntity;
import de.pogs.rl.game.world.entities.Asteroid;
import de.pogs.rl.game.world.generation.AbstractSpawner;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class AsteroidSpawner extends AbstractSpawner {
    Random random = new Random();
    public LinkedList<AbstractEntity> spawn(Vector2 chunk) {
        LinkedList<AbstractEntity> result = new LinkedList<AbstractEntity>();
        if (random.nextDouble() < 0.01) {
            int count = random.nextInt(10) + 5;
            Vector2 velocity = new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).mul(random.nextInt(50) + 25);
            for (int i = 0; i<count; i++) {
                Vector2 position = chunk.add(new Vector2(random.nextFloat(), random.nextFloat()).mul(200));
                result.add(new Asteroid(position, 1000, velocity));
            }
        }
        return result;
    }
}
