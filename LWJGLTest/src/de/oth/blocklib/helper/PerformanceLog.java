package de.oth.blocklib.helper;

import static de.oth.blocklib.helper.Log.*;
import static org.lwjgl.glfw.GLFW.*;

import de.oth.blocklib.helper.Log.Logger;

/** Prints information about the performance of the library.
 * @see Logger
 */
public final class PerformanceLog {

	/** private constructor. */
	private PerformanceLog() { }
	/**
	 * 
	 */
	public static void printMemoryUsage() {
		// Calculate Memory
		int mb = Utility.MEGABYTESIZE * Utility.MEGABYTESIZE;
		
	    //Getting the runtime reference from system
	    Runtime runtime = Runtime.getRuntime();
	     
	    info("##### Heap utilization statistics [MB] #####");
	     
	    //Print used memory
	    info("Used Memory:"
	            + (runtime.totalMemory() - runtime.freeMemory()) / mb + " mb");
	 
	    //Print free memory
	    info("Free Memory:"
	        + runtime.freeMemory() / mb + " mb");
	     
	    //Print total available memory
	    info("Total Memory:" + runtime.totalMemory() / mb + " mb");
	 
	        //Print Maximum available memory
	    info("Max Memory:" + runtime.maxMemory() / mb + " mb");	
	}
}