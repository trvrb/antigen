/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import static java.lang.Math.*;
import java.util.*;

public class GeometricPhenotype10D implements Phenotype {

	// fields	
	private double[] traits = new double[10];
	private int dimen = 10;
		
	// constructor
	public GeometricPhenotype10D() {
	
	}
	
	public GeometricPhenotype10D(double[] tarray) {
		traits = tarray;
	}
		
	public double getTrait(int i) {
		return traits[i];
	}
		
	public void setTrait(double t, int i) {
		traits[i] = t;
	}

		
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		GeometricPhenotype10D otherp = (GeometricPhenotype10D) p;		
		double dist = 0;
		for (int i = 0; i < dimen; i++) {
			double d = (getTrait(i) - otherp.getTrait(i));
			dist += d*d;
		}
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
	
	// returns a mutated copy, original Phenotype is unharmed
	public Phenotype mutate() {
		
		// random spherical point code comes from http://mathworld.wolfram.com/HyperspherePointPicking.html
		
		// spherical direction
		double[] vec = new double[dimen];
		for (int i = 0; i < dimen; i++) {
			vec[i] = Random.nextNormal();
		}
		double norm = 0.0;
		for (int i = 0; i < dimen; i++) {
			norm += vec[i]*vec[i];		
		}
		norm = Math.sqrt(norm);
		for (int i = 0; i < dimen; i++) {
			vec[i] /= norm;	
		}
		
		// size of mutation
		double r = Random.nextExponential(Parameters.meanStep);
		
		// scaling by mutation size
		for (int i = 0; i < dimen; i++) {
			vec[i] *= r;	
		}
		
		// applying to original phenotype
		for (int i = 0; i < dimen; i++) {
			vec[i] += getTrait(i);
		}
		
		Phenotype mutP = new GeometricPhenotype10D(vec);
		return mutP;
				
	}
	
	public String toString() {
//		String fullString = String.format("%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f", traits);
		String fullString = String.format("%.4f", traits[0]);
		for (int i = 1; i < dimen; i++) {
			fullString += String.format(",%.4f", traits[i]);
		}
		return fullString;
	}

}