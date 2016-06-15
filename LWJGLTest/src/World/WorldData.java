package World;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

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
	private float cubeSize = 2.0f/2;
	private int numberOfVertices = 0;
	
	// Rendering Variables
	public int cID, vaoID;
	
	// Buffer for render process
	public FloatBuffer vertexPositionData, colorPositionData;
	public IntBuffer indicesData;
	
	// List of Buffers
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	// World array
	BlockType[][][] world;

	public WorldData (int worldSize){
		this.worldSize = worldSize;
		generateWorld();
		//readChunkFromFile();
		createVerticesAsBuffer();
		initMesh();
	}
	
	private void storeDataInAttributeList(int attributeNumber, FloatBuffer buffer){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID); //save buffers to delete after
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID); //save buffers to delete after
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void initMesh() {
		vaoID = createVAO();
		storeIndicesDataInVBO();
		storeDataInAttributeList(0, vertexPositionData);
		
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
		vbos.add(vboID); //save buffers to delete after
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesData, GL15.GL_STATIC_DRAW);
	}

	/**
	 * Deletes all loaded resources
	 */
	public void cleanUp(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
//		for(int texture:textures){
//			GL11.glDeleteTextures(texture);
//		}
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
	 * Reads chunk from a chunk file
	 */
	private void readChunkFromFile(){
		world[0][0][1] = BlockType.Grass;
		world[0][0][2] = BlockType.Grass;
		world[0][0][3] = BlockType.Grass;
		world[0][0][4] = BlockType.Grass;
		world[0][0][5] = BlockType.Grass;
		world[0][0][6] = BlockType.Grass;
		world[0][0][7] = BlockType.Grass;
		world[0][0][8] = BlockType.Grass;
		world[0][0][9] = BlockType.Grass;
		world[1][0][0] = BlockType.Grass;
		world[2][0][0] = BlockType.Grass;
		world[3][0][0] = BlockType.Grass;
		world[4][0][0] = BlockType.Grass;
		world[5][0][0] = BlockType.Grass;
		world[6][0][0] = BlockType.Grass;
		world[7][0][0] = BlockType.Grass;
		world[8][0][0] = BlockType.Grass;
		world[9][0][0] = BlockType.Grass;
	}

	/**
	 * Creates cube data and puts it into buffers for one mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 * @return number of vertices added
	 */
	public int putVertices(float tx, float ty, float tz, int offset) {
	    vertexPositionData.put(new float[]{			  
	    		// Back face
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,	//0
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	//1
				cubeSize  + tx,-cubeSize + ty,-cubeSize + tz,	//2
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,		//3
				
				// Front face
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,		//4
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,	//5
				cubeSize + tx,-cubeSize + ty,cubeSize + tz,		//6
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//7
				
				// Right face
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,		//8
				cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	//9
				cubeSize + tx,-cubeSize + ty,cubeSize + tz,		//10
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//11
				
				// Left face
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,	//12
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	//13
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,	//14
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,		//15
				
				// Top face
				-cubeSize + tx,cubeSize + ty,cubeSize + tz,		//16
				-cubeSize + tx,cubeSize + ty,-cubeSize + tz,	//17
				cubeSize + tx,cubeSize + ty,-cubeSize + tz,		//18
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//19
				
				// Bottom face
				-cubeSize + tx,-cubeSize + ty,cubeSize + tz,	//20
				-cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	//21
				cubeSize + tx,-cubeSize + ty,-cubeSize + tz,	//22
				cubeSize + tx,-cubeSize + ty,cubeSize + tz		//23
	    });
	    indicesData.put(new int[]{
				0+offset,1+offset,3+offset,	// Back face
				3+offset,1+offset,2+offset,	// Back face
				
				4+offset,5+offset,7+offset, // Front face
				7+offset,5+offset,6+offset, // Front face
				
				8+offset,9+offset,11+offset, // Right face
				11+offset,9+offset,10+offset, // Right face
				
				12+offset,13+offset,15+offset, // Left face
				15+offset,13+offset,14+offset, // Left face
				
				16+offset,17+offset,19+offset, // Top face
				19+offset,17+offset,18+offset, // Top face
				
				20+offset,21+offset,23+offset, // Bottom face
				23+offset,21+offset,22+offset // Bottom face
	    });
	    return 24;
	}
	
	/**
	 * Number of vertices in the mesh
	 * @return
	 */
	public int getVerticesCount(){
		return numberOfVertices;
	}
	
	/**
	 * Creates vertices and indices for a mesh
	 * @return
	 */
	public FloatBuffer createVerticesAsBuffer(){
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		indicesData = BufferUtils.createIntBuffer(worldSize*worldSize*worldSize*36);
		int i = 0;
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	            	if(world[x][y][z] == BlockType.Grass)
	            	{
	                    i += putVertices(x*cubeSize, y*cubeSize, -z*cubeSize, i);
	            	}
	            }
	        }
	    }
		numberOfVertices = i;
		vertexPositionData.flip();
		indicesData.flip();
		return vertexPositionData;
	}
}
