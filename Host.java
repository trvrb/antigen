/* A human individual that harbors viruses and immunity */

import java.util.*;

public class Host {

	// fields
	private Virus infection;											
//	private List<Phenotype> immuneHistory = new ArrayList<Phenotype>(0);	
	private Phenotype[] immuneHistory = new Phenotype[0];	
	
	// naive host
	public Host() {
		initializeHistory();
	}
	
	// initial infected host
	public Host(Virus v) {
		infection = v;
		initializeHistory();
	}
	
	// sometimes start with immunity	
	public void initializeHistory() {
		double chanceOfSuccess = Parameters.initialPrR;
		if (Random.nextBoolean(chanceOfSuccess)) {
			Phenotype p = PhenotypeFactory.makeHostPhenotype();
			addToHistory(p);
		}	
	}
	
	public void addToHistory(Phenotype p) {
		Phenotype[] newHistory = new Phenotype[immuneHistory.length + 1];
		for (int i = 0; i < immuneHistory.length; i++) {
			newHistory[i] = immuneHistory[i];
		}
		newHistory[immuneHistory.length] = p;
		immuneHistory = newHistory;
	}
	
	// infection methods
	public void reset() {
		infection = null;
		immuneHistory = new Phenotype[0];
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
		addToHistory(p);
		infection = null;
	}
	public int getHistoryLength() {
		return immuneHistory.length;
	}
	
	// make a new virus with the mutated phenotype
	public void mutate() {
		Virus mutV = infection.mutate();
		infection = mutV;
	}
	
	// history methods
//	public List<Phenotype> getHistory() {
	public Phenotype[] getHistory() {
		return immuneHistory;
	}	

}