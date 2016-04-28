package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light
 * @author Karoc
 *
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

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColour() {
		return colour;
	}

	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
}
