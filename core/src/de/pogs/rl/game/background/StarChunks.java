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

public class StarChunks {

    LinkedList<StarChunk> chunks;

    private PerlinNoiseGenerator noise;
    private double min;
    private double max;
    private double scale;

    private int chunkRadius;
    private int renderDistance;

    private int chunksPerFrame = 5;

    public StarChunks(int chunkRadius, PerlinNoiseGenerator noise, double min, double max, double scale) {
        this.chunkRadius = chunkRadius;
        this.noise = noise;
        this.min = min;
        this.max = max;
        this.scale = scale;
        renderDistance = (int) Math.ceil((Gdx.graphics.getWidth() / 1.2) / chunkRadius);

        chunks = new LinkedList<StarChunk>();
    }

    public void update(SpriteBatch batch) {

        LinkedList<StarChunk> addChunks = new LinkedList<StarChunk>();
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
                    StarChunk oneChunk = new StarChunk(chunkRadius, x, y, noise, min, max, scale, batch);
                    addChunks.add(oneChunk);
                }
                if (addChunks.size() > chunksPerFrame && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                    break yLoop;
                }
            }
        }
        chunks.addAll(addChunks);
        chunks.removeAll(removeChunksOutOfRenderDistance());
        for (StarChunk chunk : chunks) {
            // chunk.update();
            chunk.draw(batch);
        }
    }

    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    private LinkedList<StarChunk> removeChunksOutOfRenderDistance() {
        LinkedList<StarChunk> removeChunks = new LinkedList<StarChunk>();
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        for (StarChunk chunk : chunks) {
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
        for (StarChunk chunk : chunks) {
            if (distance(new Vector2(x, y),
                    camPos) > renderDistance * chunkRadius) {
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
