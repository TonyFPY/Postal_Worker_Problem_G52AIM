package com.aim.project.pwp.hyperheuristics;


import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.SolutionPrinter;
import com.aim.project.pwp.interfaces.PWPSolutionInterface;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class SR_IE_HH extends HyperHeuristic {
	
	public SR_IE_HH(long lSeed) {
		
		super(lSeed);
	}

	@Override
	protected void solve(ProblemDomain oProblem) {

		oProblem.setMemorySize(3);
		
		oProblem.initialiseSolution(0);
		double current = oProblem.getFunctionValue(0);
		
		oProblem.setIntensityOfMutation(0.2);
		oProblem.setDepthOfSearch(0.2);
		
		int h = 1;
		long iteration = 0;
		boolean accept;
		System.out.println("Iteration\tf(s)\tf(s')\tAccept");

		while(!hasTimeExpired() ) {
			
			h = rng.nextInt(oProblem.getNumberOfHeuristics());
//			test
//			h = rng.nextInt(oProblem.getNumberOfHeuristics()-2);
			
//			check
//			h = 2;
			
			double candidate;
			if(h < 5) {
				candidate = oProblem.applyHeuristic(h, 0, 1);
			} else {
				oProblem.initialiseSolution(2);
				candidate = oProblem.applyHeuristic(h, 0, 2, 1);
			}
			
			accept = candidate <= current;
			if(accept) {
				
				oProblem.copySolution(1, 0);
				current = candidate;
			}
			
//			check
//	        System.out.println(iteration + "\t" + current + "\t" + candidate + "\t" + accept + "\t" + h);
			 
			iteration++;
			
//			check
//			if(iteration == 1000)
//				break;
			
			
		}
		
		PWPSolutionInterface oSolution = ((AIM_PWP) oProblem).getBestSolution();
		SolutionPrinter oSP = new SolutionPrinter("out.csv");
		oSP.printSolution( ((AIM_PWP) oProblem).oInstance.getSolutionAsListOfLocations(oSolution));
		System.out.println(String.format("Total iterations = %d", iteration));
	}

	@Override
	public String toString() {

		return "SR_IE_HH";
	}
}
