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

    public Color[][] fieldCache;

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
        fieldCache = new Color[radius][radius];
        this.start = new Vector2(x - radius, y - radius);
        this.create();
    }

    public void create() {
        this.texture = new Texture(this.generatePixmap(), true);
        sprite = new Sprite(texture);
        sprite.setSize(radius, radius);
        // sprite.setScale(0.98f);
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

    //generate Pixmap over a Bitstream
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
                Color color = BackgroundLayer.INSTANCE.chunkManager.getCachedColor(x, y, position);
                if (color == null) { //if there is no cached color

                    double baseValue = getBaseValue(x, y); //get light value
                    double starValue = getStarValue(x, y, baseValue); //get value of a possible star
                    if (starValue == 0) {//if theres no star
                        color = getColorValue(x, y); // generate a color for position
                        //replace transparent parts of the image with black
                        color = removeAlpha(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                (int) (saveRGBValue((int) ((Math.max(baseValue, 0) + 0.2) * 255)))));

                    } else {
                        color = removeAlpha(new Color(255, 255, 255, (int) starValue));
                    }
                }
                bytes[idx++] = color;
                fieldCache[x][y] = color;
            }
        }
        return bytes;
    }

    private Color removeAlpha(Color color) {
        //turning RGBA Color to a HSB Color
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float saturation = hsb[1];
        float brightness = hsb[2];
        brightness = color.getAlpha() / 255f; //get the brightness based on the transparency
        return new Color(Color.HSBtoRGB(hsb[0], saturation, brightness)); // return a normal Color Object
    }

    private int saveRGBValue(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

    //combining Basenoise Level 1-3
    private double getBaseValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_level1 = genNoise(ChunkManager.BASENOISE_LEVEL1, relativePositon, 0.3, 0, 0.4);
        double value_level2 = genNoise(ChunkManager.BASENOISE_LEVEL2, relativePositon, 1, 0, 0.1);
        double value_level3 = genNoise(ChunkManager.BASENOISE_LEVEL3, relativePositon, 0.1, 0, 0.2);
        return (value_level1 - Math.max(value_level2, 0) - value_level3) + 0.3;
    }

    //combining Colornoise Blue, Purple and Red
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
        //mixing colors 
        return mix(mix(purple, red, getMixValue(value_purple, value_red)), blue, getMixValue(value_blue, value_purple));
    }

    private double getMixValue(double a, double b) {
        return (a + b) / 2;
    }


    private double getStarValue(int x, int y, double baseValue) {
        if (isStarSpot(baseValue)) {
            return 255 * getStarMultiplier(baseValue);
        }
        return 0;
    }


    //randomly decide if the spot is a star value
    private boolean isStarSpot(double value) {
        if (value > -0.5 && Math.random() > 0.99) {
            return true;
        }
        return false;
    }

    private double getStarMultiplier(double value) {
        return Math.min(Math.max(value, -0.6) + 0.5, 1);
    }

    private double minmax(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    //scale size and min max values of raw noise
    private double genNoise(FastNoiseLite noise, Vector2 position, double scale, double min, double max) {
        return ((noise.GetNoise((float) (position.x * scale), (float) (position.y * scale)) * (max - min + 1)) + min);
    }

    //get position in normal coordination system
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
