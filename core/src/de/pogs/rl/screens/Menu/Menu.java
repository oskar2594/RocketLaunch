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
package de.pogs.rl.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.game.PlayerStats;
import de.pogs.rl.utils.menu.Button;

public class Menu extends ScreenAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture background = RocketLauncher.getAssetHelper().getImage("background");
	private Sprite backgroundSprite;

	private Texture logo = RocketLauncher.getAssetHelper().getImage("logo");
	private Sprite logoSprite;

	private BitmapFont font;

	private long finished = -1;
	private float alpha = 1f;

	private static int border = 30;

	private int fadeOutTime = 1000;

	private Button startButton;
	private Button settingsButton;

	private ShapeRenderer shapeRenderer;


	public Menu() {
		batch = RocketLauncher.getSpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundSprite = new Sprite(background);
		logoSprite = new Sprite(logo);
		PlayerStats.update();

		font = RocketLauncher.getAssetHelper().getFont("superstar",
				(int) (Gdx.graphics.getHeight() * 0.03));

		startButton = new Button(0, 0, (int) (Gdx.graphics.getWidth() * 0.5),
				(int) ((Gdx.graphics.getWidth() * 0.5) / 10), new Color(0x2beafcff),
				new Color(0x0183e5ff), new Color(0x06bbf4ff), "Starten", 5);

		settingsButton = new Button(0, 0, (int) (Gdx.graphics.getWidth() * 0.5),
				(int) ((Gdx.graphics.getWidth() * 0.5) / 10), new Color(0x2beafcff),
				new Color(0x0183e5ff), new Color(0x06bbf4ff), "Beenden", 5);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		updateBackground();
	}

	private void updateBackground() {
		float height;
		float width;
		if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {
			height = ((float) background.getHeight() / (float) background.getWidth())
					* (float) Gdx.graphics.getWidth();
			if (height < Gdx.graphics.getHeight()) {
				height = Gdx.graphics.getHeight();
				width = ((float) background.getWidth() / (float) background.getHeight())
						* (float) Gdx.graphics.getHeight();
			} else {
				width = Gdx.graphics.getWidth();
			}
		} else {
			width = ((float) background.getWidth() / (float) background.getHeight())
					* (float) Gdx.graphics.getHeight();
			if (width < Gdx.graphics.getWidth()) {
				width = Gdx.graphics.getWidth();
				height = ((float) background.getHeight() / (float) background.getWidth())
						* (float) Gdx.graphics.getWidth();
			} else {
				height = Gdx.graphics.getHeight();
			}
		}
		backgroundSprite.setSize(width, height);
		backgroundSprite.setPosition(-width / 2, -height / 2);
		backgroundSprite.setAlpha(alpha);
	}

	private void updateLogo() {
		logoSprite.setSize(Gdx.graphics.getWidth() * 0.5f,
				((float) logo.getHeight() / (float) logo.getWidth())
						* (float) Gdx.graphics.getWidth() * 0.5f);
		logoSprite.setPosition(-logoSprite.getWidth() / 2, -logoSprite.getHeight() * 0.3f);
		logoSprite.setAlpha(alpha);
	}

	private void updateButtons() {
		startButton.setWidth((int) (Gdx.graphics.getWidth() * 0.5));
		startButton.setHeight((int) ((Gdx.graphics.getWidth() * 0.5) / 10));
		startButton.setPosition(0,
				(int) (-startButton.getHeight() - logoSprite.getHeight() * 0.2f));
		startButton.setAlpha(alpha);

		settingsButton.setWidth((int) (Gdx.graphics.getWidth() * 0.5));
		settingsButton.setHeight((int) ((Gdx.graphics.getWidth() * 0.5) / 10));
		settingsButton.setPosition(0, (int) (-settingsButton.getHeight() - startButton.getHeight()
				- logoSprite.getHeight() * 0.2f - Gdx.graphics.getHeight() * 0.02f));
		settingsButton.setAlpha(alpha);
		if (startButton.isClicked() && finished == -1)
			next();
		if (settingsButton.isClicked()) {
			PlayerStats.update();
			Gdx.app.exit();
		}
	}

	@Override
	public void render(float delta) {
		if (finished > 0 & TimeUtils.millis() - finished > fadeOutTime) {
			RocketLauncher.getInstance().setScreen(new GameScreen());
			dispose();
			return;
		}
		update();
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		camera.update();
		startButton.update(delta);
		settingsButton.update(delta);
		batch.begin();
		backgroundSprite.draw(batch);
		logoSprite.draw(batch);
		font.draw(batch,
				"Highscore: " + PlayerStats.getLevelFromExp(PlayerStats.getHighscore()) + " LVL",
				-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - border,
				Gdx.graphics.getWidth() - border, Align.right, false);
		batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin();
		startButton.shapeRender(shapeRenderer);
		settingsButton.shapeRender(shapeRenderer);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		startButton.render(batch);
		settingsButton.render(batch);
		batch.end();

	}

	private void next() {
		finished = TimeUtils.millis();
	}

	private void update() {
		if (finished != -1) {
			alpha = 1 - (TimeUtils.millis() - finished) / (float) fadeOutTime;
		}
		updateLogo();
		updateBackground();
		updateButtons();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		updateBackground();
		startButton.resize(width, height);
		settingsButton.resize(width, height);
		font = RocketLauncher.getAssetHelper().getFont("superstar",
				(int) (Gdx.graphics.getHeight() * 0.03));
	}

	@Override
	public void dispose() {
		startButton.dispose();
	}

}
