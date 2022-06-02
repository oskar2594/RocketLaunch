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
package de.pogs.rl.game.world.generation.spawners;

import java.util.LinkedList;
import java.util.Random;
import de.pogs.rl.game.world.entities.AbstractEntity;
import de.pogs.rl.game.world.entities.Asteroid;
import de.pogs.rl.game.world.generation.AbstractSpawner;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class AsteroidSpawner extends AbstractSpawner {
    private Random random = new Random();
    public LinkedList<AbstractEntity> spawn(Vector2 chunk) {
        LinkedList<AbstractEntity> result = new LinkedList<AbstractEntity>();
        if (random.nextDouble() < 0.008) {
            int count = random.nextInt(3) + 2;
            Vector2 velocity = new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).mul(random.nextInt(50) + 25);
            for (int i = 0; i<count; i++) {
                Vector2 position = chunk.add(new Vector2(random.nextFloat(), random.nextFloat()).mul(200));
                result.add(new Asteroid(position, (int) (Math.random() * 800) + 200, velocity));
            }
        }
        return result;
    }
}
