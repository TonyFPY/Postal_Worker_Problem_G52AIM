package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class Reinsertion extends HeuristicOperators implements HeuristicInterface {

	private final Random oRandom;
	
	public Reinsertion(Random oRandom) {

		super();
		
		this.oRandom = oRandom;
	}

	/**
	 * A delivery location is selected at random, removed from the current route, 
	 * and re-inserted at a different place.
	 * 
	 * 	- A random delivery location is selected to be reinserted.
	 *  - The selected delivery location can be reinserted into any position in the route.
	 * 	- The selected delivery location is not reinserted at the same position.
	 */
	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// set the number of iterations
		int times = HeuristicOperators.timesOfIOM(intensityOfMutation);
		
		// initialisation
		int[] deliveryLocations = solution.getSolutionRepresentation().getSolutionRepresentation();
		double delta = 0, newObjectiveFuncVal = 0;
		int select,into;
		
		for(int i = 0; i < times; i++) {
			
			// find the indexes
			select = oRandom.nextInt(deliveryLocations.length);
			do {
				into = oRandom.nextInt(deliveryLocations.length);
			} while(select == into);
			
			// update objective function value using delta evaluation for inverse mutation
			delta += HeuristicOperators. deltaEvaluationForReinsertion(select, into, deliveryLocations);
			
			// insert
			deliveryLocations = insert(select, into, deliveryLocations);
		}
		
		// update the path cost and solution representation
		newObjectiveFuncVal = solution.getObjectiveFunctionValue() + delta;
		solution.setObjectiveFunctionValue(newObjectiveFuncVal);
		solution.getSolutionRepresentation().setSolutionRepresentation(deliveryLocations);
		
		return solution.getObjectiveFunctionValue();
	}
	
	private int[] insert(int select, int into, int[] locations) {
		
		// insert 'select' into index 'into' and fill the gap
		// this process can be regarded as swapping until select == into
		if(select < into) {
			while(select != into) {
				HeuristicOperators.swapLocations(select, ++select, locations);
			}
		} else {
			while(select != into) {
				HeuristicOperators.swapLocations(select, --select, locations);
			}
		}
		
		return locations;
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
