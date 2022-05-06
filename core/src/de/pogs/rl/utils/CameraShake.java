package de.pogs.rl.utils;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Interpolation.Pow;

public class CameraShake {

    public static boolean isActive = false;
    private static float currentTime = 0;
    private static float strength = 0;
    private static Random random = new Random();

    public static void activate(float s) {
        strength = s;
        currentTime = 0;
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


// private static float time = 0;
// private static float currentTime = 0;
// private static float power = 0;
// private static float currentPower = 0;
// private static Random random;
// private static Vector3 pos = new Vector3();

// public static void rumble(float rumblePower, float rumbleLength) {
// random = new Random();
// power = rumblePower;
// time = rumbleLength;
// currentTime = 0;
// }

// public static Vector3 tick(float delta) {
// if (currentTime <= time) {
// currentPower = power * ((time - currentTime) / time);

// pos.x = (random.nextFloat() - 0.5f) * 2 * currentPower;
// pos.y = (random.nextFloat() - 0.5f) * 2 * currentPower;

// currentTime += delta;
// } else {
// time = 0;
// }
// return pos;
// }

// public static float getRumbleTimeLeft() {
// return time;
// }

// public static Vector3 getPos() {
// return pos;
// }
