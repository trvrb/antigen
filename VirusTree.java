/* Performs genealogical reconstruction */
/* Needs to do a postorder traversal of the Virus objects */
/* Each virus points to its parent and its children */

import java.util.*;

public class VirusTree {

	public static Virus root = Parameters.urVirus;

	static final Comparator<Virus> descendantOrder = new Comparator<Virus>() {
		public int compare(Virus v1, Virus v2) {
			Integer descendantsV1 = new Integer(getNumberOfDescendants(v1));
			Integer descendantsV2 = new Integer(getNumberOfDescendants(v2));
			return descendantsV1.compareTo(descendantsV2);
		}
	};
	
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

}