/* Acts as constructor for Phenotype objects */
/* A completely static class */

public class PhenotypeFactory {

	public static String GEOMETRIC = "geometric";
	public static String GEOMETRIC3D = "geometric3d";
	public static String GEOMETRIC10D = "geometric10d";

	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeVirusPhenotype() {
	
		Phenotype p = null;	
		if (GEOMETRIC.equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype(); }	
		if ("geometric3d".equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype3D(); }
		if ("geometric10d".equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype10D(); }
		return p;
	
	}
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeHostPhenotype() {
	
		Phenotype p = null;
		if ("geometric".equals(Parameters.phenotypeSpace)) {
			p = new GeometricPhenotype(Parameters.initialTraitA, 0); 
		}	
		if ("geometric3d".equals(Parameters.phenotypeSpace)) {
			p = new GeometricPhenotype3D(Parameters.initialTraitA, 0, 0); 
		}
		if ("geometric10d".equals(Parameters.phenotypeSpace)) {
			double[] traits = {Parameters.initialTraitA, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			p = new GeometricPhenotype10D(traits); 
		}
		return p;
	
	}	
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeArbitaryPhenotype(double x, double y) {
	
		Phenotype p = null;
		if ("geometric".equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype(x, y); }
		return p;
	
	}		

}