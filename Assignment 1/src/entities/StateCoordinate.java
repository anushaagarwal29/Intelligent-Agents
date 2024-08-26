package entities;

public class StateCoordinate {
	private int col, row;
	public static final int TOTAL_DIRECTIONS = 4;
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

	/**
	 * As specified in the assignment, coordinates aformat: (col,row) format 
	 * Top left corner: (0,0).
	 * @param col
	 * @param row
	 */
	public StateCoordinate(int col, int row) {
		if (col < 0 || row < 0)
			throw new IllegalArgumentException("Column and Row must be a positive integer.");
		else if (col > Constants.NUM_COL - 1)
			throw new IllegalArgumentException("Column out of range.");
		else if (row > Constants.NUM_ROW - 1)
			throw new IllegalArgumentException("Rowumn out of range.");
		else {
			this.col = col;
			this.row = row;
		}
	}

	public int getCol() {
		return this.col;
	}

	public int getRow() {
		return this.row;
	}

	/**
	 * Find the neighbouring possible coordinates with respect to provided direction.
	 * @return [Intended Position, Left Angle (L), Right Angle (R)]
	 */
	public StateCoordinate[] getNeighbours(int direction) {

		//Neighbouring Coordinates for possible movement direction
		//[UP (Forward), Left Dir (L), Right Dir (R)]
		StateCoordinate[] coordinates = new StateCoordinate[3];

		/*
		 * Coordinate Offset: 4 x 3 x 2
		 * 4x: [UP, DOWN, LEFT, RIGHT]
		 * 3x: [UP (Forward), Left Dir (L), Right Dir (R)]
		 * 2x: [col, row]
		 */

		//Eg if Agent is at 2,1
		//Intended Action is to go up
		//Possible neighbours are - 
		// Actually Moves up -> (2,0)
		// Moves Left -> (1,1)
		// Moves Right-> (3,1)

		int[][][] coordinateOffset = { { { 0, -1 }, { -1, 0 }, { +1, 0 } }, 
							{ { 0, +1 }, { +1, 0 }, { -1, 0 } },
							{ { -1, 0 }, { 0, +1 }, { 0, -1 } }, 
							{ { +1, 0 }, { 0, -1 }, { 0, +1 } } };

		//UP: Forward
		try {
			coordinates[0] = new StateCoordinate(this.col + coordinateOffset[direction][0][0], this.row + coordinateOffset[direction][0][1]);
		} catch (IllegalArgumentException e) {
			coordinates[0] = this;
		}

		//LEFT: Left Angle
		try {
			coordinates[1] = new StateCoordinate(this.col + coordinateOffset[direction][1][0], this.row + coordinateOffset[direction][1][1]);
		} catch (IllegalArgumentException e) {
			coordinates[1] = this;
		}

		//RIGHT: Right Angle
		try {
			coordinates[2] = new StateCoordinate(this.col + coordinateOffset[direction][2][0], this.row + coordinateOffset[direction][2][1]);
		} catch (IllegalArgumentException e) {
			coordinates[2] = this;
		}

		return coordinates;
	}
}
