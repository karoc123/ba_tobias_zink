package de.oth.blocklib;

import java.util.Random;

import de.oth.blocklib.input.Keyboard;

/**
 * Example how to inherit from the game class and get control of
 * the update loop.
 */
public class ExampleGame extends Game {
	
	public ExampleGame() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExampleGame(Configuration config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	/** This method is used to update the game logic.
	 * 
	 * @param delta
	 *            time since last call
	 */
	public final void update(final float delta) {
		if (getWorld().isMeshLatest() == false) {
			getWorldMesh().recreateMesh();
		}
		
	}
}
