package World;

public class WorldData {
	
	public int worldSize;
	
	Biome[][][] world;

	public WorldData (int worldSize){
		this.worldSize = worldSize;
		generateWorld();
	}
	
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
}
