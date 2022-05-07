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
    private float sightRange = (float) Math.pow(200, 2);
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("monster1");
    private Sprite sprite;
    private float speed = 50;

    private float scale = 0.1f;

    private Vector2 moveDirection = new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).nor();


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
        if (position.dst2(GameScreen.INSTANCE.player.getPosition()) < sightRange) {
            moveDirection = GameScreen.INSTANCE.player.getPosition().sub(position).nor();
        }
        position = position.add(moveDirection.mul(speed * delta));

        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this)) {
            if (!(entity instanceof Enemy)) {
                entity.addDamage(5 * delta);
            }
        }
    }

    @Override
    public void addDamage(float damage) {
        this.alive = false;
    }
}
