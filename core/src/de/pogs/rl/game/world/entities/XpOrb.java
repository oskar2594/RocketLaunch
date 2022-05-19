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
        // sprite.setAlpha(0.7f + (float) Math.sin(TimeUtils.millis() / 100) * 0.3f);
        // sprite.setTexture(colorizeTexture(texture, Color.BLUE));
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

    // private Texture colorizeTexture(Texture t, Color color) {
    //     TextureData tData = t.getTextureData();
    //     if (!tData.isPrepared())
    //         tData.prepare();
    //     Pixmap sourcePixmap = tData.consumePixmap();
    //     Pixmap rPixmap = new Pixmap(t.getWidth(), t.getHeight(), Format.RGBA8888);
    //     float[] rColor = java.awt.Color.RGBtoHSB((int) (color.r * 255), (int) (color.g * 255),
    //             (int) (color.b * 255), null);
    //     // System.out.println(rColor[0]);
    //     int idx = 0;
    //     for (int x = 0; x < t.getWidth(); x++) {
    //         for (int y = 0; y < t.getHeight(); y++) {
    //             Color pixelColor = new Color(sourcePixmap.getPixel(x, y));
    //             float[] hsv = java.awt.Color.RGBtoHSB((int) (pixelColor.r * 255),
    //                     (int) (pixelColor.g * 255), (int) (pixelColor.b * 255), null);
    //             hsv[0] += rColor[0];
    //             hsv[1] = .5f;

    //             Color pCol = pixelColor.fromHsv(hsv);
    //             rPixmap.getPixels().put(idx++, (byte) (pCol.r * 255));
    //             rPixmap.getPixels().put(idx++, (byte) (pCol.g * 255));
    //             rPixmap.getPixels().put(idx++, (byte) (pCol.b * 255));
    //             rPixmap.getPixels().put(idx++, (byte) (pixelColor.a * 255));
    //         }

    //     }
    //     return new Texture(rPixmap);
    // }

    private Color mix(Color a, Color b, double percent) {
        percent = Math.max(Math.min(percent, 1), 0);
        return new Color((float) (a.r * 255 * percent + b.r * 255 * (1.0 - percent)),
                (float) (a.g * 255 * percent + b.g * 255 * (1.0 - percent)),
                (float) (a.b * 255 * percent + b.b * 255 * (1.0 - percent)), 255);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
