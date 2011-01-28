/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import static java.lang.Math.*;
import java.util.*;

public class Phenotype {

	// fields
	private double traitA = 0.0;
	private double traitB = 0.0;	
	
	// constructor
	public Phenotype() {
	
	}
	public Phenotype(double tA, double tB) {
		traitA = tA;
		traitB = tB;
	}
	// copies the phenotype
	public Phenotype(Phenotype p) {
		traitA = p.traitA;
		traitB = p.traitB;
	}	
	
	public double getTraitA() {
		return traitA;
	}
	public double getTraitB() {
		return traitB;
	}	
		
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		double dist = 0.0;
		dist += Math.pow(traitA - p.getTraitA(), 2);
		dist += Math.pow(traitB - p.getTraitB(), 2);		
		dist = Math.sqrt(dist);
		return dist;
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
		if (Parameters.quadratic) {
			risk = risk * risk;
		}
		risk = Math.max(0.0, risk);
		risk = Math.min(1.0, risk);
				
		return risk;
		
	}
	
	// returns a mutated copy, original Phenotype is unharmed
	public Phenotype mutate() {
		
		double mutA = traitA + Random.nextDouble(-Parameters.muRangeA,Parameters.muRangeA);
		double mutB = traitB + Random.nextDouble(-Parameters.muRangeB,Parameters.muRangeB);
		Phenotype mutP = new Phenotype(mutA,mutB);
		return mutP;
				
	}
	
	public String toString() {
		String fullString = String.format("%.4f,%.4f", traitA, traitB);
		return fullString;
	}

}