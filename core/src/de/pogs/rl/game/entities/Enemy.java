package de.pogs.rl.game.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class Enemy extends AbstractEntity {
    private static Random random = new Random();
    private float sightRange = (float) Math.pow(500, 2);
    private float haloRange = (float) Math.pow(200, 2);
    
    private float respectDistance = (float) Math.pow(180, 2);
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("monster1");
    private Sprite sprite;
    private float speed = 100;

    private float scale = 0.1f;

    private Vector2 moveDirection = new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).nor();

    private Vector2 velocity = moveDirection.mul(speed);

    public Enemy(float posX, float posY) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position = new Vector2(posX, posY);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(position.x - (sprite.getWidth() / 2), position.y - sprite.getHeight() / 2);
        updateVelocity(delta);
        updatePos(delta);

        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this, 10)) {
            if (!(entity instanceof Enemy)) {
                entity.addDamage(5 * delta);
            }
        }
    }

    private void updateVelocity(float delta) {
        if ((position.dst2(GameScreen.INSTANCE.player.getPosition()) > haloRange)
                && (position.dst2(GameScreen.INSTANCE.player.getPosition()) < sightRange)) {
            moveDirection = GameScreen.INSTANCE.player.getPosition().sub(position).nor();
            velocity = moveDirection.mul(speed);
        } else if (position.dst2(GameScreen.INSTANCE.player.getPosition()) < respectDistance) {
            moveDirection = GameScreen.INSTANCE.player.getPosition().sub(position).nor().mul(-1);
            velocity = moveDirection.mul(speed);
        } else if ((position.dst2(GameScreen.INSTANCE.player.getPosition()) > respectDistance)
        && (position.dst2(GameScreen.INSTANCE.player.getPosition()) < sightRange)) {
            velocity = new Vector2(0, 0);
        }
    }

    private void updatePos(float delta) {
        position = position.add(velocity.mul(delta));
    }

    @Override
    public void addDamage(float damage) {
        this.alive = false;
    }

}
