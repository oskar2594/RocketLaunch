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
package de.pogs.rl.game.ui;

import com.badlogic.gdx.math.Vector2;

/**
 * HUD Hilfen zum berechnen von Positionen
 */
public class HUDUtils {
    public static Vector2 getBottomCenter() {
        return new Vector2(HUD.getPosition().x, HUD.getPosition().y - HUD.getHeight() / 2);
    }

    public static Vector2 getTopCenter() {
        return new Vector2(HUD.getPosition().x, HUD.getPosition().y + HUD.getHeight() / 2);
    }

    public static Vector2 getLeftCenter() {
        return new Vector2(HUD.getPosition().x - HUD.getWidth() / 2, HUD.getPosition().y);
    }

    public static Vector2 getRightCenter() {
        return new Vector2(HUD.getPosition().x + HUD.getWidth() / 2, HUD.getPosition().y);
    }

    public static Vector2 getBottomRight() {
        return new Vector2(HUD.getPosition().x + HUD.getHeight() / 2, HUD.getPosition().y - HUD.getWidth() / 2);
    }

    public static Vector2 getTopRight() {
        return new Vector2(HUD.getPosition().x + HUD.getHeight() / 2, HUD.getPosition().y + HUD.getWidth() / 2);
    }

    public static Vector2 getTopLeft() {
        return new Vector2(HUD.getPosition().x - HUD.getWidth() / 2, HUD.getPosition().y + HUD.getHeight() / 2);
    }

    public static Vector2 getBottomLeft() {
        return new Vector2(HUD.getPosition().x - HUD.getWidth() / 2, HUD.getPosition().y - HUD.getHeight() / 2);
    }

    public static Vector2 getCenter() {
        return new Vector2(HUD.getPosition().x, HUD.getPosition().y );
    }
}
