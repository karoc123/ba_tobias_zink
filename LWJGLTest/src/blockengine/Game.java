package blockengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static helper.Log.*;

import java.util.ArrayList;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector3f;

import Input.Keyboard;
import Input.KeyboardHandler;
import World.WorldData;
import entities.Camera;
import entities.Entity;
import entities.Light;
import modelLoader.ModelData;
import modelLoader.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import renderer.Loader;
import renderer.MasterRenderer;
import textures.ModelTexture;

public class Game {
	private long windowID;
	private Configuration config = new Configuration();
	private GLFWKeyCallback keyCallback;
	private Loader loader;
	private MasterRenderer renderer;
	private RawModel model;
	private Entity entity;
	private ArrayList<Entity> entitys = new ArrayList<Entity>();
	private Camera camera;
	private Light light;
	private WorldData world;
    int fps;
    long lastFPS;

	public Game() {
		System.out.println("LWJGL " + Version.getVersion() + "!");
		loader = new Loader();
		
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
		
		// Load 3d Models
		ModelData modelData = OBJLoader.loadOBJ("cube");
		model = loader.loadToVAO(modelData.getVertices(), 
				modelData.getTextureCoords(), 
				modelData.getNormals(), 
				modelData.getIndices());
		
		// Load Textures and Create Models with them
		ModelTexture texture;
		texture = new ModelTexture(loader.loadTexture("grass"));
		TexturedModel texMod1 = new TexturedModel(model, texture);
		texture = new ModelTexture(loader.loadTexture("stone"));
		TexturedModel texMod2 = new TexturedModel(model, texture);
		
		entity = new Entity(texMod2, new Vector3f(0.5f,0,-1.5f),0,0,0,0.25f);

		light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1,1,1));
		
		// Create World
		world = new WorldData(35, loader);


		// TEMP
		texture = new ModelTexture(loader.loadTexture("grass"));
		entity = new Entity(new TexturedModel(world.createMesh(), texture), new Vector3f(0.5f,0,-1.5f),0,0,0,0.25f);
		entitys.add(entity);
		
//		for(int i = 0; i < 0; i++){
//			for(int k = 0; k< 0; k++){
//				for(int j = 0; j< 0; j++){
//					Entity entity = new Entity(texMod1, new Vector3f(0.20f*i-0.9f,-0.6f-0.2f*j,-1.8f-0.2f*k),0,0,0,0.2f);
//					entitys.add(entity);
//				}				
//			}
//		}
		
//		for(int i = 0; i < 1; i++){
//			for(int k = 0; k< 15; k++){
//				Entity entity = new Entity(texMod2, new Vector3f(0.4f*i-0.9f, -0.4f, -1.8f-0.20f*k),0,0,0,0.2f);
//				entitys.add(entity);				
//			}
//		}

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
		entity.increaseRotation(0, 0, 0);
		
		// DEBUGSTUFF
		//camera.move(0.002f, 0, -0.002f, 0.02f, -0.05f, 0);
		//light.setPosition(new Vector3f(light.getPosition().x-0.2f, light.getPosition().y-3.2f, 0));
		
		// Add every entity in the renderer
		for (Entity entity : entitys) {
			renderer.processEntity(entity);
		}
		
		// render scene
		renderer.render(light, camera, world);
	}

	/**
	 * Called after the game ends to cleanup
	 * 
	 */
	public void dispose() {
		loader.cleanUp();
		renderer.cleanUp();
		//shader.cleanUp();
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

		// Dispose the game
		dispose();

		// Destroy the window
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