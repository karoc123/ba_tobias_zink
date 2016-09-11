package de.oth.blocklib.entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents the state of the camera.
 * Has a position, pitch, yaw and roll value.
 */
public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	/** Constructor of Camera. Used to calculate the viewport in the shaders.
	 * Has a position, pitch, yaw and roll value
	 */
	public Camera() { }

	/** To change the angles of the "camera".
	 * 
	 * @param dx
	 *            horizontal axis
	 * @param dy
	 *            vertical axis
	 * @param dz
	 *            far / near axis
	 * @param pitch
	 *            turns camera up / down
	 * @param yaw
	 *            turns camera left / right
	 * @param roll
	 * 			  rolls the camera
	 */
	public void move(float dx, float dy, float dz, float pitch, float yaw, float roll){
//		System.out.println(position.x + " - " + position.y + " - " + position.z);
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		this.pitch += pitch;
		this.yaw += yaw;
		this.roll += roll;
	}
	
	/**
	 * moves the camera forward relative to its current rotation (yaw)
	 * @param distance to move
	 */
	public void walkForward(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
	    position.z += distance * (float)Math.cos(Math.toRadians(yaw));
	}
	 
	/**
	 * moves the camera backward relative to its current rotation (yaw)
	 * @param distance to move
	 */
	public void walkBackwards(float distance)
	{
	    position.x += distance * (float)Math.sin(Math.toRadians(yaw));
	    position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
	}
	 
	/**
	 * strafes the camera left relitive to its current rotation (yaw)
	 * @param distance to move
	 */
	public void strafeLeft(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
	    position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
	}
	 
	/**
	 * strafes the camera right relitive to its current rotation (yaw)
	 * @param distance to move
	 */
	public void strafeRight(float distance)
	{
	    position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
	    position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
	}
	
	/**
	 * Current position of the camera
	 * @return Vector with three floats
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Up / right of the camera
	 * @return how much the camera is turned up or down
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Left / right of the camera
	 * @return how much the camera is turned left or right
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Roll of the camera (not usefull in minecraft like game?)
	 * @return the roll of the camera
	 */
	public float getRoll() {
		return roll;
	}

}
