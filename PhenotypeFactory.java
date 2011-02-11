/* Acts as constructor for Phenotype objects */
/* A completely static class */

public class PhenotypeFactory {

	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeVirusPhenotype() {
	
		Phenotype p = null;
		if (Parameters.phenotypeSpace == "2D") { p = new Phenotype2D(); }
		if (Parameters.phenotypeSpace == "epochal") { p = new PhenotypeEpochal(); }	
		if (Parameters.phenotypeSpace == "sequence") { p = new PhenotypeSequence(); }			
		return p;
	
	}
	
	// returns newly instantiated Phenotype objects of type according to Parameters.phenotypeSpace
	public static Phenotype makeHostPhenotype() {
	
		Phenotype p = null;
		if (Parameters.phenotypeSpace == "2D") { p = new Phenotype2D(Parameters.initialTraitA, Parameters.initialTraitB); }
		if (Parameters.phenotypeSpace == "epochal") { p = new PhenotypeEpochal(Parameters.initialTraitA, Parameters.initialTraitB); }
		if (Parameters.phenotypeSpace == "sequence") { p = new PhenotypeSequence(); }		
		return p;
	
	}	

}