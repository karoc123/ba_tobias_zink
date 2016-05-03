package blockengine;

import helper.Log;

public class Configuration {
	
	private static int Height;
	private static int Width;
	
	public static final boolean DEBUG = true;
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;
	public static final boolean FULLSCREEN = false;
	
	public static int getHeight() {
		return Height;
	}

	public static void setHeight(int height) {
		Height = height;
	}

	public static int getWidth() {
		return Width;
	}

	public static void setWidth(int width) {
		Width = width;
	}

	public void loadConfiguration(){
		Log.set(6);
		Configuration.Height = 1080;
		Configuration.Width = 1920;
		if(DEBUG) Log.set(2);
	}

}
