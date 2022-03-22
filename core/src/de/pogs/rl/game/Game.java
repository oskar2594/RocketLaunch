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
		camera = new RocketCamera();
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
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		// DRAW

		player.render(delta, batch);

		//
		camera.update();
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
