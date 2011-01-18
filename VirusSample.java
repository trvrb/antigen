/* Stores a list of Viruses that have sampled during the course of the simulation */

public class VirusSample {

	// fields
	private List<Virus> sample = new ArrayList<Virus>();
	
	// constructor
	public VirusSample() {
	
	}
	
	// methods
	public void sample(Virus v) {
		sample.add(v);
	}

}