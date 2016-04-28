package renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

/**
 * Can render more than one entity at once
 *
 */
public class MasterRenderer {
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer = new EntityRenderer(shader);
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();

	public void render(Light sun, Camera camera){
		// Prepare renderer
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		
		// render process
		renderer.render(entities);
		
		// cleanup
		shader.stop();
		entities.clear();
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
	 * Because every renderer needs to be cleaned up
	 */
	public void cleanUp(){
		shader.cleanUp();
	}
}
