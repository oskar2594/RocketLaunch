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

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

/**
 * Head-up-Display Verwaltung
 */
public class HUD {
    private static float width;
    private static float height;

    private static Vector2 position;

    private ShapeRenderer shapeRenderer;

    private HashMap<String, HUDComponent> components;

    private PlayerHealth playerHealth;
    private PlayerArmor playerArmor;
    private Debug debug;
    private Level level;
    private Highscore highscore;

    private static int border = 10;

    public HUD() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        components = new HashMap<String, HUDComponent>();
        position = new Vector2();
        position.set(0, 0);
        playerHealth = new PlayerHealth();
        components.put("playerHealth", playerHealth);
        playerArmor = new PlayerArmor();
        components.put("playerArmor", playerArmor);
        debug = new Debug();
        components.put("debug", debug);
        level = new Level();
        components.put("level", level);
        highscore = new Highscore();
        components.put("highscore", highscore);
    }

    /**
     * HUD Elemente aktualisieren
     * @param delta Vergangene Zeit seit letztem Frame
     */
    public void update(float delta) {
        components.forEach((name, component) -> {
            component.update(delta);
        });
    }

    /**
     * HUD Elemente rendern
     * @param batch SpriteBatch zum Rendern
     */
    public void render(SpriteBatch batch) {
        components.forEach((name, component) -> {
            component.render(batch);
        });
    }

    /**
     * Formen der HUD Elemente rendern
     * @param matrix Matrix der HUDCamera
     */
    public void shapeRender(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeType.Filled);
        components.forEach((name, component) -> {
            component.shapeRender(shapeRenderer);
        });
        shapeRenderer.end();
    }

    /**
     * HUD Elemente an neue Bildschirmgröße anpasen
     * @param width
     * @param height
     */
    public void resize(int width, int height) {
        HUD.border = (int) (width * 0.03);
        HUD.width = width - border * 2;
        HUD.height = height - border * 2;
        components.forEach((name, component) -> {
            component.resize(HUD.width, HUD.height);
        });
    }

    public static float getWidth() {
        return width;
    }

    public static void setWidth(float w) {
        width = w;
    }

    public static float getHeight() {
        return height;
    }

    public static void setHeight(float h) {
        height = h;
    }

    public static Vector2 getPosition() {
        return position;
    }

    public static void setPosition(Vector2 p) {
        position = p;
    }

    public static int getBorder() {
        return border;
    }

}
