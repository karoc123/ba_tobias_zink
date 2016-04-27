package test8;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}

	public void move(float dx, float dy, float dz, float pitch, float yaw, float roll){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		this.pitch += pitch;
		this.yaw += yaw;
		this.roll += roll;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}
