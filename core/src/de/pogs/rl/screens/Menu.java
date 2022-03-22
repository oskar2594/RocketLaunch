package de.pogs.rl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.Game;

public class Menu extends ScreenAdapter {

    // public Menu() {
    // }

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isTouched()) {
			RocketLauncher.INSTANCE.setScreen(new Game());
			dispose();
		}
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void dispose() {
		
	}
    
}
