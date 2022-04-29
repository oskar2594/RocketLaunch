package de.pogs.rl.game.background;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.FastNoiseLite;

public final class ChunkManager {

    LinkedList<Chunk> chunks;

    public static FastNoiseLite BASENOISE_LEVEL1;
    public static FastNoiseLite BASENOISE_LEVEL2;
    public static FastNoiseLite BASENOISE_LEVEL3;

    public static FastNoiseLite COLORNOISE_RED;
    public static FastNoiseLite COLORNOISE_PURPLE;
    public static FastNoiseLite COLORNOISE_BLUE;

    public static FastNoiseLite STARNOISE_LEVEL1;

    private int chunkRadius;
    private int renderDistance;

    private int chunksPerFrame = 5;

    public ChunkManager(int chunkRadius, double seed) {
        this.chunkRadius = chunkRadius;

        ChunkManager.BASENOISE_LEVEL1 = new FastNoiseLite((int) seed);
        ChunkManager.BASENOISE_LEVEL1.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        ChunkManager.BASENOISE_LEVEL2 = new FastNoiseLite((int) (seed * 2));
        ChunkManager.BASENOISE_LEVEL2.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        ChunkManager.BASENOISE_LEVEL3 = new FastNoiseLite((int) (seed * 3));
        ChunkManager.BASENOISE_LEVEL3.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        
        ChunkManager.COLORNOISE_RED = new FastNoiseLite((int) (seed * 4));
        ChunkManager.COLORNOISE_RED.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ChunkManager.COLORNOISE_PURPLE = new FastNoiseLite((int) (seed * 5));
        ChunkManager.COLORNOISE_PURPLE.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ChunkManager.COLORNOISE_BLUE = new FastNoiseLite((int) (seed * 6));
        ChunkManager.COLORNOISE_BLUE.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);

        ChunkManager.STARNOISE_LEVEL1 = new FastNoiseLite((int) (seed * 7));
        ChunkManager.STARNOISE_LEVEL1.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        renderDistance = (int) Math.ceil((Gdx.graphics.getWidth() / 1.5) / chunkRadius);

        chunks = new LinkedList<Chunk>();
    }

    public void update() {
        LinkedList<Chunk> addChunks = new LinkedList<Chunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        int pixelChunkRadius = chunkRadius * renderDistance;
        xLoop: for (int x = (int) getNumInGrid(camPos.x, chunkRadius)
                - pixelChunkRadius; x < (int) getNumInGrid(camPos.x, chunkRadius)
                        + pixelChunkRadius; x += chunkRadius) {
            if (addChunks.size() > chunksPerFrame && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                break xLoop;
            }
            yLoop: for (int y = (int) getNumInGrid(camPos.y, chunkRadius)
                    - pixelChunkRadius; y < (int) getNumInGrid(camPos.y, chunkRadius)
                            + pixelChunkRadius; y += chunkRadius) {
                if (!checkForChunkAtPosition(x, y)) {
                    Chunk chunk = new Chunk(chunkRadius, x, y);
                    addChunks.add(chunk);
                }
                if (addChunks.size() > chunksPerFrame && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                    break yLoop;
                }
            }
        }
        chunks.removeAll(removeChunksOutOfRenderDistance());
        chunks.addAll(addChunks);
    }

    public void render(float delta, SpriteBatch batch) {
        for (Chunk chunk : chunks) {
            chunk.draw(batch);
        }
    }

    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    private LinkedList<Chunk> removeChunksOutOfRenderDistance() {
        LinkedList<Chunk> removeChunks = new LinkedList<Chunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (Chunk chunk : chunks) {
            if (distance(new Vector2(chunk.position.x, chunk.position.y),
                    camPos) > renderDistance * chunkRadius * 2) {
                chunk.dispose();
                removeChunks.add(chunk);
            }
        }
        return removeChunks;
    }

    private boolean checkForChunkAtPosition(int x, int y) {
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (Chunk chunk : chunks) {
            if (distance(new Vector2(x, y),
                    camPos) > renderDistance * chunkRadius * 1) {
                return true;
            }
            if (distance(chunk.position, new Vector2(x, y)) < chunkRadius) {
                return true;
            }
        }
        return false;
    }

    private double distance(Vector2 start, Vector2 end) {
        return Math.sqrt((end.y - start.y) * (end.y - start.y) + (end.x - start.x) * (end.x - start.x));
    }

}
