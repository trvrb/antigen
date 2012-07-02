/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

import static java.lang.Math.*;

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int burnin = 0; // 0
	public static final int endDay = 20*365; // 20*365 = 7300
	public static final int printStep = 30;								// print to out.timeseries every week
	public static final double tipSamplingRate = 0.0002;				// in samples per deme per day
	public static final int tipSamplesPerDeme = 2000;
	public static final boolean tipSamplingProportional = true;			// whether to sample proportional to prevalance
	public static final double treeProportion = 0.1;					// proportion of tips to use in tree reconstruction
	public static final int	diversitySamplingCount = 1000;				// how many samples to draw to calculate diversity
	public static final int	netauSamplingCount = 10000;					// how many samples to draw to calculate Ne*tau	
	public static final double	netauWindow = 30;						// window in days to calculate Ne*tau	
	public static final int	serialIntervalSamplingCount = 10000;		// how many samples to draw to calculate serial interval	
	public static final boolean repeatSim = true;						// repeat simulation until endDay is reached?
	public static final boolean immunityReconstruction = false;			// whether to print immunity reconstruction to out.immunity
	public static final boolean memoryProfiling = false;				// requires -javaagent:classmexer.jar to run
	public static final double yearsFromMK = 5.0;
	public static final boolean pcaSamples = false;						// whether to rotate and flip virus tree
	public static final boolean detailedOutput = false;					// whether to output out.hosts and out.viruses files enabling checkpointing
	public static final boolean restartFromCheckpoint = false;			// whether to load population from out.hosts

	public static Virus urVirus = new Virus();
	public static Phenotype urImmunity = PhenotypeFactory.makeHostPhenotype();

	// metapopulation parameters
	public static final int demeCount = 3;
	public static final String[] demeNames = {"north", "tropics", "south"};
	public static final int[] initialNs = {2500000,2500000,2500000};	
	
	// host parameters
	public static final double birthRate = 0.000091;				// in births per individual per day, 1/30 years = 0.000091
	public static final double deathRate = 0.000091;				// in deaths per individual per day, 1/30 years = 0.000091
	public static final boolean swapDemography = true;				// whether to keep overall population size constant
		
	// epidemiological parameters
	public static final int initialI = 10;							// in individuals
	public static final double initialPrR = 0.5; 					// as proportion of population
	public static final double beta = 0.36; // 0.3					// in contacts per individual per day
	public static final double nu = 0.2; //0.2						// in recoveries per individual per day
	public static final double betweenDemePro = 0.0005;				// relative to within-deme beta	

	// transcendental immunity
	public static final boolean transcendental = false;
	public static final double immunityLoss = 0.01;					// in R->S per individual per day
	public static final double initialPrT = 0.1;
	
	// seasonal betas
	public static final double[] demeBaselines = {1,1,1};
	public static final double[] demeAmplitudes = {0.1,0,0.1};
	public static final double[] demeOffsets = {0,0,0.5};			// relative to the year
	
	// phenotype parameters
	public static final String phenotypeSpace = "geometric";		// options include: "geometric", "geometric3d", "geometric10d"
	public static final double muPhenotype = 0.005; 				// in mutations per individual per day

	// parameters specific to GeometricPhenotype
	public static final double smithConversion = 0.07;				// multiplier to distance to give cross-immunity	
	public static final double initialTraitA = -6;	
	public static final double meanStep = 0.3; 
	public static final double sdStep = 0.3; 
	public static final boolean mut2D = false;						// whether to mutate in a full 360 degree arc
		
	// measured in years, starting at burnin
	public static double getDate() {
		return ((double) day - (double) burnin ) / 365.0;
	}
	
	public static double getSeasonality(int index) {
		double baseline = demeBaselines[index];
		double amplitude = demeAmplitudes[index];
		double offset = demeOffsets[index];
		return baseline + amplitude * Math.cos(2*Math.PI*getDate() + 2*Math.PI*offset);
	}

}