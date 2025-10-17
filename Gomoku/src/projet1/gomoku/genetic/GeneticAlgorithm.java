package projet1.gomoku.genetic;

import java.util.*;

class GeneticAlgorithm {
    int populationSize = 40;
    double mutationRate = 0.15;
    double mutationAmplitude = 1;
    int numGenes = 15; // Nombres de patterns

    List<Individual> population = new ArrayList<>();

    GeneticAlgorithm() {
    	// Crée une population avec genes randoms
        for (int i = 0; i < populationSize; i++) {
        	population.add(new Individual(numGenes));
        }
    }

    // Evalue chaque individu de la population
    void evaluateAll() {
    	int i = 0;
        for (Individual ind : population) {
        	i += 1;
        	System.out.printf("Evaluation %d/%d\r", i, populationSize);
        	ind.setFitness(FitnessCalculator.calculateFitness(ind));
        }
    }

    
    // Génére une nouvelle population à partir d'une population évaluée
    void evolve() {
        population.sort(Comparator.comparingInt(ind -> -ind.getFitness())); // Le moins pour trier ordre décroissant
        List<Individual> newPopulation = new ArrayList<>();
        
        // On prend les plus fort
        for(int i = 0; i < 3; i++) {
        	newPopulation.add(new Individual(population.get(i)));
        }
        // On en génère un nouveau
        newPopulation.add(new Individual(numGenes));
        
        // On en mute deux
        Individual candidate = new Individual(population.get((int)(Math.random() * population.size())));
        candidate.mutate(mutationRate, mutationAmplitude);
        newPopulation.add(candidate);
        candidate = new Individual(population.get((int)(Math.random() * population.size())));
        candidate.mutate(mutationRate, mutationAmplitude);
        newPopulation.add(candidate);
        
        while (newPopulation.size() < populationSize) {
            Individual p1 = tournament();
            Individual p2 = tournament();
            Individual c = p1.crossover(p2);
            
            if(Math.random() < 0.5) {
            	c.mutate(mutationRate, mutationAmplitude);
            }
            newPopulation.add(c);
        }
        population = newPopulation;
    }

    
    // Le but est de selectionner le meilleur Individu parmis 3 tirés au hasard
    Individual tournament() {
        Individual best = null;
        for (int i = 0; i < 3; i++) {
            Individual candidate = population.get((int)(Math.random() * population.size()));
            if (best == null || candidate.getFitness() > best.getFitness()) {
            	best = candidate;
            }    
        }
        return best;
    }

    Individual best() { 
        population.sort(Comparator.comparingInt(ind -> -ind.getFitness()));
        return population.get(0);
    }
}
