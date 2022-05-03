package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.background.BackgroundLayer;
import de.pogs.rl.game.entities.Enemy;
import de.pogs.rl.game.entities.EntityManager;
import de.pogs.rl.game.entities.Player;
import de.pogs.rl.game.world.EntityGen;
import de.pogs.rl.game.world.spawners.SimpleSpawner;


public class GameScreen extends ScreenAdapter {
    public static GameScreen INSTANCE;

    private SpriteBatch batch;

    private BackgroundLayer background;
    public RocketCamera camera;
    public Player player;
    public EntityManager entityManager;
    public EntityGen entityGen;

    private int renderDistance2 = (int) Math.pow(1000, 2);
    private int updateDistance2 = (int) Math.pow(2000, 2);
    private int removeDistance2 = (int) Math.pow(5000, 2);


    public GameScreen() {
        INSTANCE = this;
        batch = RocketLauncher.INSTANCE.batch;
        entityManager = new EntityManager();
        camera = new RocketCamera();
        player = new Player();
        background = new BackgroundLayer();
        entityManager.addEntity(player);
        entityManager.addEntity(new Enemy(20, 20));
        entityManager.flush();
        entityGen = new EntityGen(entityManager);
        entityGen.addSpawner(new SimpleSpawner());
    }

    @Override
    public void render(float delta) {
        entityGen.generateChunks(player.getPosition(), renderDistance2, removeDistance2);
        entityManager.removeOutOfRange(player.getPosition(), removeDistance2);
        entityManager.update(delta, player.getPosition(), updateDistance2);
        Gdx.gl.glClearColor(0.0f, 1.0f, 0.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.move();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        // DRAW
        // final float delta2 = delta;
        batch.begin();
        background.render(delta, batch);
        background.update();
        entityManager.render(batch, player.getPosition(), renderDistance2);
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
	public void resize (int width, int height) {
        camera.resize(width, height);
        background.resize(width, height);
	}

}
