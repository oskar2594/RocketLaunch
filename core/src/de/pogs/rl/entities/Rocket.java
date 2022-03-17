package de.pogs.rl.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Rocket {
    Texture texture;
    private float pos_x;
    private float pos_y;
    private float speed_x = 0;
    private float speed_y = 0;

    public void draw(SpriteBatch batch) {
        batch.draw(texture, pos_x, pos_y, 50, 50);
    }

    public Rocket(Texture texture, int x, int y) {
        this.texture = texture;
        this.pos_x = x;
        this.pos_y = y;
    }

    public void step(float delta) {
        pos_x = pos_x + speed_x * delta;
        pos_y = pos_y + speed_y * delta;
    }

    public void input(int input_x, int input_y) {
        speed_x += (input_x - pos_x) * 0.01;
        speed_y += 1 / (input_y - pos_y) * 0.01;
    }
}
