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
//		System.out.println("Day " + Parameters.day);
//		System.out.println("N: " + hostPop.getN());
//		System.out.println("S: " + hostPop.getS());
//		System.out.println("I: " + hostPop.getI());		
//		System.out.println();	
//		System.out.println(Parameters.day + "\t" + hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI());
		hostPop.printPhenotypes();
	}
	
	public void stepForward() {
		
		// check to see if host population exists...
	
		hostPop.grow();
		hostPop.decline();
		hostPop.contact();
		hostPop.recover();
		hostPop.mutate();
		Parameters.day++;
		
	}

}