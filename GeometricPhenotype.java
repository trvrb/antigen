/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import static java.lang.Math.*;
import java.util.*;

public class GeometricPhenotype implements Phenotype {

	// fields
	private double traitA;
	private double traitB;	
	
	// constructor
	public GeometricPhenotype() {
	
	}
	public GeometricPhenotype(double tA, double tB) {
		traitA = tA;
		traitB = tB;
	}
		
	public double getTraitA() {
		return traitA;
	}
	public double getTraitB() {
		return traitB;
	}	
	
	public void setTraitA(double tA) {
		traitA = tA;
	}
	public void setTraitB(double tB) {
		traitB = tB;
	}		
		
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		GeometricPhenotype p2d = (GeometricPhenotype) p;
		double distA = (getTraitA() - p2d.getTraitA());
		double distB = (getTraitB() - p2d.getTraitB());	
		double dist = (distA * distA) + (distB * distB);
		dist = Math.sqrt(dist);
		return dist;
	}

	// cross immunity between a virus phenotype and a host's immune history
	// here encoded more directly as risk of infection, which ranges from 0 to 1
	public double riskOfInfection( Phenotype[] history) {
	
		// find closest phenotype in history
		double closestDistance = 100.0;
		if (history.length > 0) {
			for (int i = 0; i < history.length; i++) {
				double thisDistance = distance(history[i]);
				if (thisDistance < closestDistance) {
					closestDistance = thisDistance;
				}
				if (thisDistance < 0.01) {
					break;
				}
			}
		} 
		
		double risk = closestDistance * Parameters.smithConversion;
		double minRisk = 1.0 - Parameters.homologousImmunity;
		risk = Math.max(minRisk, risk);
		risk = Math.min(1.0, risk);
				
		return risk;
		
	}
	
	// mutate with exponential	
	// returns a mutated copy, original Phenotype is unharmed
//	public Phenotype mutate() {
//		
//		// direction of mutation
//		double theta = 0;
//		if (Parameters.mut2D) {
//			theta = Random.nextDouble(0,2*Math.PI);
//		} else {
//			if (Random.nextBoolean(0.5)) { theta = 0; }
//			else { theta = Math.PI; }
//		}
//		
//		// size of mutation
//		double r = Random.nextExponential(Parameters.meanStep);
//			
//		double mutA = getTraitA() + r * Math.cos(theta);
//		double mutB = getTraitB() + r * Math.sin(theta);
//		Phenotype mutP = new GeometricPhenotype(mutA,mutB);
//		return mutP;
//				
//	}
	
	//	mutate with bivariate normal
	// returns a mutated copy, original Phenotype is unharmed
//	public Phenotype mutate() {
//					
//		double mutA = getTraitA() + Random.nextNormal(0,Parameters.meanStep);
//		double mutB = getTraitB();
//		if (Parameters.mut2D) { mutB += Random.nextNormal(0,Parameters.meanStep); }
//		Phenotype mutP = new GeometricPhenotype(mutA,mutB);
//		return mutP;
//				
//	}	
	
	// returns a mutated copy, original Phenotype is unharmed
	// mutate with gamma
	public Phenotype mutate() {
		
		// direction of mutation
		double theta = 0;
		if (Parameters.mut2D) {
			theta = Random.nextDouble(0,2*Math.PI);
		} else {
			if (Random.nextBoolean(0.5)) { theta = 0; }
			else { theta = Math.PI; }
		}
		
		// size of mutation
		double alpha = (Parameters.meanStep *  Parameters.meanStep) / (Parameters.sdStep * Parameters.sdStep);
		double beta = (Parameters.sdStep * Parameters.sdStep) / Parameters.meanStep;
		double r = Random.nextGamma(alpha, beta);
			
		double mutA = getTraitA() + r * Math.cos(theta);
		double mutB = getTraitB() + r * Math.sin(theta);
		Phenotype mutP = new GeometricPhenotype(mutA,mutB);
		return mutP;
				
	}	
	
	public String toString() {
		String fullString = String.format("%.4f,%.4f", traitA, traitB);
		return fullString;
	}
	
	
	// used for memory profiling to show that references are properly used
//	public String toString() {
//		String fullString = String.format("%s,%.4f,%.4f", Integer.toHexString(this.hashCode()), traitA, traitB);
//		return fullString;
//	}	

}
