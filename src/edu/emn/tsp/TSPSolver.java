/*
	tsp-framework
	Copyright (C) 2012 Fabien Lehu�d� / Damien Prot

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along
	with this program; if not, write to the Free Software Foundation, Inc.,
	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package edu.emn.tsp;

import java.util.ArrayList;

/**
 * 
 * This class is the place where you should enter your code and from which you
 * can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the
 * programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class. The other objects that are
 * created in Main can be accessed through the following TSPSolver attributes: -
 * {@link m_instance} : the Instance object which contains the problem data -
 * {@link m_solution} : the Solution object to modify. This object will store
 * the result of the program. - {@link m_time} : the maximum time limit (in
 * seconds) given to the program.
 * 
 * @author Damien Prot, Fabien Lehuédé 2012
 * 
 */
public class TSPSolver {

	// ---------------------------------------------
	// --------------- ATTRIBUTES ------------------
	// ---------------------------------------------

	/**
	 * The Traveling Salesman Problem Solution that will be returned by the
	 * program
	 */
	private Solution m_solution;

	/** The TSP data. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_time;

	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	// These methods allow to access the class attributes from outside the
	// class.

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTime() {
		return m_time;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one
	 * will be deleted).
	 * 
	 * @param sol
	 *            : new solution
	 */
	public void setSolution(Solution sol) {
		this.m_solution = sol;
	}

	/**
	 * Sets the problem data
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
	public void setTime(long time) {
		this.m_time = time;
	}

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

	// teste chaque plusprochevoisin en fonction des villes de départ et prend
	// le meilleur
	public int meilleuresolution() throws Exception {
		double[] resultats = new double[m_instance.getNbVertices()];
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			this.m_solution.plusprochevoisin(i);
			resultats[i] = m_solution.evaluate();
		}
		int pluspetit = 0;
		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			if (resultats[i] < resultats[pluspetit]) {
				pluspetit = i;
			}
		}
		return pluspetit;
	}

	// Algorithme génétique qui prend une population et l'a fais évoluer
	public void AlgoGenetique(int population, int evolution,
			double initialNearest, double mutationRate,
			double mutationRateNearest, double mutationReverse, int opt)
			throws Exception {
		Population pop = new Population(population, true, m_instance,
				initialNearest);
		// System.err.println("Initial distan: " + pop.getFittest().evaluate());

		GA ga = new GA(m_instance);

		pop = ga.evolvePopulation(pop, initialNearest, mutationRate,
				mutationRateNearest, mutationReverse, opt);
		for (int i = 0; i < evolution; i++) {
			pop = ga.evolvePopulation(pop, initialNearest, mutationRate,
					mutationRateNearest, mutationReverse, opt);
		}

		for (int i = 0; i < m_instance.getNbVertices(); i++) {
			m_solution.setVertexPosition(pop.getFittest().getSolution(i), i);
		}

		m_solution.setVertexPosition(pop.getFittest().getSolution(0),
				m_instance.getNbVertices());

		this.m_solution.VND();
	}
    	
    	public Solution quelsParametres() throws Exception {
    		double ibest = 1;
    		double jbest = 1;
    		double kbest = 1;
    		this.AlgoGenetique(100, 50, 0.9, ibest, jbest, kbest, 1);
    		Solution bestSol = this.m_solution;
    		double best = this.m_solution.evaluate();
    		double i = 0;
    		double j = 0;
    		double k = 0;
    		double increment = 0.34;
    		int nbTest = 1;
    		while (i < 1) {
    			while (j < 1) {
    				while (k < 1) {
    					for (int l = 0; l < nbTest; l++) {
    						this.AlgoGenetique(200, 50, 0.65, i, j, k, 1);
    						if (this.m_solution.evaluate() < best) {
    							best = this.m_solution.evaluate();
    							ibest = i;
    							jbest = j;
    							kbest = k;
    							bestSol = this.m_solution;
    						}
    					}
    					k = k + increment;
    				}
    				j = j + increment;
    			}
    			i = i + increment;
    		}
    		System.err.println("longueur:" + best);
    		System.err.println("mut:" + ibest);
    		System.err.println("mutNear:" + jbest);
    		System.err.println("mutRev:" + kbest);

    		return bestSol;

    	}
    	
    	public Solution meilleuresolutionGenetique2(int population, int evolution,
    			double initialNearest, double mutationRate,
    			double mutationRateNearest, double mutationReverse, int opt)
    			throws Exception {
    		long t = System.currentTimeMillis();
    		long tempspasse = 0;
    		this.AlgoGenetique(population, evolution, initialNearest, mutationRate,
    				mutationRateNearest, mutationReverse, opt);
    		tempspasse = System.currentTimeMillis() - t;

    		ArrayList<Double> resultats = new ArrayList<Double>();
    		ArrayList<Solution> solution = new ArrayList<Solution>();
    		resultats.add(m_solution.evaluate());
    		solution.add(m_solution.copy());
    		
    		long test = 0;
    		
    		while(test<((m_time-5)*1000-tempspasse)) {
    			this.AlgoGenetique(population, evolution, initialNearest,
    					mutationRate, mutationRateNearest, mutationReverse, opt);
    			resultats.add(m_solution.evaluate());
    			//System.out.println(resultats[i]);
    			solution.add(m_solution.copy());
    			test=System.currentTimeMillis() - t;
    		}
    		
    		int pluspetit = 0;
    		Solution best = m_solution;
    		for (int i = 0; i < resultats.size(); i++) {
    			if (resultats.get(i) < resultats.get(pluspetit)) {
    				pluspetit = i;
    				best = solution.get(i);
    			}
    		}

    		//System.err.println("chemin le plus court : " + best.evaluate());
    		//System.err.println("moyenne des chemins : " + moyenne);
    		//System.err.println(solution.size()+" // it�rations");
    		return best;
    	}
    	
    	public Solution meilleuresolutionGrosseInstance2(boolean b) throws Exception {   		
    		m_solution.plusprochevoisin(0);	
    		if (b) { m_solution.VND(); } else { }    		
    		double pluspetit = m_solution.evaluate();
    		Solution soluce = m_solution.copy();
    		long t = System.currentTimeMillis();
         	long tempspasse=0;
         	m_solution.plusprochevoisin(1);	
    		if (b) { m_solution.VND(); } else {  }
    		double minoupas = m_solution.evaluate();
         	tempspasse=System.currentTimeMillis()-t;
         	if (minoupas<pluspetit) {
				soluce = m_solution.copy();
				pluspetit=minoupas;
			}
        	
         	long test = 0;
    		int i=2;
    		while(test<((m_time-10)*1000-tempspasse) && i<m_instance.getNbVertices()) {
        			m_solution.plusprochevoisin(i);	
            		if (b) { m_solution.VND(); } else {  }
        			minoupas = m_solution.evaluate();
        			if (minoupas<pluspetit) {
        				soluce = m_solution.copy();
        				pluspetit=minoupas;
        			}
        			i++;
        			test=System.currentTimeMillis() - t;
        	}	
    		//System.err.println("iterations: "+i);
      		return soluce;
    	}
	
		

	public void solve() throws Exception {
		// meilleuresolutionGenetique(taillepop,iterations, nearestdvvdepart,
		// mutationRate, mutationNearest,mutationReverse,optimisation)
		if (this.m_instance.getNbVertices() < 160) {
			this.setSolution(this.meilleuresolutionGenetique2(100, 50, 0, 0,
					0, 0, 0));
		}
		else {
			if (this.m_instance.getNbVertices() < 250) {
				this.setSolution(this.meilleuresolutionGenetique2(30, 15, 0.9,
						0, 0, 0, 1));
			} else {
				if (this.m_instance.getNbVertices() < 400) {
					this.setSolution(this.meilleuresolutionGrosseInstance2(true));
					m_solution.VNDet3opt();
				}
				else {
					if (this.m_instance.getNbVertices() < 800) {
						this.setSolution(this.meilleuresolutionGrosseInstance2(true));
						m_solution.trois_opt();
						m_solution.VND();
					}
					else {
						if (this.m_instance.getNbVertices() < 2200) {
							this.setSolution(this.meilleuresolutionGrosseInstance2(true));
						}
						else {
							this.setSolution(this.meilleuresolutionGrosseInstance2(false));
						}
					}
				}
			}
		}
	}
}
