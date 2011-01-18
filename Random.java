/* Holds random number genator necessities */
/* Trying to encapsulate this, so the RNG particulars can be changed if necessary */ 
/* Completely static class, allows no instances to be instantiated */

import cern.jet.random.*;
import java.util.*;

public class Random {

	// initializing Mersenne Twister engine with current date
	private static cern.jet.random.engine.RandomEngine gen = new cern.jet.random.engine.MersenneTwister(new java.util.Date());
		
	// methods

	public static int nextInt(int from, int to) {
		cern.jet.random.AbstractDistribution dist = new cern.jet.random.Uniform(from, to, gen);
		return dist.nextInt();
	}	
	
	public static double nextDouble() {
		cern.jet.random.AbstractDistribution dist = new cern.jet.random.Uniform(gen);
		return dist.nextDouble();
	}
	
	public static int nextPoisson(double lambda) {
		cern.jet.random.AbstractDistribution dist = new cern.jet.random.Poisson(lambda, gen);
		return dist.nextInt();
	}
	
	public static boolean nextBoolean(double p) {
		boolean x = false;
		if (nextDouble() < p) {
			x = true;
		}
		return x;
	}	
	

}