package de.pogs.rl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Menu extends ScreenAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture background = RocketLauncher.INSTANCE.assetHelper.getImage("background");
	private Sprite backgroundSprite;

	private Texture logo = RocketLauncher.INSTANCE.assetHelper.getImage("logo");
	private Sprite logoSprite;

	private BitmapFont font = RocketLauncher.INSTANCE.assetHelper.getFont("roboto", 20);

	private long finished = -1;
	private float alpha = 1f;

	private int fadeOutTime = 2000;


	public Menu() {
		batch = RocketLauncher.INSTANCE.batch;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundSprite = new Sprite(background);
		logoSprite = new Sprite(logo);

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
		logoSprite.setPosition(-logoSprite.getWidth() / 2, -logoSprite.getHeight() / 2);
		logoSprite.setAlpha(alpha);
	}

	@Override
	public void render(float delta) {
		if (finished > 0 & TimeUtils.millis() - finished > fadeOutTime) {
			RocketLauncher.INSTANCE.setScreen(new GameScreen());
			dispose();
			return;
		}
		update();
		if ((Gdx.input.isKeyJustPressed(Keys.ANY_KEY) || Gdx.input.isTouched()) && finished == -1)
			next();

		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		backgroundSprite.draw(batch);
		logoSprite.draw(batch);

		font.draw(batch, "Drücke jegliche Taste, um das Spiel zu starten...",
				-Gdx.graphics.getWidth() / 2, -logoSprite.getHeight() / 2 - 20,
				Gdx.graphics.getWidth(), Align.center, false);

		batch.end();
	}

	private void next() {
		finished = TimeUtils.millis();
		System.out.println("d");
	}

	private void update() {
		if (finished != -1) {
			alpha = 1 - (TimeUtils.millis() - finished) / (float) fadeOutTime;
		}
		font.setColor(1, 1, 1,
				(float) (alpha * (1.1f + Math.sin(TimeUtils.millis() * Math.pow(10, -2)) * 0.8f)));
		updateLogo();
		updateBackground();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;

	}

	@Override
	public void dispose() {

	}

}
