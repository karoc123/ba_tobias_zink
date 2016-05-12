package World;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL15.*;

import models.RawModel;
import renderer.Loader;

/**
 * Manages the world array
 *
 */
public class WorldData {
	
	//cube size of the world
	public int worldSize;
	
	
	private Loader loader;
	private RawModel rawModel;
	
	// Rendering Variables
	public int vID, cID, vaoID;
	// Buffer for render process
	public FloatBuffer vertexPositionData, colorPositionData;
	
	BlockType[][][] world;

	public WorldData (int worldSize, Loader loader){
		this.loader = loader;
		this.worldSize = worldSize;
		generateWorld();
		createVerticesAsBuffer();
		initMesh();
	}
	
	private void initMesh() {
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		vID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vID);
		glBufferData(GL_ARRAY_BUFFER, vertexPositionData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
//		cID = glGenBuffers();
//		glBindBuffer(GL_ARRAY_BUFFER, cID);
//		glBufferData(GL_ARRAY_BUFFER, colorPositionData, GL_STATIC_DRAW);
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
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
					world[i][j][k] = BlockType.Grass;
				}				
			}
		}
	}
//	
//	/**
//	 * Gets the RawModel of the World
//	 * @return RawModel of the World
//	 */
//	public RawModel getRawModel() {
//		return rawModel;
//	}


	/**
	 * Creates a single cube for a greater mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 */
	public void putVertices(float tx, float ty, float tz) {
	    float l_length = 1.0f;
	    vertexPositionData.put(new float[]{
//	    		  // Front face
//	    		  -1.0f, -1.0f,  1.0f,
//	    		   1.0f, -1.0f,  1.0f,
//	    		   1.0f,  1.0f,  1.0f,
//	    		  -1.0f,  1.0f,  1.0f,
//	    		  
//	    		  // Back face
//	    		  -1.0f, -1.0f, -1.0f,
//	    		  -1.0f,  1.0f, -1.0f,
//	    		   1.0f,  1.0f, -1.0f,
//	    		   1.0f, -1.0f, -1.0f,
//	    		  
//	    		  // Top face
//	    		  -1.0f,  1.0f, -1.0f,
//	    		  -1.0f,  1.0f,  1.0f,
//	    		   1.0f,  1.0f,  1.0f,
//	    		   1.0f,  1.0f, -1.0f,
//	    		  
//	    		  // Bottom face
//	    		  -1.0f, -1.0f, -1.0f,
//	    		   1.0f, -1.0f, -1.0f,
//	    		   1.0f, -1.0f,  1.0f,
//	    		  -1.0f, -1.0f,  1.0f,
//	    		  
//	    		  // Right face
//	    		   1.0f, -1.0f, -1.0f,
//	    		   1.0f,  1.0f, -1.0f,
//	    		   1.0f,  1.0f,  1.0f,
//	    		   1.0f, -1.0f,  1.0f,
//	    		  
//	    		  // Left face
//	    		  -1.0f, -1.0f, -1.0f,
//	    		  -1.0f, -1.0f,  1.0f,
//	    		  -1.0f,  1.0f,  1.0f,
//	    		  -1.0f,  1.0f, -1.0f
	    		
	    		  // Front face
	    		  -0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		   0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		   0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		  -0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		  
	    		  // Back face
	    		  -0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		  -0.1f + tx,  0.1f + ty, -0.1f + tz,
	    		   0.1f + tx,  0.1f + ty, -0.1f + tz,
	    		   0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		  
	    		  // Top face
	    		  -0.1f + tx,  0.1f + ty, -0.1f + tz,
	    		  -0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		   0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		   0.1f + tx,  0.1f + ty, -0.1f + tz,
	    		  
	    		  // Bottom face
	    		  -0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		   0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		   0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		  -0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		  
	    		  // Right face
	    		   0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		   0.1f + tx,  0.1f + ty, -0.1f + tz,
	    		   0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		   0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		  
	    		  // Left face
	    		  -0.1f + tx, -0.1f + ty, -0.1f + tz,
	    		  -0.1f + tx, -0.1f + ty,  0.1f + tz,
	    		  -0.1f + tx,  0.1f + ty,  0.1f + tz,
	    		  -0.1f + tx,  0.1f + ty, -0.1f + tz
	    });
	}
	
	public int getVerticesCount(){
		return (24*3)*worldSize*worldSize*worldSize;
	}
	
//	/**
//	 * Creates a float[] array of a complete mesh
//	 * @return
//	 */
//	public float[] createVerticesAsArray(){
//		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
//		
//		for (int x = 0; x < worldSize; x++) {
//	        for (int y = 0; y < worldSize; y++) {
//	            for (int z = 0; z < worldSize; z++) {
//	                    putVertices(x, y, z);
//	            }
//	        }
//	    }
//		
//		vertexPositionData.flip();
//		return toFloatArray(vertexPositionData);
//	}
	
	/**
	 * Creates a floatbuffer of a complete mesh
	 * @return
	 */
	public FloatBuffer createVerticesAsBuffer(){
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	                    putVertices(x, y, z);
	            }
	        }
	    }
		
		vertexPositionData.flip();
		return vertexPositionData;
	}
	
	private static float[] toFloatArray(FloatBuffer bytes) {

        float[] floatArray = new float[bytes.limit()];
        bytes.get(floatArray);


        return floatArray;
    }
}
