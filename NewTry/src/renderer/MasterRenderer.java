package renderer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import World.WorldData;
import blockengine.Configuration;
import entities.Camera;

/**
 * Can render more than one entity at once
 *
 */
public class MasterRenderer {
	
	private WorldMeshRenderer worldMeshRenderer;
	private Matrix4f projectionMatrix;
	
	public MasterRenderer(WorldData worldData) {
		super();
		createProjectionMatrix();
		this.worldMeshRenderer = new WorldMeshRenderer(projectionMatrix, worldData);
		
		// Because backfaces of the model can be "culled" (not rendered)
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Manages startup of renderer, shaders and cleans everything after each frame
	 * @param camera playerobject
	 * @param worldData WorldData object
	 */
	public void render(Camera camera, WorldData worldData){
		// Prepare renderer
		prepare();
		
		// render process
		worldMeshRenderer.render(worldData);
	}
	
	
	/**
	 * Initializes the renderer
	 */
	public void prepare(){
		// enable z-index testing
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		// clear the screen and depth buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// make new background
		GL11.glClearColor(0, 205 / 255.0f, 255 / 255.0f, 1);
	}
	
	
	
	/**
	 * Creates a projectionMatrix
	 */
	private void createProjectionMatrix(){
		float aspectRatio = (float) Configuration.getWidth() / (float) Configuration.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(Configuration.FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = Configuration.FAR_PLANE - Configuration.NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((Configuration.FAR_PLANE + Configuration.NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * Configuration.NEAR_PLANE * Configuration.FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
