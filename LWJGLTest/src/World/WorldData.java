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

import renderer.Loader;
import textures.ModelTexture;

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
	public int vaoID, vboID;
	public int vertexPosDataVboID, textureCoordsDataVboID, indicesDataVboID;
	
	// Buffer for render process
	public FloatBuffer vertexPositionData, colorPositionData, textureCoords;
	public IntBuffer indicesData;
	
	// List of Buffers
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	// World array
	BlockType[][][] world;
	
	// instance of loader
	Loader loader;
	public ModelTexture texture;

	public WorldData (int worldSize, Loader loader, ModelTexture texture){
		this.worldSize = worldSize;
		this.loader = loader;
		this.texture = texture;
		generateWorld();
		//readChunkFromFile();
		
		// Create Buffers
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		textureCoords = BufferUtils.createFloatBuffer((48)*worldSize*worldSize*worldSize);
		indicesData = BufferUtils.createIntBuffer(worldSize*worldSize*worldSize*36);
		
		createVerticesAsBuffer();
		initMesh();
	}
	
	/**
	 * Change the type of a specific block
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param block type
	 */
	public void changeBlock (int x, int y, int z, BlockType block){
		world[x][y][z] = block;
		System.out.println(x + " " + y + " "+ z +"");
	}
	
	/**
	 * Recreates the whole mesh, after updates to the world
	 */
	public void recreateMesh(){
		createVerticesAsBuffer();
		updateMesh();
	}
	
//	private void storeDataInAttributeList(int attributeNumber, FloatBuffer buffer){
//		vboID = GL15.glGenBuffers();
//		vbos.add(vboID); //save buffers to delete after
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
//		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
//		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
//		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//	}

	/**
	 * stores data in glBufferData
	 * @param attributeNumber
	 * @param coordinateSize
	 * @param data
	 */
	private int storeDataInAttributeList(int attributeNumber, int coordinateSize, FloatBuffer buffer){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID); //save buffers to delete after
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID); //save buffers to delete after
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Loads updated buffers onto gl
	 */
	private void updateMesh(){
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesData, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexPosDataVboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexPositionData, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureCoordsDataVboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoords, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void initMesh() {
		vaoID = createVAO();
		indicesDataVboID = storeIndicesDataInVBO();
		vertexPosDataVboID = storeDataInAttributeList(0, 3, vertexPositionData);
		textureCoordsDataVboID = storeDataInAttributeList(1, 2, textureCoords);
		
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

	private int storeIndicesDataInVBO() {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID); //save buffers to delete after
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesData, GL15.GL_STATIC_DRAW);
		return vboID;
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
		
//		world[4][4][4] = BlockType.Nothing;
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
				0 + tx,cubeSize + ty,0 + tz,	//0
				0 + tx,0 + ty,0 + tz,			//1
				cubeSize  + tx,0 + ty,0 + tz,	//2
				cubeSize + tx,cubeSize + ty,0 + tz,		//3
				
				// Front face
				0 + tx,cubeSize + ty,cubeSize + tz,		//4
				0 + tx,0 + ty,cubeSize + tz,	//5
				cubeSize + tx,0 + ty,cubeSize + tz,		//6
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//7
				
				// Right face
				cubeSize + tx,cubeSize + ty,0 + tz,		//8
				cubeSize + tx,0 + ty,0 + tz,	//9
				cubeSize + tx,0 + ty,cubeSize + tz,		//10
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//11
				
				// Left face
				0 + tx,cubeSize + ty,0 + tz,	//12
				0 + tx,0 + ty,0 + tz,	//13
				0 + tx,0 + ty,cubeSize + tz,	//14
				0 + tx,cubeSize + ty,cubeSize + tz,		//15
				
				// Top face
				0 + tx,cubeSize + ty,cubeSize + tz,		//16
				0 + tx,cubeSize + ty,0 + tz,	//17
				cubeSize + tx,cubeSize + ty,0 + tz,		//18
				cubeSize + tx,cubeSize + ty,cubeSize + tz,		//19
				
				// Bottom face
				0 + tx,0 + ty,cubeSize + tz,	//20
				0 + tx,0 + ty,0 + tz,	//21
				cubeSize + tx,0 + ty,0 + tz,	//22
				cubeSize + tx,0 + ty,cubeSize + tz		//23
	    });
	    
	    // create triangles with vertices
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
	    // create texture coordinates
	    textureCoords.put(new float[]{
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1,

	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1,
	    		
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1,
	    		
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1,
	    		
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1,
	    		
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1
	    });
	    return 24;
	}

	/**
	 * Creates cube data and puts it into buffers for one mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 * @return number of vertices added
	 */
	public int putVertices2(float tx, float ty, float tz, int offset) {
		// create vertices
		vertexPositionData.put(new float[]{
			// Back face
			0 + tx,cubeSize + ty,0 + tz,	//0
			0 + tx,0 + ty,0 + tz,			//1
			cubeSize  + tx,0 + ty,0 + tz,	//2
			cubeSize + tx,cubeSize + ty,0 + tz,		//3
			
			// Front face
			0 + tx,cubeSize + ty,cubeSize + tz,		//4
			0 + tx,0 + ty,cubeSize + tz,	//5
			cubeSize + tx,0 + ty,cubeSize + tz,		//6
			cubeSize + tx,cubeSize + ty,cubeSize + tz,		//7
			
			// Right face
			cubeSize + tx,cubeSize + ty,0 + tz,		//8
			cubeSize + tx,0 + ty,0 + tz,	//9
			cubeSize + tx,0 + ty,cubeSize + tz,		//10
			cubeSize + tx,cubeSize + ty,cubeSize + tz,		//11
			
			// Left face
			0 + tx,cubeSize + ty,0 + tz,	//12
			0 + tx,0 + ty,0 + tz,	//13
			0 + tx,0 + ty,cubeSize + tz,	//14
			0 + tx,cubeSize + ty,cubeSize + tz,		//15
			
			// Top face
			0 + tx,cubeSize + ty,cubeSize + tz,		//16
			0 + tx,cubeSize + ty,0 + tz,	//17
			cubeSize + tx,cubeSize + ty,0 + tz,		//18
			cubeSize + tx,cubeSize + ty,cubeSize + tz,		//19
			
			// Bottom face
			0 + tx,0 + ty,cubeSize + tz,	//20
			0 + tx,0 + ty,0 + tz,	//21
			cubeSize + tx,0 + ty,0 + tz,	//22
			cubeSize + tx,0 + ty,cubeSize + tz		//23
	    });
	    // create triangles with vertices
	    indicesData.put(new int[]{
				
				7+offset,4+offset,0+offset, // Top face
				3+offset,7+offset,0+offset, // Top face
				
				1+offset,2+offset,5+offset, // Bottom face
				5+offset,6+offset,2+offset, // Bottom face
				
				0+offset,1+offset,3+offset,	// Front face
				3+offset,1+offset,2+offset,	// Front face
				
				7+offset,5+offset,4+offset, // Back face
				6+offset,5+offset,7+offset, // Back face
				
				3+offset,7+offset,6+offset, // Right face
				6+offset,2+offset,3+offset, // Right face
				
				0+offset,4+offset,1+offset, // Left face
				1+offset,4+offset,5+offset // Left face

	    });
	    
	    // create texture coordinates
	    textureCoords.put(new float[]{
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1
	    });
	    return 24;
	}
	
	/**
	 * indices
	 */
	/**
	 * Creates cube data and puts it into buffers for one mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 * @return number of vertices added
	 */
	public int putVertices3(float tx, float ty, float tz, int offset) {
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
				
				1+offset,2+offset,5+offset, // Bottom face
				5+offset,6+offset,2+offset, // Bottom face
				
				0+offset,1+offset,3+offset,	// Front face
				3+offset,1+offset,2+offset,	// Front face
				
				7+offset,5+offset,4+offset, // Back face
				6+offset,5+offset,7+offset, // Back face
				
				3+offset,7+offset,6+offset, // Right face
				6+offset,2+offset,3+offset, // Right face
				
				0+offset,4+offset,1+offset, // Left face
				1+offset,4+offset,5+offset // Left face

	    });
	    
	    // create texture coordinates
	    textureCoords.put(new float[]{
	    		0, 0,
	    		1, 0,
	    		1, 1,
	    		0, 1
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
		vertexPositionData.clear();
		textureCoords.clear();
		indicesData.clear();
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
		textureCoords.flip();
		return vertexPositionData;
	}
}
