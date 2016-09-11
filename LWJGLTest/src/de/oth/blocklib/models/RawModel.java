package de.oth.blocklib.models;

import de.oth.blocklib.loader.OBJLoader;

/**
 * A .OBJ File gets loaded as a VAO and the used as a raw model.
 * If there is a texture for the raw model, they get combined as TexturedModel
 * @see TexturedModel
 * @see OBJLoader
 */
public class RawModel {

	private int vaoID;
	private int vertexCount;
	
	/**
	 * After the object is loaded to the vao, create a raw model.
	 * @param vaoID ID of vao with the object.
	 * @param vertextCount number of vertices in the vao.
	 */
	public RawModel(int vaoID, int vertextCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertextCount;
	}

	/**
	 * Get the id of the vao with the object.
	 * @return the id of the vao
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Number of vertices in vao from the model
	 * @return te number of vertices
	 */
	public int getVertexCount() {
		return vertexCount;
	}
}
