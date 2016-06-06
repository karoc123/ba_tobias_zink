package renderer;

import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import World.WorldData;
import shaders.StaticShader;

/**
 * To render the whole world from a single mesh
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

	public void render(WorldData worldData) {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
//		GL30.glBindVertexArray(worldData.vID);
		
//		GL20.glEnableVertexAttribArray(0);		
//		GL11.glDrawArrays(GL11.GL_LINES, 0, worldData.getVerticesCount());
//		GL20.glDisableVertexAttribArray(0);
//		GL30.glBindVertexArray(0);
		
		GL30.glBindVertexArray(worldData.vaoID);
		GL20.glEnableVertexAttribArray(0);
	    
	    //GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, worldData.getVerticesCount());
		GL11.glDrawElements(GL11.GL_TRIANGLES, worldData.getVerticesCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
}