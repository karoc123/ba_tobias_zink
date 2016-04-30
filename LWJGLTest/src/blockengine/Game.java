package blockengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector3f;

import Input.KeyboardHandler;
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
	private GLFWKeyCallback keyCallback;
	private Loader loader;
	private MasterRenderer renderer;
	private RawModel model;
	private Entity entity;
	private ArrayList<Entity> entitys = new ArrayList<Entity>();
	private Camera camera;
	private Light light;

	public Game() {
		System.out.println("LWJGL " + Version.getVersion() + "!");
		loader = new Loader();
		

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
		windowID = glfwCreateWindow(1200, 920, "LWJGL Block Engine", NULL, NULL);

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

		// Init Shader & Renderer
		glEnable(GL_DEPTH_TEST);
		renderer = new MasterRenderer();
		
		// Load 3d Models
		//ModelLoader modelLoader = new ModelLoader(loader);
		//model = modelLoader.loadCube();
//		ModelData modelData = OBJLoader.loadOBJ("cube");
//		model = OBJLoader.loadOBJ("cube");
		model = OBJLoader.loadObjModel("cube", loader);
		
		// Load Textures and Create Models with them
		ModelTexture texture;
		texture = new ModelTexture(loader.loadTexture("grass"));
		TexturedModel texMod1 = new TexturedModel(model, texture);
		texture = new ModelTexture(loader.loadTexture("stone"));
		TexturedModel texMod2 = new TexturedModel(model, texture);
		
		entity = new Entity(texMod2, new Vector3f(0.5f,0,-1.5f),0,0,0,0.25f);

		light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1,1,1));
		
		for(int i = 0; i < 10; i++){
			for(int k = 0; k< 15; k++){
				Entity entity = new Entity(texMod1, new Vector3f(0.21f*i-0.9f,-0.6f,-1.8f-0.21f*k),0,0,0,0.2f);
				entitys.add(entity);				
			}
		}
		
		for(int i = 0; i < 1; i++){
			for(int k = 0; k< 15; k++){
				Entity entity = new Entity(texMod2, new Vector3f(0.4f*i-0.9f, -0.39f, -1.8f-0.21f*k),0,0,0,0.2f);
				entitys.add(entity);				
			}
		}
		
		
		camera = new Camera();
	}

	/**
	 * This method is used to update the game logic
	 * 
	 * TODO: keyboardHandler auslagern
	 * 
	 * @param delta
	 *            time since last call
	 */
	public void update(float delta) {
		
		// TOOD: Playermovement nicht Kameramovement
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_SPACE))
    	{
    		System.out.println("Space Key Pressed");
    		camera.move(0, delta*1.0f, 0, 0, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT))
    	{
    		System.out.println("Left Shift Key Pressed");
    		camera.move(0, -delta*1.0f, 0, 0, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_W))
    	{
    		System.out.println("W Key Pressed");
    		//camera.move(0, 0, -delta*1.0f, 0, 0, 0);
    		camera.walkForward(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_A))
    	{
    		System.out.println("A Key Pressed");
    		//camera.move(-delta*1.0f, 0, 0, 0, 0, 0);
    		camera.strafeLeft(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_S))
    	{
    		System.out.println("S Key Pressed");
    		//camera.move(0, 0, delta*1.0f, 0, 0, 0);
    		camera.walkBackwards(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_D))
    	{
    		System.out.println("D Key Pressed");
    		camera.strafeRight(-delta*1.0f);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_LEFT))
    	{
    		System.out.println("LEFT Key Pressed");
    		camera.move(0, 0, 0, 0, -delta*40.0f, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_RIGHT))
    	{
    		System.out.println("RIGHT Key Pressed");
    		camera.move(0, 0, 0, 0, delta*40.0f, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_UP))
    	{
    		System.out.println("UP Key Pressed");
    		camera.move(0, 0, 0, -delta*20.0f, 0, 0);
    	}
    	
    	if(KeyboardHandler.isKeyDown(GLFW_KEY_DOWN))
    	{
    		System.out.println("DOWN Key Pressed");
    		camera.move(0, 0, 0, delta*20.0f, 0, 0);;
    	}
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
		//light.setPosition(new Vector3f(light.getPosition().x+0.02f, 5, 5));
		
		// Add every entity in the renderer
		for (Entity entity : entitys) {
			renderer.processEntity(entity);
		}
		
		// render scene
		renderer.render(light, camera);
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

	public static void main(String[] args) {
		new Game().start();
	}
}