package de.oth.blocklib.entities;

import org.lwjgl.util.vector.Vector3f;

import de.oth.blocklib.models.RawModel;
import de.oth.blocklib.models.TexturedModel;
import de.oth.blocklib.textures.Texture;

/** A single entity. Can be loaded by OBJLoader and rendered by 
 * the entity renderer. Object and texture need to be already 
 * in the graphic memory.
 */
public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	/** Constructor for a new entity. The texturedModel needs to
	 * be already on the graphic memory.
	 * @see Vector3f
	 * @param model Texture and model of the entity
	 * @param position Position of the entity in world coordinates
	 * @param rotX x rotation of the entity
	 * @param rotY y rotation of the entity
	 * @param rotZ z rotation of the entity
	 * @param scale the scale of the entity
	 */
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	/**
	 * Change position of the entity
	 * @param dx x position to add
	 * @param dy y position to add
	 * @param dz z position to add
	 */
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	/**
	 * Change rotation of the entity
	 * @param dx x rotation to add
	 * @param dy y rotation to add
	 * @param dz z rotation to add
	 */
	public void increaseRotation( float dx, float dy, float dz){
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	/**
	 * Get the texturedModel from the entity.
	 * @return  a rawModel and the texture for the rawModel.
	 * @see TexturedModel
	 * @see RawModel
	 * @see Texture
	 */
	public TexturedModel getModel() {
		return model;
	}

	/**
	 * Set the texturedModel for the entity.
	 * @param model a rawModel and the texture for the rawModel.
	 * @see TexturedModel
	 * @see RawModel
	 * @see Texture
	 */
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	/**
	 * Position of the entity as float Vector.
	 * @return position of the entity
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Position of the entity as float Vector.
	 * @param position position of the entity
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Rotation of the entity as float.
	 * @return the x rotation of the entity
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * Rotation of the entity as float.
	 * @param rotX x rotation of the entity
	 */
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	/**
	 * Rotation of the entity as float.
	 * @return y rotation of the entity
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * Rotation of the entity as float.
	 * @param rotY Rotation of the entity
	 */
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	/**
	 * Rotation of the entity as float.
	 * @return Rotation of the entity
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * Rotation of the entity as float.
	 * @param rotZ Rotation of the entity
	 */
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	/**
	 * Rotation of the entity as float.
	 * @return Rotation of the entity
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Rotation of the entity as float.
	 * @param scale Rotation of the entity
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
}
