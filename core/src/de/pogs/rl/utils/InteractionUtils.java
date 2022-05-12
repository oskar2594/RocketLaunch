package de.pogs.rl.utils;

import com.badlogic.gdx.Gdx;

public class InteractionUtils {
    public static float mouseXfromPlayer() {
        return Gdx.input.getX() - (float) (Gdx.graphics.getWidth() / 2);
    }

    public static float mouseYfromPlayer() {
        return (float) (Gdx.input.getY() - (float) (Gdx.graphics.getHeight() / 2));
    }
}
