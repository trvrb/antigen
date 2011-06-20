/* Virus infection that has genotype, phenotype and ancestry */

import java.util.*;

public class Virus {

	// simulation fields
	private Virus parent;
	private Phenotype phenotype;
	private double birth;		// measured in years relative to burnin
	private int deme;
	
	// additional reconstruction fields
	private boolean marked;
	private boolean trunk;	// fill this at the end of the simulation
	private List<Virus> children = new ArrayList<Virus>(0);	// will be void until simulation ends	
	private double layout;
	private int coverage;		// how many times this Virus has been covered in tracing the tree backwards
	
	// initialization
	public Virus() {
		phenotype = PhenotypeFactory.makeVirusPhenotype();
	}
		
	// replication, copies the virus, but remembers the ancestry
	public Virus(Virus v, int d) {
		parent = v;
		phenotype = v.getPhenotype();
		birth = Parameters.getDate();
		deme = d;
	}
	
	public Virus(Virus v, int d, Phenotype p) {
		parent = v;
		phenotype = p;
		birth = Parameters.getDate();
		deme = d;
	}	
	
	public Virus(int d, Phenotype p) {
		parent = null;
		phenotype = p;
		birth = Parameters.getDate();
		deme = d;
	}		
	
	// methods
	public Phenotype getPhenotype() {
		return phenotype;
	}
	public void setPhenotype(Phenotype p) {
		phenotype = p;
	}	
	public double getBirth() {
		return birth;
	}
	public Virus getParent() {
		return parent;
	}
	public void setParent(Virus v) {
		parent = v;
	}
	public boolean isTrunk() {
		return trunk; 
	}
	public void makeTrunk() {
		trunk = true;
	}
	public void mark() {
		marked = true;
	}
	public boolean isMarked() {
		return marked;
	}
	public int getDeme() {
		return deme;
	}	
	public double getLayout() {
		return layout;
	}
	public void setLayout(double y) {
		layout = y;
	}
	public int getCoverage() {
		return coverage;
	}
	public void incrementCoverage() {
		coverage++;
	}
	
	// add virus node as child if does not already exist
	public void addChild(Virus v) {
		if (!children.contains(v)) {
			children.add(v);
		}
	}		
	public int getNumberOfChildren() {
		return children.size();
	}
	public List<Virus> getChildren() {
		return children;
	}	
	public boolean isTip() {
		return getNumberOfChildren() == 0 ? true : false;
	}
	
	// returns a mutated copy, original virus left intact
	public Virus mutate() {
	
		Phenotype mutP = phenotype.mutate();			// mutated copy
		Virus mutV = new Virus(this,deme,mutP);
		return mutV;
		
	}
	
	public Virus commonAncestor(Virus virusB) {
				
		Virus lineageA = this;
		Virus lineageB = virusB;
		Virus commonAnc = null;
		Set<Virus> ancestry = new HashSet<Virus>();		
		while (true) {
			if (lineageA.getParent() != null) {		
				lineageA = lineageA.getParent();
				if (!ancestry.add(lineageA)) { 
					commonAnc = lineageA;
					break; 
				}
			}
			if (lineageB.getParent() != null) {
				lineageB = lineageB.getParent();
				if (!ancestry.add(lineageB)) { 
					commonAnc = lineageB;
					break; 
				}
			}
			if (lineageA.getParent() == null && lineageB.getParent() == null) {	
				break;
			}
		}	
		
		return commonAnc;								// returns null when no common ancestor is present
		
	}
	
	public double distance(Virus virusB) {
		Virus ancestor = commonAncestor(virusB);
		if (ancestor != null) {
			double distA = getBirth() - ancestor.getBirth();
			double distB = virusB.getBirth() - ancestor.getBirth();
			return distA + distB;
		}
		else {
			return 0;
		}
	}
	
	public String toString() {
		return Integer.toHexString(this.hashCode());
	}

}