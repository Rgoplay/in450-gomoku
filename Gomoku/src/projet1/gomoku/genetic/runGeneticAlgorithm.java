package projet1.gomoku.genetic;

/**
 * Classe permaettant d'éxecuter un algo génétique
 */
public class runGeneticAlgorithm {
    public static void main(String[] args) {
    	
        GeneticAlgorithm ga = new GeneticAlgorithm();

        // Nombre de générations
        for (int gen = 0; gen < 40; gen++) {
            ga.evaluateAll();
            Individual best = ga.best();
            System.out.println("Gen " + gen + " | Best fitness=" + best.getFitness());
            ga.evolve();
            System.out.println("Best genes: ");
            for (int g : ga.best().getGenes()) {
            	System.out.printf("%d, ", g);
            }
            System.out.println("");
        }

        System.out.println("Best genes: ");
        for (int g : ga.best().getGenes()) {
        	System.out.printf("%d, ", g);
        }
        System.out.println("");
    }
}

