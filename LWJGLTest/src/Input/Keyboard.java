package Input;

import static org.lwjgl.glfw.GLFW.*;
import static helper.Log.*;

import entities.Camera;

public class Keyboard {

	public static void HandleInput(float delta, Camera camera, long window) {
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
    		//camera.move(0, 0, -delta*1.0f, 0, 0, 0);
    		camera.walkForward(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_A))
    	{
    		debug("A Key Pressed");
    		//camera.move(-delta*1.0f, 0, 0, 0, 0, 0);
    		camera.strafeLeft(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_S))
    	{
    		debug("S Key Pressed");
    		//camera.move(0, 0, delta*1.0f, 0, 0, 0);
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
    		glfwSetWindowShouldClose(window, 1);
    	}
    	
    	
	}

}
