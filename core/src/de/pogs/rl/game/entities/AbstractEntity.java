package de.pogs.rl.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.pogs.rl.utils.SpecialMath.Vector2;;

public abstract class AbstractEntity {
    protected Vector2 position = new Vector2(0, 0);
    public void update(float delta) {};
    public abstract void renderSpriteBatch(SpriteBatch batch);
    public abstract void renderOther();
    public Vector2 getPosition() {
        return position;
    }
    protected int renderPriority = 0;
    public int getRenderPriority() {
        return renderPriority;
    }

    public abstract void addDamage(float damage);

    protected float radius = 10;
    
    public float getRadius() {
        return radius;
    }

    protected boolean alive = true;
    public boolean getAlive(){
        return alive;
    }
}
