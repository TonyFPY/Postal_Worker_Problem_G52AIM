package com.aim.project.pwp;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import com.aim.project.pwp.heuristics.AdjacentSwap;
import com.aim.project.pwp.heuristics.CX;
import com.aim.project.pwp.heuristics.DavissHillClimbing;
import com.aim.project.pwp.heuristics.InversionMutation;
import com.aim.project.pwp.heuristics.NextDescent;
import com.aim.project.pwp.heuristics.OX;
import com.aim.project.pwp.heuristics.Reinsertion;
import com.aim.project.pwp.instance.InitialisationMode;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.reader.PWPInstanceReader;
import com.aim.project.pwp.interfaces.HeuristicInterface;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.Visualisable;
import com.aim.project.pwp.interfaces.XOHeuristicInterface;

import AbstractClasses.ProblemDomain;


public class AIM_PWP extends ProblemDomain implements Visualisable {

	private String[] instanceFiles = {
		"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
	};
	
	private PWPSolutionInterface[] aoMemoryOfSolutions;
	
	public PWPSolutionInterface oBestSolution;
	
	public PWPInstanceInterface oInstance;
	
	private HeuristicInterface[] aoHeuristics;
	
	private ObjectiveFunctionInterface oObjectiveFunction;
	
	private final long seed;
		
	public AIM_PWP(long seed) {
		
		super(seed);
		
		this.seed = seed;

		/* TODO
		 * set default memory size and create the array of low-level heuristics
		 */
		
		// set the initial memory size
		setMemorySize(2);
		
		// intialise the heuristics to be applied
		aoHeuristics = new HeuristicInterface[7];
		aoHeuristics[0] = new AdjacentSwap(rng);
		aoHeuristics[1] = new InversionMutation(rng);
		aoHeuristics[2] = new Reinsertion(rng);
		aoHeuristics[3] = new NextDescent(rng);
		aoHeuristics[4] = new DavissHillClimbing(rng);
		aoHeuristics[5] = new OX(rng);
		aoHeuristics[6] = new CX(rng);
		
	}
	
	public PWPSolutionInterface getSolution(int index) {
		
		// TODO 
		
		return aoMemoryOfSolutions[index];
	}
	
	public PWPSolutionInterface getBestSolution() {
		
		// TODO 
		
		return oBestSolution;
	}

	@Override
	public double applyHeuristic(int hIndex, int currentIndex, int candidateIndex) {
		
		/* TODO 
		 * apply heuristic and return the objective value of the candidate solution
		 * remembering to keep track/update the best solution
		 */
		
		copySolution(currentIndex, candidateIndex);
		
		double value = aoHeuristics[hIndex]
					.apply(getSolution(candidateIndex), getDepthOfSearch(), getIntensityOfMutation());
		
		updateBestSolution(candidateIndex);
		
		return value;
		
	}

	@Override
	public double applyHeuristic(int hIndex, int parent1Index, int parent2Index, int candidateIndex) {
		
		/* TODO 
		 * apply heuristic and return the objective value of the candidate solution
		 * remembering to keep track/update the best solution
		 */
		
		// initialise the candidate solution
		copySolution(parent1Index, candidateIndex);
		
		XOHeuristicInterface h =  (XOHeuristicInterface) aoHeuristics[hIndex];
		
		double value = h.apply(getSolution(parent1Index), getSolution(parent2Index), getSolution(candidateIndex)
				   , getDepthOfSearch(), getIntensityOfMutation());
		
		updateBestSolution(candidateIndex);
		
		return value;
	}

	@Override
	public String bestSolutionToString() {
		
		/* TODO 
		 * return the location IDs of the best solution including DEPOT and HOME locations
		 * e.g. "DEPOT -> 0 -> 2 -> 1 -> HOME"
		 */
		
		int[] locations = oBestSolution.getSolutionRepresentation().getSolutionRepresentation();
		
		String route = "DEPOT -> ";
		for(int i = 0; i < oBestSolution.getNumberOfLocations() - 2; i++) {
			route += locations[i] + " -> ";
		}
		route += "HOME";
		
		return route;
	}

	@Override
	public boolean compareSolutions(int iIndexA, int iIndexB) {

		/* TODO 
		 * if the objective values of the two solutions are the same, 
		 * 		return true 
		 * else 
		 * 		return false
		 */
		
		return getFunctionValue(iIndexA) == getFunctionValue(iIndexB);
	}

	@Override
	public void copySolution(int iIndexA, int iIndexB) {

		/* TODO 
		 * BEWARE this should copy the solution, not the reference to it!
		 * That is, that if we apply a heuristic to the solution in index 'b',
		 * then it does not modify the solution in index 'a' or vice-versa.
		 */
		
		aoMemoryOfSolutions[iIndexB] = aoMemoryOfSolutions[iIndexA].clone();
	}

	@Override
	public double getBestSolutionValue() {

		// TODO
		
		return oBestSolution.getObjectiveFunctionValue();
	}
	
	@Override
	public double getFunctionValue(int index) {
		
		// TODO
		
		return aoMemoryOfSolutions[index].getObjectiveFunctionValue();
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType type) {
		
		/* TODO 
		 * return an array of heuristic IDs based on the heuristic's type.
		 */
		
		int[] heuristicIndexes;
		
		if(type.equals(HeuristicType.CROSSOVER)) {
			heuristicIndexes = new int[2];
			heuristicIndexes[0] = 5; // OX
			heuristicIndexes[1] = 6; // CX
		} else if(type.equals(HeuristicType.LOCAL_SEARCH)) {
			heuristicIndexes = new int[2];
			heuristicIndexes[0] = 3; // Next Descent
			heuristicIndexes[1] = 4; // Davis Hill Climbing
		} else if(type.equals(HeuristicType.MUTATION)) {
			heuristicIndexes = new int[3];
			heuristicIndexes[0] = 0; // Adjacent Swap
			heuristicIndexes[1] = 1; // Inverse Mutation
			heuristicIndexes[2] = 2; // Re-insertion
		} else {
			heuristicIndexes = new int[0];
		}
		
		return heuristicIndexes;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		
		/* TODO 
		 * return the array of heuristic IDs that use depth of search.
		 */
		
		int[] heuristicsWithDOS = {3, 4};
		
		return heuristicsWithDOS;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		
		/* TODO 
		 * return the array of heuristic IDs that use intensity of mutation.
		 */
		
		int[] heuristicsWithIOM = {0, 1, 2, 5, 6};
		
		return heuristicsWithIOM;
	}

	@Override
	public int getNumberOfHeuristics() {

		/* TODO 
		 * It has to be hard-coded due to the design of the HyFlex framework.
		 */
		
		return 7;
	}

	@Override
	public int getNumberOfInstances() {

		/* TODO 
		 * return the number of available instances
		 */
				
		return (instanceFiles == null) ? 0 : instanceFiles.length;
	}

	@Override
	public void initialiseSolution(int index) {
		
		/* TODO 
		 * initialise a solution in index 'index' 
		 * making sure that you also update the best solution!
		 */
		
		aoMemoryOfSolutions[index] = oInstance.createSolution(InitialisationMode.RANDOM);
		
		// update the best solution
		updateBestSolution(index);
	}

	/* TODO 
	 * implement the instance reader that this method uses
	 * to correctly read in the PWP instance, and set up the objective function.
	 */
	@Override
	public void loadInstance(int instanceId) {

		String SEP = FileSystems.getDefault().getSeparator();
		String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[instanceId] + ".pwp";

		Path path = Paths.get(instanceName);
		Random random = new Random(seed);
		PWPInstanceReader oPwpReader = new PWPInstanceReader();
		oInstance = oPwpReader.readPWPInstance(path, random);

		oObjectiveFunction = oInstance.getPWPObjectiveFunction();
		
		for(HeuristicInterface h : aoHeuristics) {
			h.setObjectiveFunction(oObjectiveFunction);
		}
		
		System.out.println(path.toString());
	}

	@Override
	public void setMemorySize(int size) {

		/* TODO
		 * sets a new memory size
		 * IF the memory size is INCREASED, 
		 * 		then the existing solutions should be copied to the new memory at the same indices.
		 * IF the memory size is DECREASED
		 * 		then the first 'size' solutions are copied to the new memory.
		 */
		
		if(size <= 1) {
			return;
		}
		
		PWPSolutionInterface[] tempSolutions = new PWPSolutionInterface[size];
		
		if(aoMemoryOfSolutions == null) {
			aoMemoryOfSolutions = tempSolutions;
			return;
		} else {
			// copy over previous solutions
			for(int i = 0; i < aoMemoryOfSolutions.length && i < size; i++) {
				tempSolutions[i] = aoMemoryOfSolutions[i];
			}
			
			aoMemoryOfSolutions = tempSolutions;
		}
		
	}

	@Override
	public String solutionToString(int index) {

		// TOD
		
		int[] locations = aoMemoryOfSolutions[index].getSolutionRepresentation().getSolutionRepresentation();
		
		String route = "DEPOT -> ";
		for(int i = 0; i < locations.length; i++) {
			route += locations[i] + " -> ";
		}
		route += "HOME";
		
		return route;
	}

	@Override
	public String toString() {

		// TODO change 'AAA' to be your username
		
		return "scypf1's G52AIM PWP";
	}
	
	private void updateBestSolution(int index) {
		
		// TODO

		if(oBestSolution == null || getFunctionValue(index) < getBestSolutionValue()) {
			oBestSolution = aoMemoryOfSolutions[index];
		}
		
	}
	
	@Override
	public PWPInstanceInterface getLoadedInstance() {

		return this.oInstance;
	}

	@Override
	public Location[] getRouteOrderedByLocations() {

		int[] city_ids = getBestSolution().getSolutionRepresentation().getSolutionRepresentation();
		Location[] route = Arrays.stream(city_ids).boxed().map(getLoadedInstance()::getLocationForDelivery).toArray(Location[]::new);
		return route;
	}
}
