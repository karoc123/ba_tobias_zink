package de.oth.blocklib.models;

import de.oth.blocklib.textures.ModelTexture;

/**
 * Holds a rawModel and the texture for the rawModel
 */
public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	/**
	 * Model with texture
	 * @param model RawModel
	 * @param texture Loaded texture
	 */
	public TexturedModel(RawModel model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
