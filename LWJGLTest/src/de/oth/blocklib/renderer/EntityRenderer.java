package de.oth.blocklib.renderer;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import de.oth.blocklib.entities.Entity;
import de.oth.blocklib.helper.Maths;
import de.oth.blocklib.models.RawModel;
import de.oth.blocklib.models.TexturedModel;
import de.oth.blocklib.shaders.StaticShader;

/**
 * Single entities can be rendered with the EntityRenderer.
 * <b>For every entity there will be a draw call.</b> Rendering a (big)
 * chunk or world is not effective this way.
 * @see Entity
 * @see WorldMeshRenderer
 * @see MasterRenderer
 */
public class EntityRenderer {
	
	
	private StaticShader shader;

	/**
	 * Setups a single renderer with shader and loads projection matrix
	 * @param shader initialized shader to use
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Renders a List of Entities
	 * @param entities Map of TexturedModels to entities
	 */
	public void render(Map<TexturedModel,List<Entity>> entities){
		for(TexturedModel model:entities.keySet()){
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch){
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	/**
	 * Prepares rendering, called once a frame. Enables vbo
	 * for the TexturedModel and binds the right texture in opengl.
	 * @param model a single textured model
	 */
	private void prepareTexturedModel(TexturedModel model){
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Called once rendering is finished. Disables all vbo and the vba.
	 */
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads up the transformationMatrix for the entity on the shader.
	 */
	private void prepareInstance(Entity entity){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
