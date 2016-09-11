package de.oth.blocklib.input;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import de.oth.blocklib.Configuration;
import de.oth.blocklib.entities.Camera;
import de.oth.blocklib.helper.Maths;

/**
 * TODO: NOT FULLY IMPLEMENTED YET.
 */
public class MousePicker {

	private Vector3f currentRay;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	private Configuration config;

	public MousePicker(Camera cam, Matrix4f projection, Configuration config) {
		this.camera = cam;
		this.config = config;
		this.projectionMatrix = projection;
		this.viewMatrix = Maths.createViewMatrix(camera);
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

	public void update(float mouseX, float mouseY) {
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay(mouseX, mouseY);
	}

	private Vector3f calculateMouseRay(float mouseX, float mouseY) {
		Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / config.getWidth() - 1;
		float y = (2f * mouseY) / config.getHeight() - 1f;
		return new Vector2f(x, y);
	}
}
