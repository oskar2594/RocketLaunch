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
import de.pogs.rl.game.overlay.DeathOverlay;
import de.pogs.rl.game.overlay.OverlayHandler;
import de.pogs.rl.game.overlay.Pause;
import de.pogs.rl.game.ui.HUD;
import de.pogs.rl.game.ui.HUDCamera;
import de.pogs.rl.game.world.entities.EntityManager;
import de.pogs.rl.game.world.entities.Player;
import de.pogs.rl.game.world.generation.SpawnManager;
import de.pogs.rl.game.world.generation.spawners.AsteroidSpawner;
import de.pogs.rl.game.world.generation.spawners.SimpleSpawner;
import de.pogs.rl.game.world.generation.spawners.TankSpawner;
import de.pogs.rl.game.world.particles.ParticleManager;

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

    private static boolean paused = false; // TEST ONLY

    private static int renderDistanceBase = 1500;
    private static int updateDistanceBase = 1500;
    private static int removeDistanceBase = 2500;

    private static int renderDistance2 = (int) Math.pow(renderDistanceBase, 2);
    private static int updateDistance2 = (int) Math.pow(updateDistanceBase, 2);
    private static int removeDistance2 = (int) Math.pow(removeDistanceBase, 2);

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
        // overlayHandler.setOverlay(new DeathOverlay());

        entityManager.addEntity(player);
        entityManager.flush();
        entityGen = SpawnManager.get();
        entityGen.addSpawner(new SimpleSpawner());
        entityGen.addSpawner(new TankSpawner());
        entityGen.addSpawner(new AsteroidSpawner());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                        : 0));
        if (!GameScreen.paused) {
            entityGen.update(player.getPosition(), renderDistance2, removeDistance2);
            entityManager.update(delta, player.getPosition(), updateDistance2, removeDistance2);
            background.update();
            particleManager.update(delta);
            hud.update(delta);
        }
        hudCamera.update();
        camera.refresh(delta);

        // DRAW
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(delta, batch);
        particleManager.render(batch);
        entityManager.render(batch, player.getPosition(), renderDistance2);
        batch.end();

        // HUD SHAPES
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hud.shapeRender(hudCamera.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // HUD SPRITES
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        hud.render(batch);
        batch.end();

        // UPDATE OVERLAY
        overlayHandler.update(delta);
        // OVERLAY SHAPES
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        overlayHandler.shapeRender(hudCamera.combined);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // OVERLAY SPRITES
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

    public static void resizeZoom(int width, int height) {
        camera.resize(width, height);
        background.resize(width, height);
        resizeDistance();
    }

    private static void resizeDistance() {
        renderDistance2 = (int) Math.pow(renderDistanceBase * camera.zoom, 2);
        updateDistance2 = (int) Math.pow(updateDistanceBase * camera.zoom, 2);
        removeDistance2 = (int) Math.pow(removeDistanceBase * camera.zoom, 2);
    }

    public static boolean isPaused() {
        return paused;
    }

    public static void setPaused(boolean p) {
        paused = p;
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

    public static void reset() {}

}
