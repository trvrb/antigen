/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int burnin = 0; // 3000
	public static final int endDay = 1000; // 4825
	public static final double samplingRate = 0.001;		// in samples per individual per day
//	public static final int diversityCount = 10;			// how many samples to draw to check diversity

	// host parameters
	public static final int initialN = 10000;				// in individuals
	public static final double birthRate = 0.0000913242;	// in births per individual per day, 1/30 years = 1/(30*365)
	public static final double deathRate = 0.0000913242;	// in deaths per individual per day, 1/30 years = 1/(30*365)
	
	// epidemiological parameters
	public static final int initialI = 1;					// in individuals	
	public static final double beta = 0.5;					// in contacts per individual per day
	public static final double nu = 0.25;					// in recoveries per individual per day
	
	// evolution parameters
	public static final double muPhenotype = 0.05;			// in mutations per individual per day
	public static final double lowerPhenotype = -0.1;		// lower limit to how far a mutation moves in phenotype space
	public static final double upperPhenotype = 0.1;		// upper limit to how far a mutation moves in phenotype space

}