package de.pogs.rl.game.world.particles;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleManager {

    private LinkedList<ParticleEmitter> emitters;

    public ParticleManager() {
        emitters = new LinkedList<ParticleEmitter>();
    }

    public ParticleEmitter createEmitter(ParticleEmitter emitter) {
        emitters.add(emitter);
        return emitter;
    }

    public void update(float delta) {
        emitters.removeIf(e -> e.getDead());
        emitters.stream().forEach(e -> e.update(delta));
    }

    public void render(SpriteBatch batch) {
        for (ParticleEmitter particleEmitter : emitters) {
            particleEmitter.render(batch);
        }
    }

    public void dispose() {}

}
