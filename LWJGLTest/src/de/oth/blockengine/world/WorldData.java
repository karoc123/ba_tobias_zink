package de.oth.blockengine.world;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.oth.blockengine.renderer.Loader;
import de.oth.blockengine.textures.ModelTexture;

/**
 * Manages the world array
 */
public class WorldData {
	
	//Random Numbers
	private Random rand = new Random();
	
	//cube size of the world
	public int worldSize;
	private float cubeSize = 4.0f/2;
	private int numberOfVertices = 0;
	private int numberOfCubes = 0;
	
	// Rendering Variables
	public int vaoID, vboID;
	public int vertexPosDataVboID, textureCoordsDataVboID, indicesDataVboID, normalsDataVboID;
	
	// Buffer for render process
	public FloatBuffer vertexPositionData, colorPositionData, textureCoords, normalsData;
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
		
		// Create Buffers
		vertexPositionData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		textureCoords = BufferUtils.createFloatBuffer((48)*worldSize*worldSize*worldSize);
		indicesData = BufferUtils.createIntBuffer(worldSize*worldSize*worldSize*36);
		normalsData = BufferUtils.createFloatBuffer((24*3)*worldSize*worldSize*worldSize);
		
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
		System.out.println(x + " " + y + " "+ z +" now " + block.toString());
	}
	
	/**
	 * Recreates the whole mesh, after updates to the world
	 */
	public void recreateMesh(){
		createVerticesAsBuffer();
		updateMesh();
	}

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
		normalsDataVboID = storeDataInAttributeList(2, 3, normalsData);
		
		ubindVAO();		
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
	 * call after the world is no longer needed
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
		
		//make every block = nothing
		for(int i = 0; i < worldSize; i++){
			for(int k = 0; k< worldSize; k++){
				for(int j = 0; j< worldSize; j++){
					world[i][k][j] = BlockType.Nothing;
				}				
			}
		}
		
		
		for(int i = 0; i < worldSize; i++){
			for(int k = 0; k< worldSize; k++){
				for(int j = 0; j< worldSize; j++){
					world[i][k][j] = randomBlockType(i,k,j);
				}				
			}
		}
	}
	
	/**
	 * Gives back BlockType.Dirt or BlockType.Stone
	 * TEMP: blocks at the top are Grass
	 */
	private BlockType randomBlockType(int i, int k, int j){
		if(rand.nextInt(2) < 1){
			if(k == worldSize-1){
				return BlockType.Grass;
			}
			return BlockType.Dirt;
		} else {
			return BlockType.Stone;
		}
	}

	/**
	 * Creates cube data and puts it into buffers for one mesh
	 * @param tx
	 * @param ty
	 * @param tz
	 * @param offset number of indices
	 * @return number of vertices added
	 */
	public int putVertices(float tx, float ty, float tz, int offset, BlockType type) {
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
				
				4+offset,5+offset,6+offset, // Front face
				6+offset,7+offset,4+offset, // Front face
				
				8+offset,9+offset,11+offset, // Right face
				11+offset,9+offset,10+offset, // Right face
				
				12+offset,13+offset,15+offset, // Left face
				15+offset,13+offset,14+offset, // Left face
				
				16+offset,17+offset,19+offset, // Top face
				19+offset,17+offset,18+offset, // Top face
				
				20+offset,21+offset,23+offset, // Bottom face
				23+offset,21+offset,22+offset // Bottom face
	    });

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 1, 0.5f); // Back
		    addTextureCoordinates(1, 1, 0.5f); // Front
		    addTextureCoordinates(1, 1, 0.5f); // Right
		    
		    addTextureCoordinates(1, 1, 0.5f); // Left
		    addTextureCoordinates(2, 1, 0.5f); // Top
		    addTextureCoordinates(1, 2, 0.5f); // Bottom	
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.5f);
	    	addTextureCoordinates(1, 2, 0.5f);
	    	addTextureCoordinates(1, 2, 0.5f);
	    	
	    	addTextureCoordinates(1, 2, 0.5f);
	    	addTextureCoordinates(1, 2, 0.5f);
	    	addTextureCoordinates(1, 2, 0.5f);
	    }
	    
	    if(type == BlockType.Stone){
		    addTextureCoordinates(2, 2, 0.5f);
		    addTextureCoordinates(2, 2, 0.5f);
		    addTextureCoordinates(2, 2, 0.5f);
		    
		    addTextureCoordinates(2, 2, 0.5f);
		    addTextureCoordinates(2, 2, 0.5f);
		    addTextureCoordinates(2, 2, 0.5f);
	    }
	    
//	    // create normals
//	    normalsData.put(new float[]{
//				// Back face
//				0,1,0,	//0
//				0,0,0,			//1
//				1 ,0,0,	//2
//				1,1,0,		//3
//				
//				// Front face
//				0,1,1,		//4
//				0,0,1,	//5
//				1,0,1,		//6
//				1,1,1,		//7
//				
//				// Right face
//				1,1,0,		//8
//				1,0,0,	//9
//				1,0,1,		//10
//				1,1,1,		//11
//				
//				// Left face
//				0,1,0,	//12
//				0,0,0,	//13
//				0,0,1,	//14
//				0,1,1,		//15
//				
//				// Top face
//				0,1,1,		//16
//				0,1,0,	//17
//				1,1,0,		//18
//				1,1,1,		//19
//				
//				// Bottom face
//				0,0,1,	//20
//				0,0,0,	//21
//				1,0,0,	//22
//				1,0,1		//23
//	    });
	    
	    return 24;
	}
	
	
	/**
	 * Creates the u,v coordinates for a single texture from the atlas
	 * @param row row of the texture
	 * @param column column of the texture
	 * @param steps dimension of the texture (calculate: 1/(textures in one row) )
	 */
	private void addTextureCoordinates(int row, int column, float steps){
	    textureCoords.put(new float[]{
			((column - 1) * steps), ((row - 1) * steps),
			(column * steps), ((row - 1) * steps),
			(column * steps), (row * steps),
			((column - 1) * steps), (row * steps),	
		});
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
	            	if(world[x][y][z] != BlockType.Nothing && checkIfCubeIsVisible(x, y, z))
	            	{
	                    i += putVertices(x*cubeSize, y*cubeSize, -z*cubeSize, i, world[x][y][z]);
	            	}
	            }
	        }
	    }
		numberOfVertices = i;
		vertexPositionData.flip();
		indicesData.flip();
		textureCoords.flip();
		normalsData.flip();
		return vertexPositionData;
	}
}
