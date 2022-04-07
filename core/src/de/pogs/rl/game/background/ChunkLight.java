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

public class ChunkLight {

    LinkedList<Chunk> chunks;

    private double noiseSeed;

    private int chunkRadius = 20;
    private int renderDistance = 20;

    public ChunkLight() {
        chunks = new LinkedList<Chunk>();
        noiseSeed = new Random().nextGaussian() * 255;
    }

    public void update(SpriteBatch batch) {

        LinkedList<Chunk> addChunks = new LinkedList<Chunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        int pixelChunkRadius = chunkRadius * renderDistance;
        for (int x = (int) getNumInGrid(camPos.x, chunkRadius)
                - pixelChunkRadius; x < (int) getNumInGrid(camPos.x, chunkRadius)
                        + pixelChunkRadius; x += chunkRadius) {
            for (int y = (int) getNumInGrid(camPos.y, chunkRadius)
                    - pixelChunkRadius ; y < (int) getNumInGrid(camPos.y, chunkRadius)
                            + pixelChunkRadius; y += chunkRadius) {
                // System.out.println(x);
                if (!checkForChunkAtPosition(x, y)) {
                    addChunks.add(new Chunk(chunkRadius, x, y, noiseSeed));
                }
            }
        }
        chunks.addAll(addChunks);
        System.out.println(chunks.size());
        chunks.removeAll(removeChunksOutOfRenderDistance());

        for (Chunk chunk : chunks) {
            chunk.update();
            chunk.draw(batch);
        }

        System.out.println(chunks.size());
    }

    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    private LinkedList<Chunk> removeChunksOutOfRenderDistance() {
        LinkedList<Chunk> removeChunks = new LinkedList<Chunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (Chunk chunk : chunks) {
            if (distance(new Vector2(chunk.position.x, chunk.position.y),
                    camPos) > renderDistance * chunkRadius * 1.5) {
                chunk.dispose();
                removeChunks.add(chunk);
            }
        }
        return removeChunks;
    }

    private boolean checkForChunkAtPosition(int x, int y) {
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (Chunk chunk : chunks) {
            if (distance(new Vector2(chunk.position.x, chunk.position.y),
                    camPos) > renderDistance * chunkRadius * 1.5) {
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
