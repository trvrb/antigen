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
				String b = String.valueOf(v.getBirth());
				Phenotype p = v.getPhenotype();
				String t = p.toString();
				writer.write(b + "\t" + t + "\n");
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("Could not write to file"); 
			System.exit(0);
		}
		
	}

}