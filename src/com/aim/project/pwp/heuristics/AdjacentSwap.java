package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class AdjacentSwap extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public AdjacentSwap(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	/**
	 * In adjacent swap, two locations which are adjacent to each other in the current route are 
	 * switched. The first location should be selected randomly amongst all delivery locations 
	 * and the second delivery location chosen as the location visited after the chosen location. 
	 * Exception if the last location is chosen, then it should be swapped with the first location.
	 */
	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {
		
		// set the number of iterations
		int numOfSwaps = 2^(HeuristicOperators.timesOfIOM(intensityOfMutation) - 1);
		
		// initialisation
		int[] deliveryLocations = solution.getSolutionRepresentation().getSolutionRepresentation();
		int index_1, index_2;
		double delta = 0;
		double newObjectiveFuncVal = solution.getObjectiveFunctionValue();
		
		// repeat a number of times according to intensity of mutation
		for(int i = 0; i < numOfSwaps; i++) {

			// randomly find index_1 and its next index
			index_1 = oRandom.nextInt(deliveryLocations.length);
			index_2 = (index_1 + 1) % deliveryLocations.length;
			
			// use delta evaluation for adjacent swap to calculate the difference
			delta += HeuristicOperators.deltaEvaluationForAS(index_1, index_2, deliveryLocations);

			// after swapping, preserve current representation
			HeuristicOperators.swapLocations(index_1, index_2, deliveryLocations);
			
		}
		
		// set new objective function value
		newObjectiveFuncVal = solution.getObjectiveFunctionValue() + delta;
		solution.setObjectiveFunctionValue(newObjectiveFuncVal);

		// set new solution representation
		solution.getSolutionRepresentation().setSolutionRepresentation(deliveryLocations);
		
		return newObjectiveFuncVal;
		
	}

	@Override
	public boolean isCrossover() {
		return false;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}

}

