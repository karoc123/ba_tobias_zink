package de.oth.blocklib.world;

import java.util.Random;

import de.oth.blocklib.helper.SimplexNoise;

/**
 * The world, represented as a array of BlockType.
 * There needs to be a mesh created to render it.
 * @see BlockType
 * @see WorldMesh
 */
public class World {
	//Random Numbers
	private Random rand = new Random();
	
	// World array
	/** size of the world^3 */
	public int worldSize;
	/** array of BlockType with all blocks inside */
	public BlockType[][][] world;
	
	/** Flag indicates if the world mesh is the same as in the world array. 
	 * If not, recreate before rendering. */
	private boolean isMeshLatest = false;

	/**
	 * Generate a empty world.
	 * @param worldSize size of the world.
	 */
	public World (int worldSize){
		this.worldSize = worldSize;
		generateEmptyWorld();
		setMeshLatest(true);
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
					world[i][k][j] = BlockType.NOTHING;
				}				
			}
		}

		// recreate mesh to make change visible
		setMeshLatest(false);
	}	
	
	/**
	 * fake world generation.
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
						world[j][i][k] = BlockType.DIRT;
					} else if (noise < 0.001)
					{
						if(noise > 0.00003){
							world[j][i][k] = BlockType.STONE;
						} else if (noise < -0.001) {
							world[j][i][k] = BlockType.WALL;
						} else {
							world[j][i][k] = BlockType.DIRT;
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
							world[j][i][k] = BlockType.STONE;
						} else if (noise < -0.001) {
							world[j][i][k] = BlockType.WALL;
						} else {
							world[j][i][k] = BlockType.DIRT;
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
//					world[j][i][k] = BlockType.WALL;
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
					world[j][i][k] = BlockType.STONE;
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
				return BlockType.GRASS;
			}
			return BlockType.DIRT;
		} else {
			return BlockType.STONE;
		}
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
	
	/**
	 * Remove a block from the mesh and make it nothing instead.
	 * @param x x coordinate of the block to remove.
	 * @param y y coordinate of the block to remove.
	 * @param z z coordinate of the block to remove.
	 */
	public void removeBlock(int x, int y, int z){
		world[x][y][z] = BlockType.NOTHING;
		System.out.println(x + " " + y + " "+ z +" removed ");
		setMeshLatest(false);
	}
}
