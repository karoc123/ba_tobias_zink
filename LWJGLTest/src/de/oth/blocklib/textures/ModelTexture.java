package de.oth.blocklib.textures;

import de.oth.blocklib.models.TexturedModel;

/**
 * Represents a id of texture (from opengl, generated with glGenTextures).
 * @see TexturedModel
 */
public class ModelTexture {
	
	private int textureID;
	
	/** Set the id (from opengl) which represents the texture.
	 * @param id generated with glGenTextures
	 */
	public ModelTexture(final int id) {
		this.textureID = id;
	}

	/** Get the id (generated with glGenTextures) of the texture. 
	 * @return id (generated with glGenTextures)
	 */
	public final int getID() {
		return this.textureID;
	}
}