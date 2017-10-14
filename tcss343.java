/**
 * 
 */
package tcss343;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class creates a grid of random numbers in the form of a triangle
 * that represent costs from moving from number to number.  The program then
 * finds the shortest path from the first "trading post" to the N "trading post".
 * To find the solution, the program will use the Brute Force Algorithm, 
 * Dynamic Programming Algorithm, and the Divide and Conquer Algorithm.
 * 
 * The createTables and manageTablesCreation is initially commented out so that
 * a random table isn't created over and over again, but only when the user
 * needs to create the files.
 * 
 * @author Ahana Ghosh 
 * @author Tony Lee
 *
 */
public class tcss343 {
	private static final int INFINITY = Integer.MAX_VALUE;
	private static final int MAX_RAND = 100;
	private static final int START_POLE = 0;
	private static String myFileName;
	private static int myEndPole;
	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {		
		// Disabling the below code to make sure this is not invoked
		// every time when the program is run.
		//createTables(800, MAX_RAND);
		//createTables(2000,MAX_RAND);
	    //manageTableCreation(MAX_RAND);
		
		myFileName = args[0];		
		final int[][] matrix = readInput();
		myEndPole = matrix.length - 1;

        callAlgorithms(matrix);
    }
	//This method reads the input file.
	private static int[][] readInput() throws Exception {
		
		// Read the input into a array of strings.
		List<String> inputLines = new ArrayList<String>();
		String currLine = "";
		try {
			// Read the content of the file into an array of strings.
			Scanner myScanner = new Scanner(new File(myFileName));
			while((currLine = myScanner.nextLine()) !=  null) {
				
				inputLines.add(currLine);

				if (!myScanner.hasNextLine()) {
					myScanner.close();
					break;
				}
			}
			
			// Based out of the number of lines in the input file
			// create a nXn matrix.
			int max = inputLines.size();
			int[][] data = new int[max][max];
			int count = 0;
			
			for (int i = 0; i < max; i++) {
				for (int j = 0; j < max; j++) {
					data[i][j] = INFINITY;
				}
			}
			
			// Populate the nXn matrix.
			for(int i = 0; i < inputLines.size(); i++) {
				currLine = inputLines.get(i);
				String[] splitLine = currLine.split("\t");
				for(int j = 0; j < splitLine.length; j++) {
					try {
						data[count][j] = Integer.parseInt(splitLine[j]);
					}
					catch (NumberFormatException ex) { 
						//do nothing
					}
				}
				count++;
			}			
			return data;
			
		} catch(Exception ex) {
			System.out.println(ex.toString());
			throw ex;
		}
	}
	//This method calls all the three algorithms
	private static void callAlgorithms(int[][] theMatrix) {
		long currTime;
		long timeLapse;
		
		currTime = System.currentTimeMillis();
        DivideAndConquer2(theMatrix);
        timeLapse = System.currentTimeMillis() - currTime;
		System.out.println("The Divide and Conquer Method took " + timeLapse + " miliseconds.");
		System.out.println();
		
        currTime = System.currentTimeMillis();
        dynamicProgramming(theMatrix);
        timeLapse = System.currentTimeMillis() - currTime;
		System.out.println("The Dynamic Programming Method took " + timeLapse + " miliseconds.");
		System.out.println();
		
		currTime = System.currentTimeMillis();
		dynamicProgramming2(theMatrix);
        timeLapse = System.currentTimeMillis() - currTime;
		System.out.println("The Dynamic Programming Method2 took " + timeLapse + " miliseconds.");
		System.out.println();
		
		currTime = System.currentTimeMillis();
		bruteForce(theMatrix);
		timeLapse = System.currentTimeMillis() - currTime;
		System.out.println("The Brute Force Method took " + timeLapse + " miliseconds.");
		System.out.println();
	}
	//This method creates new tables
	private static void manageTableCreation(int upperBound) {
		createTables(100, upperBound);
		createTables(200, upperBound);
		createTables(400, upperBound);
		createTables(600, upperBound);
		createTables(800, upperBound);		
	}
	
	//This method creates a single table.
	private static void createTables(int n, int upperBound) {
		Random randomGenerator = new Random();
		int [][] randomArray = new int [n][n];
	
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i  ==  j) {
					randomArray[i][j] = 0;
				} else if(i > j) {
					randomArray[i][j] = INFINITY;
				} else {
					randomArray[i][j] = randomGenerator.nextInt(upperBound) + 1;
				}
			}
		}
		writeOutput(randomArray);
	}
	
	//this method writes the output to the table input files.
	private static void writeOutput (int[][] outData) {
		String outFile = "Input_" + outData.length + ".txt";
		
		try {
			File fileLocation = new File(outFile);		
			PrintWriter Writer = new PrintWriter(fileLocation, "UTF-8");	
			for (int i = 0; i < outData.length; i++) {
				for (int j = 0; j < outData[i].length; j++) {
					if (outData[i][j] == INFINITY) {
						Writer.print("NA");
					}else {
						Writer.print(outData[i][j]);
					}
					if (j != outData[i].length - 1) {
						Writer.print("\t");
					}
				}
				Writer.println();
			}
			
			Writer.close();
		} catch (Exception ex) {
            System.out.println(ex.toString());                  
		}
	}
	
	//This method prints the path information
	protected static void printPath(int[] thePath) {
		
		int[] orderedPath = new int[thePath.length];
	
		for (int i = 0; i < thePath.length; i++)
			orderedPath[i] = -1;
	
		orderedPath[0] = myEndPole;
		int i = myEndPole;
		int j = 1;
	 
		while (true && j < orderedPath.length) {	
			if (thePath[i] == -1 && i == START_POLE)
				break;
			orderedPath[j] = thePath[i];	
			i = thePath[i];
			j++;
		}
	
		System.out.print("The Shortest Path is ");
	
		for (i = orderedPath.length - 1; i >= 0; i--) {
			if (orderedPath[i] == -1)
				continue;
			if (i == 0) {
				System.out.print((orderedPath[i] + 1));
				continue;
			}
			System.out.print((orderedPath[i] + 1) + " -> ");
		}

		System.out.println();
		System.out.println();
	}

	
	public static boolean AllVisited(boolean[] visited)
	{
		for(int i = 0; i < visited.length; i++)
		{
			if(visited[i] == false) return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param start staring pole of the matrix
	 * @param end ending pole of the matrix
	 * @param costArray is the matrix
	 * @param path is the desired path having minimum cost
	 * @return returns the minimum cost between starting and ending pole.
	 */
	static int getMinDistance(int start, int end, int[][] costArray, List<Integer> path) {				
		
		// Initial path which is direct.
		int proposedDistance = costArray[start][end];
		path.add(start);
		path.add(end);
		
		for(int i = start + 1; i < end; i++) {
			int intermediateNode = i;
			List<Integer> path1 = new ArrayList<Integer>();
			List<Integer> path2 = new ArrayList<Integer>();
			int firstLeg = getMinDistance(start, intermediateNode, costArray, path1);
			int secondLeg = getMinDistance(intermediateNode, end, costArray, path2);
			
			if(firstLeg + secondLeg < proposedDistance) {
				proposedDistance = firstLeg + secondLeg;
				path.clear();
				path.addAll(path1);
				path.remove(path.size() - 1);
				path.addAll(path2);
			}
		}
		
		return proposedDistance;
	}
	
	/**
	 * This method prints the minimum path between starting and ending pole using brute force algorithm.
	 * @param myArray is the matrix
	 */
	public static void bruteForce(int[][] myArray){
		int countNode = myArray.length;
    	
		List<Integer> path = new ArrayList<Integer>();
		
		int minDistance = getMinDistance(0, countNode -1, myArray, path);
		
		System.out.println("Brute Force Algorithm:");
    	System.out.println("Minimum Cost to go from 1 to " + countNode + " is " + minDistance);
    	
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < path.size(); i++)
    	{
    		sb.append(path.get(i) + 1);
    		if(i != path.size() - 1) {
    			sb.append(" -> ");    			
    		}
    	}
    	
    	System.out.println("Path from 1 to end is " + sb.toString());   
    	System.out.println();
	}
	
	// Similar to Dijkastra's algorithm which is DP and greedy at the same time.
    public static void dynamicProgramming2(int[][] myArray){       	
		int countNode = myArray.length;
		int rows = myArray[0].length;
		if(rows !=  countNode) {
			throw new IllegalArgumentException();
		}
		
    	int[] cost = new int[countNode];
    	int[] prev = new int[countNode];
    	boolean[] visited = new boolean[countNode];
    	
    	for(int i = 0; i < countNode; i++) {
    		cost[i] = myArray[0][i];
    		prev[i] = 0;
    		visited[i] = false;
    	}
    	
    	int cur = 0;        	
    	while(true)
    	{
    		for(int i = cur; i < countNode; i++)
    		{
    			if(myArray[cur][i] + cost[cur] < cost[i])
    			{
    				cost[i] = myArray[cur][i] + cost[cur];
    				prev[i] = cur;
    			}
    		}
    		
    		visited[cur] = true;
    		if(AllVisited(visited)) break;
    		
    		// Choose the node which is not visited yet
    		// and has the minimum cost so far.
    		int min = Integer.MAX_VALUE;
    		for(int i = 0; i < countNode; i++){
    			if(!visited[i] && cost[i] < min) {
    				cur = i;
    				min = cost[i];
    			}
    		}
    	}
    	System.out.println("Dynamic Programming2:");
    	System.out.println("Minimum Cost to go from 1 to " + countNode + " is " + cost[countNode - 1]);
    	Stack<Integer> s = new Stack<Integer>();
    	int traveller = countNode - 1;
    	s.push(countNode);
    	while(traveller !=  0)
    	{
    		s.push(prev[traveller] + 1);
    		traveller = prev[traveller];
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	while(!s.empty())
    	{
    		sb.append(s.pop());
    		if (!s.empty()) {
        		sb.append(" -> ");
    		}
    	}
    	
    	System.out.println("Path from 1 to end is " + sb.toString());   
    	System.out.println();
    }
	
	public static void DivideAndConquer2(int[][] myArray) {
		System.out.println("Divide and Conquer Algorithm 2:");

		int [] distance = new int[myArray.length];
		
		for (int j = 0; j < myArray[START_POLE].length; j++) {
			distance[j] = myArray[START_POLE][j];
		}

		int[] path = new int[myArray.length];
		
		for (int i = 0; i < path.length; i++) {
			path[i] = 0;
		}
		
		updateMinDistance(myArray, distance, 0, distance.length - 1, path);
		
		System.out.println("Minimum Cost to go from "+(START_POLE+1)+" to "+(myEndPole +1)+" is "+distance[myEndPole]);
		int countNode = path.length;
		Stack<Integer> s = new Stack<Integer>();
    	int traveller = countNode - 1;
    	s.push(countNode);
    	while(traveller !=  0)
    	{
    		s.push(path[traveller] + 1);
    		traveller = path[traveller];
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	while(!s.empty())
    	{
    		sb.append(s.pop());
    		if (!s.empty()) {
        		sb.append(" -> ");
    		}
    	}
    	
    	System.out.println("Path from 1 to end is " + sb.toString());
	}

	public static void updateMinDistance(int[][] matrix, int[] distance, int start, int end, int[] path) {
		if(start >= end) {
			return;
		}
		
		int mid = start + ((end - start)/2);
		
		updateMinDistance(matrix, distance, start, mid, path);
		updateMinDistance(matrix, distance, mid+1, end, path);
		
		// Merge the results.
		for(int i = start; i <= end; i++)
		{
			for(int j = mid +1; j <= end; j++)
			{
				if(matrix[i][j] != INFINITY && distance[i] + matrix[i][j] < distance[j]) {
					distance[j] = distance[i] + matrix[i][j];
					path[j] = i;
				}
			}
		}
		
		return;
	}
	
    /**
     * Dynamic Programming Method
     * @param myArray 
     */
    public static void dynamicProgramming(int[][]myArray){
    	int countNode =  myArray.length;
    	int [] prev = new int [myArray.length];
    	int [] cost = new int [myArray.length];
    	for(int i = 0; i < countNode; i++)
    	{
    		cost[i] = myArray[0][i];
    		prev[i] = 0;
    	}
    	
    	for(int i = 1; i < countNode; i++){
    		int min = cost[i]; 
    		for(int k = 0; k < i ; k++){
    			if(cost[k] + myArray[k][i] < min ){
    				min = cost[k]+ myArray[k][i];
    				prev[i] = k ;
    			}
    		}
    		cost[i] = min;
    		
    	}
    	System.out.println("Dynamic Programming Algorithm:");
    	System.out.println("Minimum Cost to go from 1 to " + countNode + " is " + cost[countNode - 1]);
    	Stack<Integer> s = new Stack<Integer>();
    	int traveller = countNode - 1;
    	s.push(countNode);
    	while(traveller !=  0)
    	{
    		s.push(prev[traveller] + 1);
    		traveller = prev[traveller];
    	}
    	
    	StringBuilder sb = new StringBuilder();
    	while(!s.empty())
    	{
    		sb.append(s.pop());
    		if (!s.empty()) {
        		sb.append(" -> ");
    		}
    	}
    	
    	System.out.println("Path from 1 to end is " + sb.toString());
    }
    
 
    
	
	
}