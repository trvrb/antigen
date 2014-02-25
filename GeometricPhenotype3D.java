/* Antigenic phenotype present in individual Viruses and within Hosts as immune history */
/* Should be able to calculate distance and cross-immunity between two phenotypes */
/* Moving up to multiple dimensions is non-trivial and requires thought on the implementation */
/* Multiple Viruses can reference a single Phenotype object */

import static java.lang.Math.*;
import java.util.*;

public class GeometricPhenotype3D implements Phenotype {

	// fields
	private double traitA;
	private double traitB;	
	private double traitC;
	
	// constructor
	public GeometricPhenotype3D() {
	
	}
	public GeometricPhenotype3D(double tA, double tB, double tC) {
		traitA = tA;
		traitB = tB;
		traitC = tC;
	}
		
	public double getTraitA() {
		return traitA;
	}
	public double getTraitB() {
		return traitB;
	}	
	public double getTraitC() {
		return traitC;
	}		
	
	public void setTraitA(double tA) {
		traitA = tA;
	}
	public void setTraitB(double tB) {
		traitB = tB;
	}
	public void setTraitC(double tC) {
		traitC = tC;
	}	
		
	// raw antigenic distance between two phenotypes
	public double distance(Phenotype p) {
		GeometricPhenotype3D p3d = (GeometricPhenotype3D) p;
		double distA = (getTraitA() - p3d.getTraitA());
		double distB = (getTraitB() - p3d.getTraitB());	
		double distC = (getTraitC() - p3d.getTraitC());			
		double dist = (distA * distA) + (distB * distB) + (distC * distC);
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
		
		// random spherical point code comes from http://mathworld.wolfram.com/SpherePointPicking.html
		
		// spherical direction
		double u = Random.nextDouble(-1,1);
		double theta = Random.nextDouble(0,2*Math.PI);
		
		// size of mutation
		double r = Random.nextExponential(Parameters.meanStep);
			
		double mutA = getTraitA() + r * Math.sqrt(1-u*u) * Math.cos(theta);
		double mutB = getTraitB() + r * Math.sqrt(1-u*u) * Math.sin(theta);
		double mutC = getTraitC() + r * u;		
		Phenotype mutP = new GeometricPhenotype3D(mutA,mutB,mutC);
		return mutP;
				
	}
	
	public String toString() {
		String fullString = String.format("%.4f,%.4f,%.4f", traitA, traitB, traitC);
		return fullString;
	}

}