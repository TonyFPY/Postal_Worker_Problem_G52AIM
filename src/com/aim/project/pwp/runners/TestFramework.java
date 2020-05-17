package com.aim.project.pwp.runners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class TestFramework {

	protected final long[] Seeds;

	// runs per instanceID for both hyper-heuristics
	protected final int numOfIterations = 11;

	protected final long timeLimit = 60_000l; // 1 minute runs
	
	protected final long initialSeed = 13032020l; // initial seed
	
	// IDs for "carparks-40", "tramstops-85", "trafficsignals-446"
	protected final int[] instanceIDs = {2, 3, 4};
	
	public TestFramework() {
		
		/*
		 * Generation of SEED values
		 */
		Random random = new Random(initialSeed);
		Seeds = new long[numOfIterations];
		
		for(int i = 0; i < numOfIterations; i++)
		{
			Seeds[i] = random.nextLong();
		}
	}
	
	public int getTotalRuns() {
		return this.numOfIterations;
	}
	
	public long[] getSeeds() {
		return this.Seeds;
	}
	
	
	public int[] getInstanceIDs() {
		return instanceIDs;
	}
	
	public long getRunTime() {
		return timeLimit;
	}
	
	// the code is referrenced from the lab excercises
	public void saveData(String filePath, String data) {
		
		Path path = Paths.get("./" + filePath);
		if(!Files.exists(path)) {
			try {
				Files.createFile(path);
				
				//add header
				String header = "HH,Run Time,f_best,Trial,Instance ID";
				for(int i = 0; i < numOfIterations; i++) {
					
					header += ("," + i);
				}
				
				Files.write(path, (header + "\r\n" + data).getBytes());
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
		} else {
			try {
				byte[] currentData = Files.readAllBytes(path);
				data = "\r\n" + data;
				byte[] newData = data.getBytes();
				byte[] writeData = new byte[currentData.length + newData.length];
				System.arraycopy(currentData, 0, writeData, 0, currentData.length);
				System.arraycopy(newData, 0, writeData, currentData.length, newData.length);
				Files.write(path, writeData);
				
			} catch (IOException e) {
				System.err.println("Could not create file at " + path.toAbsolutePath());
				System.err.println("Printing data to screen instead...");
				System.out.println(data);
			}
			
		}
	}
	
}
