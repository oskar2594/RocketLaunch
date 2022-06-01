package de.pogs.rl.screens.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

		testButton = new Button(0, 0, (int)(Gdx.graphics.getWidth() * 0.5), (int) ((Gdx.graphics.getWidth() * 0.5) / 10), new Color(0x2beafcff), new Color(0x0183e5ff), new Color(0x06bbf4ff), "Starten", 5);
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
		System.out.println(logoSprite.getHeight());
		logoSprite.setSize(Gdx.graphics.getWidth() * 0.5f,
				((float) logo.getHeight() / (float) logo.getWidth())
						* (float) Gdx.graphics.getWidth() * 0.5f);
		logoSprite.setPosition(-logoSprite.getWidth() / 2, -logoSprite.getHeight() / 2);
		logoSprite.setAlpha(alpha);
	}

	private void updateButton() {
		testButton.setWidth((int)(Gdx.graphics.getWidth() * 0.5));
		testButton.setHeight((int) ((Gdx.graphics.getWidth() * 0.5) / 10));
		testButton.setPosition(0, (int) - (logoSprite.getHeight() / 2) - testButton.getHeight());
		testButton.setAlpha(alpha);
	}

	@Override
	public void render(float delta) {
		if (finished > 0 & TimeUtils.millis() - finished > fadeOutTime) {
			RocketLauncher.INSTANCE.setScreen(new GameScreen());
			dispose();
			return;
		}
		update();
		if (testButton.isClicked() && finished == -1) next();
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

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin();
		testButton.shapeRender(shapeRenderer);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.setProjectionMatrix(camera.combined);
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
		updateButton();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		updateBackground();
		testButton.resize(width, height);

	}

	@Override
	public void dispose() {
		background.dispose();
		testButton.dispose();
	}

}
