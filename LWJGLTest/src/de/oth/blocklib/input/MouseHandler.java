package de.oth.blocklib.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.system.jemalloc.MallocMessageCallback;

import de.oth.blocklib.Configuration;
import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.renderer.MasterRenderer;

/**
 * MouseHandler class extends the abstract class.
 * abstract classes should never be instantiated so here
 * a concrete that can be instantiated
 */
public class MouseHandler extends GLFWCursorPosCallback {
	
	private Camera camera;
	private MousePicker picker;
	private Configuration config;

	/**
	 * Creates new mousehandler
	 * @param camera movements get directed at this camera
	 * @param config to get height and width of the window
	 */
	public MouseHandler(Camera camera, MasterRenderer renderer, Configuration config) {
		super();
		this.config = config;
		this.camera = camera;
		this.picker = new MousePicker(camera, renderer.getProjectionMatrix(), config);
	}



	@Override
	public void invoke(long window, double xpos, double ypos) {
		// Update the raypicker
		picker.update((float)xpos, (float)ypos);
//		System.out.println(picker.getCurrentRay());
		
		float deltaX = 0;
		float deltaY = 0;
		float magicDelta = ((config.getWidth()-config.getHeight())/2);
		deltaX = config.getHeight()/2 - (float) ypos + magicDelta;
		deltaY = config.getWidth()/2 - (float) xpos;
		camera.move(0, 0, 0, -deltaX*Configuration.mouseSensitivity, -deltaY*Configuration.mouseSensitivity, 0);
	}	
}