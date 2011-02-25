/* Stores a list of Viruses that have sampled during the course of the simulation */

import java.util.*;
import java.io.*;
import static java.lang.Math.*;

public class VirusTree {

	// fields
	private static Virus root = Parameters.urVirus;	
	private static List<Virus> tips = new ArrayList<Virus>();
	private static Virus vaccineStrain = null;
	
	public static double xMin;
	public static double xMax;
	public static double yMin;
	public static double yMax;
	
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
	
	public static void dropTips() {
	
		List<Virus> reducedTips = new ArrayList<Virus>();
		for (Virus v : tips) {
			if (Random.nextBoolean(Parameters.treeProportion)) {
				reducedTips.add(v);
			}
		}
		tips = reducedTips;
	
	}

	public static void markTips() {
	
		for (Virus v : tips) {
			if (Random.nextBoolean(Parameters.treeProportion)) {
				while (v.getParent() != null) {
					v.mark();
					v = v.getParent();
				}
			}
		}
	
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
	
	// rotate the 2d euclidean space using PCA, returning an x-axis with maximum variance
	public static void rotate() {
	
		if (Parameters.phenotypeSpace == "epochal") {
			
			// load a 2d array with phenotypes
			
			List<Virus> virusList = postOrderNodes();
			int n = virusList.size();
			int m = 2;
			
			double[][] input = new double[n][m];
			
			for (int i = 0; i < n; i++) {
				Virus v = virusList.get(i);
				PhenotypeEpochal p = (PhenotypeEpochal) v.getPhenotype();
				double x = p.getTraitA();
				double y = p.getTraitB();	
				input[i][0] = x;
				input[i][1] = y;				
			}
			
			// project this array
			
			double[][] projected = SimplePCA.project(input);
			
			// reset phenotypes based on projection
			
			for (int i = 0; i < n; i++) {
				Virus v = virusList.get(i);
				PhenotypeEpochal p = (PhenotypeEpochal) v.getPhenotype();
				double x = projected[i][0];
				double y = projected[i][1];				
				p.setTraitA(x);
				p.setTraitB(y);					
			}

		}	
	
	}
	
	// flips the 2d euclidean space so that first sample is always to the left of the last sample
	public static void flip() {
	
		if (Parameters.phenotypeSpace == "epochal") {

			List<Virus> virusList = postOrderNodes();
			int n = virusList.size();	
			
			// find first and last virus			
			Virus firstVirus = virusList.get(0);
			Virus lastVirus = virusList.get(0);
			double firstDate = firstVirus.getBirth();
			double lastDate = lastVirus.getBirth();
					
			for (Virus v : virusList) {
				if (v.getBirth() < firstDate) {
					firstDate = v.getBirth();
					firstVirus = v;
				}
				if (v.getBirth() > lastDate) {
					lastDate = v.getBirth();
					lastVirus = v;
				}				
			}
			
			// is the x-value of first virus greater than the x-value of last virus?
			// if so, flip
			
			PhenotypeEpochal p = (PhenotypeEpochal) firstVirus.getPhenotype();
			double firstX = p.getTraitA();
			p = (PhenotypeEpochal) lastVirus.getPhenotype();
			double lastX = p.getTraitA();		
			
			if (firstX > lastX) {
			
				for (Virus v : virusList) {
					p = (PhenotypeEpochal) v.getPhenotype();
					double x = p.getTraitA();			
					p.setTraitA(-x);
				}
			
			}
			
		}		
	
	}
	
	// walks through list of nodes and update min and max ranges appropriately
	public static void updateRange() {
	
		xMin = 0.0;
		xMax = 0.0;
		yMin = 0.0;
		yMax = 0.0;
	
		if (Parameters.phenotypeSpace == "epochal") {
			for (Virus v : postOrderNodes()) {
			
				PhenotypeEpochal p = (PhenotypeEpochal) v.getPhenotype();
				double x = p.getTraitA();
				double y = p.getTraitB();
				if (xMin > x) { xMin = x; }
				if (xMax < x) { xMax = x; }
				if (yMin > y) { yMin = y; }
				if (yMax < y) { yMax = y; }	
			
			}
		}
		
		xMin = Math.floor(xMin) - 5;
		xMax = Math.ceil(xMax) + 5;
		yMin = Math.floor(yMin) - 5;
		yMax = Math.ceil(yMax) + 5;
	
	}

	public static void printRange() {
		
		try {
			File rangeFile = new File("out.range");
			rangeFile.delete();
			rangeFile.createNewFile();
			PrintStream rangeStream = new PrintStream(rangeFile);
			rangeStream.printf("%.4f,%.4f,%.4f,%.4f\n", xMin, xMax, yMin, yMax);
			rangeStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
	public static void printTips() {
		
		try {
			File tipFile = new File("out.tips");
			tipFile.delete();
			tipFile.createNewFile();
			PrintStream tipStream = new PrintStream(tipFile);
			tipStream.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", "name", "year", "trunk", "tip", "mark", "location", "layout", "ag1", "ag2");
			for (int i = 0; i < tips.size(); i++) {
				Virus v = tips.get(i);			
				tipStream.printf("\"%s\",%.4f,%d,%d,%d,%d,%.4f,%s\n", v, v.getBirth(), v.isTrunk()?1:0, v.isTip()?1:0, v.isMarked()?1:0, v.getDeme(), v.getLayout(), v.getPhenotype());
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
					branchStream.printf("{\"%s\",%.4f,%d,%d,%d,%d,%.4f,%s}\t", v, v.getBirth(), v.isTrunk()?1:0, v.isTip()?1:0, v.isMarked()?1:0, v.getDeme(), v.getLayout(), v.getPhenotype());
					branchStream.printf("{\"%s\",%.4f,%d,%d,%d,%d,%.4f,%s}\t", vp, vp.getBirth(), vp.isTrunk()?1:0, vp.isTip()?1:0, v.isMarked()?1:0, vp.getDeme(), vp.getLayout(), vp.getPhenotype());
					branchStream.printf("%d\n", vp.getCoverage());
				}
			}
			branchStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
	public static void printMutations() {
		
		try {
			File mutFile = new File("out.mutations");
			mutFile.delete();
			mutFile.createNewFile();
			PrintStream mutStream = new PrintStream(mutFile);
			mutStream.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n", "name", "year", "trunk", "location", "ag1start", "ag2start", "ag1stop", "ag2start");
			for (Virus v : postOrderNodes()) {
				if (v.getParent() != null) {
					Virus vp = v.getParent();
					if (v.getPhenotype() != vp.getPhenotype()) {
						int trunk = ( v.isTrunk() && vp.isTrunk() )?1:0;
						mutStream.printf("\"%s\",%.4f,%d,%d,%s,%s\n", v, v.getBirth(), trunk, v.getDeme(), v.getPhenotype(), vp.getPhenotype());
					}
				}
			}
			mutStream.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}	
	
}