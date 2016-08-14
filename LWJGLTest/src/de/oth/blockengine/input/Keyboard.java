package de.oth.blockengine.input;

import static de.oth.blockengine.helper.Log.*;
import static org.lwjgl.glfw.GLFW.*;

import de.oth.blockengine.Configuration;
import de.oth.blockengine.entities.Camera;

/**
 * All Keyboard input handles
 */
public class Keyboard {

	/**
	 * Callback to handle Keyboardinputs
	 * @param delta since last update
	 * @param camera current camera to move
	 * @param window window to catch keyboard input
	 */
	public static void HandleInput(float delta, Camera camera, long window) {
		if(KeyboardHandler.isKeyDown(GLFW_KEY_F1))
    	{
    		debug("F1 Key Pressed");
    		Configuration.showWireframe = !Configuration.showWireframe;
    	}

		if(KeyboardHandler.isKeyDown(GLFW_KEY_F2))
    	{
    		Configuration.runDemo = !Configuration.runDemo;
    		System.out.println("Demo: " + Configuration.runDemo);
    	}
		
		if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE))
    	{
    		debug("Space Key Pressed");
    		camera.move(0, delta*1.0f, 0, 0, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT))
    	{
    		debug("Left Shift Key Pressed");
    		camera.move(0, -delta*1.0f, 0, 0, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_W))
    	{
    		debug("W Key Pressed");
    		camera.walkForward(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_A))
    	{
    		debug("A Key Pressed");
    		camera.strafeLeft(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_S))
    	{
    		debug("S Key Pressed");
    		camera.walkBackwards(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_D))
    	{
    		debug("D Key Pressed");
    		camera.strafeRight(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
    	{
    		debug("LEFT Key Pressed");
    		camera.move(0, 0, 0, 0, -delta*40.0f, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
    	{
    		debug("RIGHT Key Pressed");
    		camera.move(0, 0, 0, 0, delta*40.0f, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_UP))
    	{
    		debug("UP Key Pressed");
    		camera.move(0, 0, 0, -delta*20.0f, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
    	{
    		debug("DOWN Key Pressed");
    		camera.move(0, 0, 0, delta*20.0f, 0, 0);
    	}

    	if(KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE))
    	{
    		debug("ESCAPE Key Pressed");
    		glfwSetWindowShouldClose(window, true);
    	}
    	
    	
	}

}
