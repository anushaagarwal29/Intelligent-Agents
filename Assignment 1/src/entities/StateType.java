package entities;

public enum StateType {
	WHITE, BROWN, GREEN, WALL;

	//Define Reward for corresponding state types
	public float getReward() {
		switch (this) {
		case WHITE:
			return Constants.WHITE_REWARD;
		case GREEN:
			return Constants.GREEN_REWARD;
		case BROWN:
			return Constants.BROWN_REWARD;
		case WALL:
			return Constants.WALL_REWARD;
		default:
			return 0f;
		}
	}

	//Define Symbol for corresponding state types
	public String getSymbol() {
		switch (this) {
		case WHITE:
			return "W";
		case GREEN:
			return "G";
		case BROWN:
			return "B";
		case WALL:
			return "X";
		default:
			return "";
		}
	}
}
