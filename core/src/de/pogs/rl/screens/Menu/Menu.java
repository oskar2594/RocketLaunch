package de.pogs.rl.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.menu.Button;

public class Menu extends ScreenAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;

	private Texture background = RocketLauncher.INSTANCE.assetHelper.getImage("background");
	private Sprite backgroundSprite;

	private Texture logo = RocketLauncher.INSTANCE.assetHelper.getImage("logo");
	private Sprite logoSprite;

	private long finished = -1;
	private float alpha = 1f;

	private int fadeOutTime = 2000;

	private Button testButton;
	private ShapeRenderer shapeRenderer;


	public Menu() {
		batch = RocketLauncher.INSTANCE.batch;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundSprite = new Sprite(background);
		logoSprite = new Sprite(logo);

		testButton = new Button(0, -150, 100, 50, Color.BLACK, Color.YELLOW, Color.BLUE, "Starten");
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
		if (testButton.active && finished == -1)
			next();

		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
						: 0));
		camera.update();
		testButton.update(delta);
		batch.begin();
		backgroundSprite.draw(batch);
		logoSprite.draw(batch);
		batch.end();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin();
		testButton.shapeRender(shapeRenderer);
		shapeRenderer.end();

		batch.begin();
		testButton.render(batch);
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
