package de.pogs.rl.utils;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class CameraShake {

    public static boolean isActive = false;
    private static float strength = 0;
    private static float tempStrength = 0;
    private static int tempDuration = 0;
    private static Random random = new Random();

    public static void activate(float s) {
        strength = s;
        isActive = true;
    }

    public static void deactivate() {
        isActive = false;
    }

    public static void makeShake(float strength, int duration) {
        tempStrength += strength;
        tempDuration += duration;
    }

    public static Vector2 getShake() {
        Vector2 move = new Vector2(0, 0);
        tempDuration -= 1;
        if (tempDuration < 0) {
            tempStrength = 0;
            tempDuration = 0;
        } else {
            tempDuration -= 1;
        }
        move.x = (random.nextFloat() - 0.5f) * 2 * (strength + tempStrength)
                * (Gdx.graphics.getWidth() / 1000);
        move.y = (random.nextFloat() - 0.5f) * 2 * (strength + tempStrength)
                * (Gdx.graphics.getWidth() / 1000);
        return move;
    }
}
