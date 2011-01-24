/* Simulation functions, holds the host population */

import java.util.*;
import java.io.*;

public class Simulation {

	// fields
	private List<HostPopulation> demes = new ArrayList<HostPopulation>();
	private double diversity;
	
	// constructor
	public Simulation() {
		Virus urVirus = new Virus();
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = new HostPopulation(urVirus);
			demes.add(hp);
		}
	}
	
	// methods
	
	public int getN() {
		int count = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			count += hp.getN();
		}
		return count;
	}
	
	public int getS() {
		int count = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			count += hp.getS();
		}
		return count;
	}	
	
	public int getI() {
		int count = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			count += hp.getI();
		}
		return count;
	}	
	
	public int getR() {
		int count = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			count += hp.getR();
		}
		return count;
	}		
	
	public int getCases() {
		int count = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			count += hp.getCases();
		}
		return count;
	}	
	
	public double getDiversity() {
		return diversity;
	}		
	
	// proportional to infecteds in each deme
	public int getRandomDeme() {
		int n = Random.nextInt(0,getN()-1);
		int d = 0;
		int target = (demes.get(0)).getN();
		while (n < target) {
			d += 1;
			target += (demes.get(d)).getN();
		}
		return d;
	}
	
	// return random virus proportional to worldwide prevalence
	public Virus getRandomInfection() {
	
		Virus v = null;
		
		if (getI() > 0) {
	
			// get deme proportional to prevalence
			int n = Random.nextInt(0,getI()-1);
			int d = 0;
			int target = (demes.get(0)).getI();
			while (d < Parameters.demeCount) {
				if (n < target) {
					break;
				} else {
					d++;
					target += (demes.get(d)).getI();
				}	
			}
			HostPopulation hp = demes.get(d);
					
			// return random infection from this deme
			if (hp.getI()>0) {
				Host h = hp.getRandomHostI();
				v = h.getInfection();
			}
		
		}
		
		return v;
		
	}
	
	public void makeTrunk() {
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			hp.makeTrunk();
		}
	}	
	
	public void printState() {
		System.out.printf("%d\t%d\t%d\t%d\t%d\t%d\t%.3f\n", Parameters.day, getN(), getS(), getI(), getR(), getCases(), getDiversity());
	}
	
	public void printState(PrintStream stream) {
		if (Parameters.day > Parameters.burnin) {
			stream.printf("%.4f\t%d\t%d\t%d\t%d\t%d\t%.4f\n", Parameters.getDate(), getN(), getS(), getI(), getR(), getCases(), getDiversity());
		}
	}	
	
	public void updateDiversity() {
		diversity = 0.0;
		int sampleCount = Parameters.diversitySamplingCount;
		for (int i = 0; i < sampleCount; i++) {
			Virus vA = getRandomInfection();
			Virus vB = getRandomInfection();
			if (vA != null && vB != null) {
				diversity += vA.distance(vB);
			}
		}	
		diversity /= (double) sampleCount;
	}	
		
	public void stepForward() {
			
		for (int i = 0; i < Parameters.demeCount; i++) {		
			HostPopulation hp = demes.get(i);
			hp.stepForward();
		}
		updateDiversity();
		Parameters.day++;
		
	}
	
	public void run() {
	
		try {
		
			File seriesFile = new File("out.timeseries");		
			seriesFile.delete();
			seriesFile.createNewFile();
			PrintStream seriesStream = new PrintStream(seriesFile);
			System.out.println("day\tN\tS\tI\tR\tcases\tdiversity");
			seriesStream.println("time\tN\tS\tI\tR\tcases\tdiversity");
							
			for (int i = 0; i < Parameters.endDay; i++) {
				stepForward();
				printState();
				printState(seriesStream);
				if (getI()==0) {
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
	
		makeTrunk();
		VirusSample.printTips();
		VirusSample.printPaths();
		
	}
	
	public void reset() {
		demes.clear();
		Virus urVirus = new Virus();
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = new HostPopulation(urVirus);
			demes.add(hp);
		}
		VirusSample.clear();
		Parameters.day = 0;
	}

}