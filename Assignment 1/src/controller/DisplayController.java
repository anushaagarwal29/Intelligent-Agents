package controller;

import entities.State;
import entities.StateType;
import entities.Constants;

public class DisplayController {

	// Display the experiment parameters for value iteration
	public static void printExperimentParameters(boolean isValueIteration, double convergeThreshold) {
		StringBuilder sb = frameTitle("Experiment Setup");
		if (isValueIteration) {
			sb.append("Discount Factor\t\t" + ":\t" + Constants.DISCOUNT_FACTOR + "\n");
			sb.append("Max Reward(Rmax)\t" + ":\t" + Constants.R_MAX + "\n");
			sb.append("Constant 'c'\t\t" + ":\t" + Constants.C + "\n");
			sb.append("Epsilon Value(c * Rmax)\t" + ":\t" + Constants.EPSILON + "\n");
			sb.append("Utility Upper Bound\t" + ":\t" + String.format("%.5g", Constants.UTILITY_UPPER_BOUND) + "\n");
			sb.append("Convergence Threshold\t:\t" + String.format("%.5f", convergeThreshold) + "\n\n");
		} else {
			sb.append("Discount\t:\t" + Constants.DISCOUNT_FACTOR + "\n");
			//(i.e. # of times simplified Bellman update is repeated to produce the next utility estimate)
			sb.append("k\t\t:\t" + Constants.K + "\n\n");
		}
		System.out.print(sb.toString());
	}

    // Prints GridWrold in the console
	public static void print(int numRow, int numCol, State cells[][]) {
		for (int r = 0; r < numRow; r++) {
			for (int c = 0; c < numCol; c++) {
				State currCell = cells[c][r];

				if (currCell.getStateType() != StateType.WALL) {
					double utility = currCell.getUtility();
					String stateType = currCell.getStateType().getSymbol();
					String policy = currCell.getPolicy().getSymbol();

					System.out.printf("| " + stateType + " %7.3f " + policy, utility);
				} else {
					System.out.print("|            ");
				}
			}
			System.out.println("|");
		}
		System.out.println();
	}

	//Print Original Grid World with states
	public static void printGridWorld(State cells[][]) {
		StringBuilder sb = DisplayController.frameTitle("Grid Environment");
		sb.append("|");
		for(int col = 0 ; col < Constants.NUM_COL ; col++) {
			sb.append("--------|");
		}
		sb.append("\n");

		for (int row = 0; row < Constants.NUM_ROW; row++) {

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {

				State currCell = cells[col][row];
				String temp;
				if (currCell.getStateType() != StateType.WALL) {
					// double utility = currCell.getUtility();
					String stateType = currCell.getStateType().getSymbol();
					// String policy = currCell.getPolicy().getSymbol();
					temp=stateType+" ";
				} else {
					temp="Wall";
				}
				int n = (8 - temp.length())/2;
				String str = String.format("%1$"+n+"s", "");
				sb.append(str + temp + str + "|");
			}

			sb.append("\n|");
			for(int col = 0 ; col < Constants.NUM_COL; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|");
			}
			sb.append("\n");
		}

		System.out.println(sb.toString());
	}

	//Print Optimal Policy Grid 
    public static void printPolicy(State cells[][]) {
		StringBuilder sb = DisplayController.frameTitle("Optimal Policy");
		sb.append("|");
		for(int col = 0 ; col < Constants.NUM_COL ; col++) {
			sb.append("--------|");
		}
		sb.append("\n");

		for (int row = 0; row < Constants.NUM_ROW; row++) {

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {

				State currCell = cells[col][row];
				String temp;
				if (currCell.getStateType() != StateType.WALL) {
					// double utility = currCell.getUtility();
					// String stateType = currCell.getstateType().getSymbol();
					String policy = currCell.getPolicy().getSymbol();
					temp=policy+" ";
					// System.out.printf("| " + stateType + " %7.3f " + policy, utility);
				} else {
					// System.out.print("|            ");
					temp="Wall";
				}
				int n = (8 - temp.length())/2;
				String str = String.format("%1$"+n+"s", "");
				sb.append(str + temp + str + "|");
			}

			sb.append("\n|");
			for(int col = 0 ; col < Constants.NUM_COL; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|");
			}
			sb.append("\n");
		}

		System.out.println(sb.toString());
	}

	//Print Utilities of each State in the optimal Policy
	public static void printStateUtilities(State cells[][]) {
		StringBuilder sb = DisplayController.frameTitle("Utilities of States: ");

		for (int col = 0; col < Constants.NUM_COL; col++) {
			for (int row = 0; row < Constants.NUM_ROW; row++) {
				State currCell = cells[col][row];
				if (currCell.getStateType() != StateType.WALL) {
					double utility = currCell.getUtility();
					String util = String.valueOf(utility).substring(0,6);
					sb.append("(" + col + ", " + row + "): " + util + "\n");
				}
				else{
					sb.append("(" + col + ", " + row + "): " + "WALL" + "\n");
				}
			}
		}
		System.out.println(sb.toString());
	}

	//Print Optimal Policy utilities in grid format
    public static void printUtilityGrid(State cells[][]) {
		StringBuilder sb = DisplayController.frameTitle("Optimal Policy Utilities");
		sb.append("|");
		for(int col = 0 ; col < Constants.NUM_COL ; col++) {
			sb.append("--------|");
		}
		sb.append("\n");

		for (int row = 0; row < Constants.NUM_ROW; row++) {

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {

				State currCell = cells[col][row];
				String temp;
				if (currCell.getStateType() != StateType.WALL) {
					double utility = currCell.getUtility();
					// String stateType = currCell.getstateType().getSymbol();
					// String policy = currCell.getPolicy().getSymbol();
					temp=String.valueOf(utility);
                    temp=temp.substring(0,6)+"";
					// System.out.printf("| " + stateType + " %7.3f " + policy, utility);
				} else {
					// System.out.print("|            ");
					temp="Wall";
				}
				int n = (8 - temp.length())/2;
				String str = String.format("%1$"+n+"s", "");
				sb.append(str + temp + str + "|");
			}

			sb.append("\n|");
			for(int col = 0 ; col < Constants.NUM_COL; col++) {
				sb.append("--------|".replace('-', ' '));
			}
			sb.append("\n");

			sb.append("|");
			for(int col = 0 ; col < Constants.NUM_COL ; col++) {
				sb.append("--------|");
			}
			sb.append("\n");
		}

		System.out.println(sb.toString());
	}

	//Print frame Title indicating what will be displayed next
	public static StringBuilder frameTitle(String str) {
		StringBuilder sb = new StringBuilder();
		int padding = 4;
		sb.append("\n");
		for(int i = 0; i < str.length()+padding; i++) {
			sb.append("*");
		}
		sb.append("\n");
		sb.append("* " + str + " *");
		sb.append("\n");
		for(int i = 0; i < str.length()+padding; i++) {
			sb.append("*");
		}
		sb.append("\n");
		sb.append("\n");
		return sb;
	}

}
