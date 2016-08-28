package de.oth.blocklib.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light (only one light possible at the moment)
 * @see Vector3f
 */
public class Light {
	
	private Vector3f position;
	private Vector3f colour;
	
	/**
	 * Create a light
	 * @param position
	 * of the light
	 * @param colour
	 * color of the light
	 * @see Vector3f
	 */
	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}

	/**
	 * Get position (Vector3f) of the light
	 * @see Vector3f
	 * @return position of the light as a cartesian coordinate
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Set position (Vector3f) of the light
	 * @see Vector3f
	 * @param position of the light as a cartesian coordinate
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Get colour of the light
	 * @see Vector3f
	 * @return
	 */
	public Vector3f getColour() {
		return colour;
	}

	/**
	 * Set colour of the light (Vector3f)
	 * @see Vector3f
	 * @param colour
	 */
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
}
