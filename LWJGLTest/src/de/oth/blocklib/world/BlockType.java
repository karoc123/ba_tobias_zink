package de.oth.blocklib.world;

import de.oth.blocklib.models.TexturedModel;

/**
 * Representation of the type of a block in a world array.
 * If a block is added, textures, world and worldmesh needs a reconfiguration.
 * @see WorldMesh
 * @see World
 * @see TexturedModel
 */
public enum BlockType {
	/** No block present (= air). */
	NOTHING,
	/** Represents a block of grass.
	 * Sides of dirt and top with green grass. */
	GRASS,
	/** Represents a block of dirt on all sides. */
	DIRT,
	/** Represents a block of stone on all sides. */
	STONE,
	/** Represents a block of lava. */
	LAVA,
	/** Represents a block of wall. */
	WALL,
	/** Represents a block of gold. */
	GOLD
}
