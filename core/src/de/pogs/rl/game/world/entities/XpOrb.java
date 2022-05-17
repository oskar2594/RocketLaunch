package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.PlayerStats;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class XpOrb extends AbstractEntity {
    
    private Texture texture = RocketLauncher.INSTANCE.assetHelper.getImage("xporb");
    private Sprite sprite;
    private int xpPoints;
    private Vector2 velocity;
    private float attractionRange = 500;
    private float attractionForce = 1000;
    private float maxVelocity = 100;

    public XpOrb(Vector2 position, int xpPoints) {
        this.position = position;
        this.xpPoints = xpPoints;
        velocity = new Vector2(0, 0);
        sprite = new Sprite(texture);
        sprite.setSize(10, 10);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.position = position;
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
    }

    @Override
    public void update(float delta) {
        sprite.setAlpha(0.7f + (float) Math.sin(TimeUtils.millis() / 100) * 0.3f);
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        position = position.add(velocity.mul(delta));
        if (Player.get().getPosition().dst(position) < attractionRange) {
            velocity = velocity.add(
                    position.sub(Player.get().getPosition()).nor().mul(-attractionForce * delta));
        }
        if (velocity.magn() > maxVelocity) {
            velocity = velocity.mul(maxVelocity / velocity.magn());
        }
        if (Player.get().getPosition().dst(position) < Player.get().getRadius()) {
            PlayerStats.addExp(xpPoints);
            alive = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
