/* Implements an individual-based model in which the infection's genealogical history is tracked through time */

class TreeSim {
    public static void main(String[] args) {

		// initialize simulation
		Simulation sim = new Simulation();
				
		for (int i = 0; i < 100; i++) {
			sim.printState();
			sim.stepForward();
		}	
		
		
		
	}
   	
}

