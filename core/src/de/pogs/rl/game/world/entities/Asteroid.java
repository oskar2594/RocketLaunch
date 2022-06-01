package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Asteroid extends AbstractEntity {
    private int level;
    private Vector2 velocity;
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("asteroid");
    private Sprite sprite = new Sprite(texture);
    private static final int baseSize = 5;

    private float force;
    private static final float baseForce = 100;
    private static final float dps = 3;
    public Asteroid(Vector2 position, int level, Vector2 velocity) {
        this.position = position;
        this.level = level;
        this.velocity = velocity;
        force = baseForce * (float) Math.pow(1.5, this.level);
        radius = baseSize * (float) Math.pow(1.5, this.level) / 2;
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setSize(baseSize * (float) Math.pow(1.5, this.level),
                baseSize * (float) Math.pow(1.5, this.level));
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
    }

    @Override
    public void update(float delta) {
        velocity = velocity.add(forceAdded);
        forceAdded = Vector2.zero;
        position = position.add(velocity.mul(delta));
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        
        for (AbstractEntity entity : EntityManager.get().getCollidingEntities(this)) {
            float dst2 = entity.getPosition().dst(position);
            entity.addForce(entity.getPosition().sub(position).nor().mul(force * delta / (dst2 / radius)));
            addForce(position.sub(entity.getPosition()).nor().mul(force * 0.1f * delta / (dst2 / radius)));
            entity.addDamage(delta * dps, this);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

}
