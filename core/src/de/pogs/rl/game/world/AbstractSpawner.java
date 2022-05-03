package de.pogs.rl.game.world;

import java.util.ArrayList;
import java.util.Collection;

import de.pogs.rl.game.entities.AbstractEntity;

public abstract class AbstractSpawner {
    public abstract Collection<AbstractEntity> spawn(ArrayList<Integer> chunk);
}
