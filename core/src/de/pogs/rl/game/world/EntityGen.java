package de.pogs.rl.game.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.entities.EntityManager;
import de.pogs.rl.utils.SpecialMath;

public class EntityGen {
    private EntityManager entityManager;
    private HashSet<ArrayList<Integer>> generatedChunks = new HashSet<ArrayList<Integer>>();
    private int chunkSize = 50;
    private LinkedList<AbstractSpawner> spawners = new LinkedList<AbstractSpawner>();

    public EntityGen(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void generateChunks(Vector2 pos, int renderDistance2, int removeDistance2) {
        int renderDistance = (int) Math.sqrt(renderDistance2);
        int renderPosX;
        int renderPosY;
        for (int x = -renderDistance; x < renderDistance; x += chunkSize) {
            for (int y = -renderDistance; y < renderDistance; y += chunkSize) {
                renderPosX = (int) pos.x + x;
                renderPosX -= SpecialMath.modulus(renderPosX, chunkSize);
                renderPosY = (int) pos.y + y;
                renderPosY -= SpecialMath.modulus(renderPosY, chunkSize);
                ArrayList<Integer> renderPos = new ArrayList<Integer>(Arrays.asList(new Integer[] {renderPosX, renderPosY}));
                if (!generatedChunks.contains(renderPos)) {
                    generatedChunks.add(renderPos);
                    spawn(renderPos);
                }
            }
        }
        generatedChunks.removeIf((chunk) -> Math.pow(chunk.get(0) - pos.x, 2) + Math.pow(chunk.get(1) - pos.y, 2) > removeDistance2);
    }

    private void spawn(ArrayList<Integer> chunk) {
        for (AbstractSpawner spawner : spawners) {
            entityManager.addEntities(spawner.spawn(chunk));
        }
    }

    public void addSpawner(AbstractSpawner spawner) {
        this.spawners.add(spawner);
    }

}
