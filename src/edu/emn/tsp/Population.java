package edu.emn.tsp;


public class Population {
	
	// Population de chemins, ou "tours"
	Solution[] tours;

	// Construit une population de d√©part ou une simple nouvelle population
	public Population(int populationSize, boolean initialise, Instance inst,
			double initialNearest) throws Exception {
		tours = new Solution[populationSize];
		if (initialise) {
			for (int i = 0; i < populationSize(); i++) {
				Solution newTour = new Solution(inst);
				newTour.generateIndividual(initialNearest);
				saveTour(i, newTour);
			}
		}
	}

	public void saveTour(int index, Solution tour) {
		tours[index] = tour;
	}

	public Solution getTour(int index) {
		return tours[index];
	}

	// Prend la meileure solution de la population
	public Solution getFittest() throws Exception {
		Solution fittest = tours[0];
		for (int i = 1; i < populationSize(); i++) {
			if (this.getTour(i).evaluate() < fittest.evaluate()) {
				fittest = getTour(i);
			}
		}
		return fittest;
	}

	public int populationSize() {
		return tours.length;
	}
	
	
	// méthode qui retourne l'indice de la solution "tour1" dans la population (utile
			// pour le evolvePopulation)
			public int getIndiceTour(Solution tour1) throws Exception {
				int indice = 0;
				for (int i = 0; i < this.populationSize(); i++) {
					if (this.getTour(i) == tour1) {
						indice = i;
					}
				}

				return indice;
			}

}
