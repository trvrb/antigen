/* Implements an individual-based model in which the infection's genealogical history is tracked through time */

class SimTree {
    public static void main(String[] args) {

		// initialize simulation
		Simulation sim = new Simulation();
				
		for (int i = 0; i < Parameters.endDay; i++) {
			sim.printState();
			sim.stepForward();
		}	
		
	}
   	
}

