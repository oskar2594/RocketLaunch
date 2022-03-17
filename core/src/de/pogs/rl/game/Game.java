package de.pogs.rl.game;

import com.badlogic.gdx.Screen;

import de.pogs.rl.RocketLauncher;
import de.pogs.rl.game.RocketCamera;
import de.pogs.rl.game.entities.Player;

public class Game implements Screen {
    final RocketLauncher game;
    final RocketCamera cam = new RocketCamera();
    private Player player;

    public Game(final RocketLauncher game) {
        this.game = game;
        this.player = new Player();
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
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
