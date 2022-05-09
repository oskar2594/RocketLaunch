package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Bullet extends AbstractEntity {
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("bullet");
    private Sprite sprite;

    private float scale = 0.1f;
    private float speed;
    private float angle;
    
    
    private AbstractEntity sender;

    private float damage;

    private Vector2 velocity;

    public Bullet(Vector2 position, AbstractEntity sender, float damage, Vector2 velocity) {
        sprite = new Sprite(texture);
        this.angle = 180 - SpecialMath.VectorToAngle(velocity);
        sprite.setSize(texture.getWidth() / texture.getHeight() * radius, radius);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.position = position;
        this.velocity = velocity;
        sprite.setRotation(this.angle + 90);
        this.sender = sender;
        this.damage = damage;
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2), position.getY() - sprite.getHeight() / 2);
        
        radius = 2;
    }

    @Override
    public void render(SpriteBatch batch) {
        
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        updatePosition(delta);
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2), position.getY() - sprite.getHeight() / 2);
        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this)) {
            if (entity != sender) {
                entity.addDamage(damage);
                this.alive = false;
                break;
            }
        }
    }

    private void updatePosition(float delta) {
        position = position.add(velocity.mul(delta));
    }

    @Override
    public void addDamage(float damage) {
        this.alive = false;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
