/* Virus infection that has genotype, phenotype and ancestry */

public class Virus {

	// fields
	private Virus parentVirus;
	private Phenotype antigenicType;
	private int birth;
	// location
	
	// initialization
	public Virus() {
		antigenicType = new Phenotype();
	}
	
	// replication, copies the virus, but remembers the ancestry
	public Virus(Virus p) {
		parentVirus = p;
		antigenicType = p.getPhenotype();
		birth = Parameters.day;
	}
	
	// methods
	public Phenotype getPhenotype() {
		return antigenicType;
	}
	public void setPhenotype(Phenotype p) {
		antigenicType = p;
	}	

}