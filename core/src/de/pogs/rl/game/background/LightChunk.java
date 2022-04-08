package de.pogs.rl.game.background;

import javax.swing.text.Position;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;

import de.pogs.rl.utils.PerlinNoiseGenerator;

public class LightChunk {
    private Sprite sprite;
    private Texture texture;
    private PerlinNoiseGenerator noise;
    private PerlinNoiseGenerator colorNoise;
    public Vector2 position;

    private int radius;
    private double min; 
    private double max;
    private double scale;
    private SpriteBatch batch;

    private int x;
    private int y;

    LightChunk(int radius, int x, int y, PerlinNoiseGenerator noise, PerlinNoiseGenerator colorNoise, double min,
            double max, double scale, SpriteBatch batch) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.noise = noise;
        this.colorNoise = colorNoise;
        position = new Vector2();
        position.set(x, y);
        this.noise = noise;
        this.colorNoise = colorNoise;
        this.texture = new Texture(this.generatePixmap(radius, radius, new Vector2(position.x - radius, position.y - radius), min, max, scale));
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
        Color[] bytes = generateBytes(width, height, start, max, min, scale);
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        int idx = 0;

        for (int i = 1; i < bytes.length; i++) {
            Color val = bytes[i];
            // if (val < 0)
            // val = (byte) 0;
            // Color color = new Color((int) val);
            // System.out.print(color.getBlue());
            pixmap.getPixels().put(idx++, (byte) val.getRed());
            pixmap.getPixels().put(idx++, (byte) val.getGreen());
            pixmap.getPixels().put(idx++, (byte) val.getBlue());
            pixmap.getPixels().put(idx++, (byte) val.getAlpha());
        }
        return pixmap;
    }

    private Color color1 = new Color(255, 195, 195, 255);
    private Color color2 = new Color(175, 172, 255, 255);

    public Color[] generateBytes(int width, int height, Vector2 start, double max, double min, double scale) {
        Color[] bytes = new Color[width * height + 1];
        int idx = 1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double lightValue = (Math.max(
                        noise.noise((double) (start.x + x) * scale, (double) (start.y - y) * scale) * (max - min + 1)
                                + min,
                        0));
                double colorValue = colorNoise.noise((double) (start.x + x) * scale * 0.7,
                        (double) (start.y - y) * scale * 0.7);

                int alpha = (int) (lightValue);
                Color mixColor;
                if (colorValue > 0) {
                    mixColor = mix(color1, color2, colorValue);
                } else {
                    mixColor = mix(color1, mix(Color.WHITE, color2, colorValue * -0.5), -colorValue);
                }
                // if(((lightValue / BackgroundLayer.INSTANCE.lightMax) + colorValue) < 0.2) {
                // mixColor = Color.WHITE;
                // }
                // mixColor = mix(mixColor, Color.WHITE, colorValue * 0.8);
                // mixColor = mix(mixColor, Color.white, 0.8);

                // Color color = new Color(mixColor.getRed() / 255, mixColor.getGreen() / 255,
                // mixColor.getBlue() / 255, 255);

                // int rgb = ((color2.getAlpha() << 24) | ((color2.getRed() & 255) << 16) |
                // ((color2.getGreen() & 255) << 8) | (color2.getBlue() & 255));
                bytes[idx++] = new Color(mixColor.getRed(), mixColor.getGreen(), mixColor.getBlue(), alpha);
            }
        }
        return bytes;
    }

    private Color mix(Color a, Color b, double percent) {
        percent = Math.max(Math.min(percent, 1), 0);
        return new Color((int) (a.getRed() * percent + b.getRed() * (1.0 - percent)),
                (int) (a.getGreen() * percent + b.getGreen() * (1.0 - percent)),
                (int) (a.getBlue() * percent + b.getBlue() * (1.0 - percent)));
    }
}
