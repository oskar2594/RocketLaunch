package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.screens.Loader;
import de.pogs.rl.screens.Menu;
import de.pogs.rl.utils.AssetHelper;
import de.pogs.rl.utils.ConfigLoader;


public class RocketLauncher extends Game {
	public SpriteBatch batch;
	public static RocketLauncher INSTANCE;
	
	public AssetHelper assetHelper;

	public RocketLauncher() {
		super();
		INSTANCE = this;
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
