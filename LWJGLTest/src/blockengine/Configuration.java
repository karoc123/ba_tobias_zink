package blockengine;

import helper.Log;

public class Configuration {
	
	private static int Height;
	private static int Width;
	
	public static final boolean DEBUG = false;
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	public static final boolean FULLSCREEN = false;
	private static final int WORLD_SIZE = 10;
	public static final int CHUNK_SIZE = 16;
	public static boolean showWireframe = false;
	public static boolean runDemo = true;
	
	/**
	 * Hight of the window
	 * @return Default: 1080
	 */
	public static int getHeight() {
		return Height;
	}

	/**
	 * Set hight of the window
	 * @return Default: 1080
	 */
	public static void setHeight(int height) {
		Height = height;
	}

	/**
	 * Width of the window
	 * @return Default: 1920
	 */
	public static int getWidth() {
		return Width;
	}

	/**
	 * Set width of the window
	 * Default: 1920
	 */	
	public static void setWidth(int width) {
		Width = width;
	}

	/**
	 * Loads default configuration
	 * Loglevel: 6
	 * Height: 1080
	 * Width: 1920
	 */
	public void loadConfiguration(){
		Log.set(6);
		Configuration.Height = 1080;
		Configuration.Width = 1920;
		if(DEBUG) Log.set(2);
	}

	/**
	 * Get size of the world
	 * @return Default: 16
	 */
	public static int getWorldSize() {
		return WORLD_SIZE;
	}

}
