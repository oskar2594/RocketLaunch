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

package de.pogs.rl.game.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * 
 * Hintergrund zum Hintergrund: Theoretisch sollte man für eine gute Performance die Generierung des
 * Hintergrunds auf die Grafikkarte auslagern. Leider können wir beide keine Shader programmieren.
 * Deswegen habe wir uns dazu entschieden Maßnahmen zu ergreifen, um das generieren möglichst
 * Effizienz auf dem CPU zu gestalten. - ChunkSystem - Caching - Optimierung aller Werte nach Zoom
 * und Bildschirmgröße
 * 
 */

public final class BackgroundLayer {
    private int size;
    private static BackgroundChunkManager chunkManager;
    private double seed;

    public BackgroundLayer() {
        size = (int) (Gdx.graphics.getHeight() * Gdx.graphics.getWidth() / 14000); // Optimale Größe
                                                                                   // von Chunks
                                                                                   // berechnen
        seed = new Random().nextGaussian() * 255; // Generierung eines zufälligen Seeds
        chunkManager = new BackgroundChunkManager(size, seed);
    }

    public void resize(int width, int height) {
        chunkManager.resize(width, height);
    }

    public void render(float delta, final SpriteBatch batch) {
        chunkManager.render(delta, batch);
    }

    public void update() {
        chunkManager.update();
    }

    public void dispose() {
        chunkManager.dispose();
    }

    public static BackgroundChunkManager getChunkManager() {
        return chunkManager;
    }
}
