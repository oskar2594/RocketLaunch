package de.pogs.rl.game.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.PerlinNoiseGenerator;

public class BackgroundLayer {
    public static BackgroundLayer INSTANCE;

    private int radius;

    public LightChunks lights;
    public StarChunks stars;

    private double lightSeed;
    private double starSeed;

    public PerlinNoiseGenerator lightNoise;
    public PerlinNoiseGenerator starNoise;

    public double lightScale = 0.1;
    public double starScale = 50;

    public double lightMin = 30;
    public double starMin = 0;

    public double lightMax = 70;
    public double starMax = 3;

    public BackgroundLayer() {
        INSTANCE = this;

        radius = 50;

        lightSeed = new Random().nextGaussian() * 255;
        starSeed = new Random().nextGaussian() * 255;

        lightNoise = new PerlinNoiseGenerator(lightSeed);
        starNoise = new PerlinNoiseGenerator(starSeed);

        lights = new LightChunks(radius, lightNoise, lightMax, lightMin, lightScale);
        stars = new StarChunks(radius, starNoise, starMax, starMin, starScale);

        // lightSprite = new Sprite();
        // starSprite = new Sprite();

        // radius = 50;
        // lightSeed = new Random().nextGaussian() * 255;
        // starSeed = new Random().nextGaussian() * 255;

        // light = new Light(radius, lightSeed, Color.lightGray);
        // stars = new Stars(radius, starSeed, Color.white);
    }
    // Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x,
    // GameScreen.INSTANCE.camera.position.y);
    // if (distance(position, camPos) > 1) {
    // position.set(GameScreen.INSTANCE.camera.position.x,
    // GameScreen.INSTANCE.camera.position.y);
    // light.update(camPos);
    // stars.update(camPos);
    // }
    // starSprite.setPosition(-(starSprite.getWidth() / 2) + position.x,
    // -(starSprite.getHeight() / 2) + position.y);
    // lightSprite.setPosition(-(lightSprite.getWidth() / 2) + position.x,
    // -(lightSprite.getHeight() / 2) + position.y);

    public BitmapFont font = new BitmapFont();

    public void render(float delta, final SpriteBatch batch) {
        long startTime = System.nanoTime();

        lights.update(batch);
        long endTime = System.nanoTime();
        long startTime2 = System.nanoTime();
        stars.update(batch);
        long endTime2 = System.nanoTime();
        // lights.update(batch);
        // stars.update(batch);
        long duration = (endTime - startTime);
        long duration2 = (endTime2 - startTime2);

        // System.out.println(duration / 1000000);
        font.draw(batch, Gdx.graphics.getFramesPerSecond() + " | " + (duration / 1000000) + "ms  | " + (duration2 / 1000000) + "ms",
                GameScreen.INSTANCE.camera.position.x - Gdx.graphics.getWidth() / 3,
                GameScreen.INSTANCE.camera.position.y - Gdx.graphics.getHeight() / 3);

        // starSprite.draw(batch);
        // lightSprite.draw(batch);
    }

    private double distance(Vector2 start, Vector2 end) {
        return Math.sqrt((end.y - start.y) * (end.y - start.y) + (end.x - start.x) * (end.x - start.x));
    }
}
