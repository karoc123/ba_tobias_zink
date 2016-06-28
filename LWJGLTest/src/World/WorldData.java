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
	private float cubeSize = 4.0f/2;
	private int numberOfVertices = 0;
	private int numberOfCubes = 0;
	
	// Rendering Variables
	public int cID, vaoID, vboID;
	
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
		vboID = GL15.glGenBuffers();
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
	
//	/**
//	 * Reads chunk from a chunk file
//	 */
//	private void readChunkFromFile(){
//		world[0][0][1] = BlockType.Grass;
//		world[0][0][2] = BlockType.Grass;
//		world[0][0][3] = BlockType.Grass;
//		world[0][0][4] = BlockType.Grass;
//		world[0][0][5] = BlockType.Grass;
//		world[0][0][6] = BlockType.Grass;
//		world[0][0][7] = BlockType.Grass;
//		world[0][0][8] = BlockType.Grass;
//		world[0][0][9] = BlockType.Grass;
//		world[1][0][0] = BlockType.Grass;
//		world[2][0][0] = BlockType.Grass;
//		world[3][0][0] = BlockType.Grass;
//		world[4][0][0] = BlockType.Grass;
//		world[5][0][0] = BlockType.Grass;
//		world[6][0][0] = BlockType.Grass;
//		world[7][0][0] = BlockType.Grass;
//		world[8][0][0] = BlockType.Grass;
//		world[9][0][0] = BlockType.Grass;
//	}

	/**
	 * Creates cube data and puts it into buffers for one mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 * @return number of vertices added
	 */
	public int putVertices(float tx, float ty, float tz, int offset) {
		// create vertices
	    vertexPositionData.put(new float[]{			  
	    		// Back face
				0 + tx,cubeSize + ty,0 + tz,			//0
				0 + tx,0 + ty,0 + tz,					//1
				cubeSize  + tx,0 + ty,0 + tz,			//2
				cubeSize + tx,cubeSize + ty,0 + tz,		//3
				
				// Front face
				0 + tx,cubeSize + ty,cubeSize + tz,				//4
				0 + tx,0 + ty,cubeSize + tz,					//5
				cubeSize + tx,0 + ty,cubeSize + tz,				//6
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//7
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				
				7+offset,4+offset,0+offset, // Top face
				3+offset,7+offset,0+offset, // Top face
				
				2+offset,5+offset,1+offset, // Bottom face
				2+offset,6+offset,2+offset, // Bottom face
				
				0+offset,1+offset,3+offset,	// Front face
				3+offset,1+offset,2+offset,	// Front face
				
				7+offset,5+offset,4+offset, // Back face
				6+offset,5+offset,7+offset, // Back face
				
				3+offset,7+offset,6+offset, // Right face
				6+offset,2+offset,3+offset, // Right face
				
				0+offset,4+offset,1+offset, // Left face
				1+offset,4+offset,5+offset // Left face

	    });
	    return 8;
	}
	
	/**
	 * Number of vertices in the mesh
	 * @return
	 */
	public int getVerticesCount(){
		return numberOfVertices;
	}
	
	/**
	 * Number of cubes in the mesh
	 * @return
	 */
	public int getNumberOfCubes(){
		return numberOfCubes;
	}
	
	/**
	 * If a cube has "air" on one of the sides, it is visible
	 * TODO: also check other chunks for border cubes
	 * @return
	 */
	private boolean checkIfCubeIsVisible(int x, int y, int z){
		
		//TODO check for other chunks
		if(x == worldSize-1 || x == 0 || y == worldSize-1 || y == 0 || z == worldSize-1 || z == 0)
		{
			return true;
		}
		
		if(x < worldSize-1 && world[x+1][y][z] == BlockType.Nothing)
		{
			return true;
		}
		if(x > 0 && world[x-1][y][z] == BlockType.Nothing)
		{
			return true;
		}
		if(y < worldSize-1 && world[x][y+1][z] == BlockType.Nothing)
		{
			return true;
		}
		if(y > 0 && world[x][y-1][z] == BlockType.Nothing)
		{
			return true;
		}
		if(z < worldSize-1 && world[x][y][z+1] == BlockType.Nothing)
		{
			return true;
		}
		if(z > 0 && world[x][y][z-1] == BlockType.Nothing)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Creates vertices and indices for a mesh
	 * @return
	 */
	public FloatBuffer createVerticesAsBuffer(){
		vertexPositionData = BufferUtils.createFloatBuffer((8*3)*worldSize*worldSize*worldSize);
		indicesData = BufferUtils.createIntBuffer(worldSize*worldSize*worldSize*36);
		numberOfCubes = 0;
		int i = 0;
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	            	numberOfCubes++;
	            	if(world[x][y][z] == BlockType.Grass && checkIfCubeIsVisible(x, y, z))
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
