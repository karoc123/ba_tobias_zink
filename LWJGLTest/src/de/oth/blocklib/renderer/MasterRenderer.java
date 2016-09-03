package de.oth.blocklib.renderer;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import de.oth.blocklib.Configuration;
import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.entities.Entity;
import de.oth.blocklib.entities.Light;
import de.oth.blocklib.models.TexturedModel;
import de.oth.blocklib.shaders.StaticShader;
import de.oth.blocklib.world.WorldData;

/**
 * Can render more than one entity at once
 *
 */
public class MasterRenderer {
	
	private StaticShader shader;
	private EntityRenderer entityRenderer;
	private WorldMeshRenderer worldMeshRenderer;
	private Map<TexturedModel,List<Entity>> entities;
	private Matrix4f projectionMatrix;
	private long timeToRender;
	private Configuration config;
	
	public MasterRenderer(Configuration config) {
		super(); 
		this.config = config;
		createProjectionMatrix();
		this.shader = new StaticShader();
		this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
		this.worldMeshRenderer = new WorldMeshRenderer(shader, projectionMatrix);
		entities = new HashMap<TexturedModel,List<Entity>>();
		
		// Because backfaces of the model can be "culled" (not rendered)
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Manages startup of renderer, shaders and cleans everything after each frame
	 * @param sun main light
	 * @param camera playerobject
	 */
	public void render(Light sun, Camera camera, WorldData worldData){
		long tick = System.nanoTime();
		
		// Prepare renderer
		prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		
		// render process
		entityRenderer.render(entities);
		worldMeshRenderer.render(worldData);

		// cleanup
		shader.stop();
		entities.clear();
		
		timeToRender = ((System.nanoTime() - tick) + timeToRender)/2;
	}
	
	/**
	 * How much time took the last rendering in microseconds (estimated)
	 */
	public long getTimeToRender(){
		return timeToRender / 1000;
	}
	
	/**
	 * All entities need to be sorted in every frame
	 * @param entity entity to put in list
	 */
	public void processEntity(Entity entity){
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null){
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
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
		GL11.glClearColor(0, 205 / 255.0f, 255 / 255.0f, 0.001f); // blue
//		GL11.glClearColor(255.0f, 255.0f, 255.0f, 1); // white
	}
	
	/**
	 * Because every renderer needs to be cleaned up
	 */
	public void cleanUp(){
		shader.cleanUp();
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	/**
	 * Creates a projectionMatrix
	 */
	private void createProjectionMatrix(){
		float aspectRatio = (float) config.getWidth() / (float) config.getHeight();
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
