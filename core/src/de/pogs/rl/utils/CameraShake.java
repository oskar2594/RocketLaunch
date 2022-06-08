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
package de.pogs.rl.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Kamerawackeln
 */
public class CameraShake {

    private static boolean active = true;
    private static float strength = 0;
    private static float tempStrength = 0;
    private static int tempDuration = 0;

    public boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        CameraShake.active = active;
    }

    /**
     * Kamerawackeln mit Stärke aktivieren
     * 
     * @param s Stärke
     */
    public static void setStrength(float s) {
        strength = s;
        active = true;
    }

    /**
     * Temporäres Wackeln
     * 
     * @param strength Stärke
     * @param duration Länge
     */
    public static void makeShake(float strength, int duration) {
        tempStrength += strength;
        tempDuration += duration;
    }

    /**
     * Wackeln zum Ausführen berechnen
     * 
     * @return Vector der Verschiebung
     */
    public static Vector2 getShake() {
        Vector2 move = new Vector2(0, 0);
        tempDuration -= 1;
        if (tempDuration < 0) {
            tempStrength = 0;
            tempDuration = 0;
        } else {
            tempDuration -= 1;
        }
        move.x = ((float) Math.random() - 0.5f) * 2 * (strength + tempStrength)
                * (Gdx.graphics.getWidth() / 1000);
        move.y = ((float) Math.random() - 0.5f) * 2 * (strength + tempStrength)
                * (Gdx.graphics.getWidth() / 1000);
        return move;
    }
}
