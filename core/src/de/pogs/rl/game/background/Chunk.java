package de.pogs.rl.game.background;

import javax.swing.text.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class Chunk {
    public Sprite sprite;
    public Texture texture;
    private PerlinNoiseGenerator noise;
    public Vector2 position;
    private int radius;

    Chunk(int radius, int x, int y, double seed) {
        position = new Vector2();
        position.set(x, y);
        noise = new PerlinNoiseGenerator(seed);
        this.texture = new Texture(generatePixmap(radius, radius, new Vector2(position.x - radius, position.y -radius), 10, 100, 0.2));
        sprite = new Sprite(texture);
        sprite.setSize(radius, radius);
        this.radius = radius;
    }

    public void update() {
        sprite.setPosition(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight());
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
            //     val = (byte) 0;
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
                bytes[idx++] = (byte) (noise.noise((double) (start.x + x) * scale, (double) (start.y - y) * scale)
                        * range + min);
            }
        }
        System.out.println(idx);
        return bytes;
    }
}
