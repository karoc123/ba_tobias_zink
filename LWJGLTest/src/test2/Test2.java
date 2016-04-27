package test2;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Test2 {
	private long windowID;
	private Loader loader;
	private Renderer renderer;
	private RawModel model;

	public Test2() {
		System.out.println("LWJGL " + Version.getVersion() + "!");
		loader = new Loader();
		renderer = new Renderer();

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
		windowID = glfwCreateWindow(800, 600, "My GLFW Window", NULL, NULL);

		if (windowID == NULL) {
			System.err.println("Error creating a window");
			System.exit(1);
		}

		glfwMakeContextCurrent(windowID);
		GL.createCapabilities();

		glfwSwapInterval(1);
		glfwShowWindow(windowID);
	}

	public void init() {
		glEnable(GL_DEPTH_TEST);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));

		float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f, 0.5f, 0.5f, 0 };
		
		int[] indices = { 
				0, 1, 3, // first triangle
				3, 1, 2 // second triangle
		};
		
		model = loader.loadToVAO(vertices, indices);
	}

	public void update(float delta) {
	}

	public void render(float delta) {

		renderer.prepare();
		renderer.render(model);
		// // set the color of the quad (R,G,B,A)
		// // glColor3f(0.5f,0.5f,1.0f);
		//
		// // set the color of the quad (R,G,B,A)
		// //GL11.glColor3f(0.5f,0.5f,1.0f);
		//
		// // draw quad
		// GL11.glBegin(GL11.GL_QUADS);
		// GL11.glVertex2f(100,100);
		// GL11.glVertex2f(100+200,100);
		// GL11.glVertex2f(100+200,100+200);
		// GL11.glVertex2f(100,100+200);
		// GL11.glEnd();
	}

	public void dispose() {
		loader.cleanUp();
	}

	public void start() {
		float now, last, delta;

		last = 0;

		// Initialise the Game
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
		new Test2().start();
	}
}