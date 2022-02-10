/* Acts as constructor for Phenotype objects */
/* A completely static class */

public class PhenotypeFactory {

	public static String GEOMETRIC = "geometric";
	public static String GEOMETRIC3D = "geometric3d";
	public static String GEOMETRIC10D = "geometric10d";
	public static String SEQUENCE = "sequence";

	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeVirusPhenotype() {
	
		Phenotype p = null;	
		if (GEOMETRIC.equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype(); }	
		if (GEOMETRIC3D.equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype3D(); }
		if (GEOMETRIC10D.equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype10D(); }
		if (SEQUENCE.equals(Parameters.phenotypeSpace)) { p = new SequencePhenotype(); }
		return p;
	
	}
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeHostPhenotype() {
	
		Phenotype p = null;
		if (GEOMETRIC.equals(Parameters.phenotypeSpace)) {
			p = new GeometricPhenotype(Parameters.initialTraitA, 0); 
		}	
		if (GEOMETRIC3D.equals(Parameters.phenotypeSpace)) {
			p = new GeometricPhenotype3D(Parameters.initialTraitA, 0, 0); 
		}
		if (GEOMETRIC10D.equals(Parameters.phenotypeSpace)) {
			double[] traits = {Parameters.initialTraitA, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			p = new GeometricPhenotype10D(traits); 
		}
		if (SEQUENCE.equals(Parameters.phenotypeSpace)) {
			p = new SequencePhenotype(Parameters.sequence);
		}
		return p;
	
	}	
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeArbitaryPhenotype(double x, double y) {
	
		Phenotype p = null;
		if (GEOMETRIC.equals(Parameters.phenotypeSpace)) { p = new GeometricPhenotype(x, y); }
		return p;
	
	}		

}