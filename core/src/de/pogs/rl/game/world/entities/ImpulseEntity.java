package de.pogs.rl.game.world.entities;

import de.pogs.rl.utils.SpecialMath.Vector2;

public interface ImpulseEntity {
    public Vector2 getVelocity();
    public void setVelocity(Vector2 velocity);
    public float getMass();
    public Vector2 getPosition();
}
