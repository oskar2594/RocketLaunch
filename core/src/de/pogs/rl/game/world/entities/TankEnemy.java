package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class TankEnemy extends SimpleEnemy {
    
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("monster2");
    public TankEnemy(Vector2 position) {
        
        super(position);
        sprite.setTexture(texture);
        shootingCoeff = 10f;
        bulletSpeed = 1000;
        bulletDamage = 10;
    }
    public TankEnemy(float x, float y) {
        this(new Vector2(x, y));
    }
}
