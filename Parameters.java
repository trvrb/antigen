/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

import static java.lang.Math.*;

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int burnin = 0; // 0
	public static final int endDay = 17300; // 14600
	public static final int printStep = 10;								// print to out.timeseries every X days 
	public static final double tipSamplingRate = 0.001;					// in samples per deme per day
	public static final int tipSamplesPerDeme = 2000;
	public static final boolean tipSamplingProportional = true;			// whether to sample proportional to prevalance
	public static final double treeProportion = 0.1;					// proportion of tips to use in tree reconstruction
	public static final int	diversitySamplingCount = 100;
	public static final boolean repeatSim = false;						// repeat simulation until endDay is reached?
	public static final boolean immunityReconstruction = false;			// whether to print immunity reconstruction to out.immunity
	public static Virus urVirus = new Virus();

	// metapopulation parameters
	public static final int demeCount = 3;
	public static final String[] demeNames = {"north", "tropics", "south"};
	public static final int[] initialNs = {500000,500000,500000};	

	// host parameters
	public static final double birthRate = 0.000091;			// in births per individual per day, 1/30 years = 0.000091
	public static final double deathRate = 0.000091;			// in deaths per individual per day, 1/30 years = 0.000091
	public static final boolean swapDemography = true;			// whether to keep overall population size constant
		
	// epidemiological parameters
	public static final int initialI = 5;						// in individuals
	public static final double initialPrR = 0.5; 				// as proportion of population
	public static final double beta = 0.4; // 0.36				// in contacts per individual per day
	public static final double nu = 0.2; //0.2					// in recoveries per individual per day
	public static final double betweenDemePro = 0.001;			// relative to within-deme beta	
//	public static final double developImmunityPro = 1;			// after infection, this proportion develop life-long immunity

	// transcendental immunity
	public static final boolean transcendental = false;
	public static final double immunityLoss = 0.01;				// in R->S per individual per day
	public static final double initialPrT = 0.1;
	
	// seasonal betas
	public static final double[] demeBaselines = {1,1,1};
	public static final double[] demeAmplitudes = {0.25,0,0.25};
	public static final double[] demeOffsets = {0,0,0.5};			// relative to the year
	
	// phenotype parameters
	public static final String phenotypeSpace = "epochal";		// options include: "2D", "epochal"
	public static final double muPhenotype = 0.001; //0.00014				// in mutations per individual per day
	public static final double smithConversion = 0.075;			// multiplier to distance to give cross-immunity	
	public static final double initialTraitA = -5.0;
	public static final double initialTraitB = 0.0;	
	
	// parameters specific to Phenotype2D
	// 1.2 units of AG change per year
	// average distance between clusters is 4.5 units
	// cross-immunity between clusters is 0.6 to 0.85
	// 4.5 units = 0.3 distance
	// 1.2 units = 0.08 distance per year
	public static final double muRangeA = 2;					// traitA is adjusted in a uniform manner
	public static final double muRangeB = 0.0;					// traitB is adjusted in a uniform manner	
	public static final boolean quadratic = false;				// cross-immunity function quadratic or linear	
//	public static final double boundaryA = 15;
	
	// parameters specific to PhenotypeEpochal
	public static final double meanStep = 0.9; // 0.6
	public static final boolean mut2D = true;					// whether to mutate in a full 360 degree arc

	// genotype parameters
	public static final int sites = 10;							// number of sequence sites
	public static final double hammingConversion = 0.1;

	// vaccination parameters
	public static final boolean vaccinate = false;
	public static final double vaccinationRate = 0.0027;		// per individual per day
		
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