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
import de.oth.blocklib.world.ExampleWorld;
import de.oth.blocklib.world.World;
import de.oth.blocklib.world.WorldMesh;

/**
 * This class defines the game loop and holds instances of every class.
 * First initialize the world with initWorld() and then
 * start the window with start().
 * Constructor with Configuration or without to get default configuration.
 * @see Configuration
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
	private WorldMesh worldMesh;
	private World world;
	private Configuration config;
	private int fps;
    private long lastFPS;
	private Random rand = new Random();
	
    private float temp = 0;
	
    /** Create and initialize a new game with default configuration.
     * @see Configuration
     * @see WorldMesh
     */
	public Game() {
		this(new Configuration());

	}
	
    /** Create and initialize a new game with given configuration.
     * @param config configuration to use
     * @see Configuration
     * @see WorldMesh
     */
	public Game(Configuration config) {
		this.config = config;
		// Load configuration
		config.loadConfiguration();
		
		// Init loader
		loader = new Loader();
		
		this.init();
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
			setWindowID(glfwCreateWindow(config.getWidth(), config.getHeight(), 
					"LWJGL Block Engine", glfwGetPrimaryMonitor(), NULL));

		} else {
			setWindowID(glfwCreateWindow(config.getWidth(), config.getHeight(), 
					"LWJGL Block Engine", NULL, NULL));
		}

		if (getWindowID() == NULL) {
			System.err.println("Error creating a window");
			System.exit(1);
		}

		glfwMakeContextCurrent(getWindowID());
		GL.createCapabilities();

		glfwSwapInterval(1);
		glfwShowWindow(getWindowID());
		
		// Debuginformation
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		lastFPS = Utility.getTimeInMilliseconds();
		System.out.println("LWJGL " + Version.getVersion() + "!");		
	}
	
	/** Initializes a world with the parameters from configuration
	 * @see Configuration
	 * @see WorldMesh
	 * @param world the world to render
	 */
	public void initWorld(World world) {
		this.setWorld(world);
		
		// Init Renderer
		renderer = new MasterRenderer(config);
		
		// Load 3d model for single entity
		ModelData modelData = OBJLoader.loadOBJ("cube");
		RawModel model = loader.loadToVAO(modelData.getVertices(), 
				modelData.getTextureCoords(), 
				modelData.getNormals(), 
				modelData.getIndices());
		
		// Load textures and create models with them
		ModelTexture spritesheet = new ModelTexture(loader.loadTexture("spritesheet"));
		ModelTexture stone = new ModelTexture(loader.loadTexture("stone"));
		TexturedModel texModStone = new TexturedModel(model, stone);
		
		Entity entity = new Entity(texModStone, new Vector3f(-2,-1.f,-5),15,0,0,1.f);
		//entities.add(entity);
			
		light = new Light(new Vector3f(4,4,-4), new Vector3f(1,1,1));
		
		// Create World
		setWorldMesh(new WorldMesh(world, spritesheet));
		
		// set camera in the middle of the world
		camera.setPosition(new Vector3f(config.getWorldSize()/2, config.getWorldSize()/2, -config.getWorldSize()/2));

		//Entities Test
//		for(int i = 0; i < 30; i++){
//			for(int k = 0; k< 30; k++){
//				for(int j = 0; j< 30; j++){
//					Entity entityNew = new Entity(texModStone, new Vector3f(0.20f*i-0.9f,-0.6f-0.2f*j,-1.8f-0.2f*k),0,0,0,0.2f);
//					entities.add(entityNew);
//				}				
//			}
//		}
	}

	/** This method is used to update the game logic.
	 * @param delta
	 *            time since last call
	 */
	public void update(final float delta) {
		if (getWorld().isMeshLatest() == false) {
			getWorldMesh().recreateMesh();
		}
		
		//Temp
		if (Configuration.runDemo) {
			temp += delta;
			if (temp > 2.1) {
				if (rand.nextBoolean()){
					getWorld().setBlock(
							rand.nextInt(getWorld().worldSize),
							rand.nextInt(getWorld().worldSize), 
							rand.nextInt(getWorld().worldSize), 
							BlockType.NOTHING
							);					
				} else {
					getWorld().setBlock(
							rand.nextInt(getWorld().worldSize),
							rand.nextInt(getWorld().worldSize),
							rand.nextInt(getWorld().worldSize),
							BlockType.GRASS
							);					
				}
				getWorldMesh().recreateMesh();	
				temp = 0;
			}			
		}
	}
	
	/** To process the input.
	 * @see Keyboard
	 * @param delta
	 *            time since last call
	 */
	public void processInput(final float delta) {
		//process input: keyboard
		Keyboard.handleInput(delta, getCamera(), getWindowID());
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
		renderer.render(light, getCamera(), getWorldMesh());
	}

	/** Called after the game ends to cleanup the loader, renderer and world.
	 * @see Loader
	 * @see MasterRenderer
	 * @see WorldMesh
	 */
	private void dispose() {
		loader.cleanUp();
		renderer.cleanUp();
		getWorldMesh().cleanUp();
	}

	/** Starts the library.
	 * Main Loop is here.
	 */
	public void start() {
		float now, last, delta;
		last = 0;

		// Setup a key callback. It will be called every time a key 
		// is pressed, repeated or released.
		keyCallback = new KeyboardHandler();
        glfwSetKeyCallback(getWindowID(), keyCallback);
        // Setup a mouse callback to handle mouse input.
        mouseCallback = new MouseHandler(getCamera(), renderer, config);
        glfwSetCursorPosCallback(getWindowID(), mouseCallback);
        
        // to hide and lock the cursor
		glfwSetInputMode(getWindowID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		// Loop continuously and render and update
		while (!glfwWindowShouldClose(getWindowID())) {
			// Get the time
			now = (float) glfwGetTime();
			delta = now - last;
			last = now;
			
			// Reset mouse position
			glfwSetCursorPos(
					getWindowID(),
					config.getWidth() / 2,
					config.getWidth() / 2
					);

			// Update and render
			processInput(delta);
			update(delta);
			render(delta);
			performanceLog(delta);

			// Poll the events and swap the buffers
			glfwPollEvents();
			glfwSwapBuffers(getWindowID());
		}

		// Dispose the game
		dispose();

		// Destroy the window
		glfwDestroyWindow(getWindowID());
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
	    			getWindowID(),
	    			"FPS: " + fps + " | Render time: ~"
	    			+ renderer.getTimeToRender() + "µs"
	    			+ " | Time to display: ~"
	    			+ delta + "s"
	    			);
	        fps = 0; //reset the FPS counter
	        lastFPS += Utility.SECONDSIZE; //add one second
	    }
	    fps++;
	}

	/** Change the type of a specific block in the world. Needs the
	 * cartesian coordinate of the block in the world and the type of
	 * the block to set. The placement of the block takes
	 * place in {@link WorldMesh}.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param block type
	 * @see WorldMesh
	 * @see BlockType
	 */
	public final void setBlock(
			final int x, final int y, final int z, final BlockType block) {
		getWorld().setBlock(x, y, z, block);
	}

	/** Removes a specific block in the world. Needs the
	 * cartesian coordinate of the block in the world.
	 * The remove of the block takes place in {@link WorldMesh}.
	 * @param x position
	 * @param y position
	 * @param z position
	 * @see WorldMesh
	 * @see BlockType
	 */
	public final void removeBlock(final int x, final int y, final int z) {
		getWorld().removeBlock(x, y, z);
	}
	
	/** Removes every block from the world array.
	 * The remove of the block takes place in {@link WorldMesh}.
	 * @see WorldMesh
	 * @see BlockType
	 */
	public final void fillWorldWithNothing() {
		getWorld().fillWorldWithNothing();
	}
	
	public static void testSetup() {
		Configuration config = new Configuration();
		ExampleGame game = new ExampleGame(config);

		ExampleWorld world = new ExampleWorld(config.getWorldSize());
//		world.fillWorldWithBlocksOctaveNew();
//		world.fillWorldWithBlocksOctave();
//		world.fillWorldWithChessboard();
		world.fillWorldWithBlocksLookLikeWorld();
//		world.fillWorldWithBlocksFull();
//		world.fillWorldWithWorstCaseBlocks();

//		//TEMP
//		boolean chess = true;
//		for (int i = 0; i < config.getWorldSize(); i++) {
//			for (int j = 0; j < config.getWorldSize(); j++) {
//				if (chess) {
//					world.setBlock(i, 0, j, BlockType.Stone);
//					chess = !chess;
//				} else {
//					world.setBlock(i, 0, j, BlockType.Dirt);
//					chess = !chess;
//				}
//			}
//			chess = !chess;
//		}
//		// ENDE TEMP
		
		game.initWorld(world);
		game.start();
	}

	/** Runs the library with a default configuration and 
	 * fills the world with cubes.
	 * @param args not used
	 */
	public static void main(final String[] args) {
		testSetup();
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * @return the worldMesh
	 */
	public WorldMesh getWorldMesh() {
		return worldMesh;
	}

	/**
	 * @param worldMesh the worldMesh to set
	 */
	public void setWorldMesh(WorldMesh worldMesh) {
		this.worldMesh = worldMesh;
	}

	/**
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * @return the windowID
	 */
	public long getWindowID() {
		return windowID;
	}

	/**
	 * @param windowID the windowID to set
	 */
	public void setWindowID(long windowID) {
		this.windowID = windowID;
	}
}