/* Holds random number genator necessities */
/* Trying to encapsulate this, so the RNG particulars can be changed if necessary */ 
/* Completely static class, allows no instances to be instantiated */

import cern.jet.random.*;
import java.util.*;

public class Random {
		
	// methods

	public static int nextInt(int from, int to) {
		return cern.jet.random.Uniform.staticNextIntFromTo(from, to);
	}	
	
	public static double nextDouble() {
		return cern.jet.random.Uniform.staticNextDouble();		
	}
	
	public static double nextDouble(double from, double to) {
		return cern.jet.random.Uniform.staticNextDoubleFromTo(from, to);		
	}	
	
	public static int nextPoisson(double lambda) {
		return cern.jet.random.Poisson.staticNextInt(lambda);
	}
	
	public static boolean nextBoolean(double p) {
		boolean x = false;
		if (nextDouble() < p) {
			x = true;
		}
		return x;
	}	
	

}