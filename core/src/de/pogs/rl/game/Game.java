package de.pogs.rl.game;

import java.beans.Encoder;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.game.entities.Player;

public class Game extends ScreenAdapter {
	public static Game INSTANCE;

	private SpriteBatch batch;


	private Engine engine;
	private RocketCamera camera;
	private Player player;

	public Game() {
		INSTANCE = this;
		batch = new SpriteBatch();
<<<<<<< HEAD
		// camera = new RocketCamera();
=======
		camera = new RocketCamera();
>>>>>>> 7950266ea8f76af4ccd1e34a70556819c54f1164
		engine = new Engine();
		//Entites
		player = new Player();
		engine.addEntity(player);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
<<<<<<< HEAD
		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
=======
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
>>>>>>> 7950266ea8f76af4ccd1e34a70556819c54f1164
		// DRAW

		player.render(delta, batch);

		//
<<<<<<< HEAD
		// camera.update();
=======
		camera.update();
>>>>>>> 7950266ea8f76af4ccd1e34a70556819c54f1164
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
