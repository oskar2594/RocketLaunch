package de.pogs.rl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.Game;

public class Menu implements Screen {
    final RocketLauncher game;

    public Menu(final RocketLauncher game) {
        this.game = game;
        
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		if (Gdx.input.isTouched()) {
			game.setScreen(new Game(game));
			dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
    
}
