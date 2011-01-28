/* Acts as constructor for Phenotype objects */
/* A completely static class */

public class PhenotypeFactory {

	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeVirusPhenotype() {
	
		Phenotype p = null;
	
		if (Parameters.phenotypeSpace == "2D") {
			p = new Phenotype2D();
		}
	
		return p;
	
	}
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeHostPhenotype() {
	
		Phenotype p = null;
	
		if (Parameters.phenotypeSpace == "2D") {
			p = new Phenotype2D(Parameters.initialTraitA, Parameters.initialTraitB);
		}
	
		return p;
	
	}	

}