package de.pogs.rl.game.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.game.GameScreen;

/*

    Hintergrund zum Hintergrund:
    Theoretisch sollte man für eine gute Performance die Generierung des Hintergrunds auf die Grafikkarte auslagern.
    Leider können wir beide keine Shader programmieren. Deswegen habe wir uns dazu entschieden Maßnahmen zu ergreifen, 
    um das generieren möglichst Effizienz auf dem CPU zu gestalten.
    - ChunkSystem
    - Caching
    - Optimierung aller Werte nach Zoom und Bildschirmgröße

*/

public final class BackgroundLayer {
    public static BackgroundLayer INSTANCE;

    private int radius;

    public ChunkManager chunkManager;

    private double seed;

    public BackgroundLayer() {
        INSTANCE = this;
        radius = (int) (Gdx.graphics.getHeight() * Gdx.graphics.getWidth() / 14000);
        seed = new Random().nextGaussian() * 255;
        chunkManager = new ChunkManager(radius, seed);

    }

    public BitmapFont font = new BitmapFont();

    public void resize(int width, int height) {
        chunkManager.resize(width, height);
    }

    public void render(float delta, final SpriteBatch batch) {
        // DEBUG
        long startTime = System.nanoTime();
        chunkManager.render(delta, batch);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        font.draw(batch,
                Gdx.graphics.getFramesPerSecond() + " | " + (duration / 1000000) + "ms",
                GameScreen.INSTANCE.camera.position.x - Gdx.graphics.getWidth() / 3,
                GameScreen.INSTANCE.camera.position.y - Gdx.graphics.getHeight() / 3);
    }

    public void update() {
        chunkManager.update();
    }

    public void dispose() {
        chunkManager.dispose();
    }
}
