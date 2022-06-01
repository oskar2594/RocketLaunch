package de.pogs.rl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.screens.Menu.Menu;

public class Loader extends ScreenAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private float scaleOne = 0.001f;

    private int minDuration = 1000;
    private long start;

    private Color white = Color.WHITE;
    private Color black = Color.BLACK;

    private boolean backgroundColor = true;

    public Loader() {
        start = TimeUtils.millis();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = RocketLauncher.getSpriteBatch();

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 5;
        font = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/Roboto.ttf"))
                .generateFont(parameter);

        RocketLauncher.getAssetHelper().loadAll();

    }

    @Override
    public void render(float delta) {
        RocketLauncher.getAssetHelper().assetManager.update();
        if (RocketLauncher.getAssetHelper().assetManager.isFinished()) {
            if ((TimeUtils.millis() - start) > minDuration) {
                RocketLauncher.getInstance().setScreen(new Menu());
                return;
            }
        }
        update();
        Gdx.gl.glClearColor(getColor(backgroundColor).r, getColor(backgroundColor).g,
                getColor(backgroundColor).b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                        : 0));
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.getData().setScale(scaleOne, scaleOne);
        font.setColor(getColor(!backgroundColor));
        font.draw(batch, "LOADING",
                -Gdx.graphics.getWidth() / 2 + Gdx.graphics.getHeight() * 0.015f * scaleOne,
                (font.getCapHeight()) / 2, Gdx.graphics.getWidth(), Align.center, false);
        batch.end();
    }

    private void update() {
        scaleOne += scaleOne * .2f;
        if (scaleOne * 5 > Gdx.graphics.getHeight() * 2) {
            scaleOne = 0.001f;
            backgroundColor = !backgroundColor;
        }
    }

    private Color getColor(boolean toggle) {
        return (toggle) ? white : black;
    }

    @Override
    public void hide() {

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
