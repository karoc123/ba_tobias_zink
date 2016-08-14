package de.oth.blockengine.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import de.oth.blockengine.Configuration;
import de.oth.blockengine.entities.Camera;
import de.oth.blockengine.renderer.MasterRenderer;

/**
 * MouseHandler class extends the abstract class.
 * abstract classes should never be instantiated so here
 * a concrete that can be instantiated
 */
public class MouseHandler extends GLFWCursorPosCallback {
	
	private Camera camera;
	private Configuration config;
	private MousePicker picker;

	/**
	 * Creates new mousehandler
	 * @param camera movements get directed at this camera
	 * @param config to get height and width of the window
	 */
	public MouseHandler(Camera camera, Configuration config, MasterRenderer renderer) {
		super();
		this.camera = camera;
		this.config = config;
		this.picker = new MousePicker(camera, renderer.getProjectionMatrix());
	}



	@Override
	public void invoke(long window, double xpos, double ypos) {
		// Update the raypicker
		picker.update((float)xpos, (float)ypos);
//		System.out.println(picker.getCurrentRay());
		
		// this basically just prints out the X and Y coordinates 
		// of our mouse whenever it is in the window
		float deltaX = 0;
		float deltaY = 0;
		deltaX = Configuration.getHeight()/2 - (float) ypos + 420;
		deltaY = Configuration.getWidth()/2 - (float) xpos;
		camera.move(0, 0, 0, -deltaX*Configuration.mouseSensitivity, -deltaY*Configuration.mouseSensitivity, 0);
	}	
}