package World;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import models.RawModel;
import renderer.Loader;

/**
 * Manages the world array
 *
 */
public class WorldData {
	
	public int worldSize;
	private Loader loader;
	private RawModel rawModel;
	
	private static int xOffset = 20;
	private static int zOffset = 20;
	public FloatBuffer vertexPositionData;
	
	Biome[][][] world;

	public WorldData (int worldSize, Loader loader){
		this.loader = loader;
		this.worldSize = worldSize;
		generateWorld();
		createMesh();
	}
	
	/**
	 * Generates World Array on world creation
	 */
	private void generateWorld(){
		this.world = new Biome[worldSize][worldSize][worldSize];
		
		for(int i = 0; i < worldSize; i++){
			for(int k = 0; k< worldSize; k++){
				for(int j = 0; j< worldSize; j++){
					world[i][j][k] = Biome.Grass;
				}				
			}
		}
	}
	
	/**
	 * Gets the RawModel of the World
	 * @return RawModel of the World
	 */
	public RawModel getRawModel() {
		return rawModel;
	}


	public void putVertices(float tx, float ty, float tz) {
	    float l_length = 1.0f;
	    float l_height = 1.0f;
	    float l_width = 1.0f;
	    vertexPositionData.put(new float[]{
	            xOffset + l_length + tx, l_height + ty, zOffset + -l_width + tz,
	            xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
	            xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
	            xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,

	            xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty, zOffset + l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
	            xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,

	            xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,
	            xOffset + -l_length + tx, l_height + ty,zOffset +  l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,
	            xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,

	            xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty,zOffset +  -l_width + tz,
	            xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
	            xOffset + l_length + tx, l_height + ty, zOffset + -l_width + tz,

	            xOffset + -l_length + tx, l_height + ty, zOffset + l_width + tz,
	            xOffset + -l_length + tx, l_height + ty, zOffset + -l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty, zOffset + -l_width + tz,
	            xOffset + -l_length + tx, -l_height + ty,zOffset +  l_width + tz,

	            xOffset + l_length + tx, l_height + ty,zOffset +  -l_width + tz,
	            xOffset + l_length + tx, l_height + ty, zOffset + l_width + tz,
	            xOffset + l_length + tx, -l_height + ty, zOffset + l_width + tz,
	            xOffset + l_length + tx, -l_height + ty, zOffset + -l_width + tz

	    });
	}
	
	public RawModel createMesh(){
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		int length = 72*worldSize*worldSize*worldSize;
		
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	                    putVertices(x, y, z);
	            }
	        }
	    }
		
		vertexPositionData.flip();
		return loader.loadToVAO(vertexPositionData, length);
	}
}
