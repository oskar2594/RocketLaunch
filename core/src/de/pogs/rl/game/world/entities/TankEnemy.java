package de.pogs.rl.game.world.entities;

import java.awt.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.game.world.particles.ParticleEmitter;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class TankEnemy extends SimpleEnemy {

    private float hp = 10;
    private Texture aimTexture = RocketLauncher.INSTANCE.assetHelper.getImage("aimbeam");
    Sprite aimSprite = new Sprite(aimTexture);

    public TankEnemy(Vector2 position) {
        super(position, RocketLauncher.INSTANCE.assetHelper.getImage("monster2"));
        shootingCoeff = 0.1f;
        bulletSpeed = 1000;
        bulletDamage = 25;
        bulletColor = new Color(30, 255, 30, 255);
        aimSprite.setPosition(position.getX(), position.getY());
        aimSprite.setOrigin(0, 0);
        aimSprite.setAlpha(0);

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

    @Override
    protected void shoot(float delta) {
        if (Player.get().getPosition().dst2(position) < sightRange) {
            float flightTime = Player.get().getPosition().dst(position) / bulletSpeed;
            Vector2 playerPosPredicted =
                    Player.get().getPosition().add(Player.get().getVelocity().mul(flightTime));
            Vector2 bulletDirection = playerPosPredicted.sub(position).nor();
            aimSprite.setRotation(90-SpecialMath.VectorToAngle(bulletDirection.mul(-1)));
            aimSprite.setAlpha(1);
            if (random.nextFloat()  < shootingCoeff* delta) {
                Bullet.createBullet(position, this, bulletDamage, bulletDirection.mul(bulletSpeed).add(velocity), bulletColor, 10000);
            }
        } else {
            aimSprite.setAlpha(0);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        aimSprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        aimSprite.setPosition(position.getX(), position.getY());
    }
}
