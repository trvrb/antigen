/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int burnin = 0; // 3000
	public static final int endDay = 1000; // 4825
	public static final double tipSamplingRate = 10.0;			// in samples per day
	public static final double pathSamplingProportion = 0.05;
	public static final int	diversitySamplingCount = 100;
	public static final boolean repeatSim = true;				// repeat simulation until endDay is reached?

	// host parameters
	public static final int initialN = 200000;					// in individuals
	public static final double birthRate = 0.000274;			// in births per individual per day, 1/30 years = 1/(10*365)
	public static final double deathRate = 0.000274;			// in deaths per individual per day, 1/30 years = 1/(10*365)
	public static final boolean swapDemography = true;			// whether to keep overall population size constant
		
	// epidemiological parameters
	public static final int initialI = 10;						// in individuals	
	public static final double beta = 0.5;						// in contacts per individual per day
	public static final double nu = 0.25;						// in recoveries per individual per day
	public static final double initialRecovered = 0.5;
	public static final double initialTraitA = -0.25;
	public static final double initialTraitB = -0.25;	
	
	// transcendental immunity
	public static final boolean transcendental = false;
	public static final double immunityLoss = 0.01;				// in R->S per individual per day
	
	// metapopulation parameters
	public static final int demeCount = 3;
//	public static final int[] initialNArray = {100000, 100000, 100000};
//	public static final double betweenDemeContact = 0.01;		// relative to within-deme beta
	
	// evolution parameters
	public static final double muPhenotype = 0.0005;	 // 0.0005			// in mutations per individual per day
	public static final double lowerPhenotype = -0.1;			// lower limit to how far a mutation moves in phenotype space
	public static final double upperPhenotype = 0.1;			// upper limit to how far a mutation moves in phenotype space
	
	// measured in years, starting at burnin
	public static double getDate() {
		return ((double) day - (double) burnin ) / 365.0;
	}

}