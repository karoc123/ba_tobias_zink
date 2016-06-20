package renderer;

import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import World.WorldData;
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
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_POINT);
		GL11.glPointSize(5.0f);
//		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		GL30.glBindVertexArray(worldData.vaoID);
		GL20.glEnableVertexAttribArray(0);
	    
		GL11.glDrawElements(GL11.GL_TRIANGLES, worldData.getVerticesCount()*3, GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
}