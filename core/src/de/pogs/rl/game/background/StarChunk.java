package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class StarChunk {
    private Sprite sprite;
    private Texture texture;
    private PerlinNoiseGenerator noise;
    public Vector2 position;

    private int radius;
    private double min;
    private double max;
    private double scale;
    private SpriteBatch batch;

    private int x;
    private int y;

    StarChunk(int radius, int x, int y, PerlinNoiseGenerator noise, double min,
            double max, double scale, SpriteBatch batch) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.noise = noise;
        position = new Vector2();
        position.set(x, y);
        this.noise = noise;
        this.texture = new Texture(
                generatePixmap(radius, radius, new Vector2(position.x - radius, position.y - radius), min, max, scale));
        sprite = new Sprite(texture);
        sprite.setSize(radius, radius);
        update();
        draw(batch);
    }

    public void update() {
        sprite.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);

    }

    public void dispose() {
        texture.dispose();
    }

    public Pixmap generatePixmap(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = generateBytes(width, height, start, max, min, scale);
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        int idx = 0;
        for (int i = 1; i < bytes.length; i++) {
            byte val = bytes[i];
            // if (val < 0)
            // val = (byte) 0;
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) 255);
            pixmap.getPixels().put(idx++, (byte) val);
        }
        return pixmap;
    }

    public byte[] generateBytes(int width, int height, Vector2 start, double max, double min, double scale) {
        byte[] bytes = new byte[width * height + 1];
        int idx = 1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // bytes[idx++] = (byte) (Math.abs(noise.noise((double) (start.x + x) * scale,
                // (double) (start.y - y) * scale)) * (max - min + 1)+ min);
                double value = Math.abs(noise.noise((double) (start.x + x) * scale, (double) (start.y - y) * scale))
                        * (max - min + 1) + min;
                if (value > 2.6) {
                    double lightValue = (BackgroundLayer.INSTANCE.lightNoise.noise(
                            (double) (start.x + x) * BackgroundLayer.INSTANCE.lightScale,
                            (double) (start.y - y) * BackgroundLayer.INSTANCE.lightScale))
                            * (max - min + 1) + min;
                    if (lightValue + value > 3.1) {

                        bytes[idx++] = (byte) Math.max((value / 6 + Math.abs(lightValue) / 2) * 100, 100);
                    } else {
                        bytes[idx++] = (byte) 0;
                    }
                } else {
                    bytes[idx++] = (byte) 0;
                }
            }
        }
        return bytes;
    }
}
