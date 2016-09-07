package edu.emn.tsp;

import java.util.ArrayList;

public class GA {
	// ---------------------------------------------
	// --------------- ATTRIBUTES ------------------
	// ---------------------------------------------

	/**
	 * The Traveling Salesman Problem Solution that will be returned by the
	 * program
	 */

	/** The TSP data. */
	private Instance m_instance;

	/** Time given to solve the problem. */

	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	// These methods allow to access the class attributes from outside the
	// class.

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one
	 * will be deleted).
	 * 
	 * @param sol
	 *            : new solution
	 * 
	 * 
	 *            /** Sets the problem data
	 * 
	 * @param inst
	 *            : the Instance object which contains the data.
	 */
	public void setInstance(Instance inst) {
		this.m_instance = inst;
	}

	/**
	 * Sets the time limit (in seconds).
	 * 
	 * @param time
	 *            : time given to solve the problem
	 */

	// --------------------------------------
	// -------------- METHODS ---------------
	// --------------------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using
	 * <code>System.out.print()</code> or <code>System.out.println()</code>).
	 * This output is dedicated to the result analyzer that will be used to
	 * evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (<code>System.err.print()</code> or
	 * <code>System.err.println()</code>).
	 * 
	 * When your algorithm terminates, make sure the attribute
	 * {@link m_solution} in this class points to the solution you want to
	 * return.
	 * 
	 * You have to make sure that your algorithm does not take more time than
	 * the time limit {@link m_time}.
	 * 
	 * @throws Exception
	 *             May return some error, in particular if some vertices index
	 *             are wrong.
	 */

	public GA(Instance inst) {
		this.m_instance = inst;
	}

	// Fait évoluer la population sur une génération
	public Population evolvePopulation(Population pop, double initialNearest,
			double mutationRate, double mutationRateNearest,
			double mutationReverse, int opt) throws Exception {
		Population newPopulation = new Population(pop.populationSize(), false,
				this.m_instance, initialNearest);

		// on garde le meilleur tour
		newPopulation.saveTour(0, pop.getFittest());

		// On crée une nouvelle population à l'aide du crossover
		for (int i = 1; i < newPopulation.populationSize(); i++) {
			Solution parent1 = pop.getFittest();
			//Solution parent1 = tournamentSelection(pop, initialNearest);
			Solution parent2 = tournamentSelection(pop, initialNearest);
			//Solution parent2 =pop.getTour(i);

			Solution child = crossover1(parent1, parent2);

			// selon la taille de la population on applique un DVV ou un deux
			// opt
			if (opt == 0) {
				child.VND();
			} else if (opt == 1) {
				child.deux_opt();
			}

			newPopulation.saveTour(i, child);
		}

		// On applique des mutations à la population pour sortir des minimums
		// locaux
		for (int i = 1; i < newPopulation.populationSize(); i++) {
			mutate(newPopulation.getTour(i), mutationRate, mutationRateNearest,
					mutationReverse);
		}

		return newPopulation;
	}
	
	// Fait �voluer la population sur une g�n�ration
			public Population evolvePopulation2(Population pop, double initialNearest,
					double mutationRate, double mutationRateNearest,
					double mutationReverse, int opt) throws Exception {
				Population newPopulation = new Population(pop.populationSize(), false,
						this.m_instance, initialNearest);

				// on garde le meilleur tour
				int meilleurtourpos=pop.getIndiceTour(pop.getFittest());

				for (int i = 0; i < meilleurtourpos; i++) {
					Solution parent1 = pop.getFittest();
					Solution parent2 = pop.getTour(i);

					Solution child = crossover2(parent1, parent2);

					// selon la taille de la population on applique un DVV ou un deux
					// opt
					if (opt == 0) {
						child.VND();
					} else if (opt == 1) {
						child.deux_opt();
					}
					
					newPopulation.saveTour(i, child);
					// On applique des mutations � la population pour sortir des minimums
					// locaux
					mutate(newPopulation.getTour(i), mutationRate, mutationRateNearest,
							mutationReverse);
				}
				for (int i = meilleurtourpos+1; i < pop.populationSize(); i++) {
					Solution parent1 = pop.getFittest();
					Solution parent2 = pop.getTour(i);

					Solution child = crossover2(parent1, parent2);

					// selon la taille de la population on applique un DVV ou un deux
					// opt
					if (opt == 0) {
						child.VND();
					} else if (opt == 1) {
						child.deux_opt();
					}
					newPopulation.saveTour(i, child);
					mutate(newPopulation.getTour(i), mutationRate, mutationRateNearest,
							mutationReverse);
				}
				newPopulation.saveTour(meilleurtourpos, pop.getFittest());

				return newPopulation;
			}

	// On crée un enfant à partir de deux parents dont l'un (parent1) est la
	// meilleure solution actuelle
	// Dans cette version 1, on prend une partie aléatoire du parent1 puis on
	// complète par le parent2
	public Solution crossover1(Solution parent1, Solution parent2)
			throws Exception {
		Solution enfant = new Solution(m_instance);

		int startPos = (int) (Math.random() * m_instance.getNbVertices() - 1);
		int endPos = (int) (Math.random() * m_instance.getNbVertices() - 1);
		while (endPos == startPos) {
			endPos = (int) (Math.random() * m_instance.getNbVertices() - 1);
		}
		if (endPos < startPos) {
			int transi = endPos;
			endPos = startPos;
			startPos = transi;
		}
		// On ajoute le sous-tour à l'enfant
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			if (i >= startPos && i <= endPos) {
				enfant.setVertexPosition(parent1.getSolution(i), i);
			}
		}

		// On complète par le parent2
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			// Si l'enfant n'a pas encore la ville on l'ajoute
			if (!enfant.contains(parent2.getSolution(i))) {
				// On trouve une position libre pour l'ajouter
				int j = 0;
				while (!(enfant.getSolution(j) == 0)) {
					j++;
				}
				enfant.setVertexPosition(parent2.getSolution(i), j);
			}
		}
		return enfant;
	}

	// On crée un enfant à partir de deux parents dont l'un est la meilleure
	// solution actuelle
	// Dans cette version 2, tous les edges communs sont gardés puis on complète
	// avec le parent2
	public Solution crossover2(Solution parent1, Solution parent2)
			throws Exception {
		Solution enfant = new Solution(m_instance);

		// garde les noeuds communs des deux parents
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			// on regarde si la ville suivante l'est aussi dans le parent2 à
			// l'indice d'après
			if (parent1.getSolution(i + 1) == parent2.getSolution(parent2
					.getIndice(parent1.getSolution(i)) + 1)) {
				enfant.setVertexPosition(parent1.getSolution(i), i);
				enfant.setVertexPosition(parent1.getSolution(i + 1), i + 1);
				i++;
			} else {
				// si la ville se trouve en première position dans le parent2,
				// le précédent n'existe pas...
				if (parent2.getIndice(parent1.getSolution(i)) == 0) {
					if (parent1.getSolution(i + 1) == parent2
							.getSolution(m_instance.getNbVertices() - 1)) {
						enfant.setVertexPosition(parent1.getSolution(i), i);
						enfant.setVertexPosition(parent1.getSolution(i + 1),
								i + 1);
						i++;
					}
				} else {
					// on regarde si la ville suivante l'est aussi dans le
					// parent2 à l'indice d'avant
					if (parent1.getSolution(i + 1) == parent2
							.getSolution(parent2.getIndice(parent1
									.getSolution(i)) - 1)) {
						enfant.setVertexPosition(parent1.getSolution(i), i);
						enfant.setVertexPosition(parent1.getSolution(i + 1),
								i + 1);
						i++;
					}
				}
			}
		}

		// On complète par le parent2
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			// Si l'enfant n'a pas encore la ville on l'ajoute
			if (!enfant.contains(parent2.getSolution(i))) {
				// On trouve une position libre pour l'ajouter
				int j = 0;
				while (!(enfant.getSolution(j) == 0)) {
					j++;
				}
				enfant.setVertexPosition(parent2.getSolution(i), j);
			}
		}
		return enfant;
	}

	// Mutation du tour
	// mutationRate : échange aléatoirement deux villes du tour
	// mutationRateNearest : échange une ville aléatoire avec sa plus proche
	// voisine
	// mutationReverse : renverse une partie aléatoire du chemin
	public void mutate(Solution tour, double mutationRate,
			double mutationRateNearest, double mutationReverse)
			throws Exception {
		for (int tourPos1 = 0; tourPos1 < m_instance.getNbVertices(); tourPos1++) {

			int tourPos2 = 0;
			if (Math.random() < mutationReverse) {
				tourPos2 = (int) (m_instance.getNbVertices() * Math.random());
				;

				tour.reverse(tourPos1, tourPos2);
			}

			if (Math.random() < mutationRate) {
				tourPos2 = (int) (m_instance.getNbVertices() * Math.random());

				int city1 = tour.getSolution(tourPos1);
				int city2 = tour.getSolution(tourPos2);

				tour.setVertexPosition(city1, tourPos2);
				tour.setVertexPosition(city2, tourPos1);
			}

			if (Math.random() < mutationRateNearest) {
				tourPos2 = tour.villeVoisine(tourPos1);

				int city1 = tour.getSolution(tourPos1);
				int city2 = tour.getSolution(tourPos2);

				tour.setVertexPosition(city1, tourPos2);
				tour.setVertexPosition(city2, tourPos1);
			}

		}
	}

	// Selectionne des candidats "parent2" pour le crossover
	// on sélectionne 10 tours aléatoirement et on prend le meilleur
	public Solution tournamentSelection(Population pop, double initialNearest)
			throws Exception {

		Population tournament = new Population(10, false, this.m_instance,
				initialNearest);

		for (int i = 0; i < 10; i++) {
			int randomId = (int) (Math.random() * pop.populationSize());
			tournament.saveTour(i, pop.getTour(randomId));
		}

		Solution fittest = tournament.getFittest();
		return fittest;
	}

}