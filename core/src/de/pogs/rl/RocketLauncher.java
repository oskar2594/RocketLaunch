package de.pogs.rl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import de.pogs.rl.entities.Rocket;

public class RocketLauncher extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket;

	private Texture bucketImage;
	private Rocket rocket;
	final static int FPS = 60;

	@Override
	public void create() {
		bucketImage = new Texture(Gdx.files.internal("rakete.jpg"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);	 
		batch = new SpriteBatch();

		rocket = new Rocket(bucketImage, 0, 0);


	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		rocket.draw(batch);
		batch.end();

		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			rocket.input((int) touchPos.x, (int) touchPos.y);
		 }
		 rocket.step(Gdx.graphics.getDeltaTime());
	  
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
