/* Simulation functions, holds the host population */

public class Simulation {

	// fields
	private HostPopulation hostPop;
	
	// constructor
	public Simulation() {
		hostPop = new HostPopulation();
	}
	
	// methods
	
	public void printState() {
		System.out.println(Parameters.day + "\t" + hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI());
	}
	
	public void stepForward() {
		
		// check to see if infected host population exists...
	
		hostPop.grow();
		hostPop.decline();
		hostPop.contact();
		hostPop.recover();
		hostPop.mutate();
		hostPop.sample();
		Parameters.day++;
		
	}
	
	public void run() {
		System.out.println("day\tN\tS\tI");
		for (int i = 0; i < Parameters.endDay; i++) {
			printState();
			stepForward();
		}
	}

}