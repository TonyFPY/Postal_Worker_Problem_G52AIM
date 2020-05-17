package com.aim.project.pwp.runners;


import java.awt.Color;

import com.aim.project.pwp.AIM_PWP;
import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.solution.PWPSolution;
import com.aim.project.pwp.visualiser.PWPView;

import AbstractClasses.HyperHeuristic;

/**
 * @author Warren G. Jackson
 *
 * Runs a hyper-heuristic using a default configuration then displays the best solution found.
 */
public abstract class HH_Runner_Visual {

	public HH_Runner_Visual() {
		
	}
	
	public void run() {
		
		long seed = 13032020l;
		long timeLimit = 60_000l; // set 1 minute of computational time
		AIM_PWP problem = new AIM_PWP(seed);
		problem.loadInstance(3);
		HyperHeuristic hh = getHyperHeuristic(seed);
		hh.setTimeLimit(timeLimit);
		hh.loadProblemDomain(problem);
		hh.run();
		
		System.out.println("f(s_best) = " + hh.getBestSolutionValue());
		System.out.println(problem.bestSolutionToString());
		new PWPView(problem.oInstance, problem, Color.RED, Color.GREEN);
		
	}
	
	/** 
	 * Transforms the best solution found, represented as a TSPSolution, into an ordering of Location's
	 * which the visualiser tool uses to draw the tour.
	 */
	protected Location[] transformSolution(PWPSolution solution, AIM_PWP problem) {
		
		return problem.getRouteOrderedByLocations();
	}
	
	/**
	 * Allows a general visualiser runner by making the HyperHeuristic abstract.
	 * You can sub-class this class to run any hyper-heuristic that you want.
	 */
	protected abstract HyperHeuristic getHyperHeuristic(long seed);
}
