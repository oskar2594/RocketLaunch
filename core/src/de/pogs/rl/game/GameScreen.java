package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;

public class GameScreen extends ScreenAdapter {
	public static GameScreen INSTANCE;

	private SpriteBatch batch;

	private RocketCamera camera;
	private Texture text;

	public GameScreen() {
		INSTANCE = this;
		batch = RocketLauncher.INSTANCE.batch;

		text = new Texture(Gdx.files.internal("rakete.png"));
		camera = new RocketCamera();
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// DRAW

		batch.draw(text, 10, 10);

		//
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
