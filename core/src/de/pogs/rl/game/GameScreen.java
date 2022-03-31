package de.pogs.rl.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.background.BackgroundLayer;
import de.pogs.rl.game.entities.AbstractEntity;
import de.pogs.rl.game.entities.Player;

public class GameScreen extends ScreenAdapter {
    public static GameScreen INSTANCE;

    private SpriteBatch batch;

    private BackgroundLayer background;
    public RocketCamera camera;
    public Player player;
    private LinkedList<AbstractEntity> entities = new LinkedList<AbstractEntity>();

    public GameScreen() {
        INSTANCE = this;
        batch = RocketLauncher.INSTANCE.batch;

        camera = new RocketCamera();
        player = new Player();
        background = new BackgroundLayer();
        entities.add(player);
    }

    @Override
    public void render(float delta) {
        for (AbstractEntity entity : entities) {
            entity.update(delta, Gdx.input);
        }
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.move();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // DRAW
        background.render(delta, batch);
        for (AbstractEntity entity : entities) {
            entity.render(batch);
        }
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

}
