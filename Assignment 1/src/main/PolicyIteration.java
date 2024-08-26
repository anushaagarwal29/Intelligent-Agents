package main;

import entities.State;
import entities.StateType;
import fileManager.LogFileController;
import controller.GridEnvironment;
import entities.Constants;
import entities.StateCoordinate;

public class PolicyIteration {

	/* K Value */
	// private final static int K = 4;

	public static void main(String[] args) {
		GridEnvironment grid = new GridEnvironment("preset-1.txt");

		grid.printGridWorld();
		runPolicyIteration(grid);
	}

	/**
	 * This function is used to run Policy Iteration on the given grid world to find optimal Policyp
	 * @param grid
	 */
	private static void runPolicyIteration(GridEnvironment grid) {
		//Flag to check if there is a change in policy (new and updated)
		boolean policyChanged;
		int iteration = 1;
	
		LogFileController logger = new LogFileController("PolicyIteration", grid);
		
		//Display initial grid with State Type, Reward, and Initial Policy
		System.out.println("Grid World Original :");
		grid.print();
		logger.add(grid);

		do {
			policyChanged = false;

			// 1. Policy Evaluation
			policyEvaluation(grid, Constants.K);

			// 2. Policy Improvement 
			for (int c = 0; c < Constants.NUM_COL; c++) {
				for (int r = 0; r < Constants.NUM_ROW; r++) {
					State currentCell = grid.getCell(new StateCoordinate(c, r));

					// If current cell is a wall, skip
					if (currentCell.getStateType() == StateType.WALL)
						continue;
					
					//Check if there is a change in policy after Policy Improvement
					boolean changed = policyImprovement(currentCell, grid);

					if (changed)
						policyChanged = true;
				}
			}

			iteration++;
			logger.add(grid);
		} while (policyChanged); //Continue until policy converges

		System.out.printf("Total Iterations to Converge : %d\n", iteration-1);
		grid.printExperimentParamters(false,0);
		grid.printPolicy();
		grid.printStateUtilities();
		grid.printUtilityGrid();
		
		logger.finalConvertToCSV();
	}

	/**
	 * 1. Policy Evaluation
	 * Calculates utilities for a given Policy.
	 * @param grid
	 * @return
	 */
	private static void policyEvaluation(GridEnvironment grid, int k) {
		//k = Constant K: number of times Bellman update is executed to get next utility estimate 
		for (int i = 0; i < k; i++) {
			for (int c = 0; c < Constants.NUM_COL; c++) {
				for (int r = 0; r < Constants.NUM_ROW; r++) {
					//1. Get current reward & policy
					State currentCell = grid.getCell(new StateCoordinate(c, r));
					
					//If current state is a Wall, then skip
					if (currentCell.getStateType() == StateType.WALL)
						continue;
					
					//2. Add the expected utilities of neighbouring cells(i.e. UP, LEFT, RIGHT)  based on current Policy
					State[] neighbours = grid.getNeighboursOfCell(currentCell);
					double up = Constants.PROBABILITY_UP * neighbours[0].getUtility();
					double left = Constants.PROBABILITY_LEFT * neighbours[1].getUtility();
					double right = Constants.PROBABILITY_RIGHT * neighbours[2].getUtility();

					double futureEstimateUtility = up+left+right;

					//3. Update Utility
					float reward = currentCell.getStateType().getReward();
					//Apply Simplified Belmman equation 
					//Utility of a state = currentStateReward + Max Possible Discounted Future Reward
					currentCell.setUtility(reward + Constants.DISCOUNT_FACTOR * futureEstimateUtility);
				}
			}
		}
	}

	/**
	 * 2. Policy Improvement
	 * @param currentCell
	 * @param grid
	 * @return True if change in policy (current vs updated)
	 */
	private static boolean policyImprovement(State currentCell, GridEnvironment grid) {
		
		//1. Find the maximum possible sub-utility
		double[] maxUtility = new double[StateCoordinate.TOTAL_DIRECTIONS];
		//Calculate utility of possible actions(direction) and get maximum to get best policy
		for (int dir = 0; dir < StateCoordinate.TOTAL_DIRECTIONS; dir++) {
			State[] neighbours = grid.getNeighboursOfCell(currentCell, dir);
			double up = Constants.PROBABILITY_UP * neighbours[0].getUtility();
			double left = Constants.PROBABILITY_LEFT * neighbours[1].getUtility();
			double right = Constants.PROBABILITY_RIGHT * neighbours[2].getUtility();

			maxUtility[dir] = up + left + right;
		}

		int maxUtil = 0;
		for (int x = 1; x < maxUtility.length; x++) {
			if (maxUtility[x] > maxUtility[maxUtil])
				maxUtil = x;
		}

		// 2. Get the Current sub-utility 
		State[] neighbours = grid.getNeighboursOfCell(currentCell);
		double up = Constants.PROBABILITY_UP * neighbours[0].getUtility();
		double left = Constants.PROBABILITY_LEFT * neighbours[1].getUtility();
		double right = Constants.PROBABILITY_RIGHT * neighbours[2].getUtility();

		double currSubUtility = up + left + right;

		//3. Policy improovement to best (max) possible utility
		if (maxUtility[maxUtil] > currSubUtility) {
			//Update policy to policy with max utility
			currentCell.setPolicy(maxUtil);
			return true;
		} else {
			return false;
		}
	}
}
