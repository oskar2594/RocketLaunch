package de.pogs.rl.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.SpecialMath;

public class Bullet extends AbstractEntity {
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("bullet");
    private Sprite sprite;

    private float scale = 0.2f;
    private float speed;
    private float angle;
    
    private float radius = 10;
    private AbstractEntity sender;

    public Bullet(float posX, float posY, float angle, float speed, AbstractEntity sender) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        position.set(posX, posY);
        this.angle = angle;
        this.speed = speed;
        sprite.setRotation(this.angle);
        this.sender = sender;
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
                entity.setAlive(false);
            }
        }
    }

    private void updatePosition(float delta) {
        position = position.add(SpecialMath.angleToVector(angle).scl(delta * speed));
    }

}
