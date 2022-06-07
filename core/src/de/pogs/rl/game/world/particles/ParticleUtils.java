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
package de.pogs.rl.game.world.particles;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.Color;
/**
 * Particle Utils
 */
public class ParticleUtils {
    /**
     * 1x1px große Textur in Farbe generieren
     * 
     * @param color Farbe der Textur
     * @return Textur
     */
    public static Texture generateParticleTexture(Color color) {
        int size = 1;
        int idx = 0;
        Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
        for (int i = 0; i < Math.pow(size, 2); i++) {
            pixmap.getPixels().put(idx++, (byte) (color.getRed()));
            pixmap.getPixels().put(idx++, (byte) (color.getGreen()));
            pixmap.getPixels().put(idx++, (byte) (color.getBlue()));
            pixmap.getPixels().put(idx++, (byte) (color.getAlpha()));
        }
        return new Texture(pixmap);
    }

    /**
     * Textur in Farbe generieren
     * 
     * @param color Farbe der Textur
     * @param width Breite der Textur
     * @param height Höhe der Textur
     * @return Textur
     */
    public static Texture generateParticleTexture(Color color, int width, int height) {
        int idx = 0;
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        for (int i = 0; i < width * height; i++) {
            pixmap.getPixels().put(idx++, (byte) (color.getRed()));
            pixmap.getPixels().put(idx++, (byte) (color.getGreen()));
            pixmap.getPixels().put(idx++, (byte) (color.getBlue()));
            pixmap.getPixels().put(idx++, (byte) (color.getAlpha()));
        }
        return new Texture(pixmap);
    }

    /**
     * Berechnet die Durchschnittsfarbe einer Textur Source:
     * https://stackoverflow.com/questions/28162488/get-average-color-on-bufferedimage-and-bufferedimage-portion-as-fast-as-possible
     * (Bearbeitet)
     * 
     * @param t Textur
     * @return Durchschnittsfarbe
     */
    public static Color averageColor(Texture t) {
        if (!t.getTextureData().isPrepared())
            t.getTextureData().prepare();
        Pixmap bi = t.getTextureData().consumePixmap();
        int x0 = 0;
        int y0 = 0;
        int w = t.getWidth();
        int h = t.getHeight();
        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        int count = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                com.badlogic.gdx.graphics.Color pixel =
                        new com.badlogic.gdx.graphics.Color(bi.getPixel(x, y));
                if (pixel.a == 0)
                    continue;
                count++;
                sumr += pixel.r * 255;
                sumg += pixel.g * 255;
                sumb += pixel.b * 255;
            }
        }
        return new Color((int) (sumr / count), (int) (sumg / count), (int) (sumb / count));
    }
}
