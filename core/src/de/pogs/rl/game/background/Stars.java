package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.awt.Color;
import java.text.NumberFormat.Style;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class Stars {
    public Pixmap pixmap;
    private PerlinNoiseGenerator starNoise;
    private Texture stars;

    private Color starColor;

    private int radius;

    private double minStar = 0.6,
            minLight = 0.02;

    private int minVal = 0, maxVal = 1;
    private double scale = 50;

    // private Texture[] chunks;


    public int renderDistance = 3;
    public int chunkSize = 50;

    public Stars(int radius, double starSeed, Color starColor) {
        this.radius = radius;
        this.starColor = starColor;


        // chunks = new Texture[(int) Math.round(Math.PI * Math.pow((double) renderDistance, 2))];
        // System.out.println(chunks.length);
        // starSeed = new Random().nextGaussian() * 255;
        starNoise = new PerlinNoiseGenerator(starSeed);
        stars = generateLightTexture(radius, radius, new Vector2());
        BackgroundLayer.INSTANCE.starSprite = new Sprite(stars);
    }

    public void update(Vector2 start) {
        stars.draw(generateStarPixmap(radius, radius, start, maxVal, minVal, scale), 0, 0);
    }

    public Texture generateLightTexture(int width, int height, Vector2 start) {
        return new Texture(generateStarPixmap(width, height, start, maxVal, minVal, scale));
    }

    public Pixmap generateStarPixmap(int width, int height, Vector2 start, double max, double min, double scale) {
        long startTime = System.nanoTime();
        byte[] bytes = generateStarBytes(width, height, start, max, min, scale);
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        int idx = 0;
        for (int i = 1; i < bytes.length; i++) {
            byte val = bytes[i];
            pixmap.getPixels().put(idx++, (byte) starColor.getRed());
            pixmap.getPixels().put(idx++, (byte) starColor.getGreen());
            pixmap.getPixels().put(idx++, (byte) starColor.getBlue());
            pixmap.getPixels().put(idx++, (byte) val);
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Runtime: " + totalTime / 1000000);
        return pixmap;
    }

    public byte[] generateStarBytes(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = new byte[width * height];
        int idx = 0;
        double scaleL = 0.2;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double starValue = starNoise.noise((double) (x + start.x) * scale, (double) (y + start.y) * scale);
                double lightValue = BackgroundLayer.INSTANCE.light.lightNoise.noise((double) (x + start.x) * scaleL,
                        (double) (y + start.y) * scaleL);
                if ((starValue + lightValue) > 0.7 && starValue > 0.4) {
                    bytes[idx++] = (byte) ((starValue + lightValue - (minStar + minLight)) * 255);
                } else {
                    bytes[idx++] = (byte) 0;
                }
            }
        }
        System.out.println(idx);
        return bytes;
    }
}
