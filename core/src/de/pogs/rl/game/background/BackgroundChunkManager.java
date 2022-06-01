package de.pogs.rl.game.background;

import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.awt.Color;

import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.FastNoiseLite;

public final class BackgroundChunkManager {

    private LinkedList<BackgroundChunk> chunks;

    public static FastNoiseLite BASENOISE_LEVEL1;
    public static FastNoiseLite BASENOISE_LEVEL2;
    public static FastNoiseLite BASENOISE_LEVEL3;

    public static FastNoiseLite COLORNOISE_RED;
    public static FastNoiseLite COLORNOISE_PURPLE;
    public static FastNoiseLite COLORNOISE_BLUE;

    private int chunkRadius;
    private int renderDistance;
    private float scaling = 2;

    private int chunksPerFrame = 5;

    private static Color[][] colorCache;

    public BackgroundChunkManager(int chunkRadius, double seed) {
        this.chunkRadius = (int) (chunkRadius * scaling);

        // NoiseMaps

        BackgroundChunkManager.BASENOISE_LEVEL1 = new FastNoiseLite((int) seed);
        BackgroundChunkManager.BASENOISE_LEVEL1.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        BackgroundChunkManager.BASENOISE_LEVEL2 = new FastNoiseLite((int) (seed * 2));
        BackgroundChunkManager.BASENOISE_LEVEL2.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        BackgroundChunkManager.BASENOISE_LEVEL3 = new FastNoiseLite((int) (seed * 3));
        BackgroundChunkManager.BASENOISE_LEVEL3.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);

        BackgroundChunkManager.COLORNOISE_RED = new FastNoiseLite((int) (seed * 4));
        BackgroundChunkManager.COLORNOISE_RED.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        BackgroundChunkManager.COLORNOISE_PURPLE = new FastNoiseLite((int) (seed * 5));
        BackgroundChunkManager.COLORNOISE_PURPLE.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        BackgroundChunkManager.COLORNOISE_BLUE = new FastNoiseLite((int) (seed * 6));
        BackgroundChunkManager.COLORNOISE_BLUE.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);

        // Render Distance calculated based on zoom and screen dimensions
        renderDistance = (int) Math.ceil(
                (Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom * 1) / chunkRadius);
        // chunks calculated per frame based on ratios
        chunksPerFrame = (int) Math.ceil(Math.pow(renderDistance, 2) * Math.PI / 140);
        chunks = new LinkedList<BackgroundChunk>();
    }

    private int oldWidth = -1;

    public void resize(int width, int height) {
        // possible recalculations
        if (oldWidth < 0)
            oldWidth = width;
        int newChunkRadius = (int) (Gdx.graphics.getHeight() * Gdx.graphics.getWidth()
                * GameScreen.INSTANCE.camera.zoom / 14000);
        if (newChunkRadius < width / 10)
            newChunkRadius = width / 10;
        if (newChunkRadius > width / 2)
            newChunkRadius = width / 2;
        int newRenderDistance = (int) Math.ceil(
                (Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom * 1) / newChunkRadius);
        int newChunksPerFrame = (int) Math.floor(Math.pow(newRenderDistance, 2) * Math.PI / 150);
        // check if new chunkradius is needed, because
        // changing the chunkradius leads to recreating all
        // chunks
        // newChunkRadius *= scaling;
        if (Math.abs(oldWidth - width) > width * 0.2
                || Math.abs(oldWidth - width) > oldWidth * 0.2) {
            // for better performance collecting current color
            int one = collectCache(newChunkRadius, this.renderDistance, this.chunkRadius);
            this.chunkRadius = newChunkRadius * 2;
            this.renderDistance = newRenderDistance;
            this.chunksPerFrame = newChunksPerFrame;
            // removing all current chunks
            chunks.clear();
        } else {
            // setting new generating values
            this.renderDistance =
                    (int) Math.ceil((Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom * 1)
                            / this.chunkRadius);
            this.chunksPerFrame =
                    (int) Math.floor(Math.pow(this.renderDistance, 2) * Math.PI / 150);
        }
        oldWidth = width;
    }

    private static Vector2 cacheStart = new Vector2();

    // for better performance a square of chunks is cached
    // TODO: calculate max cacheRadius based on ComputerSpecs?
    int i = 0;

    private int collectCache(int newChR, int renderD, int currChR) {
        int cacheRadius = (int) (currChR * renderD);
        colorCache = new Color[cacheRadius][cacheRadius];
        cacheStart.set(GameScreen.INSTANCE.camera.position.x - cacheRadius / 2,
                GameScreen.INSTANCE.camera.position.y - cacheRadius / 2);
        // looping through all chunks to place their data on cacheMap
        for (BackgroundChunk chunk : chunks) {
            Color[][] data = chunk.fieldCache;
            i++;
            for (int x = 0; x < data.length; x++) {
                for (int y = 0; y < data.length; y++) {
                    Vector2 realPosition = new Vector2(chunk.start.x / scaling + x - cacheStart.x,
                            chunk.start.y / scaling - y - cacheStart.y);
                    try {
                        colorCache[(int) realPosition.x][(int) realPosition.y] = data[x][y];
                    } catch (Exception e) {
                        // pixel is out of range
                    }
                }
            }
        }
        i = 0;
        return 1;
    }

    // for individual chunk get cached color on position
    public Color getCachedColor(int x, int y, Vector2 start) {
        Vector2 relPos = new Vector2((start.x / scaling + x) - cacheStart.x,
                (start.y / scaling - y) - cacheStart.y);
        try {
            return colorCache[(int) relPos.x][(int) relPos.y];
        } catch (Exception e) {
            return null;
        }
    }

    public void update() {
        LinkedList<BackgroundChunk> addChunks = new LinkedList<BackgroundChunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x,
                GameScreen.INSTANCE.camera.position.y);
        int pixelChunkRadius = chunkRadius * renderDistance;
        xLoop: for (int x = (int) getNumInGrid(camPos.x, chunkRadius)
                - pixelChunkRadius; x < (int) getNumInGrid(camPos.x, chunkRadius)
                        + pixelChunkRadius; x += chunkRadius) {
            // only skip if enough chunks are created
            if (addChunks.size() > chunksPerFrame
                    && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                break xLoop;
            }
            yLoop: for (int y = (int) getNumInGrid(camPos.y, chunkRadius)
                    - pixelChunkRadius; y < (int) getNumInGrid(camPos.y, chunkRadius)
                            + pixelChunkRadius; y += chunkRadius) {

                if (!checkForChunkAtPosition(x, y)) { // check for chunk at position
                    // create new chunk
                    BackgroundChunk chunk = new BackgroundChunk(chunkRadius / 2, x, y, scaling);
                    // add chunk to create list
                    addChunks.add(chunk);
                }
                // only skip if enough chunks are created
                if (addChunks.size() > chunksPerFrame
                        && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                    break yLoop;
                }
            }
        }
        // execute changes to chunklist
        chunks.removeAll(removeChunksOutOfRenderDistance());
        chunks.addAll(addChunks);

    }

    public void dispose() {
        for (BackgroundChunk chunk : chunks) {
            chunk.dispose();
        }
        chunks.clear();
    }

    public void render(float delta, SpriteBatch batch) {
        for (BackgroundChunk chunk : chunks) {
            chunk.draw(batch);
        }
    }

    // get a number in a numbergrid
    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    // check if chunks are out of render distance
    private LinkedList<BackgroundChunk> removeChunksOutOfRenderDistance() {
        LinkedList<BackgroundChunk> removeChunks = new LinkedList<BackgroundChunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x,
                GameScreen.INSTANCE.camera.position.y);
        for (BackgroundChunk chunk : chunks) {
            if (distance(new Vector2(chunk.position.x, chunk.position.y), camPos) > renderDistance
                    * chunkRadius * 2) {
                chunk.dispose();
                removeChunks.add(chunk);
            }
        }
        return removeChunks;
    }

    // check if there is a chunk at the position
    private boolean checkForChunkAtPosition(int x, int y) {
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x,
                GameScreen.INSTANCE.camera.position.y);
        for (BackgroundChunk chunk : chunks) {
            if (distance(new Vector2(x, y), camPos) > renderDistance * chunkRadius * 1) {
                return true;
            }
            if (distance(chunk.position, new Vector2(x, y)) < chunkRadius) {
                return true;
            }
        }
        return false;
    }

    private double distance(Vector2 start, Vector2 end) {
        return Math.sqrt(
                (end.y - start.y) * (end.y - start.y) + (end.x - start.x) * (end.x - start.x));
    }

}
