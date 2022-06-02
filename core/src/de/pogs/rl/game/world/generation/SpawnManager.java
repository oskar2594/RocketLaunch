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
package de.pogs.rl.game.world.generation;

import java.util.HashSet;
import java.util.LinkedList;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Verwaltet die Generation neuer Entities
 */
public class SpawnManager {
    private HashSet<Vector2> generatedChunks = new HashSet<Vector2>();
    private int chunkSize = 50;
    private LinkedList<AbstractSpawner> spawners = new LinkedList<AbstractSpawner>();

    /**
     * Getter für die Instanz von SpawnManager.
     * 
     * @return Die Instanz.
     */
    public SpawnManager() {}

    /**
     * Ruft registrierte Spawner für neu erschlossene Chunks auf und löscht verlassene Chunks.
     * 
     * @param pos Position des erkundenden Beobachters, in der Regel des Spielers.
     * @param renderDistance2 Das Quadrat der Entfernung, ab der Chunks als erschlossen gelten.
     * @param removeDistance2 Das Quadrat der Entfernung, ab der Chunks gelöscht werden.
     */
    public void update(Vector2 pos, int renderDistance2, int removeDistance2) {
        int renderDistance = (int) Math.sqrt(renderDistance2);
        int renderPosX;
        int renderPosY;
        for (int x = -renderDistance; x < renderDistance; x += chunkSize) {
            for (int y = -renderDistance; y < renderDistance; y += chunkSize) {
                renderPosX = (int) pos.getX() + x;
                renderPosX -= SpecialMath.modulus(renderPosX, chunkSize);
                renderPosY = (int) pos.getY() + y;
                renderPosY -= SpecialMath.modulus(renderPosY, chunkSize);
                Vector2 renderPos = new Vector2(renderPosX, renderPosY);
                if (!generatedChunks.contains(renderPos)) {
                    generatedChunks.add(renderPos);
                    spawn(renderPos);
                }
            }
        }
        generatedChunks.removeIf((chunk) -> chunk.dst2(pos) > removeDistance2);
    }

    /**
     * Ruft alle Spawner auf einen Chunk auf und registriert diese
     * 
     * @param chunk Koordinaten des Chunks
     */
    private void spawn(Vector2 chunk) {
        for (AbstractSpawner spawner : spawners) {
            GameScreen.getEntityManager().addEntities(spawner.spawn(chunk));
        }
    }

    /**
     * Registriert einen Spawner, der für jeden Chunk aufgerufen wird.
     * 
     * @param spawner Der Spawner.
     */
    public void addSpawner(AbstractSpawner spawner) {
        this.spawners.add(spawner);
    }

}
