/* Simulation functions, holds the host population */

import java.io.*;

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
	
		try {
			FileWriter writer = new FileWriter("out.timeseries");
			System.out.println("day\tN\tS\tI");
			writer.write("N\tS\tI\n");
			for (int i = 0; i < Parameters.endDay; i++) {
				printState();
				writer.write(hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI() + "\n");
				stepForward();
				if (hostPop.getI()==0) { break; }
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}	
	
		VirusSample.printTips();
		
	}

}