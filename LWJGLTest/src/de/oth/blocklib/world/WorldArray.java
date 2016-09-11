package de.oth.blocklib.world;

public class WorldArray {
	// World array
	public int worldSize;
	public BlockType[][][] world;

	public WorldArray (int worldSize){
		this.worldSize = worldSize;
//		generateEmptyWorld();
////		fillWorldWithBlocksOctave();
//		fillWorldWithBlocksLookLikeWorld();
////		fillWorldWithBlocksFull();
////		fillWorldWithWorstCaseBlocks();
//		setMeshLatest(true);
	}
}
