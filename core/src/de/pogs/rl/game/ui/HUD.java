package de.pogs.rl.game.ui;

import java.util.HashMap;
import java.util.function.BiConsumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import de.pogs.rl.game.GameScreen;

public class HUD {

    public static HUD INSTANCE;

    public float width;
    public float height;
    public Vector2 position;

    private ShapeRenderer shapeRenderer;

    private HashMap<String, HUDComponent> components;

    private PlayerHealth playerHealth;
    private PlayerArmor playerArmor;

    public int border = 10;

    public HUD() {
        INSTANCE = this;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        components = new HashMap<String, HUDComponent>();
        position = new Vector2();
        position.set(0, 0);
        playerHealth = new PlayerHealth();
        components.put("playerHealth", playerHealth);
        playerArmor = new PlayerArmor();
        components.put("playerArmor", playerArmor);
    }

    public void update(float delta) {
        Vector2 camPos = new Vector2(GameScreen.INSTANCE.camera.position.x, GameScreen.INSTANCE.camera.position.y);
        position.set(camPos.x, camPos.y);
        components.forEach((name, component) -> {
            component.update(delta);
        });
    }

    public void render(SpriteBatch batch) {
        components.forEach((name, component) -> {
            component.render(batch);
        });
    }

    public void shapeRender(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeType.Filled);
        components.forEach((name, component) -> {
            component.shapeRender(shapeRenderer);
        });
        shapeRenderer.end();
    }

    public void resize(int width, int height) {
        this.width = width * GameScreen.INSTANCE.camera.zoom - border * 2;
        this.height = height * GameScreen.INSTANCE.camera.zoom - border * 2;
        this.border = (int) (this.height * 0.02);
        components.forEach((name, component) -> {
            component.resize(this.width, this.height);
        });
    }
}
