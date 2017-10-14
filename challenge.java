package tcss343chal;
import java.io.*; 

/**
 * /**
 * This class creates a grid and sets "stones" and grid parameters
 * into the command line arguments.
 * [int grid size][int numStones][xcoord][ycoord]....[xcoord n][ycoord n]
 * 
 * The goal is to find where on the grid is the top left corner of the 
 * biggest square we can make without having any stones inside the square.
 * 
 * Run Configurations Example: 8 4 1 2 2 6 4 5 7 3
 * 
 *@author Tony Lee
 *@author Ahana Ghosh 
 * 
 */
public class challenge  {
    public static void main(String[] args) {		
		int gridSize = Integer.parseInt(args[0]); 
		int numStones = Integer.parseInt(args[1]); 
		int[] stoneGrid = new int[numStones * 2]; 
		
		//fill the grid
		for (int i = 0; i < args.length - 2; i++) {
			stoneGrid[i] = Integer.parseInt(args[i + 2]); 
		}
			
		boolean[][] grid = createGrid(gridSize, numStones, stoneGrid); 
		performBruteForce (gridSize, numStones, stoneGrid, grid); 
		performDynamicProg (gridSize, numStones, stoneGrid, grid); 	

	}

    /**
     * This method creates the false boolean grid.
     * 
     * @param gridSize Size of n x n grid.
     * @param numStones number of stones on the grid
     * @param stoneGrid the array of the filled grid.
     * @return returns the newly created grid.
     */
	private static boolean[][] createGrid (int gridSize, int numStones, int[] stoneGrid) {
		//grid of whether or not each spot is true or false.
		boolean[][] grid = new boolean[gridSize][gridSize]; 
				
		for (int j = 0; j < numStones * 2; j += 2) {
			grid[stoneGrid[j]][stoneGrid[j + 1]] = true; 
		}		
		return grid; 
	}
	
    /**
	 * This method verifies that the square of size squareSize is not
	 * obstructed by a stone on the grid.
	 * 
	 * @param x x coord of the location
	 * @param y y coord of the location
	 * @param squareSize size of the square
	 * @param gridSize size of the grid
	 * @param grid boolean array of the whole grid information.
	 * @return returns whether the square is valid.
	 */
	private static boolean verifySquare (int x, int y, int squareSize, int gridSize, boolean[][] grid) {
		//if the square goes off the grid, return false.
		if((squareSize + x) > gridSize || (squareSize + y) > gridSize) {
			return false; 
		}
		//check all the spots within the square.
		for(int i = x; i < squareSize + x; i++) {
			for(int j = y; j < squareSize + y; j++) {
				if(grid[i][j] == true) {return false;}
			}	
		}	
		return true; 
	}
	
	/**
	 * Uses the Brute Force method to find where the biggest possible
	 * square can go in the field of stones.
	 * 
	 * @param gridSize size of the grid
	 * @param numStones number of stones
	 * @param stoneGrid grid data
	 * @param grid false boolean grid.
	 */
	private static void performBruteForce (int gridSize, int numStones, int[] stoneGrid, boolean[][] grid) {
		
		System.out.println("Brute Force Method"); 
		
		//create all sizes of squares at every location starting at gridSize going down.
		for (int squareSize = gridSize; squareSize > 0; squareSize--) {
			//goes through every column
			for (int x = 0; x < gridSize; x++) {
				//goes through every row
				for (int y = 0; y < gridSize; y++) {
					if (verifySquare (x, y, squareSize, gridSize, grid) == true) {
						System.out.print("Square size " + squareSize);
						System.out.println(" at location (" + x + ", " + y + ")"); 
						 
						System.out.println(); 
						squareSize = 0;
						x = gridSize;
						y = gridSize;
					}					
				}
			}
		}		
	}
	
	/**
	 * This method uses the dynamic programming method to find the where
	 * the biggest possible square can fit in the field of stones.
	 * 
	 * @param gridSize size of grid
	 * @param numStones number of stones
	 * @param stoneGrid grid data
	 * @param grid false boolean grid
	 */
	private static void performDynamicProg(int gridSize, int numStones, int[] stoneGrid, boolean[][] grid) {
		int[][] temp = new int[gridSize][gridSize]; 		
		int max;
		int xCoord = 0; 
		int yCoord = 0; 
		
		System.out.println("Dynamic Programming Method"); 
		
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (i == 0) {
					temp[0][j] = 1;
				} 
				if (j == 0) {
					temp[i][0] = 1;
				}
				
				if (i != 0 && j != 0) {
					if (grid[i][j] == true) {
						temp[i][j] = 0; 
					} else{
						temp[i][j] = Math.min(temp[i][j - 1], Math.min(temp[i - 1][j], temp[i - 1][j - 1])) + 1 ; 
					}
				}
			}
		}
		
		max = temp[0][0]; 
		
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (temp[i][j] > max) {
					max = temp[i][j]; 
					xCoord = i - max + 1; 
					yCoord = j - max + 1; 
				}
			}
		}
		System.out.print("Square size " + max); 
		System.out.println(" at location (" + xCoord + ", " + yCoord + ")"); 
		
		System.out.println(); 	
	}
}