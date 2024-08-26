package entities;

public enum Policy {
	UP(0), DOWN(1), LEFT(2), RIGHT(3);
	// UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3
	private int direction; 
	private String symbol;

	private Policy(int direction) {
		this.direction = direction;

		switch (direction) {
		case 0:
			this.symbol = "\u2191";
			break;
		case 1:
			this.symbol = "\u2193";
			break;
		case 2:
			this.symbol = "\u2190";
			break;
		case 3:
			this.symbol = "\u2192";
			break;
		}
	}

	public String getSymbol() {
		return this.symbol;
	}
	
	public int getDirection() {
		return this.direction;
	}
}
