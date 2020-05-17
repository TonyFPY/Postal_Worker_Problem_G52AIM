package com.aim.project.pwp.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;


public class CX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public CX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface solution, double depthOfSearch, double intensityOfMutation) {

		// invalid operation and just return the same solution!
		return solution.getObjectiveFunctionValue();
	}

	/**
	 * The following code is the implementation of CX.
	 */
	@Override
	public double apply(PWPSolutionInterface p1, PWPSolutionInterface p2,
			PWPSolutionInterface c, double depthOfSearch, double intensityOfMutation) {
		
		// set the number of iterations
		int times = HeuristicOperators.timesOfIOM(intensityOfMutation);
		
		// initialisation
		int[] parent1 = p1.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] parent2 = p2.getSolutionRepresentation().getSolutionRepresentation().clone();
		int[] child1 = new int[parent1.length];
		int[] child2 = new int[parent2.length];
		
		for(int i = 0; i < times; i++) {
			
			// apply CX
			child1 = cycleCrossover(parent1, parent2, child1);
			child2 = cycleCrossover(parent2, parent1, child2);
			
			// set the children of current loop to be the parents of the next loop
			parent1 = child1.clone();
			parent2 = child2.clone();
		}
		
		// randomly select the child among two produced candidates
		int[] childToBeSelected  = (oRandom.nextBoolean()) ? parent1 : parent2;
		
		// update representation and its corresponding cost
		c.getSolutionRepresentation().setSolutionRepresentation(childToBeSelected);
		c.setObjectiveFunctionValue(oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		
		return c.getObjectiveFunctionValue();
	}

	/**
	 * We build offspring by
	 * 	- choosing each city and its position from one of the parents 
	 * 	- and when a cycle is completed, the remaining cities filled in from the other parent 
	 * 
	 * if there are multiple cycles, only the first cycle is needed for this implementation.
	 * 
	 * For example:
	 * parent 1: 1,2,3,4,5,6,7,8,9
	 * parent 2: 4,1,2,8,7,6,9,3,5
	 * =>
	 * child1: 1,2,3,4,7,6,9,8,5
	 * child2: 4,1,2,8,5,6,7,3,9
	 * 
	 * @param parent1
	 * @param parent2
	 * @param c
	 * @return a child after CX
	 */
	public int[] cycleCrossover(int[] parent1, int[] parent2, int[] c) {
		
		c = parent2.clone();
		
		// randomly pick an index
		int startingP = oRandom.nextInt(parent1.length);
		int s = startingP;
				
		// only do the first cycle if there are multiple cycles
		do {
			c[s] = parent1[s];
			s = getID(parent2[s], parent1);	
		} while(s != startingP);
	
		return c;
	}
	
	public int getID(int num, int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(num == arr[i]) {
				return i;
			}
		}
		
		return -1;
	}
	
	@Override
	public boolean isCrossover() {
		return true;
	}

	@Override
	public boolean usesIntensityOfMutation() {
		return true;
	}

	@Override
	public boolean usesDepthOfSearch() {
		return false;
	}


	@Override
	public void setObjectiveFunction(ObjectiveFunctionInterface oObjectiveFunction) {
		
		this.oObjectiveFunction = oObjectiveFunction;
	}
}
