package de.oth.blocklib;

import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.helper.Log;
import de.oth.blocklib.input.MouseHandler;
import de.oth.blocklib.renderer.WorldMeshRenderer;

import static de.oth.blocklib.helper.Log.*;

/**
 * An instance of Configuration allows the framework to specify properties.
 * @see Game
 */
public class Configuration {
	// Window variables
	/** Measures the height of the window in pixels. */
	private int windowHeight = 1080;
	/** Measures the width of the window in pixels. */
	private int windowWidth = 1920;
	
	/** Major OpenGL Version to use. */
	public static final int OPENGL_MAJOR_VERSION = 3;
	
	/** Minor OpenGL Version to use. */
	public static final int OPENGL_MINOR_VERSION = 2;
	
	/** Changes the behavior of the framework to output more 
	 * debug information. */
	public static final boolean DEBUG = false;
	
	// Camera variables
	/** Field of view, it the extent of the observable game world that is 
	 * seen on the display at any given moment. Measured as an angle. */
	public static final float FOV = 70;
	/** Configures the view frustum  near plane. */
	public static final float NEAR_PLANE = 0.10f;
	/** Configures the view frustum  far plane. */
	public static final float FAR_PLANE = 1000;
	
	/** Must be set before window creation, fullscreen can't be switched if 
	 * the game is running, support maybe in lwjgl 3.2.
	 * <a href="https://github.com/glfw/glfw/issues/43">Bug in glfw</a>
	 * @see <a href="https://github.com/glfw/glfw/issues/43">Bug in glfw</a> */
	public static final boolean FULLSCREEN = false;
	
	/** Size of the cubic world, blocks in the world = world_size^3. */
	private int worldSize = 150;
	
	/** NOT USED AT THE MOMENT. */
	public static final int CHUNK_SIZE = 16; //not used at the moment

	/** Should the mesh of the world be optimized */
	public static final boolean OPTIMIZE = true;
	
	/** Should textures be mip mapped */
	public static final boolean MIPMAPPING = false;
	
	// Runtime variables
	/** Used in the renderer to switch between rendering the cubes with texture
	 * or only the wireframe model.
	 * @see WorldMeshRenderer */
	public static boolean showWireframe = false;
	
	/** Used in the game class to switch between random delete / adding cubes
	 * every second.
	 * @see Game */
	public static boolean runDemo = false;
	
	// Mouse variables
	/** The speed of the mouse pointer and how fast it moves on the screen. 
	 * With increased sensitivity, the camera moves faster and requires less 
	 * effort to get across the screen. With a low mouse sensitivity, a camera 
	 * moves slower and requires more effort to get across the screen, but 
	 * offers better precision for users not familiar with a mouse.
	 * @see Camera
	 * @see MouseHandler */
	public static float mouseSensitivity = 0.05f;
	/** NOT USED AT THE MOMENT. */
	public static boolean mouseInvert = false;
	
	/** velocity of the player / camera movement */
	public static float velocity = 6.0f;
	
	/** Hight of the window.
	 *  Can only be set before window creation.
	 * @return Default is 1080
	 */
	public int getHeight() {
		return windowHeight;
	}

	/**
	 * Set hight of the window.
	 * Can only be set before window creation.
	 * @param height Default is 1080
	 */
	public void setHeight(int height) {
		windowHeight = height;
	}

	/**
	 * Width of the window.
	 * Can only be set before window creation.
	 * @return Default is 1920
	 */
	public int getWidth() {
		return windowWidth;
	}

	/**
	 * Set width of the window.
	 * Can only be set before window creation.
	 * @param width Default is 1920
	 */	
	public void setWidth(int width) {
		windowWidth = width;
	}

	/**
	 * Loads configuration. Sets the debug level
	 * in the minlog logger.
	 */
	public void loadConfiguration(){
		if (DEBUG) { 
			Log.set(LEVEL_DEBUG); 
		}
	}

	/** Get size of the world.
	 * Can only be set before world creation.
	 * @return Default: 16
	 */
	public int getWorldSize() {
		return worldSize;
	}
	
	/** Set the size of the world.
	 * Can only be set before world creation.
	 * @param worldSize the size of the world. Only before world creation.
	 */
	public void setWorldSize(final int worldSize) {
		this.worldSize = worldSize;
	}
}
