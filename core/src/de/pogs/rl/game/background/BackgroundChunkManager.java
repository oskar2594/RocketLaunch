package de.pogs.rl.game.background;

import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.awt.Color;

import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.FastNoiseLite;

/**
 * Verwalten von BackgroundChunks
 */

public final class BackgroundChunkManager {

    private LinkedList<BackgroundChunk> chunks;

    public static FastNoiseLite BASENOISE_LEVEL1;
    public static FastNoiseLite BASENOISE_LEVEL2;
    public static FastNoiseLite BASENOISE_LEVEL3;

    public static FastNoiseLite COLORNOISE_RED;
    public static FastNoiseLite COLORNOISE_PURPLE;
    public static FastNoiseLite COLORNOISE_BLUE;

    private int chunkSize;
    private int renderDistance;
    private float scaling = 2;
    private int oldWidth = -1;

    private int chunksPerFrame = 5;

    private static Color[][] colorCache;

    private static Vector2 cacheStart = new Vector2();

    /**
     * Erstellung eines Chunk Managers
     * 
     * @param chunkRadius Größe des einzelnen Chunks
     * @param seed Seed für die Generierung
     */
    public BackgroundChunkManager(int chunkRadius, double seed) {
        this.chunkSize = (int) (chunkRadius * scaling);

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

        // Render Distance nach größe des Bildschirms und Zoom der Kamera berechnen
        renderDistance = (int) Math.ceil(
                (Gdx.graphics.getWidth() * GameScreen.getCamera().zoom * 1) / chunkRadius);
        // Optimale Rate an Chunks pro Frame berechnen
        chunksPerFrame = (int) Math.ceil(Math.pow(renderDistance, 2) * Math.PI / 140);
        chunks = new LinkedList<BackgroundChunk>();
    }

    /**
     * Chunks an aktuelle Bildschirmgröße und Zoom der Kamera anpassen
     * 
     * @param width Breite des Fensters
     * @param height Höhe des Fensters
     */
    public void resize(int width, int height) {
        // Neuberechnung
        if (oldWidth < 0)
            oldWidth = width;
        int newChunkRadius = (int) (Gdx.graphics.getHeight() * Gdx.graphics.getWidth()
                * GameScreen.getCamera().zoom / 14000);
        if (newChunkRadius < width / 10)
            newChunkRadius = width / 10;
        if (newChunkRadius > width / 2)
            newChunkRadius = width / 2;
        int newRenderDistance = (int) Math.ceil(
                (Gdx.graphics.getWidth() * GameScreen.getCamera().zoom * 1) / newChunkRadius);
        int newChunksPerFrame = (int) Math.floor(Math.pow(newRenderDistance, 2) * Math.PI / 150);
        /**
         * Überprfung ob eine Anpassung der Chunkgröße notwendig ist, da eine Änderung bedeutet,
         * dass alle Chunks neu berechnet werden müssen.
         */
        if (Math.abs(oldWidth - width) > width * 0.2
                || Math.abs(oldWidth - width) > oldWidth * 0.2) {
            // Für bessere Performance werden aktuelle Farben abgespeichert
            collectCache(newChunkRadius, this.renderDistance, this.chunkSize);
            this.chunkSize = newChunkRadius * 2;
            this.renderDistance = newRenderDistance;
            this.chunksPerFrame = newChunksPerFrame;
            chunks.clear();
        } else {
            // Neue optimierte Werte setzen
            this.renderDistance =
                    (int) Math.ceil((Gdx.graphics.getWidth() * GameScreen.getCamera().zoom * 1)
                            / this.chunkSize);
            this.chunksPerFrame =
                    (int) Math.floor(Math.pow(this.renderDistance, 2) * Math.PI / 150);
        }
        oldWidth = width;
    }

    /**
     * Aktuelle Farben der Chunks abspeichern
     * 
     * @param newChR Neue Chunkgröße
     * @param renderD Render Distanz
     * @param currChR Aktuelle Chunkgröße
     */
    private void collectCache(int newChR, int renderD, int currChR) {
        int cacheRadius = (int) (currChR * renderD);
        colorCache = new Color[cacheRadius][cacheRadius];
        cacheStart.set(GameScreen.getCamera().position.x - cacheRadius / 2,
                GameScreen.getCamera().position.y - cacheRadius / 2);
        for (BackgroundChunk chunk : chunks) {
            Color[][] data = chunk.fieldCache;
            for (int x = 0; x < data.length; x++) {
                for (int y = 0; y < data.length; y++) {
                    Vector2 realPosition =
                            new Vector2(chunk.getStart().x / scaling + x - cacheStart.x,
                                    chunk.getStart().y / scaling - y - cacheStart.y);
                    try {
                        colorCache[(int) realPosition.x][(int) realPosition.y] = data[x][y];
                    } catch (Exception e) {
                        // Pixel ist zu weit entfernt und wird deshlab nicht abgespeichert
                    }
                }
            }
        }
    }

    /**
     * Farbe aus dem Cache abfragen
     * 
     * @param x X - Koordinate des Punktes
     * @param y Y - Koordinate des Punktes
     * @param start Startposition des Chunks
     * @return Farbe aus dem Cache
     */
    public Color getCachedColor(int x, int y, Vector2 start) {
        Vector2 relPos = new Vector2((start.x / scaling + x) - cacheStart.x,
                (start.y / scaling - y) - cacheStart.y);
        try {
            return colorCache[(int) relPos.x][(int) relPos.y];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Aktualisierung der Chunks
     */
    public void update() {
        LinkedList<BackgroundChunk> addChunks = new LinkedList<BackgroundChunk>();
        Vector2 camPos = new Vector2(GameScreen.getCamera().position.x,
                GameScreen.getCamera().position.y);
        int pixelRenderDistance = chunkSize * renderDistance; // Render Distanz in Pixeln
        xLoop: for (int x = (int) getNumInGrid(camPos.x, chunkSize)
                - pixelRenderDistance; x < (int) getNumInGrid(camPos.x, chunkSize)
                        + pixelRenderDistance; x += chunkSize) {
            // Wenn genug Chunks generiert wurde werden weitere Generierungen von Chunks abgebrochen
            if (addChunks.size() > chunksPerFrame
                    && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                break xLoop;
            }
            yLoop: for (int y = (int) getNumInGrid(camPos.y, chunkSize)
                    - pixelRenderDistance; y < (int) getNumInGrid(camPos.y, chunkSize)
                            + pixelRenderDistance; y += chunkSize) {

                if (!checkForChunkAtPosition(x, y)) { // Übeprüfung ob an der Position bereits ein
                                                      // Chunk ist
                    BackgroundChunk chunk = new BackgroundChunk(chunkSize / 2, x, y, scaling);
                    addChunks.add(chunk);
                }
                // Wenn genug Chunks generiert wurde werden weitere Generierungen von Chunks
                // abgebrochen
                if (addChunks.size() > chunksPerFrame
                        && chunks.size() > Math.pow(renderDistance, 2) * Math.PI) {
                    break yLoop;
                }
            }
        }
        // Ausführen der Änderungen
        chunks.removeAll(removeChunksOutOfRenderDistance());
        chunks.addAll(addChunks);

    }

    /**
     * Cache aufräumen, wenn Background Chunks nich mehr braucht werden
     */
    public void dispose() {
        for (BackgroundChunk chunk : chunks) {
            chunk.dispose();
        }
        chunks.clear();
    }

    /**
     * Chunks rendern
     */
    public void render(float delta, SpriteBatch batch) {
        for (BackgroundChunk chunk : chunks) {
            chunk.draw(batch);
        }
    }

    /**
     * Nummer zu einem Gitter anpassen
     * 
     * @param num Nummer
     * @param grid Vielfaches
     * @return Nummer in einem Gitter
     */
    private int getNumInGrid(double num, int grid) {
        return Math.round((float) num / grid) * grid;
    }

    /**
     * Überprüfen ob Chunks auserhalb der Render Distanz sind
     * 
     * @return Liste der zu löschenden Chunks
     */
    private LinkedList<BackgroundChunk> removeChunksOutOfRenderDistance() {
        LinkedList<BackgroundChunk> removeChunks = new LinkedList<BackgroundChunk>();
        Vector2 camPos = new Vector2(GameScreen.getCamera().position.x,
                GameScreen.getCamera().position.y);
        for (BackgroundChunk chunk : chunks) {
            if (distance(new Vector2(chunk.getPosition().x, chunk.getPosition().y),
                    camPos) > renderDistance * chunkSize * 2) {
                chunk.dispose();
                removeChunks.add(chunk);
            }
        }
        return removeChunks;
    }

    /**
     * Überprüfen ob ein Chunk an einem Punkt liegt
     * 
     * @param x X - Koordinate des Punktes
     * @param y Y - Koordinate des Punktes
     */
    private boolean checkForChunkAtPosition(int x, int y) {
        Vector2 camPos = new Vector2(GameScreen.getCamera().position.x,
                GameScreen.getCamera().position.y);
        for (BackgroundChunk chunk : chunks) {
            if (distance(new Vector2(x, y), camPos) > renderDistance * chunkSize * 1) {
                return true;
            }
            if (distance(chunk.getPosition(), new Vector2(x, y)) < chunkSize) {
                return true;
            }
        }
        return false;
    }

    /**
     * Berechnung der Distanz zwischen zwei Punkten
     * 
     * @param start Position 1
     * @param end Position 2
     * @return Distanz zwischen beiden Positionen
     */
    private double distance(Vector2 start, Vector2 end) {
        return Math.sqrt(
                (end.y - start.y) * (end.y - start.y) + (end.x - start.x) * (end.x - start.x));
    }

}
