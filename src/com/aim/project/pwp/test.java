package com.aim.project.pwp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aim.project.pwp.heuristics.HeuristicOperators;
import com.aim.project.pwp.instance.Location;

public class test {

	// To check whether the string contains characters
	public boolean judgeContainsChar(String cardNum) {  
	    String regex=".*[a-zA-Z]+.*";  
	    Matcher m=Pattern.compile(regex).matcher(cardNum);  
	    return m.matches();  
	} 
	
	// check whether the array contains the number
	public boolean arrContain(int[] arr, int num) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == num) {
				return true;
			}
		}
		return false;
	}
	
	public int getID(int num, int[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(num == arr[i]) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test t = new test();
//		BufferedReader bfr;
//		try {
//			String[] instanceFiles = {
//					"square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
//			};
//			String SEP = FileSystems.getDefault().getSeparator();
//			String instanceName = "instances" + SEP + "pwp" + SEP + instanceFiles[1] + ".pwp";
//			Path path = Paths.get(instanceName);
//			
//			bfr = Files.newBufferedReader(path);
//			
//			// TODO read in the PWP instance and create and return a new 'PWPInstance'
//			int numOfLocations = 0;
//			String line;
//			String xLoc, yLoc;
//			while((line = bfr.readLine()) != null){
//				
//				// add locations
//				if(line.contains("POSTAL_OFFICE")){
//					String[] subLine = bfr.readLine().split("\\s+");
//					System.out.println("postal office: " + subLine[0] + " , " + subLine[1]);
//				} else if(line.contains("WORKER_ADDRESS")) {
//					String[] subLine = bfr.readLine().split("\\s+");
//					System.out.println("Home: " + subLine[0] + " , " + subLine[1]);
//				} else if(t.judgeContainsChar(line)){
//					continue;
//				} else {
//					String[] subLine = line.split("\\s+");
//					System.out.println(subLine[0] + " , " + subLine[1]);
//					numOfLocations ++;
//				}     
//			} 
//			numOfLocations += 2;
//			System.out.println("number of locations = " + numOfLocations);
//			
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
		
//		int[] array = { 0,1, 2 };
//		
//		Random rand = new Random();
//		
//		for (int i = 0; i < array.length; i++) {
//			int randomIndexToSwap = rand.nextInt(3);
//			System.out.println(randomIndexToSwap);
//			int temp = array[randomIndexToSwap];
//			array[randomIndexToSwap] = array[i];
//			array[i] = temp;
//		}
//		
//		System.out.println(Arrays.toString(array));
		
//		ArrayList list1 = new ArrayList();
//		list1.add(0);//插入第一个元素
//		list1.add(1);
//		list1.add(2);
//		list1.add(3);
//        System.out.println(list);//打印list数组
//        list.add(4, 1000);
//        list.add(0, 100);
//        System.out.println(list);
		
//        ArrayList objArray = new ArrayList();
//        ArrayList objArray2 = new ArrayList();
//        objArray2.add(0);
//        objArray2.add(1);
//        objArray2.add(2);
//        objArray2.add(3);
//        objArray.add(2);
//        objArray.add(3);
//        objArray.add(4);        
//        objArray.add(5);
//        System.out.println("array1 数组元素："+objArray);
//        System.out.println("array2 数组元素："+objArray2);
//        objArray.retainAll(objArray2);
//        System.out.println("array2 & array1 数组交集为："+objArray);

//		Random rand = new Random();
//		int[] a = {0,1,2,3,4,5};
//		int p,q;
//		for(int i = 0; i < 10; i++){
//			p = rand.nextInt(a.length - 1);
//			q = a.length - 1 - rand.nextInt(a.length - 1 - p);
			
//			int left = rand.nextInt(a.length);
//			int right;
//			
//			do {
//				right = rand.nextInt(a.length - 1);
//			} while(left == right);
//			
//			// left must be less than right
//			if(left > right) {
//				int temp = left;
//				left = right;
//				right = temp;
//			}
//			
//			System.out.println("p = " + left + " ; q = " + right);
//		}

//		Random oRandom = new Random();
//		int[] parent1 = {0,1,2,3,4,5,6,7,8};
//		int[] parent2 = {4,5,2,1,8,7,6,0,3};
//		int[] c = new int[parent1.length];
//
//		int left = 0, right = 0;
//		do {
//			left = oRandom.nextInt(parent1.length - 1); 
//			right = parent1.length - 1 - oRandom.nextInt(parent1.length - 1 - left);
//			
//		} while (left == 0 && right == parent1.length - 1);
//		
//		System.out.println("left = " + left + " ; right = " + right);
//		
//		int[] subC = Arrays.copyOfRange(parent1, left, right + 1);
//		for(int i = left; i <= right; i++) {
//			c[i] = parent1[i];
//		}
//		System.out.println(Arrays.toString(subC));
//		
//		int p = right + 1;
//		int q = p;
//		do {
//			System.out.println("p = " + p % parent1.length + " ; q = " + q % parent2.length);
//			int num = parent2[q % parent2.length];
//			if(!t.arrContain(subC, num)) {
//				c[p % parent1.length] = num;
//				//System.out.println(Arrays.toString(c));
//				p++;
//			} 
//			q++;
//		}while(p % parent1.length != left); //&& q % parent2.length != right + 1
//		
//		System.out.println(Arrays.toString(c));
		
		
//		Random oRandom = new Random();
//		int[] parent1 = {1,2,3,4,5,6,7,8,9};
//		int[] parent2 = {4,1,2,8,7,6,9,3,5};
//		int startingP = oRandom.nextInt(parent1.length);
//		int s = startingP;
//		ArrayList<Integer> tempAL = new ArrayList<Integer>();
//		int[] c = new int[parent1.length];
//		Arrays.fill(c, -1);
//		
//		// only do the first cycle if there are multiple cycles
//		do {
//			c[s] = parent1[s];
//			tempAL.add(c[s]);
//			s = t.getID(parent2[s], parent1);	
//		} while(s != startingP);
//		System.out.println(Arrays.toString(c));
//		System.out.println(tempAL);
//		
//		if(tempAL.size() != parent1.length) {
//			int j = 0;
//			for(int i = 0; i < c.length; i++) {
//				if(c[i] != -1)
//					continue;
//				while(j < parent2.length) {
//					if(!tempAL.contains(parent2[j])) {
//						c[i] = parent2[j++];
//						break;
//					}
//					j++;
//				}
//			}
//		}
//		System.out.println(Arrays.toString(c));
		
//		Random oRandom = new Random();
//		int[] deliveryLocations = new int[10];
//		
//		ArrayList<Integer> temp = new ArrayList<Integer>();								
//		for(int i = 0; i < deliveryLocations.length; i++) {
//			temp.add(i);			
//		}
//		
//		// shuffle
//		Collections.shuffle(temp, oRandom);	
//		
//		int id = 0;
//		for(int num : temp) {
//			deliveryLocations[id++] = num;
//		}
//		
//		System.out.println(Arrays.toString(deliveryLocations));
		
//		char[] locationIDs = {'b','c','d','f','g','h'};
//		
//		ArrayList<Character> deliveryLocations = new ArrayList<Character>();
//		for(int i = 0; i < locationIDs.length; i++) {
//			deliveryLocations.add(i, locationIDs[i]);
//		}
//		deliveryLocations.add(locationIDs.length,'i');
//		deliveryLocations.add(0,'a');
//		
//		System.out.println(deliveryLocations);
		
//		int[] arr = {0,1,2,3,4};
//		t.swapLocations(0,1,arr);
//		System.out.println(Arrays.toString(arr));
		
//		Random oRandom = new Random();
//		int[] parent1 = {1,2,3,4,5,6,7,8,9};
//		int[] parent2 = {4,1,2,8,7,6,9,3,5};
//		int startingP = oRandom.nextInt(parent1.length);
//		int s = startingP;
//		
//		int[] c = parent2.clone();
//		do {
//			c[s] = parent1[s];
//			s = t.getID(parent2[s], parent1);	
//			System.out.println(Arrays.toString(c));
//		} while(s != startingP);
		
		int a = 0, b = 1;
		for(int i = 0; i < 100 ; i++) {
			a = (a+1)%2;
			b = (b+1)%2;
			System.out.println("a = " + a+"; b = "+ b);
		}
		
	}
	
	public int getC(int[] arr) {
		int c = 0;
		
		for(int i = 0; i < arr.length - 1; i++) {
			c += arr[i+1] - arr[i];
		}
		
		return c;
	}
	
	public int[] insert(int select, int into, int[] locations) {
		
		// insert 'select' into index 'into' and fill the gap
		// this process can be regarded as swapping until select == into
		if(select < into) {
			while(select != into) {
				HeuristicOperators.swapLocations(select, ++select, locations);
			}
		} else {
			while(select != into) {
				HeuristicOperators.swapLocations(select, --select, locations);
			}
		}
		
		return locations;
	}
	
	public void swapLocations(int i, int j, int[] arr) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
		
		arr[4] = 10000;
	}

}
