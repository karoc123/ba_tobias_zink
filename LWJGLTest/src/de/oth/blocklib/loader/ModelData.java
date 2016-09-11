package de.oth.blocklib.loader;

/**
 * Holds the vertices, textureCoords, normals, indices and the furthestPoint
 * from a model. Loaded with OBJLoader.
 * 
 * @see OBJLoader
 */
public class ModelData {

	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private int[] indices;
	private float furthestPoint;

	/**
	 * Holds the vertices, textureCoords, normals, indices and the furthestPoint
	 * from a model. Loaded with OBJLoader.
	 * 
	 * @param vertices
	 *            all vertices of the object.
	 * @param textureCoords
	 *            all texturecoords as floats.
	 * @param normals
	 *            all normals of the object.
	 * @param indices
	 *            indices to get the vertices of the object in the right order.
	 * @param furthestPoint
	 *            furthest vertex of the object.
	 * @see OBJLoader
	 */
	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	/**
	 * To get all vertices of the object.
	 * 
	 * @return Array of vertices
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * To get all texture coordinates of the object.
	 * 
	 * @return Array of texture coordinates.
	 */
	public float[] getTextureCoords() {
		return textureCoords;
	}

	/**
	 * To get all normals of the object.
	 * 
	 * @return Array of normals.
	 */
	public float[] getNormals() {
		return normals;
	}

	/**
	 * To get all indices of the object.
	 * 
	 * @return Array of indices.
	 */
	public int[] getIndices() {
		return indices;
	}

	/**
	 * To geht the furthest point of the object.
	 * 
	 * @return Furthest point.
	 */
	public float getFurthestPoint() {
		return furthestPoint;
	}

}