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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.PlayerStats;
/**
 * Level Anzeige
 */
public class Level extends HUDComponent {

    private float width;

    private BitmapFont levelFont;
    private BitmapFont smallFont;

    private GlyphLayout lLayout;
    private GlyphLayout sLayout;

    private String levelString = "";
    private String expString = "";

    Level() {
        super();
    }

    @Override
    public void resize(float width, float height) {
        this.width = (float) (width * 0.1);
        updateFonts();
    }

    public void updateFonts() {
        lLayout = new GlyphLayout();
        sLayout = new GlyphLayout();

        levelFont = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) Math.ceil(this.width * 0.75));

        smallFont = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) Math.ceil(this.width * 0.2));
    }


    @Override
    public void update(float delta) {
        position.set(HUDUtils.getBottomLeft().x, HUDUtils.getBottomLeft().y);
        float level = PlayerStats.getLevel();
        levelString = Integer.toString((int) Math.floor(level));
        expString = PlayerStats.getAccessExp() + "/" + PlayerStats.getNeedExp() + " EXP";

    }

    @Override
    public void render(SpriteBatch batch) {
        lLayout.setText(levelFont, levelString, Color.BLACK, width, Align.center, false);
        sLayout.setText(smallFont, "LVL", Color.BLACK, width, Align.center, false);
        levelFont.draw(batch, levelString, position.x,
                position.y + lLayout.height + sLayout.height * 1.5f, width, Align.left, false);
        smallFont.draw(batch, "LVL", position.x + lLayout.width * 1.1f,
                position.y + sLayout.height * 2.5f, width, Align.left, false);
        sLayout.setText(smallFont, expString, Color.BLACK, width, Align.center, false);
        smallFont.draw(batch, expString, position.x, position.y + sLayout.height, width, Align.left,
                false);
    }


    @Override
    public void dispose() {
        levelFont.dispose();
        smallFont.dispose();

    }

    @Override
    public void shapeRender(ShapeRenderer shapeRenderer) {
        //Unbenutzt
    }

}
