package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.PWPInstance;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {

		// TODO read in the PWP instance and create and return a new 'PWPInstance'
		
		BufferedReader bfr;
		try {
			bfr = Files.newBufferedReader(path);
	
			// initialisation
			int numOfLocations = 0;
			String line;
			Location[] aoLocations;
			Location oPostalDepotLocation = null, oHomeAddressLocation = null;
			ArrayList<Location> tempLocations = new ArrayList<Location>();
			
			// read 
			while((line = bfr.readLine()) != null){
					
				// set locations
				if(line.contains("POSTAL_OFFICE")) {
					oPostalDepotLocation = setLocation(bfr.readLine());
				} else if(line.contains("WORKER_ADDRESS")) {
					oHomeAddressLocation = setLocation(bfr.readLine());
				} else if(containsChar(line) == true) {
					continue;
				} else {
					tempLocations.add(numOfLocations, setLocation(line));
					numOfLocations ++;
				}
			}
			
			// take home and depot into consideration
			numOfLocations += 2;
			
			// put the data into a structure of delivery locations
			aoLocations = new Location[numOfLocations - 2];
			for(int i = 0; i < aoLocations.length; i++) {
				aoLocations[i] = tempLocations.get(i);
			}
			
			return new PWPInstance(numOfLocations, aoLocations, oPostalDepotLocation, oHomeAddressLocation,random);
			
		} catch (IOException e) {

			e.printStackTrace();
			
			return null;
		}
	}
	
	/** 
	 * splits the string into x and y and set the location
	 * @param line
	 * @return
	 */
	public Location setLocation(String line) {
		
		String[] subLine = line.split("\\s+");
		double x = Double.valueOf(subLine[0].toString());
		double y = Double.valueOf(subLine[1].toString());
		
		return new Location(x,y);
	}
	
	/**
	 * checks whether the string contains characters
	 * @param line
	 * @return
	 */
	public boolean containsChar(String line) {  
	    String regex=".*[a-zA-Z]+.*";  
	    Matcher m = Pattern.compile(regex).matcher(line);  
	    return m.matches();  
	} 
	
}
