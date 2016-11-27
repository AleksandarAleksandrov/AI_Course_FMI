package model;

public class Individual {
	
	private boolean[] chromosome;
	private long fitWeight = -1;
	private long fitValue = -1;
	
	public Individual(boolean[] chromosome) {
		this.chromosome = chromosome;
	}
	
	public Individual(int chromoLength) {
		chromosome = new boolean[chromoLength];
		
		for (int gene = 0; gene < chromoLength; gene++) {
			if (0.5 < Math.random()) {
				setGene(gene, true);
			} else {
				setGene(gene, false);
			}
		}
	}	
	
	public boolean[] getChromosome() {
		return chromosome;
	}
	
	public void setGene(int offset, boolean gene) {
		this.chromosome[offset] = gene;
	}
	
	public long getFitness() {
		return fitWeight;
	}
	
	public void setFitValue(long fitness) {
		this.fitValue = fitness;
	}
	
	public long getFitValue() {
		return fitValue;
	}
	
	public long getFitWeight() {
		return fitWeight;
	}
	
	public void setFitWeight(long fitWeight) {
		this.fitWeight = fitWeight;
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder(chromosome.length);
		for(boolean b : chromosome) {
			builder.append(b ? '1' : '0');
		}
		
		builder.append(" , weight: " + fitWeight);
		builder.append(" , value: " + fitValue);
		
		return builder.toString();
	}

	public int getChromosomeLength() {
		return chromosome.length;
	}

	public boolean getGene(int geneIndex) {
		return chromosome[geneIndex];
	}
	
}
