/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

import java.util.*;
import static java.lang.Math.*;
import java.io.*;

public class Parameters {
	
	// global parameters
	public static int day = 0;
	public static Virus urVirus = null;
	public static Phenotype urImmunity = null;		
	
	// simulation parameters
	public static int burnin = 0;
	public static int endDay = 5000; 
	public static int printStep = 10;									// print to out.timeseries every week
	public static double tipSamplingRate = 0.0002;						// in samples per deme per day
	public static int tipSamplesPerDeme = 1000;
	public static boolean tipSamplingProportional = true;				// whether to sample proportional to prevalance
	public static double treeProportion = 0.1;							// proportion of tips to use in tree reconstruction
	public static int diversitySamplingCount = 1000;					// how many samples to draw to calculate diversity
	public static int netauSamplingCount = 1000;						// how many samples to draw to calculate Ne*tau	
	public static int netauWindow = 100;								// window in days to calculate Ne*tau	
	public static int serialIntervalSamplingCount = 1000;				// how many samples to draw to calculate serial interval	
	public static boolean repeatSim = true;								// repeat simulation until endDay is reached?
	public static boolean immunityReconstruction = false;				// whether to print immunity reconstruction to out.immunity
	public static boolean memoryProfiling = false;						// requires -javaagent:classmexer.jar to run
	public static double yearsFromMK = 5.0;
	public static boolean pcaSamples = false;							// whether to rotate and flip virus tree
	public static boolean detailedOutput = false;						// whether to output out.hosts and out.viruses files enabling checkpointing
	public static boolean restartFromCheckpoint = false;				// whether to load population from out.hosts

	// metapopulation parameters
	public static int demeCount = 3;
	public static String[] demeNames = {"north", "tropics", "south"};
	public static int[] initialNs = {1000000,1000000,1000000};	
	
	// host parameters
	public static double birthRate = 0.000091;				// in births per individual per day, 1/30 years = 0.000091
	public static double deathRate = 0.000091;				// in deaths per individual per day, 1/30 years = 0.000091
	public static boolean swapDemography = true;				// whether to keep overall population size constant
		
	// epidemiological parameters
	public static int initialI = 10;							// in individuals
	public static int initialDeme = 2;						// index of deme where infection starts, 1..n
	public static double initialPrR = 0.5; 					// as proportion of population
	public static double beta = 0.36; // 0.3					// in contacts per individual per day
	public static double nu = 0.2; //0.2						// in recoveries per individual per day
	public static double betweenDemePro = 0.0005;				// relative to within-deme beta	

	// transcendental immunity
	public static boolean transcendental = false;
	public static double immunityLoss = 0.01;					// in R->S per individual per day
	public static double initialPrT = 0.1;
	
	// seasonal betas
	public static double[] demeBaselines = {1,1,1};
	public static double[] demeAmplitudes = {0.1,0,0.1};
	public static double[] demeOffsets = {0,0,0.5};				// relative to the year
	
	// phenotype parameters
	public static String phenotypeSpace = "geometric";			// options include: "geometric", "geometric3d", "geometric10d"
	public static double muPhenotype = 0.005; 					// in mutations per individual per day

	// parameters specific to GeometricPhenotype
	public static double smithConversion = 0.1;					// multiplier to distance to give cross-immunity	
	public static double initialTraitA = -6;	
	public static double meanStep = 0.3; 
	public static double sdStep = 0.3; 
	public static boolean mut2D = false;						// whether to mutate in a full 360 degree arc
		
	// measured in years, starting at burnin
	public static double getDate() {
		return ((double) day - (double) burnin ) / 365.0;
	}
	
	public static double getSeasonality(int index) {
		double baseline = demeBaselines[index];
		double amplitude = demeAmplitudes[index];
		double offset = demeOffsets[index];
		double beta = baseline + amplitude * Math.cos(2*Math.PI*getDate() + 2*Math.PI*offset);
		return beta;
	}
		
	// initialize
	public static void initialize() {
		urVirus = new Virus();
		urImmunity = PhenotypeFactory.makeHostPhenotype();	
	}
	
	// load parameters.yml	
	public static void load() {
		
		try {
		
			org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
			Map map = null;			
			InputStream input = new FileInputStream(new File("parameters.yml"));
			map = (Map) yaml.load(input);
			input.close();
			
			System.out.println("Loading parameters from parameters.yml");
		
			burnin = (int) map.get("burnin");
			endDay = (int) map.get("endDay");
			printStep = (int) map.get("printStep");
			tipSamplingRate = (double) map.get("tipSamplingRate");
			tipSamplesPerDeme = (int) map.get("tipSamplesPerDeme");
			tipSamplingProportional = (boolean) map.get("tipSamplingProportional");
			treeProportion = (double) map.get("treeProportion");
			diversitySamplingCount = (int) map.get("diversitySamplingCount");
			netauSamplingCount = (int) map.get("netauSamplingCount");		
			netauWindow = (int) map.get("netauWindow");	
			serialIntervalSamplingCount = (int) map.get("serialIntervalSamplingCount");
			repeatSim = (boolean) map.get("repeatSim");
			immunityReconstruction = (boolean) map.get("immunityReconstruction");
			memoryProfiling = (boolean) map.get("memoryProfiling");
			yearsFromMK = (double) map.get("yearsFromMK");
			pcaSamples = (boolean) map.get("pcaSamples");
			detailedOutput = (boolean) map.get("detailedOutput");
			restartFromCheckpoint = (boolean) map.get("restartFromCheckpoint");
			demeCount = (int) map.get("demeCount");
			demeNames = toStringArray((List<String>) map.get("demeNames"));
			initialNs = toIntArray((List<Integer>) map.get("initialNs"));		
			birthRate = (double) map.get("birthRate");
			deathRate = (double) map.get("deathRate");
			swapDemography = (boolean) map.get("swapDemography");
			initialI = (int) map.get("initialI");
			initialDeme = (int) map.get("initialDeme");
			initialPrR = (double) map.get("initialPrR");
			beta = (double) map.get("beta");
			nu = (double) map.get("nu");
			betweenDemePro = (double) map.get("betweenDemePro");
			transcendental = (boolean) map.get("transcendental");
			immunityLoss = (double) map.get("immunityLoss");
			initialPrT = (double) map.get("initialPrT");
			demeBaselines = toDoubleArray((List<Double>) map.get("demeBaselines"));	
			demeAmplitudes = toDoubleArray((List<Double>) map.get("demeAmplitudes"));
			demeOffsets = toDoubleArray((List<Double>) map.get("demeOffsets"));		
			phenotypeSpace = (String) map.get("phenotypeSpace");
			muPhenotype = (double) map.get("muPhenotype");
			smithConversion = (double) map.get("smithConversion");
			initialTraitA = (double) map.get("initialTraitA");
			meanStep = (double) map.get("meanStep");
			sdStep = (double) map.get("sdStep");
			mut2D = (boolean) map.get("mut2D");			
		
		} catch (IOException e) {
			System.out.println("Cannot load parameters.yml, using defaults");
		}		
	
	}
		
	private static int[] toIntArray(List<Integer> list){
  		int[] ret = new int[list.size()];
  		for (int i = 0; i < ret.length; i++) {
    		ret[i] = list.get(i);
    	}
  		return ret;
	}
	
	private static double[] toDoubleArray(List<Double> list){
  		double[] ret = new double[list.size()];
  		for (int i = 0; i < ret.length; i++) {
    		ret[i] = list.get(i);
    	}
  		return ret;
	}	
	
	private static String[] toStringArray(List<String> list){
  		String[] ret = new String[list.size()];
  		for (int i = 0; i < ret.length; i++) {
    		ret[i] = list.get(i);
    	}
  		return ret;
	}	
	
}