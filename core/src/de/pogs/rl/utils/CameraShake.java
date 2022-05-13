package de.pogs.rl.utils;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class CameraShake {

    public static boolean isActive = false;
    private static float strength = 0;
    private static Random random = new Random();

    public static void activate(float s) {
        strength = s;
        isActive = true;
    }

    public static void deactivate() {
        isActive = false;
    }

    public static Vector2 getShake() {
        Vector2 move = new Vector2(0, 0);
        if (isActive) {
            move.x = (random.nextFloat() - 0.5f) * 2 * strength * (Gdx.graphics.getWidth() / 1000);
            move.y = (random.nextFloat() - 0.5f) * 2 * strength * (Gdx.graphics.getWidth() / 1000);
        }
        return move;
    }
}