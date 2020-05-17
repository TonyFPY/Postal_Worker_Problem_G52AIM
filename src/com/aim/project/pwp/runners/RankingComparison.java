package com.aim.project.pwp.runners;

import com.aim.project.pwp.AIM_PWP;

import com.aim.project.pwp.hyperheuristics.SCYPF1_HH;
import com.aim.project.pwp.hyperheuristics.SR_IE_HH;

import AbstractClasses.HyperHeuristic;

public class RankingComparison {

	final TestFramework config;
	final int[] INSTANCE_IDs;
	final long RUN_TIME;
	final long[] SEEDS;
	final int TOTAL_RUNS;
	
	public RankingComparison(TestFramework config) {
		this.config = config;
		
		this.TOTAL_RUNS = config.getTotalRuns();
		this.INSTANCE_IDs = config.getInstanceIDs();
		this.SEEDS = config.getSeeds();
		this.RUN_TIME = config.getRunTime();
	}
	
	public void runTestsOnInstance(int instanceID) {
		for(int i = 0; i < TOTAL_RUNS; i++) {
			long seed = SEEDS[i];
			
			// run SR_IE_HH
			AIM_PWP problem1 = new AIM_PWP(seed);
			problem1.loadInstance(INSTANCE_IDs[instanceID]);
			HyperHeuristic sr_ie = new SR_IE_HH(seed);
			sr_ie.setTimeLimit(RUN_TIME);
			sr_ie.loadProblemDomain(problem1);
			sr_ie.run();
			
			System.out.println(sr_ie.toString() + " f_best = " + sr_ie.getBestSolutionValue() + ", Trial = " + i + ", InstanceID = " + instanceID + "\n");
			config.saveData("test.csv", sr_ie.toString() + "," + RUN_TIME + "," + sr_ie.getBestSolutionValue() + "," + i + "," + instanceID);
			
			// run SCYPF1_HH
			AIM_PWP problem2 = new AIM_PWP(seed);
			problem2.loadInstance(INSTANCE_IDs[instanceID]);
			HyperHeuristic scypf1 = new SCYPF1_HH(seed);
			scypf1.setTimeLimit(RUN_TIME);
			scypf1.loadProblemDomain(problem2);
			scypf1.run();
			
			System.out.println(scypf1.toString() + " f_best = " + scypf1.getBestSolutionValue() + ", Trial = " + i + ", InstanceID = " + instanceID + "\n");
			config.saveData("test.csv", scypf1.toString() + "," + RUN_TIME + "," + scypf1.getBestSolutionValue() + "," + i + "," + instanceID);
		}
	}
	
	public void runAllTests() {
		for(int instanceID = 0; instanceID < INSTANCE_IDs.length; instanceID++) {
			runTestsOnInstance(instanceID);
		}
	}
	
	
	public static void main(String[] args) {
		
		RankingComparison r = new RankingComparison(new TestFramework());
		
		//r.runTestsOnInstance(0);
		//r.runTestsOnInstance(1);
		//r.runTestsOnInstance(2);
		
		r.runAllTests();
	}

}
