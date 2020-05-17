package com.aim.project.pwp;

import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.instance.Location;

public class PWPObjectiveFunction implements ObjectiveFunctionInterface {
	
	private final PWPInstanceInterface oInstance;
	
	public PWPObjectiveFunction(PWPInstanceInterface oInstance) {
		
		this.oInstance = oInstance;
	}

	/*
	 * Calculate the trip cost by traversing the array
	 * Update the path cost associated with home and depot
	 */
	@Override
	public double getObjectiveFunctionValue(SolutionRepresentationInterface oSolution) {
		
		int[] solutionArr = oSolution.getSolutionRepresentation();	
		double cost = 0;
	
		for(int i = 0; i < solutionArr.length - 1; i++) {
			cost += getCost(solutionArr[i], solutionArr[i + 1]);
		}
		
		// add the trips between the postal office and worker’s home locations with the start and end points
		cost += getCostBetweenDepotAnd(solutionArr[0]) + getCostBetweenHomeAnd(solutionArr[solutionArr.length - 1]);
		
		return cost;
	}
	
	@Override
	public double getCost(int iLocationA, int iLocationB) {
		
		Location a = oInstance.getLocationForDelivery(iLocationA);
		Location b = oInstance.getLocationForDelivery(iLocationB);
		
		return Math.sqrt( Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	@Override
	public double getCostBetweenDepotAnd(int iLocation) {
		
		Location delivery = oInstance.getLocationForDelivery(iLocation);
		Location depot = oInstance.getPostalDepot();
		
		return Math.sqrt(Math.pow(delivery.getX() - depot.getX(), 2) + Math.pow(delivery.getY() - depot.getY(), 2));
	}

	@Override
	public double getCostBetweenHomeAnd(int iLocation) {
		
		Location delivery = oInstance.getLocationForDelivery(iLocation);
		Location home = oInstance.getHomeAddress();
		
		return Math.sqrt(Math.pow(delivery.getX() - home.getX(), 2) + Math.pow(delivery.getY() - home.getY(), 2));
	}
}
