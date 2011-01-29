/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

import static java.lang.Math.*;

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int burnin = 0; // 0
	public static final int endDay = 2000; // 5475
	public static final double tipSamplingRate = 0.05;				// in samples per deme per day
	public static final double pathSamplingProportion = 1;
	public static final int	diversitySamplingCount = 100;
	public static final boolean repeatSim = false;				// repeat simulation until endDay is reached?
	public static Virus urVirus = new Virus();

	// metapopulation parameters
	public static final int demeCount = 2;
	public static final String[] demeNames = {"north", "tropics", "south"};
	public static final int[] initialNs = {200000,200000};	

	// host parameters
	public static final double birthRate = 0.000091;			// in births per individual per day, 1/30 years = 0.000091
	public static final double deathRate = 0.000091;			// in deaths per individual per day, 1/30 years = 0.000091
	public static final boolean swapDemography = true;			// whether to keep overall population size constant
		
	// epidemiological parameters
	public static final int initialI = 10;						// in individuals
	public static final double initialPrR = 0.5;				// as proportion of popultation
	public static final double beta = 0.5;						// in contacts per individual per day
	public static final double nu = 0.25;						// in recoveries per individual per day
	public static final double betweenDemePro = 0.001;			// relative to within-deme beta	

	// transcendental immunity
	public static final boolean transcendental = false;
	public static final double immunityLoss = 0.01;				// in R->S per individual per day
	
	// seasonal betas
	public static final double[] demeBaselines = {1, 1};
	public static final double[] demeAmplitudes = {0, 0};
	public static final double[] demeOffsets = {0, 0};			// relative to the year
	
	// phenotype parameters
	public static final String phenotypeSpace = "2D";			// options include: "2D"
	public static final double muPhenotype = 0.01;				// in mutations per individual per day
	
	// parameters specific to Phenotype2D
	public static final double muRangeA = 0.1;					// traitA is adjusted in a uniform manner
	public static final double muRangeB = 0;					// traitB is adjusted in a uniform manner	
	public static final double initialTraitA = -0.5;
	public static final double initialTraitB = 0.0;
	public static final boolean quadratic = false;				// cross-immunity function quadratic or linear	
	
	
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