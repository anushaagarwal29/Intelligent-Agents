package entities;

public class Constants {
	// Grid World
	public final static int NUM_COL = 6;
	public final static int NUM_ROW = 6;
	// Assignment 1 -- Part 2 (c)
	// public final static int NUM_COL = 12;
	// public final static int NUM_ROW = 12;
	
	// Reward
	public final static float WHITE_REWARD = -0.04f;
	public final static float GREEN_REWARD = 1f;
	public final static float BROWN_REWARD = -1f;
	public final static float WALL_REWARD = 0f;
	
	// Transition Probability
	public final static float PROBABILITY_UP = 0.8f;
	public final static float PROBABILITY_LEFT = 0.1f;
	public final static float PROBABILITY_RIGHT = 0.1f;

	//Grid-World Parameters
	public static final int TOTAL_DIRECTIONS = 4;
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	
	// Discount Factor 
	public final static float DISCOUNT_FACTOR = 0.99f;

	// Rmax
	public static final float R_MAX = 1.00f;

	// Parameters to adjust the maximum error threshold allowed
	// Constant C 
	public static final float C = 0.01f;	
	// Epsilon e = c * Rmax
	public static final float EPSILON = C * R_MAX;
	public static final float UTILITY_UPPER_BOUND = R_MAX / (1 - DISCOUNT_FACTOR);

	// Constant K
	// Parameter to set number of times Bellman update is executed to get next utility estimate 
	public static final int K = 40;
}
