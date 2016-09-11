package de.oth.blocklib.models;

import de.oth.blocklib.entities.Entity;
import de.oth.blocklib.textures.ModelTexture;

/**
 * Holds a rawModel and the texture for the rawModel.
 */
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	/**
	 * Model with texture. Used for entities.
	 * @param model RawModel
	 * @param texture Loaded texture
	 * @see Entity
	 */
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * Get the RawModel from the TexturedModel.
	 * @return the raw model of the textured model.
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * Get the Texture from the TexturedModel.
	 * @return the texture of the textured model.
	 */
	public ModelTexture getTexture() {
		return texture;
	}
}
