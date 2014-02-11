/* Implements an individual-based model in which the infection's genealogical history is tracked through time */

class Antigen {
    public static void main(String[] args) {

		// initialize random number generator
		cern.jet.random.AbstractDistribution.makeDefaultGenerator();
		
		// initialize static parameters
		Parameters.report();
		
		// run simulation
		Simulation sim = new Simulation();
		sim.run();	
		
	}
   	
}

