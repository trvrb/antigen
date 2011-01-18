/* Virus infection that has genotype, phenotype and ancestry */

public class Virus {

	// fields
	// location
	// antigenic phenotype
	private Virus parentVirus;
	private int birth;	
	
	// initialization
	public Virus() {
	
	}
	
	// replication
	public Virus(Virus p) {
		parentVirus = p;
		birth = Parameters.day;
	}
	
	// methods

}