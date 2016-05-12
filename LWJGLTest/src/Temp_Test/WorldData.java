package Temp_Test;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;


/**
 * Manages the world array
 *
 */
public class WorldData {
	
	//cube size of the world
	public int worldSize;
	
	
	private Loader loader;
	private Temp_Test.RawModel rawModel;
	
	// Buffer for render process
	public FloatBuffer vertexPositionData;
	
	Type[][][] world;

	public WorldData (int worldSize, Loader loader){
		this.loader = loader;
		this.worldSize = worldSize;
		generateWorld();
	}
	
	/**
	 * Generates World Array on world creation
	 */
	private void generateWorld(){
		this.world = new Type[worldSize][worldSize][worldSize];
		
		for(int i = 0; i < worldSize; i++){
			for(int k = 0; k< worldSize; k++){
				for(int j = 0; j< worldSize; j++){
					world[i][j][k] = Type.Grass;
				}				
			}
		}
	}
	
	/**
	 * Gets the RawModel of the World
	 * @return RawModel of the World
	 */
	public Temp_Test.RawModel getRawModel() {
		return rawModel;
	}


	/**
	 * Creates a single cube for a greater mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 */
	public void putVertices(float tx, float ty, float tz) {
	    float length = 1.0f;
	    vertexPositionData.put(new float[]{
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
	
	/**
	 * Creates a RawModel(?) of a complete mesh
	 * @return
	 */
	public float[] createMesh(){
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		int length = 72*worldSize*worldSize*worldSize;
		
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	                    putVertices(x, y, -z);
	            }
	        }
	    }
		
		vertexPositionData.flip();
		return toFloatArray(vertexPositionData);
	}
	
	private static float[] toFloatArray(FloatBuffer bytes) {

        float[] floatArray = new float[bytes.limit()];
        bytes.get(floatArray);


        return floatArray;
    }
}
