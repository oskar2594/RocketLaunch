package de.pogs.rl.game.world.generation;

import java.util.HashSet;
import java.util.LinkedList;
import de.pogs.rl.game.world.entities.EntityManager;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Verwaltet die Generation neuer Entities
 */
public class SpawnManager {
    private static SpawnManager instance = new SpawnManager();
    private HashSet<Vector2> generatedChunks = new HashSet<Vector2>();
    private int chunkSize = 50;
    private LinkedList<AbstractSpawner> spawners = new LinkedList<AbstractSpawner>();

    /**
     * Getter für die Instanz von SpawnManager.
     * @return Die Instanz.
     */
    public static SpawnManager get() {
        return instance;
    }
    private SpawnManager() {
    }

    /**
     * Ruft registrierte Spawner für neu erschlossene Chunks auf und löscht verlassene Chunks.
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
     * @param chunk Koordinaten des Chunks
     */
    private void spawn(Vector2 chunk) {
        for (AbstractSpawner spawner : spawners) {
            EntityManager.get().addEntities(spawner.spawn(chunk));
        }
    }

    /**
     * Registriert einen Spawner, der für jeden Chunk aufgerufen wird.
     * @param spawner Der Spawner.
     */
    public void addSpawner(AbstractSpawner spawner) {
        this.spawners.add(spawner);
    }

}
