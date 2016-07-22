package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light
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
	 */
	public Light(Vector3f position, Vector3f colour) {
		super();
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Set position (Vector3f) of the light
	 * @param position
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * Get colour of the light
	 * @return
	 */
	public Vector3f getColour() {
		return colour;
	}

	/**
	 * Set colour of the light (Vector3f)
	 * @param colour
	 */
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
}
