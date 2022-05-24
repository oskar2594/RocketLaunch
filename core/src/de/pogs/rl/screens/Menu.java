package de.pogs.rl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Menu extends ScreenAdapter {

    // public Menu() {
    // }

	private SpriteBatch batch;
	private OrthographicCamera camera;


	@Override
	public void show() {
		batch = RocketLauncher.INSTANCE.batch;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isTouched()) {
			RocketLauncher.INSTANCE.setScreen(new GameScreen());
			dispose();
		}
		batch.begin();

		batch.end();
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void dispose() {
		
	}
    
}
