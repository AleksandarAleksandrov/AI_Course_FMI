package algorithm;

import java.util.ArrayList;
import java.util.List;

import model.Individual;
import model.Item;
import model.Population;

public class GeneticAlgorithm {

	/**
	 * The items to consider putting in the bag.
	 */
	private List<Item> items;

	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;

	private int maxGenerationsCount;
	private int generationsCount = 0;

	private long bagCapacity;

	public GeneticAlgorithm(List<Item> items, int populationSize, double mutationRate, double crossoverRate,
			int elitismCount, long bagCapacity, int maxGenerationsCount) {
		this.items = items;
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.bagCapacity = bagCapacity;
		this.maxGenerationsCount = maxGenerationsCount;
	}

	public static void findSolution(List<Item> items, int populationSize, double mutationRate, double crossoverRate,
			int elitismCount, long bagCapacity, int maxGenerationsCount) {
		GeneticAlgorithm ga = new GeneticAlgorithm(items, populationSize, mutationRate, crossoverRate, elitismCount,
				bagCapacity, maxGenerationsCount);
		// Initialize population
		Population population = ga.initPopulation();
		// Evaluate population
		ga.evalPopulation(population);
		// Keep track of current generation
		while (ga.isTerminationConditionMet(population) == false) {
			// Print fittest individual from population
			System.out.println("Best solution: " + population.getFittest(0).toString());
			// Apply crossover
			population = ga.crossoverPopulation(population);
			// Apply mutation
			population = ga.mutatePopulation(population);
			// Evaluate population
			ga.evalPopulation(population);
			// Increment the current generation
			ga.incrementGenerations();
		}
		System.out.println("Found solution in " + ga.getGenerationsCount() + " generations");
		System.out.println("Best solution: " + population.getFittest(0).toString());

	}

	public Population initPopulation() {
		Population population = new Population(populationSize, items.size());
		return population;
	}

	public long calcFitness(Individual individual) {
		boolean[] chromosome = individual.getChromosome();

		long fitWeight = 0;
		long fitValue = 0;
		for (int i = 0; i < chromosome.length; i++) {
			if (chromosome[i]) {
				fitWeight += items.get(i).getWeight();
				fitValue += items.get(i).getValue();
			}
		}
		
		long diff = bagCapacity - fitWeight;
		if(diff < 0) {
			fitValue = 0;
		}
		
		individual.setFitValue(fitValue);
		individual.setFitWeight(fitWeight);
		return fitValue;
	}

	public void evalPopulation(Population population) {
		long populationFitness = 0;

		
		List<Individual> individuals = population.getIndividuals();
		List<Individual> newIndividuals = new ArrayList<>();
		
		for(Individual ind : individuals) {
			long calcFitness = calcFitness(ind);
			Individual temp = ind;
			while(calcFitness == 0) {
				temp = new Individual(items.size());
				calcFitness = calcFitness(temp);
			}
			
			newIndividuals.add(temp);
		}
		
		population.setPopulation(newIndividuals);
		for(Individual ind : newIndividuals) {
			populationFitness += ind.getFitValue();
		}
		
		population.setPopulationFitness(populationFitness);

	}
	
	public void incrementGenerations() {
		this.generationsCount++;
	}
	
	public int getGenerationsCount() {
		return generationsCount;
	}

	public boolean isTerminationConditionMet(Population population) {

		// uncomment if an estimation is ok
//		if (generationsCount > maxGenerationsCount) {
//			return true;
//		}

		for (Individual individual : population.getIndividuals()) {
			if (bagCapacity == individual.getFitWeight()) {
				return true;
			}
		}
		return false;
	}

	public Individual selectParent(Population population) {
		// Get individuals
		List<Individual> individuals = population.getIndividuals();
		// Spin roulette wheel
		double populationFitness = population.getPopulationFitness();
		double rouletteWheelPosition = Math.random() * populationFitness;

		// Find parent
		double spinWheel = 0;
		for (Individual individual : individuals) {
			spinWheel += individual.getFitness();
			if (spinWheel >= rouletteWheelPosition) {
				return individual;
			}
		}

		return individuals.get(individuals.size() - 1);
	}

	public Population crossoverPopulation(Population population) {
		// Create new population
		Population newPopulation = new Population(population.getIndividuals().size());
		// Loop over current population by fitness
		population.sort();
		for (int populationIndex = 0; populationIndex < population.getIndividuals().size(); populationIndex++) {
			Individual parent1 = population.getFittest(populationIndex);
			// Apply crossover to this individual?
			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
				// Initialize offspring
				Individual offspring = new Individual(parent1.getChromosomeLength());
				// Find second parent
				Individual parent2 = selectParent(population);
				// Loop over genome
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					// Use half of parent1's genes and half of
					// parent2's genes
					if (0.5 > Math.random()) {
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
					} else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}
				// Add offspring to new population
				newPopulation.addIndividual(offspring);
			} else {
				// Add individual to new population without applying
				// crossover
				newPopulation.addIndividual(parent1);
			}
		}
		return newPopulation;
	}

	public Population mutatePopulation(Population population) {
		// Initialize new population
		Population newPopulation = new Population(this.populationSize);
		// Loop over current population by fitness
		population.sort();
		for (int populationIndex = 0; populationIndex < population.getIndividuals().size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);
			// Loop over individual's genes
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				// Skip mutation if this is an elite individual
				if (populationIndex >= this.elitismCount) {
					// Does this gene need mutation?
					if (this.mutationRate > Math.random()) {
						// Get new gene
						boolean newGene = false;

						if (individual.getGene(geneIndex)) {
							newGene = false;
						}
						// Mutate gene
						individual.setGene(geneIndex, newGene);
					}
				}
			}
			// Add individual to population
			newPopulation.addIndividual(individual);
		}
		// Return mutated population
		return newPopulation;
	}

}
