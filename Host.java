/* A human individual that harbors viruses and immunity */

public class Host {

	// fields
	private Virus infection;	// if infection != null then this individual is infected
	// immuneHistory
	// location
	
	// naive host
	public Host() {

	}
	// initial infected host
	public Host(Virus v) {
	//	infected = true;
		infection = v;
	}
	
	// methods
	public boolean isInfected() {
		boolean infected = false;
		if (infection != null) {
			infected = true;
		}
		return infected;
	}
	public Virus getInfection() {
		return infection;
	}
	public void infect(Virus pV) {
		Virus nV = new Virus(pV);
	}
	public void clearInfection() {
		infection = null;
	}
	

}