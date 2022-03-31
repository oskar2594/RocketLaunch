package de.pogs.rl.game.background;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


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

    public BackgroundLayer() {
        INSTANCE = this;
        position = new Vector2();
        position.set(0, 0);
    
        lightSprite = new Sprite();
        starSprite = new Sprite();

        radius = Gdx.graphics.getWidth() * 2;
        lightSeed = new Random().nextGaussian() * 255;
        starSeed = new Random().nextGaussian() * 255;

        light = new Light(radius, lightSeed, Color.RED);
        stars = new Stars(radius, starSeed, new Color(255, 255, 255, 255));
    }

    public void update(float delta) {
        light.update();

        starSprite.setPosition(-(starSprite.getWidth() / 2) + position.x,  -(starSprite.getHeight() / 2) + position.x);
        lightSprite.setPosition(-(lightSprite.getWidth() / 2) + position.x,  -(lightSprite.getHeight() / 2) + position.x);
    }

    public void render(float delta, SpriteBatch batch) {
        this.update(delta);
        starSprite.draw(batch);
        lightSprite.draw(batch);
    }
}
