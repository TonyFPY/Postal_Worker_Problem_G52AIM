package com.aim.project.pwp.heuristics;

import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


public class InversionMutation extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public InversionMutation(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	/**
	 * In inversion mutation, two delivery locations are selected at random. l_a and l_b are ordered 
	 * based on their position in the current route. That is, the index of the first selected 
	 * location l_a appears before l_b. The route between these delivery locations is preserved but 
	 * reversed.
	 */
	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {

		// set the number of iterations
		int times = HeuristicOperators.timesOfIOM(dIntensityOfMutation);
		
		// initialisation
		int[] deliveryLocations = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int index_1, index_2;
		double delta = 0;
		double newObjectiveFuncVal = oSolution.getObjectiveFunctionValue();
		
		for(int i = 0; i < times; i++) {

			// The following step is a bit mathematical, 
			// which makes sure that index_1 appears before index_2
			index_2 = oRandom.nextInt(deliveryLocations.length - 1) + 1;
			index_1 = oRandom.nextInt(index_2);
			
			// update objective function value using delta evaluation for inverse mutation
			delta += HeuristicOperators.deltaEvaluationForIM(index_1, index_2, deliveryLocations);
			
			// Inversion. This can be regarded as swapping elements from both sides
			while(index_1 < index_2) {
				HeuristicOperators.swapLocations(index_1, index_2, deliveryLocations);
				index_1 ++;
				index_2 --;
			}
		}
		
		// update the path cost and solution representation
		newObjectiveFuncVal = oSolution.getObjectiveFunctionValue() + delta;
		oSolution.setObjectiveFunctionValue(newObjectiveFuncVal);
		oSolution.getSolutionRepresentation().setSolutionRepresentation(deliveryLocations);
		
		return oSolution.getObjectiveFunctionValue();
		
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
