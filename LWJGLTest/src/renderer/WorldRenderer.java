package renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import World.WorldData;
import helper.Maths;
import modelLoader.ModelData;
import modelLoader.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;

public class WorldRenderer {
	
	private TexturedModel texturedModel;
	private RawModel rawModel;
	private ModelData modelData;
	private StaticShader shader;

	/**
	 * Setups a single renderer with shader and loads projection matrix
	 * @param shader initialized shader to use
	 */
	public WorldRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		init();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Initializes world renderer
	 */
	private void init() {
		Loader loader = new Loader();
		// Load 3d Models
		modelData = OBJLoader.loadOBJ("cube");
		rawModel = loader.loadToVAO(modelData.getVertices(), 
				modelData.getTextureCoords(), 
				modelData.getNormals(), 
				modelData.getIndices());
		
		ModelTexture texture;
		texture = new ModelTexture(loader.loadTexture("grass"));
		this.texturedModel = new TexturedModel(rawModel, texture);
	}

	/**
	 * Renders a world array
	 * @param worldData worldData array
	 */
	public void render(WorldData worldData) {
		prepareTexturedModel(texturedModel);
		
		for(int i = 0; i < worldData.worldSize; i++){
			for(int k = 0; k< worldData.worldSize; k++){
				for(int j = 0; j< worldData.worldSize; j++){
					prepareInstance(new Vector3f(0.20f*i-0.9f,-0.6f-0.2f*j,-1.8f-0.2f*k));
					GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}				
			}
		}
		
		unbindTexturedModel();
	}
	
	/**
	 * Prepares rendering, called once a frame
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
	 * Called once rendering is finished
	 */
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Prepares instances of TexturedModels
	 */
	private void prepareInstance(Vector3f position){
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(position, 
				0, 0,0, 0.2f);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
