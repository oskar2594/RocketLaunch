package de.pogs.rl.game.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.GameScreen;

import java.awt.Color;

public class BackgroundLayer {
    public static BackgroundLayer INSTANCE;

    public Sprite lightSprite;
    public Sprite starSprite;
    public Light light;
    public Stars stars;

    private Vector2 position;

    private int radius;

    private double lightSeed;
    private double starSeed;

    private ChunkLight ch;

    public BackgroundLayer() {
        INSTANCE = this;

        ch = new ChunkLight();

        position = new Vector2();
        position.set(0, 0);

        lightSprite = new Sprite();
        starSprite = new Sprite();

        radius = 50;
        lightSeed = new Random().nextGaussian() * 255;
        starSeed = new Random().nextGaussian() * 255;

        light = new Light(radius, lightSeed, Color.lightGray);
        stars = new Stars(radius, starSeed, Color.white);
    }

    public void update(float delta) {

        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        if (distance(position, camPos) > 1) {
            position.set(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
            light.update(camPos);
            stars.update(camPos);
        }
        starSprite.setPosition(-(starSprite.getWidth() / 2) + position.x, -(starSprite.getHeight() / 2) + position.y);
        lightSprite.setPosition(-(lightSprite.getWidth() / 2) + position.x,
                -(lightSprite.getHeight() / 2) + position.y);
    }

    public void render(float delta, SpriteBatch batch) {
        ch.update(batch);
        this.update(delta);
        starSprite.draw(batch);
        lightSprite.draw(batch);
    }

    private double distance(Vector2 start, Vector2 end) {
        return Math.sqrt((end.y - start.y) * (end.y - start.y) + (end.x - start.x) * (end.x - start.x));
    }
}
