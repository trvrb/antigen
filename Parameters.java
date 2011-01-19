/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

public class Parameters {

	// simulation parameters
	public static int day = 0;
	public static final int endDay = 100000;

	// host parameters
	public static final int initialN = 10000;				// in individuals
	public static final double birthRate = 0.0000913242;	// in births per individual per day, 1/30 years = 1/(30*365)
	public static final double deathRate = 0.0000913242;	// in deaths per individual per day, 1/30 years = 1/(30*365)
	
	// epidemiological parameters
	public static final int initialI = 10;					// in individuals	
	public static final double beta = 0.5;					// in contacts per individual per day
	public static final double nu = 0.1;					// in recoveries per individual per day
	
	// evolution parameters
	public static final double muPhenotype = 0.01;			// in mutations per individual per day
	public static final double lowerPhenotype = 0.05;		// lower limit to how far a mutation moves in phenotype space
	public static final double upperPhenotype = 0.15;		// upper limit to how far a mutation moves in phenotype space

}