/* Acts as constructor for Phenotype objects */
/* A completely static class */

public class PhenotypeFactory {

	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeVirusPhenotype() {
	
		Phenotype p = null;
		if (Parameters.phenotypeSpace == "geometric") { p = new GeometricPhenotype(); }	
		if (Parameters.phenotypeSpace == "geometric3d") { p = new GeometricPhenotype3D(); }		
		if (Parameters.phenotypeSpace == "geometric10d") { p = new GeometricPhenotype10D(); }				
		return p;
	
	}
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeHostPhenotype() {
	
		Phenotype p = null;
		if (Parameters.phenotypeSpace == "geometric") { 
			p = new GeometricPhenotype(Parameters.initialTraitA, 0); 
		}	
		if (Parameters.phenotypeSpace == "geometric3d") { 
			p = new GeometricPhenotype3D(Parameters.initialTraitA, 0, 0); 
		}
		if (Parameters.phenotypeSpace == "geometric10d") { 
			double[] traits = {Parameters.initialTraitA, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			p = new GeometricPhenotype10D(traits); 
		}		
		return p;
	
	}	
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeArbitaryPhenotype(double x, double y) {
	
		Phenotype p = null;
		if (Parameters.phenotypeSpace == "geometric") { p = new GeometricPhenotype(x, y); }	
		return p;
	
	}		

}