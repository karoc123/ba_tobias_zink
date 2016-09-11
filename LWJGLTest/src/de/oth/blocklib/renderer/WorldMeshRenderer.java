package de.oth.blocklib.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import de.oth.blocklib.Configuration;
import de.oth.blocklib.entities.Entity;
import de.oth.blocklib.helper.Maths;
import de.oth.blocklib.shaders.ShaderProgram;
import de.oth.blocklib.shaders.StaticShader;
import de.oth.blocklib.world.World;
import de.oth.blocklib.world.WorldMesh;

/**
 * To render the whole world from a single mesh per chunk.
 */
public class WorldMeshRenderer {
	
	private StaticShader shader;

	/**
	 * Needs a shader to load the right projection matrix.
	 * @param shader Shader to use.
	 * @param projectionMatrix Projection marix to use.
	 * @see ShaderProgram
	 * @see WorldMesh
	 * @see World
	 */
	public WorldMeshRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders a lot of chunks (at the moment only one: the world)
	 * @param worldData the mesh of the world to render.
	 */
	public void render(WorldMesh worldData) {
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
	    
		// normals
		GL20.glEnableVertexAttribArray(2);
		
		// draw
		prepareWorldMesh();
		GL11.glDrawElements(GL11.GL_TRIANGLES, worldData.getNumberOfVertices()*3, GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	/**
	 * Prepares transformationMatrix for the world mesh.
	 */
	private void prepareWorldMesh(){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(-2.5f,-2.5f,-7), 1.0f);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}