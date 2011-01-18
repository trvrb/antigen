/* Stores parameters for use across simulation */
/* Start with parameters in source, implement input file later */
/* A completely static class.  */

public class Parameters {

	// host parameters
	public static final int INIT_POP = 100;				// in individuals
	public static final double BIRTH_RATE = 0.01;		// in births per individual per day, 1/30 years = 1/(30*365)
	public static final double DEATH_RATE = 0.01;		// in deaths per individual per day, 1/30 years = 1/(30*365)
	
	// epidemiological parameters
	public static final double BETA = 0.5;				// in contacts per individual per day

}