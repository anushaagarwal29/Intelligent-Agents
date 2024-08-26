package controller;

import entities.Policy;
import entities.State;
import entities.StateCoordinate;
import entities.StateType;

public class NeighbourhoodStates {
	
    public static State getCell(StateCoordinate coordinate,State cells[][]) {
		int c = coordinate.getCol();
		int r = coordinate.getRow();

		return cells[c][r];
	}

	/**
	 * Get the corresponding neighbours (UP, LEFT, RIGHT) wrt current Policy
	 * @param currentCell
	 * @param cells
	 * @return [Intended Position, Left Angle (L), Right Angle (R)]
	 */
	public static State[] getNeighboursOfCell(State currentCell, State cells[][]) {
		Policy currentPolicy = currentCell.getPolicy();
		StateCoordinate[] neighbourCoordinates = currentCell.getNeighbours(currentPolicy.getDirection());

		//Ensure the neighbour stateType is not a wall
		State[] neighbourCells = new State[neighbourCoordinates.length];
		for (int n = 0; n < neighbourCoordinates.length; n++) {
			State neighbourCell = getCell(neighbourCoordinates[n],cells);

			//If neighout a wall, stay in current state
			if (neighbourCell.getStateType() == StateType.WALL)
				neighbourCoordinates[n] = (StateCoordinate) currentCell; 

			neighbourCells[n] = getCell(neighbourCoordinates[n],cells);
		}
		//Collect possible neighbourhood of current state
		return neighbourCells;
	}

	/**
	 * Get the corresponding neighbours (UP, LEFT, RIGHT) wrt given Policy Direction
	 * @param currentCell
	 * @param direction
	 * @param cells
	 * @return [Intended Position, Left Angle (L), Right Angle (R)]
	 */
	public static State[] getNeighboursOfCell(State currentCell, int direction, State cells[][]) {
		StateCoordinate[] neighbourCoordinates = currentCell.getNeighbours(direction);

		/* Make sure neighbour stateType is not a wall */
		State[] neighbourCells = new State[neighbourCoordinates.length];
		for (int n = 0; n < neighbourCoordinates.length; n++) {
			State neighbourCell = getCell(neighbourCoordinates[n],cells);

			//If neighout a wall, stay in current state
			if (neighbourCell.getStateType() == StateType.WALL)
				neighbourCoordinates[n] = (StateCoordinate) currentCell; 

			neighbourCells[n] = getCell(neighbourCoordinates[n],cells);
		}
		//Collect possible neighbourhood of current state
		return neighbourCells;
	}

}
