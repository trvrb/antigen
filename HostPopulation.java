/* A population of host individuals */

import java.util.*;

public class HostPopulation {

	// fields
	private List<Host> susceptibles = new ArrayList<Host>();
	private List<Host> infecteds = new ArrayList<Host>();	
	
	// constructors
	public HostPopulation() {
		// fill population with Host objects
		int initialS = Parameters.initialN - Parameters.initialI;
		for (int i = 0; i < initialS; i++) {
			Host h = new Host();
			susceptibles.add(h);
		}
		// infect some individuals
		for (int i = 0; i < Parameters.initialI; i++) {
			Virus v = new Virus();
			Host h = new Host(v);
			infecteds.add(h);
		}		
	}
	
	// accessors
	public int getN() {
		return susceptibles.size() + infecteds.size();
	}
	public int getS() {
		return susceptibles.size();
	}
	public int getI() {
		return infecteds.size();
	}
	public double getPrS() {
		return (double) getS() / (double) getN();
	}
	public double getPrI() {
		return (double) getI() / (double) getN();
	}
	public int getRandomN() {
		return Random.nextInt(0,getN()-1);
	}
	public int getRandomS() {
		return Random.nextInt(0,getS()-1);
	}
	public int getRandomI() {
		return Random.nextInt(0,getI()-1);
	}	
	
	// draw a Poisson distributed number of births and add these hosts to the end of the population list
	public void grow() {
		double totalBirthRate = getN() * Parameters.birthRate;
		int births = Random.nextPoisson(totalBirthRate);
		for (int i = 0; i < births; i++) {
			Host h = new Host();
			susceptibles.add(h);
		}
	}
	
	// draw a Poisson distributed number of deaths and remove random hosts from the population list
	public void decline() {
		// deaths in susceptible class
		if (getS()>0) {
			double totalDeathRate = getS() * Parameters.deathRate;
			int deaths = Random.nextPoisson(totalDeathRate);
			for (int i = 0; i < deaths; i++) {
				int sndex = getRandomS();
				susceptibles.remove(sndex);
			}		
		}
		// deaths in infectious class
		if (getI()>0) {
			double totalDeathRate = getI() * Parameters.deathRate;
			int deaths = Random.nextPoisson(totalDeathRate);
			for (int i = 0; i < deaths; i++) {
				int index = getRandomI();
				infecteds.remove(index);
			}		
		}		
	}

	// draw a Poisson distributed number of contacts and move from S->I based upon this
	public void contact() {
		if (getS()>0 && getI()>0) {
			// each infected makes I->S contacts on a per-day rate of beta * S/N
			double totalContactRate = getI() * getPrS() * Parameters.beta;
			int contacts = Random.nextPoisson(totalContactRate);
			for (int i = 0; i < contacts; i++) {
				int index = getRandomI();
				int sndex = getRandomS();
				Host iH = infecteds.get(index);
				Host sH = susceptibles.get(sndex);
				Virus v = iH.getInfection();
				sH.infect(v);
				susceptibles.remove(sndex);
				infecteds.add(sH);
			}			
		}
	}
	
	// draw a Poisson distributed number of recoveries and move from I->S based upon this
	public void recover() {
		if (getI()>0) {
			// each infected recovers at a per-day rate of nu
			double totalRecoveryRate = getI() * Parameters.nu;
			int recoveries = Random.nextPoisson(totalRecoveryRate);
			for (int i = 0; i < recoveries; i++) {
				int index = getRandomI();
				Host h = infecteds.get(index);
				h.clearInfection();
				infecteds.remove(index);
				susceptibles.add(h);
			}			
		}
	}

}