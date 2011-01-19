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
	
	public static void printTips() {
		
		try {
			FileWriter writer = new FileWriter("out.tips");
			for (int i = 0; i < sample.size(); i++) {
				Virus v = sample.get(i);
				int b = v.getBirth();
				if (b > Parameters.burnin) {
					Phenotype p = v.getPhenotype();
					writer.write(b + "\t" + p + "\n");
				}
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}
	
	public static void printPaths() {
		
		try {
			FileWriter writer = new FileWriter("out.paths");
			for (int i = 0; i < sample.size(); i++) {
				Virus v = sample.get(i);
				int b = v.getBirth();
				Phenotype p = v.getPhenotype();
				if (b > Parameters.burnin) {
					while (b > Parameters.burnin && v.getParent() != null) {
						writer.write("{" + b + "," + p + "}\t");
						v = v.getParent();
						b = v.getBirth();
						p = v.getPhenotype();
					}
					writer.write("\n");
				}
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}	

}