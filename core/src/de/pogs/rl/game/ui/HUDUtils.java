package de.pogs.rl.game.ui;

import com.badlogic.gdx.math.Vector2;

public class HUDUtils {
    public static Vector2 getBottomCenter() {
        return new Vector2(HUD.INSTANCE.position.x, HUD.INSTANCE.position.y - HUD.INSTANCE.height / 2);
    }

    public static Vector2 getTopCenter() {
        return new Vector2(HUD.INSTANCE.position.x, HUD.INSTANCE.position.y + HUD.INSTANCE.height / 2);
    }

    public static Vector2 getLeftCenter() {
        return new Vector2(HUD.INSTANCE.position.x - HUD.INSTANCE.width / 2, HUD.INSTANCE.position.y);
    }

    public static Vector2 getRightCenter() {
        return new Vector2(HUD.INSTANCE.position.x + HUD.INSTANCE.width / 2, HUD.INSTANCE.position.y);
    }

    public static Vector2 getBottomRight() {
        return new Vector2(HUD.INSTANCE.position.x + HUD.INSTANCE.height / 2, HUD.INSTANCE.position.y - HUD.INSTANCE.width / 2);
    }

    public static Vector2 getTopRight() {
        return new Vector2(HUD.INSTANCE.position.x + HUD.INSTANCE.height / 2, HUD.INSTANCE.position.y + HUD.INSTANCE.width / 2);
    }

    public static Vector2 getTopLeft() {
        return new Vector2(HUD.INSTANCE.position.x - HUD.INSTANCE.width / 2, HUD.INSTANCE.position.y + HUD.INSTANCE.height / 2);
    }

    public static Vector2 getBottomLeft() {
        return new Vector2(HUD.INSTANCE.position.x - HUD.INSTANCE.width / 2, HUD.INSTANCE.position.y - HUD.INSTANCE.height / 2);
    }

    public static Vector2 getCenter() {
        return new Vector2(HUD.INSTANCE.position.x, HUD.INSTANCE.position.y );
    }
}
