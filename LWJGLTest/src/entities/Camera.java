package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents the state of the camera
 */
public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}

	/**
	 * To change the angles of the "camera"
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
	 */
	public void move(float dx, float dy, float dz, float pitch, float yaw, float roll){
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
	 * @return
	 */
	public float getRoll() {
		return roll;
	}

}
