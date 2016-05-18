package World;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Manages the world array
 *
 */
public class WorldData {
	
	//cube size of the world
	public int worldSize;
	public float cubeSize = 0.2f/2;
	
	// Rendering Variables
	public int vID, cID, vaoID;
	
	// Buffer for render process
	public FloatBuffer vertexPositionData, colorPositionData;
	public IntBuffer indicesData;
	
	// World array
	BlockType[][][] world;

	public WorldData (int worldSize){
		this.worldSize = worldSize;
		generateWorld();
		createVerticesAsBuffer();
		initMesh();
	}
	
	private void storeDataInAttributeList(int attributeNumber, FloatBuffer buffer){
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void initMesh() {
		vaoID = createVAO();

		storeDataInAttributeList(0, vertexPositionData);
		storeIndicesDataInVBO();
		
		ubindVAO();
//		vID = glGenBuffers();
//		glBindBuffer(GL_ARRAY_BUFFER, vID);
//		glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
//		cID = glGenBuffers();
//		glBindBuffer(GL_ARRAY_BUFFER, cID);
//		glBufferData(GL_ARRAY_BUFFER, colorPositionData, GL_STATIC_DRAW);
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
	}
	
	private void storeIndicesDataInVBO() {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesData, GL15.GL_STATIC_DRAW);
	}

	private void ubindVAO(){
		GL30.glBindVertexArray(0);
	}

	/**
	 * Generates World Array on world creation
	 */
	private void generateWorld(){
		this.world = new BlockType[worldSize][worldSize][worldSize];
		
		for(int i = 0; i < worldSize; i++){
			for(int k = 0; k< worldSize; k++){
				for(int j = 0; j< worldSize; j++){
					world[i][k][j] = BlockType.Grass;
				}				
			}
		}
	}

	/**
	 * Creates a single cube for a greater mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 */
	public void putVertices(float tx, float ty, float tz, int offset) {
	    vertexPositionData.put(new float[]{			  
	    		// Back face
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,	
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	
				cubeSize  + tx,-cubeSize + ty,-cubeSize + tz,
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,
				
				// Front face
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,
				cubeSize + tx,-cubeSize + ty,cubeSize + tz,
				cubeSize + tx,cubeSize + ty,cubeSize + tz,
				
				// Right face
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,	
				cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	
				cubeSize + tx,-cubeSize + ty,cubeSize + tz,	
				cubeSize + tx,cubeSize + ty,cubeSize + tz,
				
				// Left face
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,	
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,	
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,
				
				// Top face
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,
				cubeSize + tx,cubeSize + ty,cubeSize + tz,
				
				// Bottom face
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,
				cubeSize + tx,-cubeSize + ty,-cubeSize + tz,
				cubeSize + tx,-cubeSize + ty,cubeSize + tz
	    });
	    indicesData.put(new int[]{
				0+offset,1+offset,3+offset,	
				3+offset,1+offset,2+offset,	
				4+offset,5+offset,7+offset,
				7+offset,5+offset,6+offset,
				8+offset,9+offset,11+offset,
				11+offset,9+offset,10+offset,
				12+offset,13+offset,15+offset,
				15+offset,13+offset,14+offset,	
				16+offset,17+offset,19+offset,
				19+offset,17+offset,18+offset,
				20+offset,21+offset,23+offset,
				23+offset,21+offset,22+offset
	    });
	}
	
	public int getVerticesCount(){
		return (24*3)*worldSize*worldSize*worldSize;
	}
	
	/**
	 * Creates a floatbuffer of a complete mesh
	 * @return
	 */
	public FloatBuffer createVerticesAsBuffer(){
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		indicesData = BufferUtils.createIntBuffer(worldSize*worldSize*worldSize*36);
		int i = 0;
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	                    putVertices(x*0.1f, y*0.1f, z*0.1f, i++*24);
	            }
	        }
	    }
		
		vertexPositionData.flip();
		indicesData.flip();
		return vertexPositionData;
	}
}
