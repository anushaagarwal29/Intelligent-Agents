package entities;

public class State extends StateCoordinate {
	private double utility;
	private Policy policy;
	private StateType stateType;

	/**
	 * Initialise State with cooridinates 
	 * Default Values: Utility = 0, Policy = UP, StateType = WHITE
	 * @param coordinate
	 */
	public State(StateCoordinate coordinate) {
		super(coordinate.getCol(), coordinate.getRow());
		this.utility = 0;
		this.policy = Policy.UP;
		this.stateType = StateType.WHITE;
	}

	/**
	 * Define Getter-Setter Methods for StateType
	 */
	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(char type) {
		switch (type) {
		case 'W':
			this.stateType = StateType.WHITE;
			this.setUtility(Constants.WHITE_REWARD);
			break;
		case 'G':
			this.stateType = StateType.GREEN;
			this.setUtility(Constants.GREEN_REWARD);
			break;
		case 'B':
			this.stateType = StateType.BROWN;
			this.setUtility(Constants.BROWN_REWARD);
			break;
		case 'X':
			this.stateType = StateType.WALL;
			this.setUtility(Constants.WALL_REWARD);
			break;
		}
	}

	/**
	 * Define Getter-Setter Methods for Utility
	 */
	public double getUtility() {
		return utility;
	}
	public void setUtility(double utility) {
		this.utility = utility;
	}

	/**
	 * Define Getter-Setter Methods for Policy
	 */
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(int val) {
		switch (val) {
		case StateCoordinate.UP:
			this.policy = Policy.UP;
			break;
		case StateCoordinate.DOWN:
			this.policy = Policy.DOWN;
			break;
		case StateCoordinate.LEFT:
			this.policy = Policy.LEFT;
			break;
		case StateCoordinate.RIGHT:
			this.policy = Policy.RIGHT;
			break;
		}
	}

}
