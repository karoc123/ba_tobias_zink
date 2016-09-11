package de.oth.blocklib.helper;

/**
 * Utility methods needed by the block library.
 * 
 * @see Maths
 */
public final class Utility {
	/** Private constructor because utility method. */
	private Utility() {
	}

	/** Size of a megabyte in kb. */
	public static final int MEGABYTESIZE = 1024;

	/** Size of a millisecond in nanoseconds. */
	public static final int MILLISECONDSIZE = 1000000;

	/** Size of a second in milliseconds. */
	public static final int SECONDSIZE = 1000;

	/**
	 * Get the time in milliseconds.
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTimeInMilliseconds() {
		return System.nanoTime() / MILLISECONDSIZE;
	}
}
