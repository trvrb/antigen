/* Stores a list of Viruses that have sampled during the course of the simulation */

import java.util.*;

public class VirusSample {

	// fields
	private static List<Virus> sample = new ArrayList<Virus>();
		
	// static methods
	public static void add(Virus v) {
		sample.add(v);
	}

}