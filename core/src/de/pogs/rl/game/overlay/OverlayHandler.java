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
package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import de.pogs.rl.game.GameScreen;
/**
 * Verwaltung der Overlays
 */
public class OverlayHandler {

    private Overlay currentOverlay;
    private ShapeRenderer shapeRenderer;

    /**
     * Aktuelles Overlay (er)setzen
     * 
     * @param overlay Overlay
     */
    public void setOverlay(Overlay overlay) {
        if (currentOverlay != null)
            currentOverlay.clear();
        currentOverlay = overlay;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

    }

    /**
     * Overlay rendern
     * 
     * @param batch SpriteBatch zum Rendern
     */
    public void render(SpriteBatch batch) {
        if (currentOverlay == null)
            return;
        currentOverlay.render(batch);
    }

    /**
     * Formen des Overlays rendern
     * 
     * @param combined Matrix für shapeRenderer
     */
    public void shapeRender(Matrix4 combined) {
        if (currentOverlay == null)
            return;
        shapeRenderer.setProjectionMatrix(combined);
        shapeRenderer.begin();
        currentOverlay.renderShape(shapeRenderer);
        shapeRenderer.end();

    }

    /**
     * Overlay aktualisieren
     * 
     * @param delta Vergangene Zeit seit letztem Frame
     */
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            if (currentOverlay instanceof PauseOverlay && GameScreen.isPaused()) {
                ((PauseOverlay) currentOverlay).resume();
            } else if (currentOverlay == null) {
                if (!GameScreen.isPaused() && GameScreen.getPlayer().isAlive()) {
                    this.setOverlay(new PauseOverlay());
                    GameScreen.setPaused(true);
                }
            }
        }
        if (currentOverlay == null)
            return;
        currentOverlay.update(delta);
    }

    /**
     * Overlay an neue Größe anpassen
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
        if (currentOverlay == null)
            return;
        currentOverlay.resize(width, height);
    }

    /**
     * Aktuelles Overlay Object erfragen
     * 
     * @return Overlay Object
     */
    public Overlay getOverlay() {
        return currentOverlay;
    }

}
