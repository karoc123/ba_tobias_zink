package de.oth.blocklib.world;

import static de.oth.blocklib.helper.Log.*;

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

import de.oth.blocklib.Configuration;
import de.oth.blocklib.helper.SimplexNoise;
import de.oth.blocklib.helper.SimplexNoise_octave;
import de.oth.blocklib.helper.Utility;
import de.oth.blocklib.renderer.Loader;
import de.oth.blocklib.textures.ModelTexture;

/**
 * Manages the world array
 */
public class WorldData {
	
	//Random Numbers
	private Random rand = new Random();
	
	//cube size of the world
	public int worldSize;
	private float cubeSize = 2f/2;
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
	
	/** Flag indicates if the world mesh is the same as in the world array. 
	 * If not, recreate before rendering. */
	private boolean isMeshLatest = false;
	
	/** Texture for the World */
	public ModelTexture texture;

	public WorldData (int worldSize, Loader loader, ModelTexture texture){
		this.worldSize = worldSize;
		this.loader = loader;
		this.texture = texture;
		textures.add(texture.getID());
		generateEmptyWorld();
//		fillWorldWithBlocksOctave();
		fillWorldWithBlocksLookLikeWorld();
//		fillWorldWithBlocksFull();
//		fillWorldWithWorstCaseBlocks();
		createVerticesAsBuffer();
		initMesh();
		setMeshLatest(true);
	}
	
	/**
	 * Change the type of a specific block
	 * @param x position
	 * @param y position
	 * @param z position
	 * @param block type
	 */
	public void setBlock (int x, int y, int z, BlockType block){
		world[x][y][z] = block;
		System.out.println(x + " " + y + " "+ z +" now " + block.toString());
		setMeshLatest(false);
	}
	
	public void removeBlock(int x, int y, int z){
		world[x][y][z] = BlockType.Nothing;
		System.out.println(x + " " + y + " "+ z +" removed ");
		setMeshLatest(false);
	}
	
	/**
	 * Recreates the whole mesh, after updates to the world
	 * TODO: HIER IST IRGENDWO EIN BUG :(
	 */
	public void recreateMesh(){
		long time = Utility.getTimeInMilliseconds();
		createVerticesAsBuffer();
		deleteOldMeshFromOpengl();
		initMesh();
		setMeshLatest(true);
		info("mesh recreated: " + (Utility.getTimeInMilliseconds() - time) + "ms");
	}

	/**
	 * stores data in glBufferData
	 * @param attributeNumber Specifies the index of the generic vertex attribute to be modified.
	 * @param coordinateSize Specifies the number of components per generic vertex attribute.
	 * 			Must be 1, 2, 3, 4.
	 * @param buffer Data to put in the vbo
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
	 * call glDeleteBuffer to finally free the memory allocated for the buffers.
	 */
	private void deleteOldMeshFromOpengl(){
		//nothing to do here?
	}
	
	/**
	 * Loads updated buffers onto gl
	 * TODO: remove?
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
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private void ubindVAO(){
		GL30.glBindVertexArray(0);
	}

	/**
	 * Generates a World Array on world creation without any blocks at all.
	 */
	private void generateEmptyWorld() {
		System.out.format("Worldsize Cubes: %,8d%n", 
				worldSize * worldSize * worldSize);
		this.world = new BlockType[worldSize][worldSize][worldSize];
		fillWorldWithNothing();
	}
	
	/**
	 * Deletes every block from the world array.
	 */
	public void fillWorldWithNothing() {		
		//make every block = nothing
		for (int i = 0; i < worldSize; i++) {
			for (int k = 0; k < worldSize; k++) {
				for (int j = 0; j < worldSize; j++) {
					world[i][k][j] = BlockType.Nothing;
				}				
			}
		}

		// recreate mesh to make change visible
		setMeshLatest(false);
	}	
	
	/**
	 * Pseudoworldgeneration.
	 */
	public void fillWorldWithBlocksLookLikeWorld() {
		SimplexNoise sn = new SimplexNoise(100,0.1,5000);
		int cubesInMesh = 0;
		// fills the world with blocks
		for (int i = 0; i < (worldSize-(worldSize/2)); i++) {
			for (int k = 0; k < worldSize; k++) {
				for (int j = 0; j < worldSize; j++) {
					double noise = sn.getNoise(i, k, j);
					if (i == 0){
						world[j][i][k] = BlockType.Dirt;
					} else if (noise < 0.001)
					{
						if(noise > 0.00003){
							world[j][i][k] = BlockType.Stone;
						} else if (noise < -0.001) {
							world[j][i][k] = BlockType.Wall;
						} else {
							world[j][i][k] = BlockType.Dirt;
						}
//						world[j][i][k] = randomBlockType(i, k, j);
						cubesInMesh++;
					}
				}				
			}
		}
		System.out.format("Cubes in mesh: %,8d%n", cubesInMesh);
		// recreate mesh to make change visible
		setMeshLatest(false);
	}
	
	/**
	 * Fills the world array with random blocks.
	 */
	public void fillWorldWithBlocksOctave() {
		SimplexNoise sn = new SimplexNoise(100,0.1,5000);
		int cubesInMesh = 0;
		// fills the world with blocks
		for (int i = 0; i < worldSize; i++) {
			for (int k = 0; k < worldSize; k++) {
				for (int j = 0; j < worldSize; j++) {
					double noise = sn.getNoise(i, k, j);
					if (noise < 0.001)
					{
						if(noise > 0.00003){
							world[j][i][k] = BlockType.Stone;
						} else if (noise < -0.001) {
							world[j][i][k] = BlockType.Wall;
						} else {
							world[j][i][k] = BlockType.Dirt;
						}
//						world[j][i][k] = randomBlockType(i, k, j);
						cubesInMesh++;
					}
				}				
			}
		}
		System.out.format("Cubes in mesh: %,8d%n", cubesInMesh);
		// recreate mesh to make change visible
		setMeshLatest(false);
	}

	/**
	 * Fills the world array with random blocks.
	 */
	public void fillWorldWithBlocksFull() {
		// fills the world with blocks
		for (int i = 0; i < worldSize; i++) {
			for (int k = 0; k < worldSize; k++) {
				for (int j = 0; j < worldSize; j++) {
					world[j][i][k] = randomBlockType(i, k, j);
//					world[j][i][k] = BlockType.Stone;
				}				
			}
		}

		// recreate mesh to make change visible
		setMeshLatest(false);
	}

	/**
	 * Fills the world array with random blocks.
	 */
	public void fillWorldWithWorstCaseBlocks() {
		// fills the world with blocks
		for (int i = 0; i < worldSize; i++) {
			for (int k = 0; k < worldSize; k++) {
				for (int j = 0; j < worldSize; j++) {
//					world[j][i][k] = randomBlockType(i, k, j);
					world[j][i][k] = BlockType.Stone;
					j++;
				}		
				k++;
			}
			i++;
		}

		// recreate mesh to make change visible
		setMeshLatest(false);
	}
	
	/**
	 * Gives back BlockType.Dirt or BlockType.Stone.
	 * TEMP: blocks at the top are Grass
	 * @param i cartesian coordinate: x
	 * @param k cartesian coordinate: y
	 * @param j cartesian coordinate: z
	 * @see BlockType
	 * @return a random block
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
	 * @param sides array containing the values for the cubes
	 * 				order: left, right, top, bottom, before, behind
	 * @return number of vertices added
	 */
	public int putVertices(float tx, float ty, float tz, int offset, BlockType type, boolean[] sides) {
		if (sides[5]){
			offset += addBackSide(tx, ty, tz, offset, type);
		}
		if (sides[4]){
			offset += addFrontSide(tx, ty, tz, offset, type);
		}
		if (sides[1]){
			offset += addRightSide(tx, ty, tz, offset, type);
		}
		if (sides[0]){
			offset += addLeftSide(tx, ty, tz, offset, type);
		}
		if (sides[2]){
			offset += addTopSide(tx, ty, tz, offset, type);
		}
		if (sides[3]){
			offset += addBotSide(tx, ty, tz, offset, type);
		}
	    
	    return offset;
	}

	private int addBotSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Bottom face
				0 + tx,0 + ty,cubeSize + tz,	//20
				0 + tx,0 + ty,0 + tz,	//21
				cubeSize + tx,0 + ty,0 + tz,	//22
				cubeSize + tx,0 + ty,cubeSize + tz		//23
	    });

	    // create triangles with vertices
	    indicesData.put(new int[]{
				0+offset,1+offset,3+offset, // Bottom face
				3+offset,1+offset,2+offset // Bottom face
	    });

	    // create normals
	    normalsData.put(new float[]{
				// Bottom face
				0,0,1,	//20
				0,0,0,	//21
				1,0,0,	//22
				1,0,1		//23
	    });	    

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 2, 0.25f); // Bottom	
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    
	    if(type == BlockType.Stone){ 
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
	}
	
	private int addTopSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Top face
				0 + tx,cubeSize + ty,cubeSize + tz,		//16
				0 + tx,cubeSize + ty,0 + tz,	//17
				cubeSize + tx,cubeSize + ty,0 + tz,		//18
				cubeSize + tx,cubeSize + ty,cubeSize + tz		//19
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				1+offset,0+offset,3+offset, // Top face
				3+offset,2+offset,1+offset // Top face
	    });

	    // create normals
	    normalsData.put(new float[]{
				// Top face
				0,1,1,		//16
				0,1,0,	//17
				1,1,0,		//18
				1,1,1,		//19
	    });	 
	    
	    if(type == BlockType.Grass){
		    addTextureCoordinates(2, 1, 0.25f); // Top	
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    
	    if(type == BlockType.Stone){ 
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
	}
	
	private int addLeftSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Left face
				0 + tx,cubeSize + ty,0 + tz,	//12
				0 + tx,0 + ty,0 + tz,	//13
				0 + tx,0 + ty,cubeSize + tz,	//14
				0 + tx,cubeSize + ty,cubeSize + tz		//15
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				0+offset,1+offset,3+offset, // Left face
				3+offset,1+offset,2+offset // Left face
	    });
	    
	    normalsData.put(new float[]{
				// Left face
				0,1,0,	//12
				0,0,0,	//13
				0,0,1,	//14
				0,1,1,		//15
	    });	 

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 1, 0.25f); // Left
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    
	    if(type == BlockType.Stone){ 
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
	}
	
	private int addRightSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Right face
				cubeSize + tx,cubeSize + ty,0 + tz,		//8
				cubeSize + tx,0 + ty,0 + tz,	//9
				cubeSize + tx,0 + ty,cubeSize + tz,		//10
				cubeSize + tx,cubeSize + ty,cubeSize + tz		//11
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				1+offset,0+offset,3+offset, // Right face
				3+offset,2+offset,1+offset // Right face
	    });
	    
	    normalsData.put(new float[]{
				// Right face
				1,1,0,		//8
				1,0,0,	//9
				1,0,1,		//10
				1,1,1,		//11
	    });	 

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 1, 0.25f); // Right
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    
	    if(type == BlockType.Stone){
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
	}
	
	private int addFrontSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Front face
				0 + tx,cubeSize + ty,cubeSize + tz,		//4
				0 + tx,0 + ty,cubeSize + tz,	//5
				cubeSize + tx,0 + ty,cubeSize + tz,		//6
				cubeSize + tx,cubeSize + ty,cubeSize + tz		//7
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				0+offset,1+offset,2+offset, // Front face
				2+offset,3+offset,0+offset	// Front face
	    });
	    
	    normalsData.put(new float[]{
//				// Front face
				0,1,1,		//4
				0,0,1,	//5
				1,0,1,		//6
				1,1,1,		//7
	    });	 

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 1, 0.25f); // Front	
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    if(type == BlockType.Stone){
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
	}
	
	private int addBackSide(float tx, float ty, float tz, int offset, BlockType type){
		// create vertices
	    vertexPositionData.put(new float[]{			  
				// Back face
				0 + tx,cubeSize + ty,0 + tz,	//0
				0 + tx,0 + ty,0 + tz,			//1
				cubeSize  + tx,0 + ty,0 + tz,	//2
				cubeSize + tx,cubeSize + ty,0 + tz		//3
	    });
	    
	    // create triangles with vertices
	    indicesData.put(new int[]{
				1+offset,0+offset,3+offset,	// Back face
				3+offset,2+offset,1+offset	// Back face
	    });
	    
	    normalsData.put(new float[]{
				// Back face
				0,1,0,	//0
				0,0,0,			//1
				1 ,0,0,	//2
				1,1,0,		//3
	    });	 

	    if(type == BlockType.Grass){
		    addTextureCoordinates(1, 1, 0.25f); // Back
	    }
	    if(type == BlockType.Dirt){
	    	addTextureCoordinates(1, 2, 0.25f);
	    }
	    
	    if(type == BlockType.Stone){
		    addTextureCoordinates(2, 2, 0.25f);
	    }
	    if(type == BlockType.Rock){
	    	addTextureCoordinates(3, 1, 0.25f);
	    }
	    if(type == BlockType.Wall){
	    	addTextureCoordinates(2, 3, 0.25f);
	    }
		return 4;
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
	
	public int getNumberOfVertices(){
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
	 * <b>ATTENTION:</b> changes the values of parameter sides!
	 * TODO: also check other chunks for border cubes
	 * @param sides array containing the values for the cubes
	 * 				order: left, right, top, bottom, before, behind.
	 * 				<b>Gets changed in this method!</b>
	 * @return
	 */
	private boolean checkIfCubeIsVisible(int x, int y, int z, boolean[] sides){
		boolean isVisible = false;
		
		if(x > 0 && world[x-1][y][z] == BlockType.Nothing)
		{
			//Left cube
			isVisible = true;
			sides[0] = true;
		} else
		{
			sides[0] = false;
		}
		if(x < worldSize-1 && world[x+1][y][z] == BlockType.Nothing)
		{
			//Right cube
			isVisible = true;
			sides[1] = true;
		} else 
		{
			sides[1] = false;
		}
		if(y < worldSize-1 && world[x][y+1][z] == BlockType.Nothing)
		{
			//top cube
			isVisible = true;
			sides[2] = true;
		} else
		{
			sides[2] = false;
		}
		if(y > 0 && world[x][y-1][z] == BlockType.Nothing)
		{
			//bottom cube
			isVisible = true;
			sides[3] = true;
		} else
		{
			sides[3] = false;
		}
		if(z > 0 && world[x][y][z-1] == BlockType.Nothing)
		{
			//cube before
			isVisible = true;
			sides[4] = true;
		} else
		{
			sides[4] = false;
		}
		if(z < worldSize-1 && world[x][y][z+1] == BlockType.Nothing)
		{
			//cube behind
			isVisible = true;
			sides[5] = true;
		} else
		{
			sides[5] = false;
		}
		
		// if cube is on a border to the world or the next chunk: add the side
		if(x == worldSize-1)
		{
			isVisible = true;
			sides[1] = true;
		}
		if(x == 0)
		{
			isVisible = true;
			sides[0] = true;
		}
		if(y == worldSize-1)
		{
			isVisible = true;
			sides[2] = true;
		}
		if(y == 0)
		{
			isVisible = true;
			sides[3] = true;
		}
		if(z == worldSize-1)
		{
			isVisible = true;
			sides[5] = true;
		}
		if(z == 0)
		{
			isVisible = true;
			sides[4] = true;
		}
		
		// Optimization is not active
		if(!Configuration.OPTIMIZE) {
			isVisible = true;
			sides[0] = true;
			sides[1] = true;
			sides[2] = true;
			sides[3] = true;
			sides[4] = true;
			sides[5] = true;			
		}
		return isVisible;
	}
	
	/**
	 * To make buffers with the correct size (in relation to the cubes), the
	 * number of cubes to be rendered is calculated.
	 * @return number of cubes to be rendered.
	 */
	private int numberOfCubesToRender() {
		int numberOfCubesToRender = 0;
		boolean[] sides = new boolean[6];
		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {
				for (int z = 0; z < worldSize; z++) {

					if (world[x][y][z] != BlockType.Nothing && checkIfCubeIsVisible(x, y, z, sides)) {
						numberOfCubesToRender++;
					}
				}
			}
		}
		System.out.println("Rendered cubes: " + numberOfCubesToRender);
		return numberOfCubesToRender;
	}
	
	/**
	 * To make buffers with the correct size (in relation to the sides of the cubes), the
	 * number of sides of cubes to be rendered is calculated.
	 * @return number of cubes to be rendered.
	 */
	private int numberOfSidesToRender() {
		int numberOfSidesToRender = 0;
		int numberOfCubesToRender = 0;
		boolean[] sides = new boolean[6];
		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {
				for (int z = 0; z < worldSize; z++) {

					if (world[x][y][z] != BlockType.Nothing && checkIfCubeIsVisible(x, y, z, sides)) {
						numberOfCubesToRender++;
						for (boolean side : sides)
						{
							if (side)
							{
								numberOfSidesToRender++;
							}
						}
					}
				}
			}
		}
		System.out.format("Sides to render: : %,8d%n", numberOfSidesToRender);
		System.out.format("Cubes to render: : %,8d%n", numberOfCubesToRender);
		return numberOfSidesToRender;
	}

	/**
	 * Creates vertices and indices for a mesh
	 * @return
	 */
	public FloatBuffer createVerticesAsBuffer(){
		// Create Buffers
		int numberOfSidesToRender = numberOfSidesToRender();
		vertexPositionData = BufferUtils.createFloatBuffer(4*3*numberOfSidesToRender);
		textureCoords = BufferUtils.createFloatBuffer(8*numberOfSidesToRender);
		indicesData = BufferUtils.createIntBuffer(numberOfSidesToRender*6);
		normalsData = BufferUtils.createFloatBuffer((4*3)*numberOfSidesToRender);
		
		vertexPositionData.clear();
		textureCoords.clear();
		indicesData.clear();
		normalsData.clear();
		
		numberOfCubes = 0;
		int i = 0; //offset
		boolean[] sides = new boolean[6]; //visible sides of the dice
		
		for (int x = 0; x < worldSize; x++) {
	        for (int y = 0; y < worldSize; y++) {
	            for (int z = 0; z < worldSize; z++) {
	            	numberOfCubes++;
	            	if(world[x][y][z] != BlockType.Nothing && checkIfCubeIsVisible(x, y, z, sides))
	            	{
	                    i = putVertices(x*cubeSize, y*cubeSize, -z*cubeSize, i, world[x][y][z], sides);
//	                    System.out.println(i);
	            	}
	            }
	        }
	    }
		System.out.format("Triangles: %,8d%n", i/2);
		System.out.format("Vertices: %,8d%n", i);
		numberOfVertices = i;
		vertexPositionData.flip();
		indicesData.flip();
		textureCoords.flip();
		normalsData.flip();
		return vertexPositionData;
	}

	/**
	 * @return the isMeshLatest
	 */
	public boolean isMeshLatest() {
		return isMeshLatest;
	}

	/**
	 * @param isMeshLatest the isMeshLatest to set
	 */
	public void setMeshLatest(boolean isMeshLatest) {
		this.isMeshLatest = isMeshLatest;
	}
}
