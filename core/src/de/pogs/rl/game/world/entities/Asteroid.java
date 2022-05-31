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

    public Asteroid(Vector2 position, int level, Vector2 velocity) {
        this.position = position;
        this.level = level;
        this.velocity = velocity;
        sprite.setScale(baseSize * (float) Math.pow(1.5, this.level) / texture.getWidth(),
                baseSize * (float) Math.pow(1.5, this.level) / texture.getHeight());
        sprite.setPosition(position.getX(), position.getY());
    }

    @Override
    public void update(float delta) {
        position = position.add(velocity.mul(delta));
        sprite.setPosition(position.getX(), position.getY());
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
