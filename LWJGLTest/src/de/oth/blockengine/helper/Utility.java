package de.oth.blockengine.helper;

/**
 * Some utility methods
 */
public class Utility {
	
	/**
	 * Get the time in milliseconds
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTimeInMilliseconds() {
	    return System.nanoTime() / 1000000;
	}
}
