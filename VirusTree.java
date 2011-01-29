/* Stores a list of Viruses that have sampled during the course of the simulation */

import java.util.*;
import java.io.*;

public class VirusTree {

	// fields
	private static Virus root = Parameters.urVirus;	
	private static List<Virus> tips = new ArrayList<Virus>();
	
	static final Comparator<Virus> descendantOrder = new Comparator<Virus>() {
		public int compare(Virus v1, Virus v2) {
			Integer descendantsV1 = new Integer(getNumberOfDescendants(v1));
			Integer descendantsV2 = new Integer(getNumberOfDescendants(v2));
			return descendantsV1.compareTo(descendantsV2);
		}
	};	
		
	// static methods
	public static void add(Virus v) {
		tips.add(v);
	}
	public static void clear() {
		tips.clear();
	}
	public static List<Virus> getTips() {
		return tips;
	}
	public static Virus getRoot() {
		return root;
	}
	
	// work backwards for each sample filling the children lists
	public static void fillBackward() {
	
		for (Virus child : tips) {
			Virus parent = child.getParent();
			while (parent != null) {
				parent.addChild(child);
				child = parent;
				parent = child.getParent();
			}
		}
	
	}
	
	// returns virus v and all its descendents via a depth-first traversal
	public static List<Virus> postOrderNodes(Virus v) {
	
		List<Virus> vNodes = new ArrayList<Virus>();
		vNodes.add(root);
		vNodes = postOrderChildren(vNodes);
		return vNodes;
	
	}
	
	// returns virus v and all its descendents via a depth-first traversal
	public static List<Virus> postOrderChildren(List<Virus> vNodes) {
	
		Virus last = vNodes.get(vNodes.size()-1);
	
		for(Virus child : last.getChildren()) {
			vNodes.add(child);
			postOrderChildren(vNodes);
		}
		
		return vNodes;
	
	}


	// Count total descendents of a Virus, working through its children and its children's children
	public static int getNumberOfDescendants(Virus v) {
	
		int numberOfDescendants = v.getNumberOfChildren();
		
		for(Virus child : v.getChildren()) {
			numberOfDescendants += getNumberOfDescendants(child);
		}
		
		return numberOfDescendants;
		
	}
	
	public static int getNumberOfDescendants() {
		return getNumberOfDescendants(root);
	}
	
	// sorts children lists so that first member is child with more descendents than second member
	public static void sortChildrenByDescendants() {
	
		Virus v = root;		
		Collections.sort(v.getChildren(), descendantOrder);
		
	}	
	
	public static void printTips() {
		
		try {
			File tipFile = new File("out.tips");
			tipFile.delete();
			tipFile.createNewFile();
			PrintStream tipStream = new PrintStream(tipFile);
			tipStream.printf("\"%s\",\"%s\",\"%s\",\"%s\"\n", "name", "year", "location", "phenotype");
			for (int i = 0; i < tips.size(); i++) {
				Virus v = tips.get(i);
				double b = v.getBirth();
				int d = v.getDeme();
				Phenotype p = v.getPhenotype();				
				if (b >= 0) {	
					tipStream.printf("\"%s\",%.4f,%d,%s\n", v, b, d, p);
				}
			}
			tipStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
	public static void printPaths() {
		
		try {
			File pathFile = new File("out.paths");
			pathFile.delete();
			pathFile.createNewFile();
			PrintStream pathStream = new PrintStream(pathFile);
			for (int i = 0; i < tips.size(); i++) {
				double chanceOfSuccess = Parameters.pathSamplingProportion;
				if (Random.nextBoolean(chanceOfSuccess)) {
					Virus v = tips.get(i);
					double b = v.getBirth();
					boolean t = v.isTrunk();
					int d = v.getDeme();
					Phenotype p = v.getPhenotype();
					if (b >= 0) {
						while (b >= 0 && v.getParent() != null) {
							pathStream.printf("{\"%s\",%.4f,%d,%d,%s}\t", v, b, (t)?1:0, d, p);
							v = v.getParent();
							b = v.getBirth();
							t = v.isTrunk();
							d = v.getDeme();
							p = v.getPhenotype();

						}
						pathStream.println();
					}
				}
			}
			pathStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}	

}