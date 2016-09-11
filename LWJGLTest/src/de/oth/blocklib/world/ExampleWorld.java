package de.oth.blocklib.world;

import de.oth.blocklib.helper.SimplexNoise;

public class ExampleWorld extends World{

	public ExampleWorld(int worldSize) {
		super(worldSize);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a chessboard
	 */
	public void fillWorldWithChessboard() {
		boolean chess = true;
		for (int i = 0; i < worldSize; i++) {
			for (int j = 0; j < worldSize; j++) {
				if (chess) {
					world[i][0][j] = BlockType.Stone;
					chess = !chess;
				} else {
					world[i][0][j] = BlockType.Dirt;
					chess = !chess;
				}
			}
			chess = !chess;
		}
		setMeshLatest(false);
	}
	
	/**
	 * Fills the world array with random blocks.
	 */
	public void fillWorldWithBlocksOctaveNew() {
		SimplexNoise sn = new SimplexNoise(100,0.1,5001);
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
}
