package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.screens.Loader;
import de.pogs.rl.utils.AssetHelper;
import de.pogs.rl.utils.ConfigLoader;


public class RocketLauncher extends Game {
	private static RocketLauncher instance;
	private static SpriteBatch batch;
	private static AssetHelper assetHelper;

	public RocketLauncher() {
		super();
		instance = this;
	}

	public static SpriteBatch getSpriteBatch() {
		return batch;
	}

	public static AssetHelper getAssetHelper() {
		return assetHelper;
	}

	public static RocketLauncher getInstance() {
		return instance;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		assetHelper = new AssetHelper();
		this.setScreen(new Loader());
	}


	@Override
	public void render() {
		super.render();
		ConfigLoader.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
