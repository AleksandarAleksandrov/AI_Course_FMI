package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population {

	private List<Individual> population;
	private long populationFitness = -1;

	public Population(int populationSize) {
		this.population = new ArrayList<>(populationSize);
	}

	public Population(int populationSize, int chromosomeLength) {
		this.population = new ArrayList<>(populationSize);
		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			Individual individual = new Individual(chromosomeLength);
			this.population.add(individual);
		}
	}

	public List<Individual> getIndividuals() {
		return this.population;
	}
	
	public void setPopulation(List<Individual> population) {
		this.population = population;
	}

	public void sort() {
		Collections.sort(this.population, new Comparator<Individual>() {
			@Override
			public int compare(Individual o1, Individual o2) {
				if(o1.getFitness() == 0 && o2.getFitness() == 0) {
					return 0;
				}
				
				if(o1.getFitness() == 0) {
					return 1;
				} 
				
				if(o2.getFitness() == 0) {
					return -1;
				}
				
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});
	}
	
	public Individual getFittest(int offset) {
		sort();
		return population.get(offset);
	}
	
	public void addIndividual(Individual individual) {
		population.add(individual);
	}

	public Individual getIndividual(int offset) {
		return population.get(offset);
	}
	
	public long getPopulationFitness() {
		return populationFitness;
	}
	
	public void setPopulationFitness(long populationFitness) {
		this.populationFitness = populationFitness;
	}
	
	@Override
	public String toString() {
		return population.toString();
	}

}
