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
package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.background.BackgroundLayer;
import de.pogs.rl.game.overlay.CountdownOverlay;
import de.pogs.rl.game.overlay.DeathOverlay;
import de.pogs.rl.game.overlay.OverlayHandler;
import de.pogs.rl.game.ui.HUD;
import de.pogs.rl.game.ui.HUDCamera;
import de.pogs.rl.game.world.entities.EntityManager;
import de.pogs.rl.game.world.entities.Player;
import de.pogs.rl.game.world.generation.SpawnManager;
import de.pogs.rl.game.world.generation.spawners.AsteroidSpawner;
import de.pogs.rl.game.world.generation.spawners.SimpleSpawner;
import de.pogs.rl.game.world.generation.spawners.SpeedSpawner;
import de.pogs.rl.game.world.generation.spawners.TankSpawner;
import de.pogs.rl.game.world.particles.ParticleManager;
import de.pogs.rl.utils.CameraShake;

/**
 * Spielbildschirm
 */
public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;

    private static BackgroundLayer background;
    private static RocketCamera camera;
    private static HUDCamera hudCamera;
    private static Player player;
    private static EntityManager entityManager;
    private static ParticleManager particleManager;
    private static HUD hud;
    private static OverlayHandler overlayHandler;
    private static SpawnManager entityGen;

    private static int COUNTDOWN_DURATION = 3;

    private static boolean paused = false;

    private static int renderDistanceBase = 1500;
    private static int updateDistanceBase = 1500;
    private static int removeDistanceBase = 2500;

    private static int renderDistance2 = (int) Math.pow(renderDistanceBase, 2);
    private static int updateDistance2 = (int) Math.pow(updateDistanceBase, 2);
    private static int removeDistance2 = (int) Math.pow(removeDistanceBase, 2);

    private static int spawnProtectionRange = 400;

    /**
     * Erzeugt ein neues Spiel
     */
    public GameScreen() {
        batch = RocketLauncher.getSpriteBatch();
        entityManager = new EntityManager();
        particleManager = new ParticleManager();
        camera = new RocketCamera();
        hudCamera = new HUDCamera();
        player = new Player();
        background = new BackgroundLayer();
        hud = new HUD();
        overlayHandler = new OverlayHandler();
        overlayHandler.setOverlay(new CountdownOverlay(COUNTDOWN_DURATION));

        entityManager.addEntity(player);
        entityManager.flush();
        entityGen = new SpawnManager();
        entityGen.addSpawner(new SimpleSpawner());
        entityGen.addSpawner(new TankSpawner());
        entityGen.addSpawner(new AsteroidSpawner());
        entityGen.addSpawner(new SpeedSpawner());
        paused = false;
        PlayerStats.reset();
    }

    /**
     * Zeichnet alle Elemente des Spieles
     * 
     * @param delta Vergangene Zeit seit letztem Frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                        : 0));
        // Elemente nur aktualisieren, wenn Spiel nicht pausiert ist
        if (!GameScreen.paused) {
            entityGen.update(player.getPosition(), renderDistance2, removeDistance2,
                    spawnProtectionRange);
            entityManager.update(delta, player.getPosition(), updateDistance2, removeDistance2);
            particleManager.update(delta);
        }
        if (!BackgroundLayer.getChunkManager().isLoaded() && !paused) {
            GameScreen.setPaused(true);
        } else if (overlayHandler.getOverlay() == null && paused) {
            GameScreen.setPaused(false);
        }
        background.update();
        hud.update(delta);

        hudCamera.update();
        camera.refresh(delta);

        // Hauptelemente zeichnen
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(delta, batch);
        particleManager.render(batch);
        entityManager.render(batch, player.getPosition(), renderDistance2);
        batch.end();

        // Formenelemente des HUDs Zeichnen
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hud.shapeRender(hudCamera.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Elemente des HUDs Zeichnen
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        hud.render(batch);
        batch.end();

        // eventuelles Overlay aktualisieren
        overlayHandler.update(delta);
        // Formenelemente des eventuellen Overlays zeichenn
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        overlayHandler.shapeRender(hudCamera.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Elemente des HUDs Zeichnen
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        overlayHandler.render(batch);
        batch.end();

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        background.dispose();
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        camera.resize(width, height);
        hudCamera.resize(width, height);
        background.resize(width, height);
        overlayHandler.resize(width, height);
        resizeDistance();
    }

    /**
     * Wird aufgerufen, wenn der Zoom der Kamera angepasst wird
     * @param width Breite des tatsächlichen Bildschirm mit Zoom
     * @param height Höhe des tatsächlichen Bildschirm mit Zoom
     */
    public static void resizeZoom(int width, int height) {
        camera.resize(width, height);
        background.resize(width, height);
        resizeDistance();
    }

    /**
     * Distanzen für Entity Generierung anpassen
     */
    private static void resizeDistance() {
        renderDistance2 = (int) Math.pow(renderDistanceBase * camera.zoom, 2);
        updateDistance2 = (int) Math.pow(updateDistanceBase * camera.zoom, 2);
        removeDistance2 = (int) Math.pow(removeDistanceBase * camera.zoom, 2);
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void setPaused(boolean p) {
        if (p)
            player.pauseSounds();
        paused = p;
    }

    public static void startCountdown() {
        setPaused(true);
        overlayHandler.setOverlay(new CountdownOverlay(COUNTDOWN_DURATION));
    }

    public static RocketCamera getCamera() {
        return camera;
    }

    public static Player getPlayer() {
        return player;
    }

    public static ParticleManager getParticleManager() {
        return particleManager;
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static HUD getHud() {
        return hud;
    }

    public static OverlayHandler getOverlayHandler() {
        return overlayHandler;
    }

    /**
     * Spiel zurücksetzen (Static Elemente zurücksetzen und dannnneuen GameScreen erstellen)
     */
    public static void reset() {
        PlayerStats.reset();
        CameraShake.deactivate();
        CameraShake.isActive = false;
        RocketLauncher.getInstance().setScreen(new GameScreen());
    }

    /**
     * Spieler ist tot "event"
     */
    public static void playerDied() {
        CameraShake.deactivate();
        overlayHandler.setOverlay(new DeathOverlay());
        setPaused(true);
    }

}
