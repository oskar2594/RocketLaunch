package de.pogs.rl.game.world.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
    private float maxVelocity = 500;

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
        sprite.setTexture(colorizeTexture(texture, mix(Color.BLUE, Color.RED,
                0.5 + (float) Math.sin((((float) (TimeUtils.millis() % 20000)) / 500f)) * 0.5f)));
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

    private Texture colorizeTexture(Texture t, Color color) {
        TextureData tData = t.getTextureData();
        if (!tData.isPrepared())
            tData.prepare();
        Pixmap sourcePixmap = tData.consumePixmap();
        Pixmap rPixmap = new Pixmap(t.getWidth(), t.getHeight(), Format.RGBA8888);
        int idx = 0;
        for (int x = 0; x < t.getWidth(); x++) {
            for (int y = 0; y < t.getHeight(); y++) {
                Color pCol = new Color(sourcePixmap.getPixel(x, y));
                rPixmap.getPixels().put(idx++, (byte) (pCol.r * color.r * 255));
                rPixmap.getPixels().put(idx++, (byte) (pCol.g * color.g * 255));
                rPixmap.getPixels().put(idx++, (byte) (pCol.b * color.b * 255));
                rPixmap.getPixels().put(idx++, (byte) (pCol.a * color.a *255));
            }
        
        }
        return new Texture(rPixmap);
    }

    private Color mix(Color a, Color b, double ratio) {
        ratio = Math.max(Math.min(ratio, 1), 0);
        Color color = new Color((float) (a.r * ratio + b.r * (1.0 - ratio)),
                (float) (a.g * ratio + b.g * (1.0 - ratio)),
                (float) (a.b * ratio + b.b * (1.0 - ratio)), 1f);
        return color;
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
