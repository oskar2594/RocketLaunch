package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.utils.AssetHelper;


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
		assetHelper.loadAll();
		assetHelper.aManager.finishLoading();
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
