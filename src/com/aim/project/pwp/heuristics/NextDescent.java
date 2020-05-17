package com.aim.project.pwp.heuristics;


import java.util.Random;

import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;


/**
 * 
 * @author Warren G. Jackson
 * Performs adjacent swap, returning the first solution with strict improvement
 *
 */
public class NextDescent extends HeuristicOperators implements HeuristicInterface {
	
	private final Random oRandom;
	
	public NextDescent(Random oRandom) {
	
		super();
		
		this.oRandom = oRandom;
	}

	/**
	 * Next descent tries perturbations of the solution one-by-one and chooses the first of 
	 * such which generates an improvement (strict improvement; OI) in the solution (if any).
	 * When Next Descent is applied, it is usually applied from the starting delivery location 
	 * up and makes its way sequentially through the route until it makes its way back to the 
	 * start. That is, the final delivery location is swapped with the first delivery location. 
	 * This will favour swapping delivery locations earlier in the tour. 
	 */
	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		
		// set the number of iterations
		int times = HeuristicOperators.timesOfDOS(dDepthOfSearch);
		
		// initialisation
		int[] deliveryLocations = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		int currentID = oRandom.nextInt(deliveryLocations.length);
		int nextID = (currentID + 1) % deliveryLocations.length;
		int count= 0;
		double currentFitness = oSolution.getObjectiveFunctionValue(); 
		double delta = 0;
		
		while(count != deliveryLocations.length && times > 0) { 
			
			// calculate the difference of cost using delta evaluation
			delta = HeuristicOperators.deltaEvaluationForAS(currentID, nextID, deliveryLocations);
			
			// check whether to accept the new solution
			if(currentFitness + delta < currentFitness) {
				
				currentFitness += delta;
				HeuristicOperators.swapLocations(currentID, nextID, deliveryLocations);				
				times--;
				count = -1; // in the next step, count++, count will be reset to 0
			}
			
			count++;
			currentID = nextID;
			nextID = (currentID + 1) % deliveryLocations.length;
			
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
