/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
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
    private Texture texture;
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
        texture = textureRegion[0][0].getTexture();
        velocity = Vector2.zero;
        sprite = new Sprite(texture);
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
