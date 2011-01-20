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
		System.out.println(Parameters.day + "\t" + hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI() + "\t" + hostPop.getCases());
	}
	
	public void stepForward() {
		
		// check to see if infected host population exists...
	
		hostPop.resetCases();
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
			File seriesFile = new File("out.timeseries");
			seriesFile.delete();
			seriesFile.createNewFile();
			PrintStream seriesStream = new PrintStream(seriesFile);
			System.out.println("day\tN\tS\tI\tcases");
			seriesStream.println("N\tS\tI\tcases\tdiversity");
			
			for (int i = 0; i < Parameters.endDay; i++) {
				stepForward();
				printState();
				if (Parameters.day > Parameters.burnin) {
					seriesStream.println(hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI() + "\t" + hostPop.getCases() + "\t" + hostPop.getDiversity(Parameters.diversitySamplingCount));
				}
				if (hostPop.getI()==0) { break; }
			}
			
			seriesStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}	
	
		hostPop.makeTrunk();
		VirusSample.printTips();
		VirusSample.printPaths();
		
	}

}