package projet1.gomoku.genetic;

import java.util.Random;

class Individual {
    private int[] genes;
    private int fitness;

    Individual(int n) {
    	fitness = 0;
        genes = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++)
            genes[i] = random.nextInt(10);
    }
    
    Individual(Individual other) {
    	this.genes = other.genes.clone();
    	this.fitness = other.fitness;
    }

    // Mélange les genes de deux individus
    Individual crossover(Individual other) {
        Individual child = new Individual(genes.length);
        for (int i = 0; i < genes.length; i++) {
        	child.genes[i] = Math.random() < 0.5 ? genes[i] : other.genes[i];
        }
        return child;
    }

    void mutate(double rate, double amplitude) {
        for (int i = 0; i < genes.length; i++) {
        	Random random = new Random();
        	if (Math.random() < rate) {
            	genes[i] += (random.nextInt(1 + 1) - 1) * amplitude;
            	if(genes[i] < 0) {
            		genes[i] = 0;
            	}
            	if(genes[i] > 10) {
            		genes[i] = 10;
            	}
            }
        }        
    }

	/**
	 * @return the genes
	 */
	public int[] getGenes() {
		return genes;
	}

	/**
	 * @return the fitness
	 */
	public int getFitness() {
		return fitness;
	}

	/**
	 * @param fitness the fitness to set
	 */
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
}
