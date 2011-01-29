/* Stores a list of Viruses that have sampled during the course of the simulation */

import java.util.*;
import java.io.*;

public class VirusSample {

	// fields
	private static List<Virus> sample = new ArrayList<Virus>();
		
	// static methods
	public static void add(Virus v) {
		sample.add(v);
	}
	public static void clear() {
		sample.clear();
	}
	public static List<Virus> getSample() {
		return sample;
	}
	public static Virus getRoot() {
		Virus v = sample.get(0);
		while (v.getParent() != null) {
			if (v.getParent() != null) {
				v = v.getParent();
			}
		}
		return v;
	}
	
	// work backwards for each sample filling the children lists
	public static void fillBackward() {
	
		for (Virus child : sample) {
			Virus parent = child.getParent();
			while (parent != null) {
				parent.addChild(child);
				child = parent;
				parent = child.getParent();
			}
		}
	
	}
	
	public static void printTips() {
		
		try {
			File tipFile = new File("out.tips");
			tipFile.delete();
			tipFile.createNewFile();
			PrintStream tipStream = new PrintStream(tipFile);
			tipStream.printf("\"%s\",\"%s\",\"%s\",\"%s\"\n", "name", "year", "location", "phenotype");
			for (int i = 0; i < sample.size(); i++) {
				Virus v = sample.get(i);
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
			for (int i = 0; i < sample.size(); i++) {
				double chanceOfSuccess = Parameters.pathSamplingProportion;
				if (Random.nextBoolean(chanceOfSuccess)) {
					Virus v = sample.get(i);
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