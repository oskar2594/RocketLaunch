package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.background.BackgroundLayer;
import de.pogs.rl.game.ui.HUD;
import de.pogs.rl.game.ui.HUDCamera;
import de.pogs.rl.game.world.entities.EntityManager;
import de.pogs.rl.game.world.entities.Player;
import de.pogs.rl.game.world.generation.SpawnManager;
import de.pogs.rl.game.world.generation.spawners.SimpleSpawner;
import de.pogs.rl.game.world.particles.ParticleManager;

public class GameScreen extends ScreenAdapter {
    public static GameScreen INSTANCE;

    private SpriteBatch batch;

    private BackgroundLayer background;
    public RocketCamera camera;
    private HUDCamera hudCamera;
    public Player player;
    public EntityManager entityManager;
    public ParticleManager particleManager;
    public HUD hud;
    public SpawnManager entityGen;

    private int renderDistanceBase = 1500;
    private int updateDistanceBase = 1500;
    private int removeDistanceBase = 2500;

    private int renderDistance2 = (int) Math.pow(renderDistanceBase, 2);
    private int updateDistance2 = (int) Math.pow(updateDistanceBase, 2);
    private int removeDistance2 = (int) Math.pow(removeDistanceBase, 2);

    public GameScreen() {
        INSTANCE = this;
        batch = RocketLauncher.INSTANCE.batch;
        entityManager = EntityManager.get();
        particleManager = new ParticleManager();
        camera = new RocketCamera();
        hudCamera = new HUDCamera();
        player = Player.get();
        background = new BackgroundLayer();
        hud = new HUD();
        entityManager.addEntity(player);
        entityManager.flush();
        entityGen = SpawnManager.get();
        entityGen.addSpawner(new SimpleSpawner());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                        : 0));

        entityGen.update(player.getPosition(), renderDistance2, removeDistance2);
        entityManager.update(delta, player.getPosition(), updateDistance2, removeDistance2);
        background.update();
        particleManager.update(delta);
        hud.update(delta);
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

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        this.dispose();
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        camera.resize(width, height);
        hudCamera.resize(width, height);
        background.resize(width, height);
        resizeDistance();
    }

    public void resizeZoom(int width, int height) {
        camera.resize(width, height);
        background.resize(width, height);
        resizeDistance();
    }

    private void resizeDistance() {
        renderDistance2 = (int) Math.pow(renderDistanceBase * camera.zoom, 2);
        updateDistance2 = (int) Math.pow(updateDistanceBase * camera.zoom, 2);
        removeDistance2 = (int) Math.pow(removeDistanceBase * camera.zoom, 2);
    }
}
