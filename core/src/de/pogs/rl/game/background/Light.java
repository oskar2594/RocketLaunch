package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class Light {
    public Pixmap pixmap;
    private Texture light;
    public PerlinNoiseGenerator lightNoise;

    private Color lightColor;

    private int minVal = 10, maxVal = 80;
    private double scale = 0.2;

    public Light(int radius, double lightSeed, Color lightColor) {
        this.lightColor = lightColor;
        // lightSeed = new Random().nextGaussian() * 255;
        lightNoise = new PerlinNoiseGenerator(lightSeed);
        light = generateLightTexture(radius, radius, new Vector2());

        BackgroundLayer.INSTANCE.lightSprite = new Sprite(light);
    }

    public void update() {

    }

    public Texture generateLightTexture(int width, int height, Vector2 start) {
        return new Texture(generatePixmap(width, height, start, minVal, maxVal, scale));
    }

	public Pixmap generatePixmap(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = generateBytes(width, height, start, max, min, scale);
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        int idx = 0;
        for (int i = 1; i < bytes.length; i++) {
            byte val = bytes[i];
            if(val < 0) val = (byte) 0;
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) val);
        }
        return pixmap;
    }

    public byte[] generateBytes(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = new byte[width * height];
        int idx = 0;
        double range = max - min;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bytes[idx++] = (byte) (lightNoise.noise((double) x * scale, (double) y * scale) * range + min);
            }
        }
        System.out.println(idx);
        return bytes;
    }
}
