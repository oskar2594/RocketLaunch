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
package de.pogs.rl.utils.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.utils.InteractionUtils;

public class Button {
    private Vector2 position;
    private BitmapFont font;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private int width;
    private int height;
    private Color background;
    private Color origBackground;
    private Color border;
    private Color textColor;
    private String content;
    private float alpha = 1f;
    private int borderWidth = 1;
    private Sound hoverSound;
    private Sound clickSound;

    private boolean hover = false;
    private boolean beforeHover = false;
    private boolean active = false;
    private boolean beforeActive = false;

    public Button(int x, int y, int width, int height, Color textColor, Color background,
            Color border, String content) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.background = background;
        this.origBackground = background;
        this.border = border;
        this.textColor = textColor;
        this.content = content.toUpperCase();
        hoverSound = RocketLauncher.getAssetHelper().getSound("hover");
        clickSound = RocketLauncher.getAssetHelper().getSound("active");
        updateFont();
    }

    public Button(int x, int y, int width, int height, Color textColor, Color background,
            Color border, String content, int borderWidth) {
        this(x, y, width, height, textColor, background, border, content);
        this.borderWidth = borderWidth;
    }

    private void updateFont() {
        font = RocketLauncher.getAssetHelper().getFont("superstar",
                (int) Math.ceil((this.height - borderWidth / 2) * 0.5));
        font.setColor(new Color(textColor.r, textColor.g, textColor.b, textColor.a * alpha));
    }

    public void update(float delta) {
        float mouseX = InteractionUtils.mouseXfromPlayer();
        float mouseY = InteractionUtils.mouseYfromPlayer() * -1;
        if (mouseX > position.x - width / 2 && mouseX < position.x + width / 2
                && mouseY > position.y - height / 2 && mouseY < position.y + height / 2) {
            this.hover = true;
            if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
                this.active = true;
                background = border;
            } else {
                this.active = false;
                background = origBackground;
            }
        } else {
            this.hover = false;
            this.active = false;
        }
        if (hover != beforeHover) {
            if (this.hover) {
                Gdx.graphics.setSystemCursor(SystemCursor.Hand);
                hoverSound.play(.5f);
            } else {
                Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
            }
            beforeHover = hover;
        }
        if(active != beforeActive) {
            if(active) {
                clickSound.play(.5f);
            }
            beforeActive = active;
        }
    }

    public void render(SpriteBatch batch) {
        glyphLayout.setText(font, content, Color.BLACK, width, Align.center, true);
        font.draw(batch, content, position.x - width / 2, position.y + glyphLayout.height / 2,
                width, Align.center, true);
    }

    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(
                new Color(background.r, background.g, background.b, background.a * alpha));
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(position.x - width / 2, position.y - height / 2, width, height);
        shapeRenderer.setColor(new Color(border.r, border.g, border.b, border.a * alpha));
        shapeRenderer.set(ShapeType.Filled);
        if (hover) {
            // TOP
            shapeRenderer.rectLine(position.x - width / 2,
                    position.y + height / 2 - borderWidth / 2, position.x + width / 2,
                    position.y + height / 2 - borderWidth / 2, borderWidth);
            // BOTTOM
            shapeRenderer.rectLine(position.x - width / 2,
                    position.y - height / 2 + borderWidth / 2, position.x + width / 2,
                    position.y - height / 2 + borderWidth / 2, borderWidth);
            // LEFT
            shapeRenderer.rectLine(position.x - width / 2 + borderWidth / 2,
                    position.y + height / 2, position.x - width / 2 + borderWidth / 2,
                    position.y - height / 2, borderWidth);
            // RIGHT
            shapeRenderer.rectLine(position.x + width / 2 - borderWidth / 2,
                    position.y + height / 2, position.x + width / 2 - borderWidth / 2,
                    position.y - height / 2, borderWidth);
        }
    }

    public void resize(int width, int height) {
        updateFont();
    }


    public void setPosition(int x, int y) {
        position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
        updateFont();
    }

    public int getHeight() {
        return this.height;
    }

    public void dispose() {
        font.dispose();
        Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isClicked() {
        return this.active;
    }

    public boolean isHovered() {
        return this.hover;
    }

    public float getAlpha() {
        return this.alpha;
    }

}
