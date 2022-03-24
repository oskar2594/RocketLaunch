package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.screens.Menu;

public class RocketLauncher extends Game {
	private SpriteBatch batch;
	public static RocketLauncher INSTANCE;

	public RocketLauncher() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new Menu());
	}


	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
