package com.aim.project.pwp.runners;

import com.aim.project.pwp.hyperheuristics.SCYPF1_HH;

import AbstractClasses.HyperHeuristic;

public class SCYPF1_VisualRunner extends HH_Runner_Visual {

	@Override
	protected HyperHeuristic getHyperHeuristic(long seed) {

		return new SCYPF1_HH(seed);
	}
	
	public static void main(String [] args) {
		
		HH_Runner_Visual runner = new SCYPF1_VisualRunner();
		runner.run();
	}
	
}
