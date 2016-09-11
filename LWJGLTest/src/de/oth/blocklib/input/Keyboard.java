package de.oth.blocklib.input;

import static de.oth.blocklib.helper.Log.*;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import de.oth.blocklib.Configuration;
import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.helper.Log;
import de.oth.blocklib.helper.PerformanceLog;

/**
 * All Keyboard input handles get handled in this class. There are two
 * specifically methods, one for continuous key presses and one for trigger
 * presses.
 * 
 * @see KeyboardHandler
 * @see MouseHandler
 * @see MousePicker
 */
public final class Keyboard {

	/** private constructor. */
	private Keyboard() {
	}

	/**
	 * Callback to handle Keyboardinputs. Gets called once every update. For
	 * continuous key presses only
	 * 
	 * @param delta
	 *            since last update
	 * @param camera
	 *            current camera to move
	 * @param window
	 *            window to catch keyboard input
	 * @see GLFWKeyCallback
	 */
	public static void handleInput(final float delta, final Camera camera, final long window) {

		if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)) {
			debug("Space Key Pressed");
			camera.move(0, delta * 1.0f, 0, 0, 0, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
			debug("Left Shift Key Pressed");
			camera.move(0, -delta * 1.0f, 0, 0, 0, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
			debug("W Key Pressed");
			camera.walkForward(-delta * 1.0f);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
			debug("A Key Pressed");
			camera.strafeLeft(-delta * 1.0f);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
			debug("S Key Pressed");
			camera.walkBackwards(-delta * 1.0f);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
			debug("D Key Pressed");
			camera.strafeRight(-delta * 1.0f);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT)) {
			debug("LEFT Key Pressed");
			camera.move(0, 0, 0, 0, -delta * 40.0f, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT)) {
			debug("RIGHT Key Pressed");
			camera.move(0, 0, 0, 0, delta * 40.0f, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_UP)) {
			debug("UP Key Pressed");
			camera.move(0, 0, 0, -delta * 20.0f, 0, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_DOWN)) {
			debug("DOWN Key Pressed");
			camera.move(0, 0, 0, delta * 20.0f, 0, 0);
		}

		if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE)) {
			debug("ESCAPE Key Pressed");
			glfwSetWindowShouldClose(window, true);
		}

	}

	/**
	 * Callback to handle Keyboardinputs. Gets called every time a key is
	 * pressed. For <B>non</B> continuous key presses only.
	 * 
	 * @param key
	 *            a constant from GLFW_KEY
	 * @param action
	 *            a constant from GLFW (release or press)
	 * @see GLFWKeyCallback
	 */
	public static void keyPressed(final int key, final int action) {
		if (key == GLFW_KEY_F1 && action == GLFW_RELEASE) {
			debug("F1 Key Pressed");
			Configuration.showWireframe = !Configuration.showWireframe;
		}

		if (key == GLFW_KEY_F2 && action == GLFW_RELEASE) {
			Configuration.runDemo = !Configuration.runDemo;
			System.out.println("Demo: " + Configuration.runDemo);
		}

		if (key == GLFW_KEY_F3 && action == GLFW_RELEASE) {
			PerformanceLog.printMemoryUsage();
		}
	}

}
