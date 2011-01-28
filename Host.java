/* A human individual that harbors viruses and immunity */

import java.util.*;

public class Host {

	// fields
	private Virus infection;											
	private List<Phenotype> immuneHistory = new ArrayList<Phenotype>();	
	
	// naive host
	public Host() {
	}
	
	// initial infected host
	public Host(Virus v) {
		infection = v;
	}
	
	// infection methods
	public void reset() {
		infection = null;
		immuneHistory.clear();
	}
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
	public void infect(Virus pV, int d) {
		Virus nV = new Virus(pV, d);
		infection = nV;
	}
	public void clearInfection() {
		Phenotype p = infection.getPhenotype();
		immuneHistory.add(p);
		infection = null;
	}

	// make a new virus with the mutated phenotype
	public void mutate() {
		Virus mutV = infection.mutate();
		infection = mutV;
	}
	
	// history methods
	public List<Phenotype> getHistory() {
		return immuneHistory;
	}
	public Phenotype getRandomImmunity() {
		Phenotype p = null;
		if (immuneHistory.size() > 0) {
			int index = Random.nextInt(0,immuneHistory.size()-1);
			p = immuneHistory.get(index);
		} 
		return p;
	}
	

}