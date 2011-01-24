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
		System.out.println(Parameters.day + "\t" + hostPop.getN() + "\t" + hostPop.getS() + "\t" + hostPop.getI() + "\t"+  hostPop.getR() + "\t" + hostPop.getCases());
	}
	
	public void stepForward() {
			
		hostPop.stepForward();
		Parameters.day++;
		
	}
	
	public void run() {
	
		try {
		
			File seriesFile = new File("out.timeseries");		
			seriesFile.delete();
			seriesFile.createNewFile();
			PrintStream seriesStream = new PrintStream(seriesFile);
			System.out.println("day\tN\tS\tI\tR\tcases");
			seriesStream.println("time\tN\tS\tI\tR\tcases\tdiversity");
							
			for (int i = 0; i < Parameters.endDay; i++) {
				stepForward();
				printState();
				hostPop.printState(seriesStream);
				if (hostPop.getI()==0) {
					if (Parameters.repeatSim) {
						reset();
						i = 0; 
						seriesFile.delete();
						seriesFile.createNewFile();
						seriesStream = new PrintStream(seriesFile);
						seriesStream.println("time\tN\tS\tI\tR\tcases\tdiversity");	
					} else {
						break;
					}
				}
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
	
	public void reset() {
		hostPop = new HostPopulation();
		VirusSample.clear();
		Parameters.day = 0;
	}

}