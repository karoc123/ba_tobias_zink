package de.oth.blocklib;

import static de.oth.blocklib.helper.Log.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector3f;

import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.entities.Entity;
import de.oth.blocklib.entities.Light;
import de.oth.blocklib.helper.Log.Logger;
import de.oth.blocklib.helper.PerformanceLog;
import de.oth.blocklib.helper.Utility;
import de.oth.blocklib.input.Keyboard;
import de.oth.blocklib.input.KeyboardHandler;
import de.oth.blocklib.input.MouseHandler;
import de.oth.blocklib.loader.ModelData;
import de.oth.blocklib.loader.OBJLoader;
import de.oth.blocklib.models.RawModel;
import de.oth.blocklib.models.TexturedModel;
import de.oth.blocklib.renderer.Loader;
import de.oth.blocklib.renderer.MasterRenderer;
import de.oth.blocklib.textures.ModelTexture;
import de.oth.blocklib.world.BlockType;
import de.oth.blocklib.world.WorldData;

/**
 * This class defines the game loop and holds instances of every class.
 * Start the window with start()
 */
public class Game {
	private long windowID;
	private GLFWKeyCallback keyCallback;
	private MouseHandler mouseCallback;
	private Loader loader;
	private MasterRenderer renderer;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Camera camera = new Camera();
	private Light light;
	private WorldData world;
	public Configuration config;
	private int fps;
    private long lastFPS;
	private Random rand = new Random();
	
    float temp = 0;
	
    /** Create and initialize a new game with default configuration.
     * @see Configuration
     * @see WorldData
     */
	public Game() {
		// Load default configuration
		config = new Configuration();
		config.loadConfiguration();
		
		loader = new Loader();
	}

	/** Creates the opengl context. After the init method <b>initWorld()</b> and
	 * <b>start()</b> needs to be called to create a output.
	 */
	public final void init() {
		// Check for opengl
		if (!glfwInit()) {
			System.err.println("Error initializing GLFW");
			System.exit(1);
		}

		// Window Hints for OpenGL context
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, Configuration.OPENGL_MAJOR_VERSION);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, Configuration.OPENGL_MINOR_VERSION);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		if (Configuration.FULLSCREEN) {
			windowID = glfwCreateWindow(config.getWidth(), config.getHeight(), 
					"LWJGL Block Engine", glfwGetPrimaryMonitor(), NULL);

		} else {
			windowID = glfwCreateWindow(config.getWidth(), config.getHeight(), 
					"LWJGL Block Engine", NULL, NULL);
		}

		if (windowID == NULL) {
			System.err.println("Error creating a window");
			System.exit(1);
		}

		glfwMakeContextCurrent(windowID);
		GL.createCapabilities();

		glfwSwapInterval(1);
		glfwShowWindow(windowID);
		
		// Debuginformation
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		lastFPS = Utility.getTimeInMilliseconds();
		System.out.println("LWJGL " + Version.getVersion() + "!");		
	}
	
	/** Initializes a world with the parameters from configuration
	 * @see Configuration
	 * @see WorldData
	 */
	public void initWorld() {
		// Init Renderer
		renderer = new MasterRenderer(config);
		
		// Load 3d model for single entity
		ModelData modelData = OBJLoader.loadOBJ("cube");
		RawModel model = loader.loadToVAO(modelData.getVertices(), 
				modelData.getTextureCoords(), 
				modelData.getNormals(), 
				modelData.getIndices());
		
		// Load textures and create models with them
		ModelTexture texture;
		texture = new ModelTexture(loader.loadTexture("spritesheet"));
		TexturedModel texMod = new TexturedModel(model, texture);
		
		Entity entity = new Entity(texMod, new Vector3f(4,4,-4),0,0,0,0.25f);
		entities.add(entity);
			
		light = new Light(new Vector3f(4,4,-4), new Vector3f(1,1,1));
		
		// Create World
		world = new WorldData(Configuration.getWorldSize(), loader, texture);

		
//		for(int i = 0; i < 0; i++){
//			for(int k = 0; k< 0; k++){
//				for(int j = 0; j< 0; j++){
//					Entity entity = new Entity(texture, new Vector3f(0.20f*i-0.9f,-0.6f-0.2f*j,-1.8f-0.2f*k),0,0,0,0.2f);
//					entitys.add(entity);
//				}				
//			}
//		}
	}

	/** This method is used to update the game logic.
	 * 
	 * @param delta
	 *            time since last call
	 */
	public final void update(final float delta) {
		if (world.isMeshLatest() == false) {
			world.recreateMesh();
		}
		
		//process input: keyboard
		Keyboard.handleInput(delta, camera, windowID);
		
		//Temp
		if (Configuration.runDemo) {
			temp += delta;
			if (temp > 2) {
				if (rand.nextBoolean()){
					world.setBlock(
							rand.nextInt(world.worldSize),
							rand.nextInt(world.worldSize), 
							rand.nextInt(world.worldSize), 
							BlockType.Nothing
							);					
				} else {
					world.setBlock(
							rand.nextInt(world.worldSize),
							rand.nextInt(world.worldSize),
							rand.nextInt(world.worldSize),
							BlockType.Grass
							);					
				}
				world.recreateMesh();	
				temp = 0;
			}			
		}
	}
	
	/** This method is used to initialize the renderer & shader.
	 * 
	 * @param delta
	 *            time since last call
	 */
	private void render(float delta) {
		// Add every entity in the renderer
		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}
		
		// render scene
		renderer.render(light, camera, world);
	}

	/** Called after the game ends to cleanup.
	 * 
	 */
	private void dispose() {
		loader.cleanUp();
		renderer.cleanUp();
		world.cleanUp();
	}

	/** Starts the engine.
	 * 
	 */
	public void start() {
		float now, last, delta;
		last = 0;

		// Setup a key callback. It will be called every time a key 
		// is pressed, repeated or released.
		keyCallback = new KeyboardHandler();
        glfwSetKeyCallback(windowID, keyCallback);
        // Setup a mouse callback to handle mouse input.
        mouseCallback = new MouseHandler(camera, renderer, config);
        glfwSetCursorPosCallback(windowID, mouseCallback);
        
        // to hide and lock the cursor
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		// Loop continuously and render and update
		while (!glfwWindowShouldClose(windowID)) {
			// Get the time
			now = (float) glfwGetTime();
			delta = now - last;
			last = now;
			
			// Reset mouse position
			glfwSetCursorPos(
					windowID,
					config.getWidth() / 2,
					config.getWidth() / 2
					);

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

	/** Prints information about the performance of the engine.
	 * @param delta time since last update call
	 * @see Logger
	 */
	private void performanceLog(final float delta) {
		// Calculate FPS
	    if (Utility.getTimeInMilliseconds() - lastFPS > Utility.SECONDSIZE) {
	    	glfwSetWindowTitle(
	    			windowID,
	    			"FPS: " + fps + " | Render time: ~"
	    			+ renderer.getTimeToRender() + "µs"
	    			);
	        fps = 0; //reset the FPS counter
	        lastFPS += Utility.SECONDSIZE; //add one second
	    }
	    fps++;
	}

	/** Change the type of a specific block in the world. Needs the
	 * cartesian coordinate of the block in the world and the type of
	 * the block to set. The placement of the block takes
	 * place in {@link WorldData}.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param block type
	 * @see WorldData
	 * @see BlockType
	 */
	public final void setBlock(
			final int x, final int y, final int z, final BlockType block) {
		world.setBlock(x, y, z, block);
	}

	/** Removes a specific block in the world. Needs the
	 * cartesian coordinate of the block in the world.
	 * The remove of the block takes place in {@link WorldData}.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @see WorldData
	 * @see BlockType
	 */
	public final void removeBlock(final int x, final int y, final int z) {
		world.removeBlock(x, y, z);
	}
	
	/** Removes every block from the world array.
	 * The remove of the block takes place in {@link WorldData}.
	 * @see WorldData
	 * @see BlockType
	 */
	public final void fillWorldWithNothing() {
		world.fillWorldWithNothing();
	}
	
	public static void testSetup() {
		Game game = new Game();
		game.config.setHeight(1200);
		game.config.setWidth(1600);
		game.init();
		game.initWorld();
		game.start();		
	}

	/** Runs the library with a default configuration and 
	 * fills the world with cubes.
	 * @param args not used
	 */
	public static void main(final String[] args) {
		testSetup();
	}
}