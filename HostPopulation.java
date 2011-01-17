/* A population of host individuals */

import java.util.*;

public class HostPopulation {

	// fields
	private List<Host> pop;
	private int popSize = 100;
	private static final double BIRTH_RATE = 0.1;			// births per individual per day
	private static final double DEATH_RATE = 0.1;			// deaths per individual per day 
	
	// constructors
	public HostPopulation() {
		pop = new ArrayList<Host>(popSize);
	}
	public HostPopulation(int s) {
		popSize = s;
		pop = new ArrayList<Host>(popSize);
	}	
	
	// methods
	public int getPopSize() {
		return popSize;
	}

	// draw a Poisson distributed number of births and add these hosts to the population
	public void grow() {
	
	}

}