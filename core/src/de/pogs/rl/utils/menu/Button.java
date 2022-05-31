package de.pogs.rl.utils.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.GameScreen;
import de.pogs.rl.utils.InteractionUtils;

public class Button {

    private Vector2 position;
    private BitmapFont font;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private int width;
    private int height;
    private Color background;
    private Color border;
    private Color textColor;
    private String content;

    public boolean hover = false;
    public boolean active = false;

    public Button(int x, int y, int width, int height, Color textColor, Color background, Color border, String content) {
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.background = background;
        this.border = border;
        this.textColor = textColor;
        this.content = content;
        updateFont();
    }

    private void updateFont() {
        font = RocketLauncher.INSTANCE.assetHelper.getFont("roboto",
                (int) Math.ceil(this.height * 0.3));
        font.setColor(textColor);
    }

    public void update(float delta) {
        float mouseX = InteractionUtils.mouseXfromPlayer();
        float mouseY = InteractionUtils.mouseYfromPlayer() * -1;
        if (mouseX > position.x - width / 2 && mouseX < position.x + width / 2 && mouseY > position.y - height / 2 && mouseY < position.y + height / 2) {
            this.hover = true;
            if(Gdx.input.isTouched()) {
                this.active = true;
            } else {
                this.active = false;
            }
        } else {
            this.hover = false;
            this.active = false;
        }
    }

    public void render(SpriteBatch batch) {
        glyphLayout.setText(font, content, Color.BLACK, width, Align.center, true);
        font.draw(batch, content, position.x - width / 2, position.y + glyphLayout.height / 2,
                width, Align.center, true);
    }

    public void shapeRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(background);
        shapeRenderer.set(ShapeType.Filled);
        shapeRenderer.rect(position.x - width / 2, position.y - height / 2, width, height);
        shapeRenderer.setColor(border);
        shapeRenderer.set(ShapeType.Filled);
        if (hover) {
            int borderWidth = 3;
            shapeRenderer.rectLine(position.x - width / 2,
                    position.y - height / 2 - borderWidth / 2, position.x - width / 2,
                    position.y + height / 2 + borderWidth / 2, borderWidth);
            shapeRenderer.rectLine(position.x + width / 2, position.y + height / 2,
                    position.x - width / 2, position.y + height / 2, borderWidth);
            shapeRenderer.rectLine(position.x + width / 2,
                    position.y - height / 2 + borderWidth / 2 - borderWidth / 2,
                    position.x + width / 2, position.y + height / 2 + borderWidth / 2, borderWidth);
            shapeRenderer.rectLine(position.x - width / 2, position.y - height / 2,
                    position.x + width / 2 + borderWidth / 2, position.y - height / 2, borderWidth);

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

}
