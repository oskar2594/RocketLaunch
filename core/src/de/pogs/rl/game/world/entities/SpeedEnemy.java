package de.pogs.rl.game.world.entities;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Schnellerer Gegner
 */
public class SpeedEnemy extends SimpleEnemy {
    public SpeedEnemy(Vector2 position) {
        super(position, RocketLauncher.getAssetHelper().getImage("enemy4"));
        sightRange = (float) Math.pow(1000, 2);
        playerAttraction = 300;
        playerRepulsion = 600;
        mass = 50;
    }
    public SpeedEnemy(float x, float y) {
        this(new Vector2(x, y));
    }
}
