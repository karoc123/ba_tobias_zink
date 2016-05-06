package models;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertextCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertextCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Number of vertices in vao from the model
	 * @return
	 */
	public int getVertexCount() {
		return vertexCount;
	}
}
