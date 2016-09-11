package de.oth.blocklib.loader;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a complete vertex. Only used by the object file loader.
 * 
 * @see OBJLoader
 */
public class Vertex {

	private static final int NO_INDEX = -1;

	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;

	/**
	 * Create a new Vertex from a vector.
	 * 
	 * @param index index of the vertex
	 * @param position position of the vertex
	 */
	public Vertex(int index, Vector3f position) {
		this.index = index;
		this.position = position;
		this.length = position.length();
	}

	/**
	 * Get the index.
	 * @return the index of the vertex
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Get the legnth.
	 * @return the lenght of the vertex
	 */
	public float getLength() {
		return length;
	}

	public boolean isSet() {
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}

	/**
	 * If texture index and normal index are both the same.
	 * @param textureIndexOther texture index to check.
	 * @param normalIndexOther normal index to check.
	 * @return true if both are the same
	 */
	public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}

	/**
	 * Set texture index.
	 * @param textureIndex texture index to set.
	 */
	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	/**
	 * Set normal index.
	 * @param normalIndex normal index to set.
	 */
	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	/**
	 * Get position of the vertex.
	 * @return Vector of the position of the vertex.
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Get the texture index of the vertex.
	 * @return Texture index of the vertex.
	 */
	public int getTextureIndex() {
		return textureIndex;
	}

	/**
	 * Get normal index of the vertex.
	 * @return the normal index of the vertex.
	 */
	public int getNormalIndex() {
		return normalIndex;
	}

	/**
	 * If there is a duplicate of this vertex, get it.
	 * @return duplicate of the vertex.
	 */
	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	/**
	 * Set a vertex as a duplicate of this vertex.
	 * @param duplicateVertex duplicate of the vertex.
	 */
	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

}