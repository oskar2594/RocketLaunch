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
package de.pogs.rl.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;

public class Debug extends HUDComponent {
    private BitmapFont font = RocketLauncher.getAssetHelper().getFont("superstar", 16);
    private String text = "";

    Debug() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(float delta) {
        text = "X: " + Math.round(GameScreen.getCamera().position.x) + " | Y: "
                + Math.round(GameScreen.getCamera().position.y) + "\n";
        text += "FPS: " + Gdx.graphics.getFramesPerSecond();
    }

    @Override
    public void render(SpriteBatch batch) {
        position.set(HUDUtils.getTopLeft().x, HUDUtils.getTopLeft().y);
        font.setColor(Color.WHITE);
        font.draw(batch, text, position.x, position.y);

    }

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
