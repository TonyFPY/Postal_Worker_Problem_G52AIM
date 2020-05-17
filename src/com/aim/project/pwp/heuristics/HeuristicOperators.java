package com.aim.project.pwp.heuristics;

import java.util.ArrayList;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.pwp.instance.Pair;

public class HeuristicOperators {

	private static ObjectiveFunctionInterface oObjectiveFunction;

	public HeuristicOperators() {

	}

	public void setObjectiveFunction(ObjectiveFunctionInterface f) {

		oObjectiveFunction = f;
	}

	/**
	 * TODO implement any common functionality here so that your
	 * 			heuristics can reuse them!
	 * E.g.  you may want to implement the swapping of two delivery locations here!
	 */
	
	/**
	 * swaps the two selected locations
	 * @param index_1
	 * @param index_2
	 * @param deliveryLocations
	 */
	public static void swapLocations(int index_1, int index_2, int[] deliveryLocations) {
		int temp = deliveryLocations[index_1];
		deliveryLocations[index_1] = deliveryLocations[index_2];
		deliveryLocations[index_2] = temp;
	}
	
	/**
	 * produces pairs of all possible adjacent delivery locations indexes 
	 * @param deliveryLocations
	 * @return
	 */
	public static ArrayList<Pair> produceAllPossibleAdjacentPairs(int[] deliveryLocations) {
		
		ArrayList<Pair> possiblePairs = new ArrayList<Pair>();
		
		for(int i = 0; i < deliveryLocations.length - 1; i++) {
			possiblePairs.add(new Pair(deliveryLocations[i], deliveryLocations[i + 1]));
		}
		 
		return possiblePairs;
	}
	
	/**
	 * Calculates the cost between two adjacent address
	 * @param pairs
	 * @return
	 */
	public static double pairCost(ArrayList<Pair> pairs) {
		
		double cost = 0;
		
		for(Pair pair : pairs) {
			cost += oObjectiveFunction.getCost(pair.getX(), pair.getY()); 
		}
		
		return cost;
	}
	
	/**
	 * Uses delta evaluation to calculate the cost after adjacent swap
	 * 
	 * Firstly. we can consider the general cases and think about the paths to be changed;
	 * Then, we can take special cases into account
	 * 		- index_1 == deliveryLocations.length - 1 && index_2 == 0
	 * 		- index_1 == 0
	 * 		- index_2 == deliveryLocations.length - 1
	 * We can write down calculating expressions about the new paths and paths to be removed and 
	 * then decide which expression to be used in each case
	 * 
	 * @param index_1
	 * @param index_2
	 * @param deliveryLocations
	 * @return
	 */
	public static double deltaEvaluationForAS(int index_1, int index_2, int[] deliveryLocations) {
		
		double delta = 0;
		
		double oldPathA = oObjectiveFunction.getCost(deliveryLocations[index_2], deliveryLocations[(index_2 + 1) % deliveryLocations.length]); 
		
		double oldPathB = oObjectiveFunction.getCost(deliveryLocations[Math.floorMod(index_1 - 1, deliveryLocations.length)], deliveryLocations[index_1]); 
		
		double newPathA = oObjectiveFunction.getCost(deliveryLocations[index_1], deliveryLocations[(index_2 + 1) % deliveryLocations.length]); 
		
		double newPathB = oObjectiveFunction.getCost(deliveryLocations[Math.floorMod(index_1 - 1, deliveryLocations.length)], deliveryLocations[index_2]);
		
		if(index_1 == deliveryLocations.length - 1 && index_2 == 0) {
			double before = oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_2]) + oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_1]);
			double after = oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_1]) + oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_2]);
			delta = (newPathA + newPathB) - (oldPathA + oldPathB) + (after - before);
		} else if(index_1 == 0) {
			delta = newPathA - oldPathA + oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_2]) - oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_1]);
		} else if(index_2 == deliveryLocations.length - 1) {
			delta = newPathB - oldPathB + oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_1]) - oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_2]);
		} else {
			delta = (newPathA + newPathB) - (oldPathA + oldPathB);
		}
		
		return delta;
	}
	
	/**
	 * Uses delta evaluation to calculate the cost after inversion mutation.
	 * 
	 * Here, we only need to care about the distance between the location A and its previous 
	 * Location, location B and its post Location, locationB and previous Location 
	 * of A, location A and post Location of B. The distance between two selected locations 
	 * is not changed.
	 * 
	 * For example,
	 * 		old solution: 1,2,3,4,5,6
	 * 		index_1: 2
	 * 		index_2: 5
	 * 		new solution: 1,5,4,3,2,6
 	 *		paths to be removed: 1 -> 2, 5 -> 6
	 *		paths to be created: 1 -> 5, 2 -> 6
	 * 		
	 * 
	 * @param index_1
	 * @param index_2
	 * @param deliveryLocations
	 * @return
	 */
	public static double deltaEvaluationForIM(int index_1, int index_2, int[] deliveryLocations) {
		
		double delta = 0;
		
		if(index_1 == 0) {
			delta += oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_2]) - oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[index_1]);
		} else {
			delta += oObjectiveFunction.getCost(deliveryLocations[index_2], deliveryLocations[index_1 - 1]) - oObjectiveFunction.getCost(deliveryLocations[index_1], deliveryLocations[index_1 - 1]);
		}
		
		if(index_2 == deliveryLocations.length - 1) {
			delta += oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_1]) - oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[index_2]);
		} else {
			delta += oObjectiveFunction.getCost(deliveryLocations[index_1], deliveryLocations[index_2 + 1]) - oObjectiveFunction.getCost(deliveryLocations[index_2], deliveryLocations[index_2 + 1]);
		}
		
		return delta;
	}
	
	/**
	 * Uses delta evaluation to calculate the cost after re-insertion
	 * 
	 * For example:
	 *			Original array: 1 2 3 4 5 6
	 *			select: 5
	 *			into: 1
	 *			Re-insertion array: 5 1 2 3 4 6
	 *			paths to be removed: 4 -> 5, 5 -> 6, depot -> 1
	 *			paths to be created: 4 -> 6, 5 -> 1, depot -> 5
	 *
	 * @param select
	 * @param into
	 * @param deliveryLocations
	 * @return
	 */
	public static double deltaEvaluationForReinsertion(int select, int into, int[] deliveryLocations) {
		
		// if 'select' and 'into' are adjacent,
		// just regard it as adjacent swap
		if(select - into == 1) {
			return deltaEvaluationForAS(into, select, deliveryLocations);
		}
		
		if(into - select == 1) {
			return deltaEvaluationForAS(select, into, deliveryLocations);
		}
		
		double delta = 0;
		double newPath, oldPath;
		
		// we need classify the cases here
		if(select < into) {
			if(select == 0) {
				delta += oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[select + 1]) 
					   - oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[select])
					   - oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[select + 1]);
			} else {
				newPath = oObjectiveFunction.getCost(deliveryLocations[select - 1], deliveryLocations[select + 1]);
				oldPath = oObjectiveFunction.getCost(deliveryLocations[select - 1], deliveryLocations[select]) 
						+ oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[select + 1]);
				delta += newPath - oldPath;
			}
			
			if(into == deliveryLocations.length - 1) {
				delta += oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[select]) 
					   + oObjectiveFunction.getCost(deliveryLocations[into], deliveryLocations[select])
					   - oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[into]);
			} else {
				newPath = oObjectiveFunction.getCost(deliveryLocations[into], deliveryLocations[select]) 
						+ oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[into + 1]);
				oldPath = oObjectiveFunction.getCost(deliveryLocations[into], deliveryLocations[into + 1]);
				
				delta += newPath - oldPath;
			}
		} else {
			if(into == 0) {
				delta += oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[select]) 
					   + oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[into])
					   - oObjectiveFunction.getCostBetweenDepotAnd(deliveryLocations[into]);
			} else {
				newPath = oObjectiveFunction.getCost(deliveryLocations[into - 1], deliveryLocations[select]) 
						+ oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[into]);
				oldPath = oObjectiveFunction.getCost(deliveryLocations[into - 1], deliveryLocations[into]);
				delta += newPath - oldPath;
			}
			
			if(select == deliveryLocations.length - 1) {
				delta += oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[select - 1]) 
					   - oObjectiveFunction.getCost(deliveryLocations[select - 1], deliveryLocations[select])
					   - oObjectiveFunction.getCostBetweenHomeAnd(deliveryLocations[select]);
			} else {
				newPath = oObjectiveFunction.getCost(deliveryLocations[select - 1], deliveryLocations[select + 1]);
				oldPath = oObjectiveFunction.getCost(deliveryLocations[select - 1], deliveryLocations[select]) 
						+ oObjectiveFunction.getCost(deliveryLocations[select], deliveryLocations[select + 1]);
				delta += newPath - oldPath; 
			}
		}
		
		return delta;
	}
	
	/**
	 * this method is used to check whether my implementation of delta evaluation is correct.
	 * Here I obtain the difference between new solution cost and the old one
	 * 
	 * @param newLocations
	 * @param oldLocations
	 * @return
	 */
	public static double checkDeltaEvaluation(int[] newLocations, int[] oldLocations) {
		
		double delta = 0;
		
		delta += oObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(newLocations))
			 - oObjectiveFunction.getObjectiveFunctionValue(new SolutionRepresentation(oldLocations));
		
		return delta;
	}
	
	/**
	 * checks whether the array contains the number
	 * @param arr
	 * @param num
	 * @return
	 */
	public static boolean arrContain(int[] arr, int num) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == num) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * obtains the number of iterations by passing intensityOfMutation
	 * @param intensityOfMutation
	 * @return
	 */
	public static int timesOfIOM(double intensityOfMutation) {

		// hard code
		if(intensityOfMutation == 1.0) {
			return 6;
		} else if(intensityOfMutation >= 0.8) {
			return 5;
		} else if(intensityOfMutation >= 0.6) {
			return 4;
		} else if(intensityOfMutation >= 0.4) {
			return 3;
		} else if(intensityOfMutation >= 0.2) {
			return 2;
		} else {
			return 1;
		}
	}
	
	/**
	 * obtains the number of iterations by passing depthOfSearch
	 * @param depthOfSearch
	 * @return
	 */
	public static int timesOfDOS(double depthOfSearch) {

		// hard code
		if(depthOfSearch == 1.0) {
			return 6;
		} else if(depthOfSearch >= 0.8) {
			return 5;
		} else if(depthOfSearch >= 0.6) {
			return 4;
		} else if(depthOfSearch >= 0.4) {
			return 3;
		} else if(depthOfSearch >= 0.2) {
			return 2;
		} else {
			return 1;
		}
	}
}
