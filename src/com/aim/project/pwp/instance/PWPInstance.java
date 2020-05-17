package com.aim.project.pwp.instance;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.aim.project.pwp.PWPObjectiveFunction;
import com.aim.project.pwp.interfaces.ObjectiveFunctionInterface;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;
import com.aim.project.pwp.interfaces.SolutionRepresentationInterface;
import com.aim.project.pwp.solution.SolutionRepresentation;
import com.aim.project.pwp.solution.PWPSolution;


public class PWPInstance implements PWPInstanceInterface {
	
	private final Location[] aoLocations;
	
	private final Location oPostalDepotLocation;
	
	private final Location oHomeAddressLocation;
	
	private final int iNumberOfLocations;
	
	private final Random oRandom;
	
	private ObjectiveFunctionInterface oObjectiveFunction = null;
	
	/**
	 * 
	 * @param numberOfLocations The TOTAL number of locations (including DEPOT and HOME).
	 * @param aoLocations The delivery locations.
	 * @param oPostalDepotLocation The DEPOT location.
	 * @param oHomeAddressLocation The HOME location.
	 * @param random The random number generator to use.
	 */
	public PWPInstance(int numberOfLocations, Location[] aoLocations, Location oPostalDepotLocation, Location oHomeAddressLocation, Random random) {
		
		this.iNumberOfLocations = numberOfLocations;
		this.oRandom = random;
		this.aoLocations = aoLocations;
		this.oPostalDepotLocation = oPostalDepotLocation;
		this.oHomeAddressLocation = oHomeAddressLocation;
	}

	@Override
	public PWPSolution createSolution(InitialisationMode mode) {
		
		// TODO construct a new 'PWPSolution' using RANDOM initialisation
		
		if(mode.equals(InitialisationMode.RANDOM)) {
			
			// initialisation
			int[] deliveryLocations = new int[aoLocations.length];
			ArrayList<Integer> temp = new ArrayList<Integer>();								
			for(int i = 0; i < deliveryLocations.length; i++) {
				temp.add(i);			
			}
			
			// shuffle
			Collections.shuffle(temp, oRandom);	
			
			// fill the array
			int id = 0;
			for(int num : temp) {
				deliveryLocations[id++] = num;
			}
			
			SolutionRepresentationInterface SR = new SolutionRepresentation(deliveryLocations);
			
			double objectFuncValue = this.getPWPObjectiveFunction().getObjectiveFunctionValue(SR); 
			
			return new PWPSolution(SR, objectFuncValue);
			
		} else if(mode.equals(InitialisationMode.CONSTRUCTIVE)) {
			
			//Implement Constructive solution initialization
			
		} else if(mode.equals(InitialisationMode.ZEROES_TO_N)) {
			
			//Implement Zeros to N solution initialization
			
		} 
		
		return null;
		
	}
	
	@Override
	public ObjectiveFunctionInterface getPWPObjectiveFunction() {
		
		if(oObjectiveFunction == null) {
			this.oObjectiveFunction = new PWPObjectiveFunction(this);
		}

		return oObjectiveFunction;
	}

	@Override
	public int getNumberOfLocations() {

		return iNumberOfLocations;
	}

	@Override
	public Location getLocationForDelivery(int deliveryId) {

		return aoLocations[deliveryId];
	}

	@Override
	public Location getPostalDepot() {
		
		return this.oPostalDepotLocation;
	}

	@Override
	public Location getHomeAddress() {
		
		return this.oHomeAddressLocation;
	}
	
	@Override
	public ArrayList<Location> getSolutionAsListOfLocations(PWPSolutionInterface oSolution) {
		
		// TODO return an 'ArrayList' of ALL LOCATIONS in the solution.
		
		int[] locationIDs = oSolution.getSolutionRepresentation().getSolutionRepresentation();
		
		ArrayList<Location> deliveryLocations = new ArrayList<Location>();
		
		for(int i = 0; i < iNumberOfLocations - 2; i++) {
			deliveryLocations.add(i, aoLocations[locationIDs[i]]);
		}
		
		// add home and depot addresses
		deliveryLocations.add(aoLocations.length,oHomeAddressLocation);
		deliveryLocations.add(0,oPostalDepotLocation);
		
		return deliveryLocations;
	
	}

}
