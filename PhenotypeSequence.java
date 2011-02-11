/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import java.util.*;

public class PhenotypeSequence implements Phenotype {

	public static char randomBase() {
		char base = 'A';
		int rand = Random.nextInt(0,3);
		if (rand == 0) { base = 'A'; }
		else if (rand == 1) { base = 'T'; }
		else if (rand == 2) { base = 'G'; }
		else if (rand == 3) { base = 'C'; }
		return base;
	}

	// fields
	private char[] sequence = new char[Parameters.sites];
			
	// constructor
	public PhenotypeSequence() {
		for (int i = 0; i < Parameters.sites; i++) {
			sequence[i] = 'A';
		}
	}
	
	public PhenotypeSequence(char[] s) {
		for (int i = 0; i < Parameters.sites; i++) {
			sequence[i] = s[i];
		}
	}
		
	public char[] getSequence() {
		return sequence;
	}
		
	// raw genetic distance between two genotypes
	public double distance(Phenotype p) {
		PhenotypeSequence ps = (PhenotypeSequence) p;
		char[] s2 = ps.getSequence();
		double dist = 0.0;
		for (int i = 0; i < Parameters.sites; i++) {
			if (sequence[i] != s2[i]) {
				dist++;
			}
		}
		return dist;
	}
	
	public double riskOfInfection( List<Phenotype> history) {
	
		// find closest phenotype in history
		double closestDistance = 100.0;
		if (history.size()>0) {
			for (int i = 0; i< history.size(); i++) {
				double thisDistance = distance(history.get(i));
				if (thisDistance < closestDistance) {
					closestDistance = thisDistance;
				}
			}
		} 
		
		double risk = closestDistance * Parameters.hammingConversion;
		risk = Math.max(0.0, risk);
		risk = Math.min(1.0, risk);
				
		return risk;
	
	}
	
	// returns a mutated copy, original Phenotype is unharmed
	public Phenotype mutate() {
	
		int randomSite = Random.nextInt(0,Parameters.sites-1);
	
		char[] mutS = new char[Parameters.sites];
		for (int i = 0; i < Parameters.sites; i++) {
			if (i == randomSite) {
				mutS[i] = randomBase();
			} else {
				mutS[i] = sequence[i];
			}
		}
		
		Phenotype mutP = new PhenotypeSequence(mutS);
		return mutP;
				
	}
	
	public String toString() {
		String fullString = new String(sequence);
		return fullString;
	}

}