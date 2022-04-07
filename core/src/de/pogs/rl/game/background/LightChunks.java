package de.pogs.rl.game.background;

import java.nio.channels.Pipe;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.text.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.PerlinNoiseGenerator;

public class LightChunks {

    LinkedList<LightChunk> chunks;

    private PerlinNoiseGenerator noise;
    private PerlinNoiseGenerator colorNoise;

    private double min;
    private double max;
    private double scale;

    private int chunkRadius;
    private int renderDistance;

    public LightChunks(int chunkRadius, PerlinNoiseGenerator noise, double min, double max, double scale) {
        this.chunkRadius = chunkRadius;
        this.noise = noise;
        colorNoise = new PerlinNoiseGenerator(new Random().nextGaussian() * 255);
        this.min = min;
        this.max = max;
        this.scale = scale;
        renderDistance = (int) Math.ceil((Gdx.graphics.getWidth() / 1.5) / chunkRadius);

        chunks = new LinkedList<LightChunk>();
    }

    public void update(SpriteBatch batch) {
        LinkedList<LightChunk> addChunks = new LinkedList<LightChunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        int pixelChunkRadius = chunkRadius * renderDistance;
        for (int x = (int) getNumInGrid(camPos.x, chunkRadius)
                - pixelChunkRadius; x < (int) getNumInGrid(camPos.x, chunkRadius)
                        + pixelChunkRadius; x += chunkRadius) {
            for (int y = (int) getNumInGrid(camPos.y, chunkRadius)
                    - pixelChunkRadius ; y < (int) getNumInGrid(camPos.y, chunkRadius)
                            + pixelChunkRadius; y += chunkRadius) {
                if (!checkForChunkAtPosition(x, y)) {
                    LightChunk ent = new LightChunk(chunkRadius, x, y, noise, colorNoise, min, max, scale, batch);
                    ent.run();
                    addChunks.add(ent);
                }
            }
        }
        chunks.removeAll(removeChunksOutOfRenderDistance());
        chunks.addAll(addChunks);
        for (LightChunk chunk : chunks) {
            // chunk.update();
            chunk.draw(batch);
        }
    }

    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    private LinkedList<LightChunk> removeChunksOutOfRenderDistance() {
        LinkedList<LightChunk> removeChunks = new LinkedList<LightChunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (LightChunk chunk : chunks) {
            if (distance(new Vector2(chunk.position.x, chunk.position.y),
                    camPos) > renderDistance * chunkRadius * 3) {
                chunk.dispose();
                removeChunks.add(chunk);
            }
        }
        return removeChunks;
    }

    private boolean checkForChunkAtPosition(int x, int y) {
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (LightChunk chunk : chunks) {
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
