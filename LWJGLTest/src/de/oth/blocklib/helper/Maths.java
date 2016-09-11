package de.oth.blocklib.helper;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import de.oth.blocklib.entities.Camera;

/**
 * Extra Math functions to create view and transformation matrix
 */
public class Maths {

	/**
	 * Calculates the difference between u and v -> u-v
	 * @param u
	 * @param v
	 * @return
	 */
	public static Vector3f vectorSubtraction(Vector3f u, Vector3f v){
		return new Vector3f(u.x-v.x, u.y - v.y, u.z-v.z);
	}

	/**
	 * Create a transformation matrix with a position and scale
	 * @param translation position where to translate the vertices
	 * @param scale to scale the vertices
	 * @return transformation matrix for given vector
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
	
	/**
	 * Create a transformation matrix with a position, rotation and scale
	 * @param translation position where to translate the vertices
	 * @param rx x-rotation
	 * @param ry y-rotation
	 * @param rz z-rotation
	 * @param scale to scale the vertices
	 * @return transformation matrix for given vector
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}

	/**
	 * Create the view matrix with the position, pitch and yaw of the camera 
	 * @param camera
	 * @return
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
}
