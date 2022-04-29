package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;
import java.io.Console;

import de.pogs.rl.utils.FastNoiseLite;

public final class Chunk {

    private Color blue = new Color(58, 80, 224);
    private Color purple = new Color(212, 0, 255);
    private Color red = new Color(166, 46, 50);

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
                Color color;
                double baseValue = getBaseValue(x, y);
                double starValue = getStarValue(x, y, baseValue);
                if (starValue == 0) {
                    color = getColorValue(x, y);
                    bytes[idx++] = removeAlpha(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            (int) (saveRGBValue((int) ((Math.max(baseValue, 0) + 0.2) * 255)))));

                } else {
                    bytes[idx++] = removeAlpha(new Color(255, 255, 255, (int) starValue));
                }
            }
        }
        return bytes;
    }

    private Color removeAlpha(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float saturation = hsb[1];
        float brightness = hsb[2];
        brightness = color.getAlpha() / 255f;
        return new Color(Color.HSBtoRGB(hsb[0], saturation, brightness));
    }

    private int saveRGBValue(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

    private double getBaseValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_level1 = genNoise(ChunkManager.BASENOISE_LEVEL1, relativePositon, 0.3, 0, 0.4);
        double value_level2 = genNoise(ChunkManager.BASENOISE_LEVEL2, relativePositon, 1, 0, 0.1);
        double value_level3 = genNoise(ChunkManager.BASENOISE_LEVEL3, relativePositon, 0.1, 0, 0.2);
        return (value_level1 - Math.max(value_level2, 0) - value_level3) + 0.3;
    }

    private Color getColorValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_blue = genNoise(ChunkManager.COLORNOISE_BLUE, relativePositon, 0.1, 0, 1);
        double value_purple = genNoise(ChunkManager.COLORNOISE_PURPLE, relativePositon, 0.3, 0, 1);
        double value_red = genNoise(ChunkManager.COLORNOISE_RED, relativePositon, 0.1, 0, 1);
        value_blue = minmax(value_blue, 0.001, 1);
        value_purple = minmax(value_purple, 0.001, 1);
        value_red = minmax(value_red, 0.001, 1);
        if (Double.isNaN(value_blue / value_purple) || value_blue == 0) {
            return Color.RED;
        }
        return mix(mix(purple, red, getMixValue(value_purple, value_red)), blue, getMixValue(value_blue, value_purple));
    }

    private double getMixValue(double a, double b) {
        return (a + b) / 2;
    }

    private double getStarValue(int x, int y, double baseValue) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double starValue = Math.abs(genNoise(ChunkManager.STARNOISE_LEVEL1, relativePositon, 20, 0, 255));
        if (starValue > 250 && isStarSpot(baseValue)) {
            return starValue * getStarMultiplier(baseValue);
        } else {
            return 0;
        }
    }

    private boolean isStarSpot(double value) {
        if (value > -0.5) {
            return true;
        }
        return false;
    }

    private double getStarMultiplier(double value) {
        return Math.min(Math.max(value, -0.6) + 1, 1);
    }

    private double minmax(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    private double genNoise(FastNoiseLite noise, Vector2 position, double scale, double min, double max) {
        return ((noise.GetNoise((float) (position.x * scale), (float) (position.y * scale)) * (max - min + 1)) + min);
    }

    private Vector2 getRelativePosition(Vector2 position) {
        return new Vector2(position.x + start.x, position.y - start.y);
    }

    private Color mix(Color a, Color b, double percent) {
        percent = Math.max(Math.min(percent, 1), 0);
        return new Color((int) (a.getRed() * percent + b.getRed() * (1.0 - percent)),
                (int) (a.getGreen() * percent + b.getGreen() * (1.0 - percent)),
                (int) (a.getBlue() * percent + b.getBlue() * (1.0 - percent)));
    }
}
