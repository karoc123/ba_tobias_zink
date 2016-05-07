package renderer;

import org.lwjgl.opengl.GL11;
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
		GL30.glBindVertexArray(worldData.getRawModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, worldData.getRawModel().getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
