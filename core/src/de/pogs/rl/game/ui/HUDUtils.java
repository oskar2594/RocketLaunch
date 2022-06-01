package de.pogs.rl.game.ui;

import com.badlogic.gdx.math.Vector2;

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
