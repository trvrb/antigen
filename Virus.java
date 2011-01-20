/* Virus infection that has genotype, phenotype and ancestry */

import java.util.*;

public class Virus {

	// fields
	private Virus parentVirus;
	private Phenotype antigenicType;
	private int birth;
	private boolean trunk;	// fill this at the end of the simulation
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
	public int getBirth() {
		return birth;
	}
	public Virus getParent() {
		return parentVirus;
	}
	public boolean isTrunk() {
		return trunk; 
	}
	public void makeTrunk() {
		trunk = true;
	}
	
	public Virus commonAncestor(Virus virusB) {
		
		// go through current virus's history and add to a set
		Virus lineage = this;
		Set<Virus> ancestry = new HashSet<Virus>();
		while (lineage.getParent() != null) {
			lineage = lineage.getParent();
			ancestry.add(lineage);
		}
		
		// go through other virus's history and add to this set, stop when duplicate is encountered and return this duplicate
		lineage = virusB;
		while (lineage.getParent() != null) {
			lineage = lineage.getParent();
			if (!ancestry.add(lineage)) {
				break;
			}
		}		
		
		
		return lineage;
		
	}
	
	public int distance(Virus virusB) {
		Virus ancestor = commonAncestor(virusB);
		int distA = getBirth() - ancestor.getBirth();
		int distB = virusB.getBirth() - ancestor.getBirth();
		return distA + distB;
	}

}