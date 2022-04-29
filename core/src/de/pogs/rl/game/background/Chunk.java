package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;

import de.pogs.rl.utils.FastNoiseLite;

public final class Chunk {
    private Sprite sprite;
    private Texture texture;
    public Vector2 position;
    private Vector2 start;
    public int radius;

    Chunk(int radius, int x, int y) {
        this.radius = radius;
        position = new Vector2();
        position.set(x, y);
        this.start = new Vector2(x - radius, y - radius);
        this.texture = new Texture(this.generatePixmap(), true);
        sprite = new Sprite(texture);
        sprite.setSize(radius, radius);
        update();
    }

    public void update() {
        sprite.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        // texture.dispose();
    }

    public Pixmap generatePixmap() {
        Color[] bytes = generateBytes();
        Pixmap pixmap = new Pixmap(radius, radius, Format.RGBA8888);
        int idx = 0;

        for (int i = 1; i < bytes.length; i++) {
            Color val = bytes[i];
            pixmap.getPixels().put(idx++, (byte) (val.getRed()));
            pixmap.getPixels().put(idx++, (byte) (val.getGreen()));
            pixmap.getPixels().put(idx++, (byte) (val.getBlue()));
            pixmap.getPixels().put(idx++, (byte) (val.getAlpha()));
        }
        return pixmap;
    }

    public Color[] generateBytes() {
        Color[] bytes = new Color[((int) (Math.pow(radius, 2) + 1))];
        int idx = 1;
        for (int y = 0; y < radius; y++) {
            for (int x = 0; x < radius; x++) {
                // GENERATE COLOR
                double baseValue = getBaseValue(x, y);
                bytes[idx++] = new Color(255, 255, 255, (int) (saveRGBValue((int) (baseValue * 255))));
            }
        }
        return bytes;
    }

    private int saveRGBValue(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

    private double getBaseValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_level1 = genNoise(ChunkManager.BASENOISE_LEVEL1, relativePositon, 0.3, 0, 0.6);
        double value_level2 = genNoise(ChunkManager.BASENOISE_LEVEL2, relativePositon, 1, 0, 0.1);
        double value_level3 = genNoise(ChunkManager.BASENOISE_LEVEL3, relativePositon, 0.1, 0.1, 0.3);
        return (value_level1 + value_level2 + value_level3) / 3 + 0.2;
    }

    private double genNoise(FastNoiseLite noise, Vector2 position, double scale, double min, double max) {
        return (noise.GetNoise((float) (position.x * scale), (float) (position.y * scale)) * (max - min + 1)) + min;
    }

    private Vector2 getRelativePosition(Vector2 position) {
        return new Vector2(position.x + start.x, position.y - start.y);
    }

    // private double getLightValue(double x, double y, Vector2 start) {
    // double lightValue = noise.GetNoise((float) ((start.x + x) * scale), (float)
    // ((start.y - y) * scale));
    // double roughValue = roughNoise.GetNoise((float) ((start.x + x) * scale * 15),
    // (float) ((start.y - y) * scale * 15));

    // return ((lightValue * 0.9 + roughValue * 0.1) * (max - min + 1) + min);
    // }

    private Color mix(Color a, Color b, double percent) {
        percent = Math.max(Math.min(percent, 1), 0);
        return new Color((int) (a.getRed() * percent + b.getRed() * (1.0 - percent)),
                (int) (a.getGreen() * percent + b.getGreen() * (1.0 - percent)),
                (int) (a.getBlue() * percent + b.getBlue() * (1.0 - percent)));
    }
}
