package com.aim.project.pwp.hyperheuristics;

import java.util.Arrays;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;

public class SCYPF1_HH extends HyperHeuristic{
	
	public int[] mutationHeuristics,LSHeuristics;
	public long[] mutationScores,LSScores;
	
	public SCYPF1_HH(long seed) {
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {
		
		// problem initial setting
		problem.initialiseSolution(0);
		problem.setIntensityOfMutation(0.2);
		problem.setDepthOfSearch(0.2);
		
		// create an array to store IDs of mutation heuristics and local search heuristics respectively
		mutationHeuristics = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		LSHeuristics = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		
		// create arrays to maintain scores of each heuristic
		mutationScores = new long[mutationHeuristics.length];
		LSScores = new long[LSHeuristics.length];
		
		// set the all the score to 0 initially
		Arrays.fill(mutationScores, 0);
		Arrays.fill(LSScores, 0);
	
		// parameter initialisation
		long numOfIteration = 0;
		int parentIndex = 0, childIndex = 1;
		int bestMutationHeuristicID, bestLSHeuristicID;
		double currentFitness, candidateFitness, tempFitness, temperature;
		currentFitness = candidateFitness = tempFitness = temperature = problem.getFunctionValue(0);
		
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");
		
		// repeat searching the solution until time's out
		while(!hasTimeExpired()) {
			
			/*
			 * Stage 1
			 * Perform mutation with highest score heuristic
			 * Update score based on that mutation heuristic performance
			 */
			bestMutationHeuristicID = getHeuristicIDwithHighScore(mutationScores);
			tempFitness = problem
					.applyHeuristic(mutationHeuristics[bestMutationHeuristicID], parentIndex, childIndex);
	
			/* 
			 * Give corresponding Mutation heuristic grade according to its performance
			 * if there is an improvement
			 * 		increase the score by 1
			 * else 
			 * 		decrease the score by 1
			 */
			if(tempFitness < currentFitness)
				mutationScores[bestMutationHeuristicID]++;
			else
				mutationScores[bestMutationHeuristicID]--;
			
			/* 
			 * Stage 2
			 * Perform local search with highest score heuristic
			 * Update score based on that heuristic performance
			 */
			bestLSHeuristicID = getHeuristicIDwithHighScore(LSScores);
			candidateFitness = problem
					.applyHeuristic(LSHeuristics[bestLSHeuristicID], childIndex, childIndex);
			
			/*
			 * Move acceptance
			 * Accept if solution is non-worsening or with Boltzmann probability
			 */
			if(candidateFitness <= currentFitness || 
					rng.nextDouble() < getProbability(candidateFitness - currentFitness, temperature)) {
				
				/* 
				 * Give corresponding Local Search heuristic grade according to its performance
				 * if there is an improvement
				 * 		increase the score by 1
				 * else 
				 * 		decrease the score by 1
				 */
				if(candidateFitness < currentFitness)
					LSScores[bestLSHeuristicID]++;
				else
					LSScores[bestLSHeuristicID]--;
				
				/* 
				 * Update the child and parent index
				 * the current child index becomes parent index in next loop
				 * the current parent index will be the child index in next loop
				 * e.g. 0 -> 1; 1 -> 0
				 */
				parentIndex = (parentIndex + 1) % 2;
				childIndex = (childIndex + 1) % 2;
				
				// Update currentFitness
				currentFitness = candidateFitness;
			} 
			
			// Lundy-Mees cooling schedule
			temperature = temperature / (1 + 0.0001 * temperature);
			
			numOfIteration++;

		} 
		
		// print result 
		PWPSolutionInterface bestSolution = ((AIM_PWP) problem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("output.txt");
		oSP.printSolution( ((AIM_PWP) problem).oInstance.getSolutionAsListOfLocations(bestSolution));
		System.out.println(String.format("Total iterations = %d", numOfIteration));
	}
	
	/**
	 * Gets the heuristic ID with the highest score
	 * @param arr
	 * @return
	 */
	private int getHeuristicIDwithHighScore(long[] arr) {
		
		int bestID = 0;
		for(int i = 1; i < arr.length; i++) {
			if(arr[i] > arr[bestID]) {
				bestID = i;
			}
		}
		return bestID;
	}

	/**
	 * Gets the Boltzmann probability
	 * @param delta
	 * @param temperature
	 * @return
	 */
	private double getProbability(double delta, double temperature) {
		return Math.exp(- delta/temperature);
	}
	
	@Override
	public String toString() {
		
		return "scypf1's Hyper-Hueristic";
	}

}
