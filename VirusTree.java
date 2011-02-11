/* Stores a list of Viruses that have sampled during the course of the simulation */

import java.util.*;
import java.io.*;

public class VirusTree {

	// fields
	private static Virus root = Parameters.urVirus;	
	private static List<Virus> tips = new ArrayList<Virus>();
	private static Virus vaccineStrain = null;
	
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
	
	public static int getDemeCount(int d) {
		int count = 0;
		for (Virus v : tips) {
			if (v.getDeme() == d) {
				count++;
			}
		}
		return count;
	}	
	
	public static Virus getVaccineStrain() {
		return vaccineStrain;
	}
	public static void updateVaccineStrain() {
		double date = Parameters.getDate() - 1.0;
		for (Virus v : tips) {
			if (Math.abs(date - v.getBirth()) < 0.05) {
				vaccineStrain = v;
				break;
			}
		}
	}
		
	// work backwards for each sample filling the children lists
	public static void fillBackward() {
	
		for (Virus child : tips) {
			Virus parent = child.getParent();
			while (parent != null) {
				parent.addChild(child);
				parent.incrementCoverage();
				child = parent;
				parent = child.getParent();
			}
		}
	
	}
	
	// drop tips
	public static void dropTips() {
	
		List<Virus> reducedTips = new ArrayList<Virus>();
		for (Virus v : tips) {
			if (Random.nextBoolean(Parameters.treeProportion)) {
				reducedTips.add(v);
			}
		}
		tips = reducedTips;
	
	}
	
	// prune tips
	public static void pruneTips() {
	
		List<Virus> reducedTips = new ArrayList<Virus>();
		for (int d = 0; d < Parameters.demeCount; d++) {
			double keepProportion = (double) Parameters.tipSamplesPerDeme / (double) getDemeCount(d);
			for (Virus v : tips) {
				if (Random.nextBoolean(keepProportion) && v.getDeme() == d) {
					reducedTips.add(v);
				}
			}
		}
		tips = reducedTips;
	
	}
	
	// returns virus v and all its descendents via a depth-first traversal
	public static List<Virus> postOrderNodes(Virus v) {
		List<Virus> vNodes = new ArrayList<Virus>();
		vNodes.add(v);
		vNodes = postOrderChildren(vNodes);
		return vNodes;
	}
	
	public static List<Virus> postOrderNodes() {
		return postOrderNodes(root);
	}	
	
	// returns virus v and all its descendents via a depth-first traversal
	public static List<Virus> postOrderChildren(List<Virus> vNodes) {
	
		Virus last = vNodes.get(vNodes.size()-1);
	
		for (Virus child : last.getChildren()) {
			vNodes.add(child);
			postOrderChildren(vNodes);
		}
		
		return vNodes;
	
	}


	// Count total descendents of a Virus, working through its children and its children's children
	public static int getNumberOfDescendants(Virus v) {
	
		int numberOfDescendants = v.getNumberOfChildren();
		
		for (Virus child : v.getChildren()) {
			numberOfDescendants += getNumberOfDescendants(child);
		}
		
		return numberOfDescendants;
		
	}
	
	public static int getNumberOfDescendants() {
		return getNumberOfDescendants(root);
	}
		
	// sorts children lists so that first member is child with more descendents than second member
	public static void sortChildrenByDescendants(Virus v) {
		
		List<Virus> children = v.getChildren();
		Collections.sort(children, descendantOrder);
		
		for (Virus child : children) {
			sortChildrenByDescendants(child);
		}
				
	}	
	
	public static void sortChildrenByDescendants() {
		sortChildrenByDescendants(root);
	}
	
	// sets Virus layout based on a postorder traversal
	public static void setLayoutByDescendants() {
	
		List<Virus> vNodes = postOrderNodes();
		
		// set layout of tips based on traversal
		double y = 0;
		for (Virus v : vNodes) {
//			if (tips.contains(v)) {
			if (v.isTip()) {
				v.setLayout(y);
				y++;
			}
		}
		
		// update layout of internal nodes
		Collections.reverse(vNodes);
		for (Virus v : vNodes) {
			if (v.getNumberOfChildren() > 0) {
				double mean = 0;
				for (Virus child : v.getChildren()) {
					mean += child.getLayout();
				}
				mean /= v.getNumberOfChildren();
				v.setLayout(mean);
			}
		}
		
	}	
	
	// looks at a virus and its grandparent, if traits are identical and there is no branching
	// then make virus child rather than grandchild
	// returns v.parent after all is said and done
	public static Virus collapse(Virus v) {
	
		Virus vp = null;
		Virus vgp = null;
		if (v.getParent() != null) {
			vp = v.getParent();
			if (vp.getParent() != null) {
				vgp = vp.getParent();
			}
		}

		if (vp != null && vgp != null) {
			if (vp.getNumberOfChildren() == 1 && v.getPhenotype() == vp.getPhenotype() && v.isTrunk() == vp.isTrunk() && v.getDeme() == vp.getDeme()) {
		
				List<Virus> vgpChildren = vgp.getChildren();
				int vpIndex =  vgpChildren.indexOf(vp);
				
				if (vpIndex >= 0) {
				
					// replace virus as child of grandparent
					vgpChildren.set(vpIndex, v);
				
					// replace grandparent as parent of virus
					v.setParent(vgp);
				
					// erase parent
					vp = null;
				
				}
		
			}
		}
		
		return v.getParent();

	}
	
	// walks backward using the list of tips, collapsing where possible
	public static void streamline() {
		
		for (Virus v : tips) {
			Virus vp = v;
			while (vp != null) {
				vp = collapse(vp);
			}
		}
		
	}
	
	public static void printTips() {
		
		try {
			File tipFile = new File("out.tips");
			tipFile.delete();
			tipFile.createNewFile();
			PrintStream tipStream = new PrintStream(tipFile);
			tipStream.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", "name", "year", "trunk", "location", "layout", "ag1", "ag1");
			for (int i = 0; i < tips.size(); i++) {
				Virus v = tips.get(i);			
				tipStream.printf("\"%s\",%.4f,%d,%d,%.4f,%s\n", v, v.getBirth(), v.isTrunk()?1:0, v.getDeme(), v.getLayout(), v.getPhenotype());
			}
			tipStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
	public static void printBranches() {
		
		try {
			File branchFile = new File("out.branches");
			branchFile.delete();
			branchFile.createNewFile();
			PrintStream branchStream = new PrintStream(branchFile);
			for (Virus v : postOrderNodes()) {
				if (v.getParent() != null) {
					Virus vp = v.getParent();
					int trunk = v.isTrunk() && vp.isTrunk() ? 1 : 0;
					branchStream.printf("{\"%s\",%.4f,%d,%d,%.4f,%s}\t", v, v.getBirth(), trunk, v.getDeme(), v.getLayout(), v.getPhenotype());
					branchStream.printf("{\"%s\",%.4f,%d,%d,%.4f,%s}\t", vp, vp.getBirth(), trunk, vp.getDeme(), vp.getLayout(), vp.getPhenotype());
					branchStream.printf("%d\n", vp.getCoverage());
				}
			}
			branchStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
}