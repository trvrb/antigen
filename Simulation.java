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
		
		// check to see if infected host population exists...
	
		hostPop.resetCases();
		if (Parameters.swapDemography) {
			hostPop.swap();
		} else {
			hostPop.grow();
			hostPop.decline();
		}
		hostPop.contact();
		hostPop.recover();
		if (Parameters.transcendental) { 
			hostPop.loseImmunity(); 
		}
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
			System.out.println("day\tN\tS\tI\tR\tcases");
			seriesStream.println("time\tN\tS\tI\tR\tcases\tdiversity");
			
//			File immunityFile = new File("out.immunity");	
//			immunityFile.delete();
//			immunityFile.createNewFile();	
//			PrintStream immunityStream = new PrintStream(immunityFile);			
			
			for (int i = 0; i < Parameters.endDay; i++) {
				stepForward();
				printState();
				hostPop.printState(seriesStream);
//				hostPop.printImmunity(immunityStream);
				if (hostPop.getI()==0) {
					if (Parameters.repeatSim) {
						reset();
						i = 0; 
						seriesFile.delete();
						seriesFile.createNewFile();
						seriesStream = new PrintStream(seriesFile);
						seriesStream.println("time\tN\tS\tI\tR\tcases\tdiversity");	
//						immunityFile.delete();
//						immunityFile.createNewFile();	
//						immunityStream = new PrintStream(immunityFile);
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