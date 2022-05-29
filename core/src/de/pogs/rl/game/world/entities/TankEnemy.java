package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class TankEnemy extends SimpleEnemy {

    private float hp = 10;

    public TankEnemy(Vector2 position) {
        super(position, RocketLauncher.INSTANCE.assetHelper.getImage("monster2"));
        shootingCoeff = 10f;
        bulletSpeed = 5000;
        bulletDamage = 0;
        bulletColor = new java.awt.Color(30, 255, 30, 255);
    }

    public TankEnemy(float x, float y) {
        this(new Vector2(x, y));
    }

    @Override
    public void addDamage(float damage, AbstractEntity source) {
        hp -= damage;
        if (hp < 0) {
            this.alive = false;
            source.killOtherEvent(this);
            splashEffectSelf();
        }
    }
}
