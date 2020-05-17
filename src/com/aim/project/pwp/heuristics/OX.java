package com.aim.project.pwp.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;


public class OX implements XOHeuristicInterface {
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction;

	public OX(Random oRandom) {
		
		this.oRandom = oRandom;
	}

	@Override
	public double apply(PWPSolutionInterface oSolution, double dDepthOfSearch, double dIntensityOfMutation) {
		
		// invalid operation and just return the same solution!
		return oSolution.getObjectiveFunctionValue();
	}

	/**
	 * The following code is the implementation of OX
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
		
		// repeat the OX process according to the intensity of mutation
		for(int i = 0; i < times; i++) {
			
			// apply OC
			child1 = orderedCrossover(parent1, parent2, child1);
			child2 = orderedCrossover(parent2, parent1, child2);
			
			// make the children of this iteration the parents of the next
			parent1 = child1.clone();
			parent2 = child2.clone();
		}
		
		// randomly select the child among two produced candidates
		int[] childToBeSelected = (oRandom.nextBoolean() == true) ? parent1 : parent2;

		// update representation and its corresponding cost
		c.getSolutionRepresentation().setSolutionRepresentation(childToBeSelected);
		c.setObjectiveFunctionValue(oObjectiveFunction.getObjectiveFunctionValue(c.getSolutionRepresentation()));
		
		return c.getObjectiveFunctionValue();
	}
	
	/**
	 * We build offspring by
	 * 		-	choosing a subsequence of a tour from one parent 
	 * 		-	preserving relative order of cities from other parent 
	 * 
	 * It exploits the property that ordering of cities important, not position
	 * i.e. 1-2-3-4 and 2-3-4-1 are identical
	 * 
	 * For example
	 * parent 1: 1,2,3,4,5,6,7,8,9
	 * parent 2: 4,5,2,1,8,7,6,9,3
	 * =>
	 * child 1: 2,1,8,4,5,6,7,9,3
	 * child 2: 3,4,5,1,8,7,6,9,2
	 * 
	 * @param parent1
	 * @param parent2
	 * @param c
	 * @return
	 */
	public int[] orderedCrossover(int[] parent1, int[] parent2, int[] c) {
		
		// The following steps make sure that left <= right 
		// and O and N cannot be the cut points at the same time
		int left = 0, right = 0;
		do {
			right = oRandom.nextInt(parent1.length - 1) + 1;
			left = oRandom.nextInt(right);
		} while (left == 0 && right == parent1.length - 1);
		
		// record which elements have been filled in the child array
		int[] subC = Arrays.copyOfRange(parent1, left, right + 1);
		for(int i = left; i <= right; i++) {
			c[i] = parent1[i];
		}
		
		int p = right + 1;
		int q = p;
		do {
			// if subC contains the element, move to the next index; 
			// otherwise assign it
			if(!HeuristicOperators.arrContain(subC, parent2[q % parent2.length])) {
				c[p % parent1.length] = parent2[q % parent2.length];
				p++;
			} 
			q++;
			
		} while(p % parent1.length != left);
		
		return c;
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
	public void setObjectiveFunction(ObjectiveFunctionInterface f) {
		
		this.oObjectiveFunction = f;
	}
}
