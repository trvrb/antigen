/* A population of host individuals */

import java.util.*;

public class HostPopulation {

	// fields
	private List<Host> population = new ArrayList<Host>();
	
	// constructors
	public HostPopulation() {
		// fill population with Host objects
		for (int i = 0; i < Parameters.INIT_POP; i++) {
			Host h = new Host();
			population.add(h);
		}
	}
	
	// methods
	public int getPopSize() {
		return population.size();
	}
	
	// draw a Poisson distributed number of births and add these hosts to the end of the population list
	public void grow() {
		double totalBirthRate = getPopSize() * Parameters.BIRTH_RATE;
		int births = Random.nextPoisson(totalBirthRate);
		for (int i = 0; i < births; i++) {
			Host h = new Host();
			population.add(h);
		}
	}
	
	// draw a Poisson distributed number of deaths and remove random hosts from the population list
	public void decline() {
		if (getPopSize()>0) {
			double totalDeathRate = getPopSize() * Parameters.DEATH_RATE;
			int deaths = Random.nextPoisson(totalDeathRate);
			for (int i = 0; i < deaths; i++) {
				int index = Random.nextInt(0,getPopSize()-1);
				population.remove(index);
			}		
		}
	}

}