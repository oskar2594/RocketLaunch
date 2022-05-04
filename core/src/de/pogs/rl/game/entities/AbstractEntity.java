package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.utils.SpecialMath.Vector2;;

public abstract class AbstractEntity {
    protected Vector2 position = new Vector2(0, 0);
    public void update(float delta) {};
    public abstract void render(SpriteBatch batch);
    public Vector2 getPosition() {
        return position;
    }
    protected int renderPriority = 0;
    public int getRenderPriority() {
        return renderPriority;
    }

    protected boolean alive = true;
    public abstract void addDamage(float damage);

    public boolean getAlive(){
        return alive;
    }
}
