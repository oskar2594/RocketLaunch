package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class Stars {
    public Pixmap pixmap;
    private PerlinNoiseGenerator starNoise;
    private Texture stars;

    private Color starColor;

    private double minStar = 0.2,
    minLight = 0.04;

    private int minVal = 0, maxVal = 5;
    private double scale = 20;

    public Stars(int radius, double starSeed, Color starColor) {
        this.starColor = starColor;
        // starSeed = new Random().nextGaussian() * 255;
        starNoise = new PerlinNoiseGenerator(starSeed);
        stars = generateLightTexture(radius, radius, new Vector2());
        BackgroundLayer.INSTANCE.starSprite = new Sprite(stars);
    }

    public Texture generateLightTexture(int width, int height, Vector2 start) {
        return new Texture(generateStarPixmap(width, height, start, minVal, maxVal, scale));
    }

    public Pixmap generateStarPixmap(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = generateStarBytes(width, height, start, max, min, scale);
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        System.out.println(starColor.r);
        int idx = 0;
        for (int i = 1; i < bytes.length; i++) {
            byte val = bytes[i];
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) val);
        }
        return pixmap;
    }

    public byte[] generateStarBytes(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = new byte[width * height];
        int idx = 0;
        double scaleL = 0.2;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double starValue = starNoise.noise((double) x * scale, (double) y * scale);
                double lightValue = BackgroundLayer.INSTANCE.light.lightNoise.noise((double) x * scaleL,
                        (double) y * scaleL);
                if (starValue > minStar && lightValue > minLight) {
                    bytes[idx++] = (byte) 255;
                } else {
                    bytes[idx++] = (byte) 0;
                }
            }
        }
        System.out.println(idx);
        return bytes;
    }
}
