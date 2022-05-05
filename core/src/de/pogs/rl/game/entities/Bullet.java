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
    
    private float radius = 20;
    private AbstractEntity sender;

    private float damage;

    private Vector2 velocity;

    public Bullet(float posX, float posY, AbstractEntity sender, float damage, Vector2 velocity, float angle) {
        sprite = new Sprite(texture);
        this.angle = angle;
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position = new Vector2(posX, posY);
        this.velocity = velocity;
        sprite.setRotation(this.angle);
        this.sender = sender;
        this.damage = damage;
    }
    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        updatePosition(delta);
        sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - sprite.getHeight() / 2);
        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this, radius)) {
            if (entity != sender) {
                System.out.println(entity);
                System.out.println(entity.getPosition());
                entity.addDamage(damage);
            }
        }
    }

    private void updatePosition(float delta) {
        position = position.add(velocity.mul(delta));
    }

    @Override
    public void addDamage(float damage) {

    }
}
