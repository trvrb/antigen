/* A human individual that harbors viruses and immunity */

import java.util.*;

public class Host {

	// fields
	private Virus infection;											
	private List<Phenotype> immuneHistory = new ArrayList<Phenotype>();	
	// location
	
	// naive host
	public Host() {

	}
	// initial infected host
	public Host(Virus v) {
		infection = v;
	}
	
	// infection methods
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
		infection = nV;
	}
	public void clearInfection() {
		Phenotype p = infection.getPhenotype();
		immuneHistory.add(p);
		infection = null;
	}
	
	// history methods
	public List<Phenotype> getHistory() {
		return immuneHistory;
	}
	

}