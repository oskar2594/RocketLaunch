package de.pogs.rl.utils;

import com.badlogic.gdx.math.Vector2;

public class SpecialMath {
    public static float modulus(float x, float n) {
        float r = x % n;
        if (r < 0) {
            r += n;
        }
        return r;
    }
    public static float angleDifferenceSmaller(float angle1, float angle2, float angleBase) {
        angle1 = angle1 + angleBase / 2f;
        angle2 = angle2 + angleBase / 2f;
        float diff1 = angle1 - angle2;
        float diff2 = 0;
        if (diff1 < 0) {
            diff2 = angleBase + diff1;
        } else {
            diff2 = diff1 - angleBase;
        }
        diff1 %= angleBase;
        diff2 %= angleBase;
        if (Math.abs(diff1) > Math.abs(diff2)) {
            return diff2;
        }
        return diff1;
    }
    public static Vector2 angleToVector(float angle) {
        return new Vector2(((float) Math.cos((angle + 90) * Math.PI / 180)),
        ((float) Math.sin((angle + 90) * (Math.PI / 180))));
    }
}
