/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */

package de.pogs.rl.game.background;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;

import de.pogs.rl.utils.FastNoiseLite;


/**
 * Einzelner Chunk des Hintergrundes
 */

public final class BackgroundChunk {

    public Color[][] fieldCache;

    private Color blue = new Color(58, 80, 224);
    private Color purple = new Color(212, 0, 255);
    private Color red = new Color(166, 46, 50);

    private Sprite sprite;
    private Texture texture;

    private Vector2 position;

    private Vector2 start;
    private int size;

    private float scaling;

    /**
     * Erstellung des Chunks
     * 
     * @param size Größe des Chunks
     * @param x X - Koordinate im Chunkgitter
     * @param y Y - Koordinate im Chunkgitter
     * @param scaling Skalierung der Generierung (Für verschiedene Detaillierung)
     */
    BackgroundChunk(int size, int x, int y, float scaling) {
        this.size = size;
        this.scaling = scaling;
        position = new Vector2();
        position.set(x, y);
        fieldCache = new Color[(int) (size)][(int) (size)];
        this.start = new Vector2(x - size, y - size);
        this.texture = new Texture(this.generatePixmap(), true);
        sprite = new Sprite(texture);
        sprite.setSize(size, size);
        sprite.setScale(scaling);
        update();
    }

    /**
     * Position des Sprites zu der Chunkposition setzen
     */
    public void update() {
        sprite.setPosition(position.x, position.y);
    }

    /**
     * Chunk rendern
     * 
     * @param batch SpriteBatch des GameScreens
     */
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Unbenötigte Textur aus dem Cache löschen
     */
    public void dispose() {
        texture.dispose();
    }

    /**
     * Pixmap aus den berechneten Farben generieren
     * 
     * @return Pixmap (Bild)
     */
    public Pixmap generatePixmap() {
        Color[] bytes = generateBytes();
        Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
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

    /**
     * Farben für jeden Punkt des Chunkes aus verschiedenen NoiseMaps berechnen
     * 
     * @return Liste mit Farben
     */
    public Color[] generateBytes() {
        Color[] bytes = new Color[((int) (Math.pow(size, 2) + 1))];
        int idx = 1;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Color color = BackgroundLayer.getChunkManager().getCachedColor(x, y, start); // Überprüfen
                                                                                             // von
                                                                                             // eventuell
                                                                                             // gespeicherten
                                                                                             // Wert
                if (color == null) {

                    double baseValue = getBaseValue(x, y); // Helligkeit
                    double starValue = getStarValue(x, y, baseValue); // Stern
                    color = getColorValue(x, y); // Farbe
                    color = mix(Color.WHITE, removeAlpha(new Color(color.getRed(), color.getGreen(),
                            color.getBlue(),
                            (int) (saveRGBValue((int) ((Math.max(baseValue, 0) + 0.2) * 255))))),
                            starValue); // Generierung der Farbe durch Mischen
                }
                bytes[idx++] = color;
                fieldCache[(int) (x)][(int) (y)] = color;
            }
        }
        return bytes;
    }

    /**
     * Alphakanal einer Farbe entfernen
     * 
     * @param color Farbe mit Alphakanal
     * @return Farbe ohne Alphakanal mit der Hintergrundfarbe Schwarz
     */
    private Color removeAlpha(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        // Farbe in eine HSV Farbe umwandeln
        float brightness = hsb[2];
        brightness = ((color.getAlpha()/ 255f) - 0.1f) * 0.7f; // Helligkeit nach Alphakanal anpassn
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness));
    }

    /**
     * RGB Wert in Spanne 0 bis 255 halten
     * 
     * @param value RGB Wert
     * @return RGB Wert im Bereich 0 bis 255
     */
    private int saveRGBValue(int value) {
        return Math.min(Math.max(value, 0), 255);
    }

    /**
     * Helligkeit von Position aus einer 3-Layer NoiseMap
     * 
     * @param x X - Koordinate des Punktes
     * @param y Y - Koordinate des Punktes
     * @return Helligkeits Wert
     */
    private double getBaseValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_level1 =
                genNoise(BackgroundChunkManager.BASENOISE_LEVEL1, relativePositon, 0.3, 0, 0.4);
        double value_level2 =
                genNoise(BackgroundChunkManager.BASENOISE_LEVEL2, relativePositon, 1, 0, 0.1);
        double value_level3 =
                genNoise(BackgroundChunkManager.BASENOISE_LEVEL3, relativePositon, 0.1, 0, 0.2);
        return (value_level1 - Math.max(value_level2, 0) - value_level3) + 0.3;
    }

    /**
     * Farbe aus einer 3-Layer NoiseMap bestehend aus Blau, Violett und Rot
     * 
     * @param x X - Koordinate des Punktes
     * @param y Y - Koordinate des Punktes
     * @return Farbe
     */
    private Color getColorValue(int x, int y) {
        Vector2 relativePositon = getRelativePosition(new Vector2(x, y));
        double value_blue =
                genNoise(BackgroundChunkManager.COLORNOISE_BLUE, relativePositon, 0.1, 0, 1);
        double value_purple =
                genNoise(BackgroundChunkManager.COLORNOISE_PURPLE, relativePositon, 0.3, 0, 1);
        double value_red =
                genNoise(BackgroundChunkManager.COLORNOISE_RED, relativePositon, 0.1, 0, 1);
        value_blue = minmax(value_blue, 0.001, 1);
        value_purple = minmax(value_purple, 0.001, 1);
        value_red = minmax(value_red, 0.001, 1);
        if (Double.isNaN(value_blue / value_purple) || value_blue == 0) {
            return Color.RED;
        }
        return mix(mix(purple, red, getMixValue(value_purple, value_red)), blue,
                getMixValue(value_blue, value_purple));
    }

    /**
     * Durchschnitt aus zwei Zahlen berechnen
     * 
     * @param a Wert 1
     * @param b Wert 2
     * @return Durchschnitt der beiden Zahlen
     */
    private double getMixValue(double a, double b) {
        return (a + b) / 2;
    }

    /**
     * Nach Zufallsprinzip Stern Helligkeit berechnen
     * 
     * @param x X - Koordinate des Punktes
     * @param y Y - Koordinate des Punktes
     * @param baseValue Lichtwert
     * @return Helligkeit von Stern
     */
    private double getStarValue(int x, int y, double baseValue) {
        if (isStarSpot(baseValue)) {
            return getStarMultiplier(baseValue);
        }
        return 0f;
    }

    /**
     * Nach Zufall entscheiden ob ein Stern generiert werden soll
     * 
     * @param value Lichtwert
     * @return
     */
    private boolean isStarSpot(double value) {
        if (value > -0.5 && Math.random() > 0.99) {
            return true;
        }
        return false;
    }

    /**
     * Sternhelligkeit nach Lichtwert
     * 
     * @param value Lichtwert
     * @return Wert für Stern
     */
    private double getStarMultiplier(double value) {
        return Math.min(Math.max(value, -0.6) + 0.5, 1);
    }

    private double minmax(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Wert einer NoiseMap vorbereiten
     * 
     * @param noise NoiseMap
     * @param position Position in NoiseMap
     * @param scale Skalierung der NoiseMap
     * @param min Minimalwert (Skalierung der Werte)
     * @param max Maximalwert (Skalierung der Werte)
     * @return Wert der NoiseMap
     */
    private double genNoise(FastNoiseLite noise, Vector2 position, double scale, double min,
            double max) {
        return ((noise.GetNoise((float) (position.x * scale * 1.5),
                (float) (position.y * scale * 1.5)) * (max - min + 1)) + min);
    }

    /**
     * Position eines Punktes im Koordinatensystem des GameScreens
     * 
     * @param position Position im Chunk
     * @return absolute Koordinaten
     */
    public Vector2 getRelativePosition(Vector2 position) {
        return new Vector2(position.x * scaling + start.x, position.y * scaling - start.y);
    }

    /**
     * Farben mischen
     * 
     * @param a Farbe 1
     * @param b Farbe 2
     * @param percent Verhältnis zwischen Farbe 1 und Farbe 2
     * @return gemischte Farbe
     */
    private Color mix(Color a, Color b, double percent) {
        percent = Math.max(Math.min(percent, 1), 0);
        return new Color((int) (a.getRed() * percent + b.getRed() * (1.0 - percent)),
                (int) (a.getGreen() * percent + b.getGreen() * (1.0 - percent)),
                (int) (a.getBlue() * percent + b.getBlue() * (1.0 - percent)));
    }


    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getStart() {
        return start;
    }

    public void setStart(Vector2 start) {
        this.start = start;
    }
}
