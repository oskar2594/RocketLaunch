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

    private void splashEffectSelf() {
        ParticleEmitter pe = GameScreen.INSTANCE.particleManager.createEmitter(
                new ParticleEmitter((int) position.x, (int) position.y, 50, 5,
                        generateParticle(averageColor(texture)),
                        -180, 180, 10, 150,
                        1, 5, 1f, 1f, .5f, .1f, true));
        pe.attach(this.sprite, 0, 0, this);
    }

    private Texture generateParticle(Color color) {
        int size = 50;
        int idx = 0;
        Pixmap pixmap = new Pixmap(size, size, Format.RGBA8888);
        for (int i = 0; i < Math.pow(size, 2); i++) {
            pixmap.getPixels().put(idx++, (byte) (color.getRed()));
            pixmap.getPixels().put(idx++, (byte) (color.getGreen()));
            pixmap.getPixels().put(idx++, (byte) (color.getBlue()));
            pixmap.getPixels().put(idx++, (byte) (color.getAlpha()));
        }
        return new Texture(pixmap);
    }

    public static Color averageColor(Texture t) {
        t.getTextureData().prepare();
        Pixmap bi = t.getTextureData().consumePixmap();
        int x0 = 0;
        int y0 = 0;
        int w = t.getWidth();
        int h = t.getHeight();
        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        int count = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                com.badlogic.gdx.graphics.Color pixel = new com.badlogic.gdx.graphics.Color(bi.getPixel(x, y));
                if (pixel.a == 0)
                    continue;
                count++;
                sumr += pixel.r * 255;
                sumg += pixel.g * 255;
                sumb += pixel.b * 255;
            }
        }
        return new Color((int) (sumr / count), (int) (sumg / count), (int) (sumb / count));
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
        splashEffectSelf();
    }

}
