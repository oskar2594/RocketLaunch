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
        LinkedList<ParticleEmitter> remEmitters = new LinkedList<ParticleEmitter>();
        for (ParticleEmitter particleEmitter : emitters) {
            if(particleEmitter.isDead) {
                remEmitters.add(particleEmitter);
                continue;
            }
            particleEmitter.update(delta);
        }
        emitters.removeAll(remEmitters);
    }

    public void render(SpriteBatch batch) {
        for (ParticleEmitter particleEmitter : emitters) {
            particleEmitter.render(batch);
        }
    }

}
