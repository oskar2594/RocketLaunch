package de.pogs.rl.game.world.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.game.PlayerStats;
import de.pogs.rl.utils.SpecialMath.Vector2;

public class XpOrb extends AbstractEntity {

    private Texture originTexture = RocketLauncher.getAssetHelper().getImage("xporb");
    private TextureRegion[][] textureRegion;
    private float currentTexture = 0;
    private Texture animatedTexture;
    private Sprite sprite;
    private int xpPoints;
    private Vector2 velocity;
    private float attractionRange = 500;
    private float attractionForce = 2000;
    private float maxVelocity = 500;

    public XpOrb(Vector2 position, int xpPoints) {
        this.position = position;
        this.xpPoints = xpPoints;
        textureRegion = TextureRegion.split(originTexture, 25, 25);
        animatedTexture = textureRegion[0][0].getTexture();
        velocity = Vector2.zero;
        sprite = new Sprite(animatedTexture);
        sprite.setSize(10, 10);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.position = position;
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
    }

    @Override
    public void update(float delta) {
        currentTexture += .1;
        if (currentTexture > 4) {
            currentTexture = 0;
        }
        sprite.setRegion(textureRegion[(int) Math.floor(currentTexture)][0]);
        sprite.setPosition(position.getX() - (sprite.getWidth() / 2),
                position.getY() - sprite.getHeight() / 2);
        position = position.add(velocity.mul(delta));
        if (GameScreen.getPlayer().getPosition().dst(position) < attractionRange) {
            velocity = velocity.add(position.sub(GameScreen.getPlayer().getPosition()).nor()
                    .mul(-attractionForce * delta));
        }
        if (velocity.magn() > maxVelocity) {
            velocity = velocity.mul(maxVelocity / velocity.magn());
        }
        if (GameScreen.getPlayer().getPosition().dst(position) < GameScreen.getPlayer()
                .getRadius()) {
            PlayerStats.addExp(xpPoints);
            alive = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
