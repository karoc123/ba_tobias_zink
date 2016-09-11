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
	Nothing,
	/** Represents a block of grass.
	 * Sides of dirt and top with green grass. */
	Grass,
	/** Represents a block of dirt on all sides. */
	Dirt,
	/** Represents a block of stone on all sides. */
	Stone,
	/** Represents a block of rock. */
	Rock,
	/** Represents a block of wall. */
	Wall
}
