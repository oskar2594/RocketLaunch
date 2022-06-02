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
package de.pogs.rl.game.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Align;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.screens.Menu.Menu;
import de.pogs.rl.utils.menu.Button;


public class PauseOverlay extends Overlay{
    
    private BitmapFont font;
    private Button resumeButton;
    private Button menuButton;

    public PauseOverlay() {
        super();
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) (Gdx.graphics.getWidth() * 0.2));
        resumeButton = new Button(0, 0, (int) (Gdx.graphics.getWidth() * 0.5),
                (int) ((Gdx.graphics.getWidth() * 0.5) / 10), new Color(0x2beafcff),
                new Color(0x0183e5ff), new Color(0x06bbf4ff), "Fortsetzen", 5);
        menuButton = new Button(0, 0, (int) (Gdx.graphics.getWidth() * 0.5),
                (int) ((Gdx.graphics.getWidth() * 0.5) / 10), new Color(0x2beafcff),
                new Color(0x0183e5ff), new Color(0x06bbf4ff), "Zurück zum Hauptmenü", 5);
    }

    @Override
    public void clear() {
        Gdx.graphics.setSystemCursor(RocketLauncher.defaultCursor);
    }

    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch, "Pausiert", -Gdx.graphics.getWidth() / 2, font.getCapHeight(),
                Gdx.graphics.getWidth(), Align.center, false);
        resumeButton.render(batch);
        menuButton.render(batch);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {

        // DARK BACKGROUND
        shapeRenderer.setColor(new Color(0x000000b0));
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        resumeButton.shapeRender(shapeRenderer);
        menuButton.shapeRender(shapeRenderer);
    }

    public void updateButtons(float delta) {
        resumeButton.setWidth((int) (Gdx.graphics.getWidth() * 0.5));
        resumeButton.setHeight((int) ((Gdx.graphics.getWidth() * 0.5) / 10));
        resumeButton.setPosition(0, (int) (-resumeButton.getHeight()));

        menuButton.setWidth((int) (Gdx.graphics.getWidth() * 0.5));
        menuButton.setHeight((int) ((Gdx.graphics.getWidth() * 0.5) / 10));
        menuButton.setPosition(0, (int) (-resumeButton.getHeight() - menuButton.getHeight()
                - Gdx.graphics.getHeight() * 0.02f));

        resumeButton.update(delta);
        menuButton.update(delta);
    }

    @Override
    public void update(float delta) {
        updateButtons(delta);
        if(resumeButton.isClicked()) {
            resume();
        }
        if(menuButton.isClicked()) {
            RocketLauncher.getInstance().setScreen(new Menu());
        }
    }

    public void resume() {
        GameScreen.setPaused(false);
        GameScreen.getOverlayHandler().setOverlay(null);
    }

    @Override
    public void resize(int width, int height) {
        resumeButton.resize(width, height);
        menuButton.resize(width, height);

        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) (width * 0.2f));
    }

}
