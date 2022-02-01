/* Simulation functions, holds the host population */

import java.util.*;
import java.io.*;

import com.javamex.classmexer.*;

public class Simulation {

	// fields
	private List<HostPopulation> demes = new ArrayList<HostPopulation>();
	private double diversity;
	private double tmrca;
	private double netau;
	private double serialInterval;
	private double antigenicDiversity;	
	
	private List<Double> diversityList = new ArrayList<Double>();
	private List<Double> tmrcaList = new ArrayList<Double>();	
	private List<Double> netauList = new ArrayList<Double>();		
	private List<Double> serialIntervalList = new ArrayList<Double>();		
	private List<Double> antigenicDiversityList = new ArrayList<Double>();
	private List<Double> nList = new ArrayList<Double>();
	private List<Double> sList = new ArrayList<Double>();	
	private List<Double> iList = new ArrayList<Double>();	
	private List<Double> rList = new ArrayList<Double>();		
	private List<Double> casesList = new ArrayList<Double>();			
	
	// constructor
	public Simulation() {
		for (int i = 0; i < Parameters.demeCount; i++) {
			if (Parameters.restartFromCheckpoint) {
				HostPopulation hp = new HostPopulation(i, true);
				demes.add(hp);
			}
			else {
				HostPopulation hp = new HostPopulation(i);
				demes.add(hp);
			}
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
	
	public double getNetau() {
		return netau;
	}	
	
	public double getTmrca() {
		return tmrca;
	}	
	
	public double getSerialInterval() {
		return serialInterval;	
	}		
	
	public double getAntigenicDiversity() {
		return antigenicDiversity;
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
	
	// return random host from random deme
	public Host getRandomHost() {
		int d = Random.nextInt(0,Parameters.demeCount-1);
		HostPopulation hp = demes.get(d);
		return hp.getRandomHost();
	}
	
	public double getAverageRisk(Phenotype p) {
		
		double averageRisk = 0;
		for (int i = 0; i < 10000; i++) {
			Host h = getRandomHost();
			Phenotype[] history = h.getHistory();
			averageRisk += p.riskOfInfection(history);
		}
		averageRisk /= 10000.0;
		return averageRisk;
		
	}
		
	public void printImmunity() {
	
		try {
			File immunityFile = new File("out.immunity");
			immunityFile.delete();
			immunityFile.createNewFile();
			PrintStream immunityStream = new PrintStream(immunityFile);
			
			for (double x = VirusTree.xMin; x <= VirusTree.xMax; x += 0.5) {
				for (double y = VirusTree.yMin; y <= VirusTree.yMax; y += 0.5) {
				
					Phenotype p = PhenotypeFactory.makeArbitaryPhenotype(x,y);
					double risk = getAverageRisk(p);
					immunityStream.printf("%.4f,", risk);
				
				}
				immunityStream.println();
			}
			
			immunityStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
	
	}
	
	public void printHostPopulation() {
	
		try {
			File hostFile = new File("out.hosts");
			hostFile.delete();
			hostFile.createNewFile();
			PrintStream hostStream = new PrintStream(hostFile);
			for (int i = 0; i < Parameters.demeCount; i++) {
				HostPopulation hp = demes.get(i);
				hp.printHostPopulation(hostStream);
			}
			hostStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
	
	}	
		
	public void makeTrunk() {
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			hp.makeTrunk();
		}
	}	
	
	public void printState() {
	
		System.out.printf("%d\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%d\t%d\t%d\t%d\t%d\n", (int) Parameters.day, getDiversity(), getTmrca(),  getNetau(), getSerialInterval(), getAntigenicDiversity(), getN(), getS(), getI(), getR(), getCases());
		
		if (Parameters.memoryProfiling && Parameters.day % 10 == 0) {
			long noBytes = MemoryUtil.deepMemoryUsageOf(this);
			System.out.println("Total: " + noBytes);
			HostPopulation hp = demes.get(1);
			noBytes = MemoryUtil.deepMemoryUsageOf(hp);
			System.out.println("One host population: " + noBytes);
			Host h = hp.getRandomHostS();
			noBytes = MemoryUtil.deepMemoryUsageOf(h);
			System.out.println("One susceptible host with " +  h.getHistoryLength() + " previous infection: " + noBytes);
			//h.printHistory();
			if (getI() > 0) {
				Virus v = getRandomInfection();
				noBytes = MemoryUtil.memoryUsageOf(v);
				System.out.println("One virus: " + noBytes);
				noBytes = MemoryUtil.deepMemoryUsageOf(VirusTree.getTips());
				System.out.println("Virus tree: " + noBytes);
			}
		}
		
	}

	public void printHeader(PrintStream stream) {
		stream.print("date\tdiversity\ttmrca\tnetau\tserialInterval\tantigenicDiversity\ttotalN\ttotalS\ttotalI\ttotalR\ttotalCases");
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			hp.printHeader(stream);
		}
		stream.println();
	}
	
	public void printState(PrintStream stream) {
		stream.printf("%.4f\t%.4f\t%.4f\t%.4f\t%.5f\t%.4f\t%d\t%d\t%d\t%d\t%d", Parameters.getDate(), getDiversity(), getTmrca(), getNetau(), getSerialInterval(), getAntigenicDiversity(), getN(), getS(), getI(), getR(), getCases());
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			hp.printState(stream);
		}
		stream.println();
	}	
	
	public void printSummary() {
	
		try {
			File summaryFile = new File("out.summary");
			summaryFile.delete();
			summaryFile.createNewFile();
			PrintStream summaryStream = new PrintStream(summaryFile);
			summaryStream.printf("parameter\tfull\n");
			summaryStream.printf("endDate\t%.4f\n", Parameters.getDate());
			summaryStream.printf("diversity\t%.4f\n", mean(diversityList));
			summaryStream.printf("tmrca\t%.4f\n", mean(tmrcaList));
			summaryStream.printf("netau\t%.4f\n", mean(netauList));		
			summaryStream.printf("serialInterval\t%.5f\n", mean(serialIntervalList));	
			summaryStream.printf("antigenicDiversity\t%.4f\n", mean(antigenicDiversityList));	
			summaryStream.printf("N\t%.4f\n", mean(nList));		
			summaryStream.printf("S\t%.4f\n", mean(sList));		
			summaryStream.printf("I\t%.4f\n", mean(iList));		
			summaryStream.printf("R\t%.4f\n", mean(rList));		
			summaryStream.printf("cases\t%.4f\n", mean(casesList));					
			
			summaryStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
	
	}
	
	private double mean(List<Double> list) {
		double mean = 0;
		if(!list.isEmpty()) {
			for (Double item : list) {
				mean += (double) item;
    		}
    	mean /= (double) list.size();
  		}
  		return mean;
	}	
		
	public void updateDiversity() {

		diversity = 0.0;
		tmrca = 0.0;
		antigenicDiversity = 0.0;		
		netau = 0.0;
		serialInterval = 0.0;
		
		double coalCount = 0.0;	
		double coalOpp = 0.0;
		double coalWindow = Parameters.netauWindow / 365.0;
		int sampleCount = Parameters.diversitySamplingCount;
		
		for (int i = 0; i < sampleCount; i++) {
			Virus vA = getRandomInfection();
			Virus vB = getRandomInfection();
			if (vA != null && vB != null) {
				double dist = vA.distance(vB);
				diversity += dist;
				if (dist > tmrca) {
					tmrca = dist;
				}
				antigenicDiversity += vA.antigenicDistance(vB);
				coalOpp += coalWindow;
				coalCount += vA.coalescence(vB, coalWindow);
				serialInterval += vA.serialInterval();
			}
		}	
	
		diversity /= (double) sampleCount;
		tmrca /= 2.0;	
		netau = coalOpp / coalCount;
		serialInterval /= (double) sampleCount;
		antigenicDiversity /= (double) sampleCount;			
		
	}	
	
	public void pushLists() {
		diversityList.add(diversity);
		tmrcaList.add(tmrca);
		netauList.add(netau);
		serialIntervalList.add(serialInterval);
		antigenicDiversityList.add(antigenicDiversity);
		nList.add((double) getN());
		sList.add((double) getS());
		iList.add((double) getI());
		rList.add((double) getR());
		casesList.add((double) getCases());		
	}
				
	public void resetCases() {
		for (int i = 0; i < Parameters.demeCount; i++) {	
			HostPopulation hp = demes.get(i);
			hp.resetCases();
		}
	}
		
	public void stepForward() {
				
		for (int i = 0; i < Parameters.demeCount; i++) {		
			HostPopulation hp = demes.get(i);
			hp.stepForward();
			for (int j = 0; j < Parameters.demeCount; j++) {
				if (i != j) {
					HostPopulation hpOther = demes.get(j);
					hp.betweenDemeContact(hpOther);
				}
			}
		}
							
		Parameters.day += Parameters.deltaT;
		
	}
	
	public void run() {
	
		try {
		
			File seriesFile = new File("out.timeseries");		
			seriesFile.delete();
			seriesFile.createNewFile();
			PrintStream seriesStream = new PrintStream(seriesFile);
			System.out.println("day\tdiversity\ttmrca\tnetau\tserialInterval\tantigenicDiversity\tN\tS\tI\tR\tcases");
			printHeader(seriesStream);
							
			while (Parameters.day < (double) Parameters.endDay) {
									
				if (Parameters.day % (double) Parameters.printStep < Parameters.deltaT) {			
					updateDiversity();
					printState();
					if (Parameters.day > Parameters.burnin) {
						printState(seriesStream);
						pushLists();
					}
					resetCases();
				}
				
				if (getI()==0) {
					if (Parameters.repeatSim) {
						reset();
						seriesFile.delete();
						seriesFile.createNewFile();
						seriesStream = new PrintStream(seriesFile);
						printHeader(seriesStream);
					} else {
						break;
					}
				}
				
				stepForward();				
				
			}
			
			seriesStream.close();

			writeDataCSV();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}

		// tree reduction
		VirusTree.pruneTips();
		VirusTree.markTips();		
		VirusTree.reroot();		
	
		// tree prep
		makeTrunk();
		VirusTree.fillBackward();			
		VirusTree.sortChildrenByDescendants();
		VirusTree.setLayoutByDescendants();
		VirusTree.streamline();			
		
		// rotation
		if (Parameters.pcaSamples) {
			VirusTree.rotate();
			VirusTree.flip();
		}
		
		
		// Summary
		printSummary();		
		VirusTree.printMKSummary();		// appends to out.summary
		

		if (!Parameters.reducedOutput) {	
		
			// tip and tree output	
			VirusTree.printTips();				
			VirusTree.printBranches();	
			VirusTree.printNewick();
					
			// immunity output
			if (Parameters.phenotypeSpace == "geometric") {
				VirusTree.updateRange();
				VirusTree.printRange();
				if (Parameters.immunityReconstruction) {
					printImmunity();
				}
			}		
		
			// detailed output
			if (Parameters.detailedOutput) {
				printHostPopulation();
			}					
			
		}
	}

	public void writeDataCSV() throws FileNotFoundException {
		// Creates csv file from the most recent out.timeseries (i.e., not from example/out.timeseries)
		Scanner input = new Scanner(new File("out.timeseries"));
		PrintStream output = new PrintStream(new File("out_timeseries.csv"));

		// Check for next line
		while(input.hasNextLine()) {
			String line = input.nextLine();
			Scanner lineScan = new Scanner(line);

			// Check for next token
			while(lineScan.hasNext()) {
				String token = lineScan.next();
				if (lineScan.hasNext()) {
					output.print(token + ",");
				} else {
					output.print(token);
				}
			}
			output.println();
		}
	}
	
	public void reset() {
		Parameters.day = 0;
		diversity = 0;
		for (int i = 0; i < Parameters.demeCount; i++) {
			HostPopulation hp = demes.get(i);
			hp.reset();
		}
		VirusTree.clear();
	}
	
}