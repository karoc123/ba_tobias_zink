package blockengine;

import static helper.Log.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import Input.Keyboard;
import Input.KeyboardHandler;
import World.WorldData;
import entities.Camera;
import renderer.MasterRenderer;
import shaders.StaticShader;

public class Game {
	private long windowID;
	private Configuration config = new Configuration();
	private GLFWKeyCallback keyCallback;
	private MasterRenderer renderer;
	private StaticShader shader;
	private Camera camera;
	private WorldData world;
    int fps;
    long lastFPS;

	public Game() {
		System.out.println("LWJGL " + Version.getVersion() + "!");
		
		config.loadConfiguration();

		if (glfwInit() != GL_TRUE) {
			System.err.println("Error initializing GLFW");
			System.exit(1);
		}

		// Window Hints for OpenGL context
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		if(Configuration.FULLSCREEN){
			windowID = glfwCreateWindow(config.getWidth(), config.getHeight(), "LWJGL Block Engine", glfwGetPrimaryMonitor(), NULL);

		} else {
			windowID = glfwCreateWindow(config.getWidth(), config.getHeight(), "LWJGL Block Engine", NULL, NULL);
		}

		if (windowID == NULL) {
			System.err.println("Error creating a window");
			System.exit(1);
		}

		glfwMakeContextCurrent(windowID);
		GL.createCapabilities();

		glfwSwapInterval(1);
		glfwShowWindow(windowID);
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowID, keyCallback = new KeyboardHandler());
	}

	/**
	 * Initializes opengl, shaders, entities, camera and loads everything
	 * 
	 */
	public void init() {
		// Debuginformation
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		lastFPS = getTime();

		// Init Shader & Renderer
		glEnable(GL_DEPTH_TEST);
		
		renderer = new MasterRenderer(world);
		
		// Create World
		world = new WorldData(5);
		shader = new StaticShader(world.vID);
		camera = new Camera();
	}

	/**
	 * This method is used to update the game logic
	 * 
	 * @param delta
	 *            time since last call
	 */
	public void update(float delta) {
		Keyboard.HandleInput(delta, camera, windowID);
	}

	/**
	 * This method is used to initialize the renderer & shader
	 * 
	 * @param delta
	 *            time since last call
	 */
	public void render(float delta) {
		//shader.start();
		renderer.render(camera, world);
		//shader.stop();
	}
	
	/**
	 * Starts the engine
	 * 
	 */
	public void start() {
		float now, last, delta;
		last = 0;

		// Initialize the Game
		init();

		// Loop continuously and render and update
		while (glfwWindowShouldClose(windowID) != GL_TRUE) {
			// Get the time
			now = (float) glfwGetTime();
			delta = now - last;
			last = now;

			// Update and render
			update(delta);
			render(delta);
			performanceLog(delta);

			// Poll the events and swap the buffers
			glfwPollEvents();
			glfwSwapBuffers(windowID);
		}


		// Destroy the window
		shader.cleanUp();
		glfwDestroyWindow(windowID);
		glfwTerminate();

		System.exit(0);
	}

	/**
	 * Prints information about the performance of the engine
	 * @param delta time since last update call
	 */
	public void performanceLog(float delta){
		// Calculate Memory
		int mb = 1024*1024;
        if (getTime() - lastFPS > 1000) {
		    //Getting the runtime reference from system
		    Runtime runtime = Runtime.getRuntime();
		     
		    info("##### Heap utilization statistics [MB] #####");
		     
		    //Print used memory
		    info("Used Memory:"
		            + (runtime.totalMemory() - runtime.freeMemory()) / mb);
		 
		        //Print free memory
		    info("Free Memory:"
		        + runtime.freeMemory() / mb);
		     
		    //Print total available memory
		    info("Total Memory:" + runtime.totalMemory() / mb);
		 
		        //Print Maximum available memory
		    info("Max Memory:" + runtime.maxMemory() / mb);
        }
        
		// Calculate FPS
	    if (getTime() - lastFPS > 1000) {
	    	glfwSetWindowTitle(windowID, "FPS: " + fps);
	    	info("FPS: " + fps);
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}

	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return System.nanoTime() / 1000000;
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
}