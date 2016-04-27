package blockengine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import models.TexturedModel;
import renderer.Loader;
import renderer.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class Game {
	private long windowID;
	private Loader loader;
	private Renderer renderer;
	private RawModel model;
	private TexturedModel texturedModel;
	private StaticShader shader;
	private Entity entity;
	private ArrayList<Entity> entitys = new ArrayList<Entity>();
	private Camera camera;

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
	}

	/**
	 * Initializes opengl, shaders, entities, camera and loads everything
	 * 
	 */
	public void init() {
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		shader = new StaticShader();
		renderer = new Renderer(shader);
		
		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,
				
				4,5,7,
				7,5,6,
				
				8,9,11,
				11,9,10,
				
				12,13,15,
				15,13,14,
				
				16,17,19,
				19,17,18,
				
				20,21,23,
				23,21,22

		};
		model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass"));
		texturedModel = new TexturedModel(model, texture);
		
		texture = new ModelTexture(loader.loadTexture("stone"));
		TexturedModel texMod2 = new TexturedModel(model, texture);
		
		entity = new Entity(texMod2, new Vector3f(0.5f,0,-1.5f),0,0,0,0.25f);
//		entity2 = new Entity(texturedModel, new Vector3f(0.5f,-0.5f,-2.0f),0,0,0,0.25f);
//		Entity entity3 = new Entity(texturedModel, new Vector3f(0.25f,-0.5f,-2.0f),0,0,0,0.25f);
//		Entity entity4 = new Entity(texturedModel, new Vector3f(0.0f,-0.5f,-2.0f),0,0,0,0.25f);
//		entitys.add(entity);
//		entitys.add(entity2);
//		entitys.add(entity3);
//		entitys.add(entity4);
		
		for(int i = 0; i < 10; i++){
			for(int k = 0; k< 15; k++){
				Entity entity = new Entity(texturedModel, new Vector3f(0.21f*i-0.9f,-0.6f,-1.8f-0.21f*k),0,0,0,0.2f);
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
	 * @param delta
	 *            time since last call
	 */
	public void update(float delta) {
	}

	/**
	 * This method is used to initialize the renderer & shader
	 * 
	 * @param delta
	 *            time since last call
	 */
	public void render(float delta) {
		entity.increaseRotation(0, 0, 0);
		
		camera.move(0, 0, 0, 0, 0, 0);

		renderer.prepare();
		shader.start();
		shader.loadViewMatrix(camera);

		for (Entity entity : entitys) {
			renderer.render(entity, shader);
		}

		// renderer.render(entity, shader);
		// renderer.render(entity2, shader);
		shader.stop();
	}

	/**
	 * Called after the game ends to cleanup
	 * 
	 */
	public void dispose() {
		loader.cleanUp();
		shader.cleanUp();
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