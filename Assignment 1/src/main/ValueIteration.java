package main;

import entities.State;
import entities.StateType;
import fileManager.LogFileController;
import controller.GridEnvironment;
import entities.Constants;
import entities.StateCoordinate;

public class ValueIteration {

	/* Epsilon */
	// C = 0.01;
	// R_MAX = 1;
	// EPSILON = C * R_MAX;

	public static void main(String[] args) {
		GridEnvironment grid = new GridEnvironment("preset-1.txt");
		grid.printGridWorld();
		runValueIteration(grid);
	}

	/**
	 * Function to run value Iteration on Grid
	 * @param grid
	 */
	private static void runValueIteration(GridEnvironment grid) {
		double threshold = Constants.EPSILON * ((1 - Constants.DISCOUNT_FACTOR) / Constants.DISCOUNT_FACTOR);
		LogFileController logger = new LogFileController("ValueIteration", grid);
		
		//Display grid with State Type, Reward, and Initial Policy
		System.out.println("Grid World Original :");
		grid.print();
		logger.add(grid);

		double maxChangeInUtility = 0;
		int iteration = 1;
		do {
			// Calculate Maximum change in utility 
			maxChangeInUtility = 0;

			//Run loop for 1 iteration
			for (int c = 0; c < Constants.NUM_COL; c++) {
				for (int r = 0; r < Constants.NUM_ROW; r++) {
					State currentCell = grid.getCell(new StateCoordinate(c, r));

					//If current cell is a wall then skip
					if (currentCell.getStateType() == StateType.WALL)
						continue;

					// Find change in utility 
					double changeInUtility = calculateUtilityChange(currentCell, grid);
					if (changeInUtility > maxChangeInUtility)
						maxChangeInUtility = changeInUtility;
				}
			}

			iteration++;
			// maze.print();
			logger.add(grid);
		} while (maxChangeInUtility > threshold);

		System.out.printf("Total Iterations to Converge : %d\n", iteration-1);
		// System.out.printf("Change in utility from previous iteration: %5.3f\n", maxChangeInUtility);
		grid.printExperimentParamters(true,threshold);
		grid.printPolicy();
		grid.printStateUtilities();
		grid.printUtilityGrid();
		logger.finalConvertToCSV();
	}

	/**
	 * Calculate the utility of the given State.
	 * @param currentCell
	 * @param grid
	 * @return The difference previous Utility and new update Utility
	 */
	private static double calculateUtilityChange(State currentCell, GridEnvironment grid) {
		double[] subUtilities = new double[StateCoordinate.TOTAL_DIRECTIONS];

		//1. Find all possible utilities (i.e. 4 possible directions)
		for (int dir = 0; dir < StateCoordinate.TOTAL_DIRECTIONS; dir++) {
			//1.1 Add the expected utilities of neighbouring cells (i.e. UP, LEFT, RIGHT) 
			State[] neighbours = grid.getNeighboursOfCell(currentCell, dir);
			double up = Constants.PROBABILITY_UP * neighbours[0].getUtility();
			double left = Constants.PROBABILITY_LEFT * neighbours[1].getUtility();
			double right = Constants.PROBABILITY_RIGHT * neighbours[2].getUtility();

			//Sub state utility 
			subUtilities[dir] = up + left + right;
		}

		//2. Find the maximum possible utility
		int maxUtilityIndex = 0;
		for (int u = 1; u < subUtilities.length; u++) {
			if (subUtilities[u] > subUtilities[maxUtilityIndex])
				maxUtilityIndex = u;
		}

		//3. Set utility & policy of current cell
		float currentReward = currentCell.getStateType().getReward();
		double prevUtility = currentCell.getUtility();

		//Apply Simplified Belmman equation 
		//Utility of a state = currentStateReward + Max Possible Discounted Future Reward
		double newUtility = currentReward + Constants.DISCOUNT_FACTOR * subUtilities[maxUtilityIndex];
		currentCell.setUtility(newUtility);
		currentCell.setPolicy(maxUtilityIndex);

		//4. Return the difference of prevUtility & newUtility
		// Converges when this new utility becomes very close to previous utility
		return (Math.abs(prevUtility - newUtility));
	}
}
