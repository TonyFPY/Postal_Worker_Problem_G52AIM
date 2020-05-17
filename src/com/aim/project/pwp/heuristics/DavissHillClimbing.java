package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class DavissHillClimbing extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public DavissHillClimbing(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	/**
	 * Davis’s Hill Climbing, like Davis’s Bit Hill Climbing, performs a sequence of perturbations, 
	 * persisting any which results in an improvement in the solution. For the perturbation operator, 
	 * Here Adjacent Swap is used. The ordering of the sequence should be randomised such that the 
	 * order in which the delivery locations are tried is not the order of the current route. I 
	 * should persist any swap which results in an improving or equal quality solution.
	 */
	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

		// set the number of iterations
		int times = HeuristicOperators.timesOfDOS(dDepthOfSearch);
		
		int[] deliveryLocations = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int index_1, index_2;
		double currentFitness = oSolution.getObjectiveFunctionValue();
		double delta;
		
		// Permutation initialisation
		ArrayList<Integer> permutation = new ArrayList<Integer>();								
		for(int i = 0; i < deliveryLocations.length - 1; i++) {
			permutation.add(i);			
		}
		
		while(times > 0) {
			
			// shuffle the permutation for every loop
			Collections.shuffle(permutation, oRandom);	
			
			for(int i = 0; i < permutation.size(); i++) {
				index_1 = permutation.get(i);
				index_2 = (index_1 + 1) % deliveryLocations.length;
				
				// calculate the fitness of the candidate solution using delta evaluation
				delta = HeuristicOperators.deltaEvaluationForAS(index_1, index_2, deliveryLocations);
				
				// check whether to accept the solution
				if(currentFitness + delta <= currentFitness) {
					currentFitness += delta;
					HeuristicOperators.swapLocations(index_1, index_2, deliveryLocations);
				}
			}
			
			times--;
		}
		
		// update the path cost and solution representation
		oSolution.setObjectiveFunctionValue(currentFitness);
		oSolution.getSolutionRepresentation().setSolutionRepresentation(deliveryLocations);
		
		return oSolution.getObjectiveFunctionValue();
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return false;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return true;
	}
}
