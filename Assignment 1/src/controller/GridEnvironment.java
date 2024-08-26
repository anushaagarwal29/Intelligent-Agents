package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import entities.Constants;
import entities.State;
import entities.StateCoordinate;


// import entities.Constants;

public class GridEnvironment {
	State[][] cells;
	int numOfCol, numOfRow;

	/**
	 * Initialise Grid with speicified numb of rows and columns. States are set to default values
	 * Default Values: Utility = 0, Policy = UP, StateType = WHITE
	 * @param col
	 * @param row
	 */
	public GridEnvironment() {
		if (numOfCol < 0 || numOfRow < 0)
			throw new IllegalArgumentException("Column and Row must be a positive integer.");

		this.numOfCol = Constants.NUM_COL;
		this.numOfRow = Constants.NUM_ROW;
		this.cells = new State[this.numOfCol][this.numOfRow];

		for (int c = 0; c < numOfCol; c++) {
			for (int r = 0; r < numOfRow; r++) {
				StateCoordinate coordinates = new StateCoordinate(c, r);
				cells[c][r] = new State(coordinates);
			}
		}
		
	}

	//Paramterized constructor to extract states from the text file
	public GridEnvironment(String fileName) {
		this();
		this.importGridFromFile(fileName);
	}

	public State getCell(StateCoordinate coordinate) {
		return NeighbourhoodStates.getCell(coordinate,cells);
	}

	/**
	 * Get Neighbourhood states wrt current policy and policy direction
	 * Call Display Controller
	 */


	/**
	 * Get the corresponding neighbours (UP, LEFT, RIGHT) wrt current Policy
	 * @param currentCell
	 * @return [Intended Position, Left Angle (L), Right Angle (R)]
	 */
	
	public State[] getNeighboursOfCell(State currentCell) {
		return NeighbourhoodStates.getNeighboursOfCell(currentCell, cells);
	}

	/**
	 * Get the corresponding neighbours (UP, LEFT, RIGHT) wrt Policy direction.
	 * @param currentCell
	 * @return [Intended Position, Right Angle (L), Right Angle (R)]
	 */
	public State[] getNeighboursOfCell(State currentCell, int direction) {
		return NeighbourhoodStates.getNeighboursOfCell(currentCell, direction, cells);
	}

	/**
	 * Display Grid World Functions
	 * Call Display Controller
	 */
	public void printGridWorld(){
		DisplayController.printGridWorld(cells);
	}
	public void print() {
		DisplayController.print(numOfRow,numOfCol,cells);
	}
	public void printPolicy() {
		DisplayController.printPolicy(cells);
	}
	public void printUtilityGrid() {
		DisplayController.printUtilityGrid(cells);
	}
	public void printStateUtilities() {
		DisplayController.printStateUtilities(cells);
	}
	public void printExperimentParamters(boolean isValueIteration,double threshold) {
		DisplayController.printExperimentParameters(isValueIteration,threshold);
	}

	/**
	 * Import the grid world states from text file
	 * @param currentCell
	 */
	public void importGridFromFile(String fileName) {
		try {
			String filePath = new File("").getAbsolutePath();
			Scanner s = new Scanner(new BufferedReader(new FileReader(filePath.concat("/presetGridWorlds/" + fileName))));

			while (s.hasNext()) {
				for (int r = 0; r < this.numOfRow; r++) {
					for (int c = 0; c < this.numOfCol; c++) {
						char stateType = s.next().charAt(0);
						cells[c][r].setStateType(stateType);
					}
				}
			}

			s.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
