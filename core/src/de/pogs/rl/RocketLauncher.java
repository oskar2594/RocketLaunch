/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
package de.pogs.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.screens.Loader;
import de.pogs.rl.utils.AssetHelper;
import de.pogs.rl.utils.ConfigLoader;

/**
 * Spielinstanz
 */
public class RocketLauncher extends Game {
	private static RocketLauncher instance;
	private static SpriteBatch batch;
	private static AssetHelper assetHelper;

	public static final SystemCursor defaultCursor = SystemCursor.Arrow;

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
