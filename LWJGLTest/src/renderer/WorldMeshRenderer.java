package renderer;

import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import World.WorldData;
import blockengine.Configuration;
import helper.Maths;
import shaders.StaticShader;

/**
 * To render the whole world from a single mesh per chunk
 *
 */
public class WorldMeshRenderer {

	private StaticShader shader;
	private WorldData worldData;

	public WorldMeshRenderer(StaticShader shader, Matrix4f projectionMatrix, WorldData worldData) {
		this.shader = shader;
		this.worldData = worldData;
		init();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	private void init() {
	}

	/**
	 * Renders a lot of chunks
	 * @param worldData
	 */
	public void render(WorldData worldData) {
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
		GL11.glPointSize(5.0f);
		if(Configuration.showWireframe){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE );		
		}
		
		GL30.glBindVertexArray(worldData.vaoID);
		GL20.glEnableVertexAttribArray(0);
		
		//texture stuff
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, worldData.texture.getID());
		
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, worldData.vboID);
	    
		GL11.glDrawElements(GL11.GL_TRIANGLES, 36*worldData.getNumberOfCubes(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
}