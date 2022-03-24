package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class RocketLauncher extends Game {
	public SpriteBatch batch;
	public static RocketLauncher INSTANCE;

	public RocketLauncher() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new de.pogs.rl.game.GameScreen());
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
