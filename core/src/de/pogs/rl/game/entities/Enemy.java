package de.pogs.rl.game.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.pogs.rl.RocketLauncher;
import java.awt.Color;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.game.world.particles.ParticleEmitter;
import de.pogs.rl.game.world.particles.ParticleUtils;
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

    private Vector2 moveDirection =
            new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).nor();

    private Vector2 velocity = moveDirection.mul(speed);

    private float repulsionRadius = 20;

    private float playerAttraction = 100;
    private float playerRepulsion = 200;

    private float tractionCoeff = 0.1f;

    private Vector2 constAcceleration = new Vector2(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).nor().mul(10);


    public Enemy(Vector2 position) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth() * scale, texture.getHeight() * scale);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.position = position;
    }

    public Enemy(float posX, float posY) {
        this(new Vector2(posX, posY));
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        sprite.setPosition(position.x - (sprite.getWidth() / 2),
                position.y - sprite.getHeight() / 2);
        updateVelocity(delta);
        updatePos(delta);

        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this)) {
            if (!(entity instanceof Enemy)) {
                entity.addDamage(5 * delta);
            }
        }
        for (AbstractEntity entity : GameScreen.INSTANCE.entityManager.getCollidingEntities(this,
                repulsionRadius)) {

            velocity = velocity.add(repulsion(delta, entity));
        }
    }

    private Vector2 repulsion(float delta, AbstractEntity entity) {
        return position.sub(entity.getPosition())
                .mul(20 * delta * speed / (float) Math.pow(position.dst(entity.getPosition()), 3));
    }

    private void splashEffectSelf() {
        ParticleEmitter pe = GameScreen.INSTANCE.particleManager
                .createEmitter(new ParticleEmitter((int) position.x, (int) position.y, 50, 5,
                        ParticleUtils.generateParticleTexture(ParticleUtils.averageColor(texture)),
                        -180, 180, 10, 150, 1, 5, 1f, 1f, .5f, .1f, true));
        pe.attach(this.sprite, 0, 0, this);
    }


    private void updateVelocity(float delta) {
        Vector2 playerPos = GameScreen.INSTANCE.player.getPosition();
        if ((position.dst2(playerPos) > haloRange) && (position.dst2(playerPos) < sightRange)) {
            moveDirection = playerPos.sub(position).nor();
            velocity = velocity.add(moveDirection.mul(playerAttraction * delta));
        } else if (position.dst2(playerPos) < respectDistance) {
            moveDirection = playerPos.sub(position).nor().mul(-1);
            velocity = velocity.add(moveDirection.mul(playerRepulsion * delta));
        }
        if (position.dst2(playerPos) < sightRange) {
            velocity = velocity.sub(velocity.mul(tractionCoeff * delta));
        }
        velocity = velocity.add(constAcceleration.mul(delta));
    }

    private void updatePos(float delta) {
        position = position.add(velocity.mul(delta));
    }

    @Override
    public void addDamage(float damage) {
        this.alive = false;
        splashEffectSelf();
    }

}
