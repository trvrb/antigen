/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */

import static java.lang.Math.*;
import java.util.*;

public class Phenotype {

	// fields
	private double trait = 0.0;
	
	// constructor
	public Phenotype() {
	
	}
	public Phenotype(double t) {
		trait = t;
	}
	// copies the phenotype
	public Phenotype(Phenotype p) {
		trait = p.trait;
	}	
	
	public double getTrait() {
		return trait;
	}
	
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		return Math.abs( trait - p.trait);
	}

	// cross immunity between a virus phenotype and a host's immune history
	// here encoded more directly as risk of infection, which ranges from 0 to 1
	public double riskOfInfection( List<Phenotype> history) {
	
		// find closest phenotype in history
		double closestDistance = 1.0;
		if (history.size()>0) {
			for (int i = 0; i< history.size(); i++) {
				double thisDistance = distance(history.get(i));
				if (thisDistance < closestDistance) {
					closestDistance = thisDistance;
				}
			}
		} 
		
		double risk = closestDistance;
		risk = Math.max(0.0, risk);
		risk = Math.min(1.0, risk);
		
		return risk;
		
	}
	
	public void mutate() {
		trait = trait + Random.nextDouble(0.45,0.55);
	}
	
	public String toString() {
		return String.valueOf(trait);
	}

}