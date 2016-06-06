package Input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import blockengine.Configuration;
import entities.Camera;

/**
 * MouseHandler class extends the abstract class.
 * abstract classes should never be instantiated so here
 * a concrete that can be instantiated
 */
public class MouseHandler extends GLFWCursorPosCallback {
	
	private Camera camera;
	private Configuration config;	

	public MouseHandler(Camera camera, Configuration config) {
		super();
		this.camera = camera;
		this.config = config;
	}



	@Override
	public void invoke(long window, double xpos, double ypos) {
		// TODO Auto-generated method stub
		// this basically just prints out the X and Y coordinates 
		// of our mouse whenever it is in our window
//		float deltaX = 0;
//		float deltaY = 0;
//		deltaX = Configuration.getHeight()/2 - (float) xpos + 420;
//		deltaY = Configuration.getWidth()/2 - (float) ypos;
//		System.out.println("X: " + xpos + " Y: " + ypos);
//		System.out.println("dX: " + deltaX + " dY: " + deltaY);
//		camera.move(0, 0, 0, deltaX, deltaY, 0);
	}	
}