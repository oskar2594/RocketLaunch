package de.pogs.rl.loader;

import de.pogs.rl.RocketLauncher;

public class Loader {

    public Loader() {

    }

    public void load() {
        RocketLauncher.INSTANCE.setScreen(new LoaderScreen());
    }
}
