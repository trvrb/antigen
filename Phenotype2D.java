/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import static java.lang.Math.*;
import java.util.*;

public class Phenotype2D implements Phenotype {

	// fields
	private double traitA = 0.0;
	private double traitB = 0.0;	
	
	// constructor
	public Phenotype2D() {
	
	}
	public Phenotype2D(double tA, double tB) {
		traitA = tA;
		traitB = tB;
	}
		
	public double getTraitA() {
		return traitA;
	}
	public double getTraitB() {
		return traitB;
	}	
		
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		Phenotype2D p2d = (Phenotype2D) p;
		double dist = 0.0;
		dist += Math.pow(traitA - p2d.getTraitA(), 2);
		dist += Math.pow(traitB - p2d.getTraitB(), 2);		
		dist = Math.sqrt(dist);
		
//		double boundary = Parameters.boundaryA;
//		double overlapdist = (boundary - traitA) + (p2d.getTraitA() - 0);
//		if (overlapdist < dist) { dist = overlapdist; }
//		overlapdist = (boundary - p2d.getTraitA()) + (traitA - 0);
//		if (overlapdist < dist) { dist = overlapdist; }
		
		return dist;
	}

	// cross immunity between a virus phenotype and a host's immune history
	// here encoded more directly as risk of infection, which ranges from 0 to 1
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
		
		double risk = closestDistance * Parameters.smithConversion;
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
//		double mutA;
//		if(Random.nextBoolean(0.5)) {
//			mutA = traitA + Parameters.muRangeA; 
//		} else {
//			mutA = traitA - Parameters.muRangeA;
//		}
//		double mutB = traitB;
//		double boundary = Parameters.boundaryA;
//		if (mutA > boundary) { mutA = mutA - boundary; }
//		if (mutA < 0) { mutA = boundary - mutA; }
//		if (mutB > boundary) { mutB = mutB - boundary; }
//		if (mutB < 0) { mutB = boundary - mutB; }		
		Phenotype mutP = new Phenotype2D(mutA,mutB);
		return mutP;
				
	}
	
	public String toString() {
		String fullString = String.format("%.4f,%.4f", traitA, traitB);
		return fullString;
	}

}