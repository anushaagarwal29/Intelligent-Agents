import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ThreePrisonersDilemma {
	
	/* 
	 This Java program models the two-player Prisoner's Dilemma game.
	 We use the integer "0" to represent cooperation, and "1" to represent 
	 defection. 
	 
	 Recall that in the 2-players dilemma, U(DC) > U(CC) > U(DD) > U(CD), where
	 we give the payoff for the first player in the list. We want the three-player game 
	 to resemble the 2-player game whenever one player's response is fixed, and we
	 also want symmetry, so U(CCD) = U(CDC) etc. This gives the unique ordering
	 
	 U(DCC) > U(CCC) > U(DDC) > U(CDC) > U(DDD) > U(CDD)
	 
	 The payoffs for player 1 are given by the following matrix: */
	
	static int[][][] payoff = {  
		{{6,3},  //payoffs when first and second players cooperate 
		 {3,0}}, //payoffs when first player coops, second defects
		{{8,5},  //payoffs when first player defects, second coops
	     {5,2}}};//payoffs when first and second players defect
	
	/* 
	 So payoff[i][j][k] represents the payoff to player 1 when the first
	 player's action is i, the second player's action is j, and the
	 third player's action is k.
	 
	 In this simulation, triples of players will play each other repeatedly in a
	 'match'. A match consists of about 100 rounds, and your score from that match
	 is the average of the payoffs from each round of that match. For each round, your
	 strategy is given a list of the previous plays (so you can remember what your 
	 opponent did) and must compute the next action.  */
	
	
	abstract class Player {
		// This procedure takes in the number of rounds elapsed so far (n), and 
		// the previous plays in the match, and returns the appropriate action.
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			throw new RuntimeException("You need to override the selectAction method.");
		}
		
		// Used to extract the name of this player class.
		final String name() {
			String result = getClass().getName();
			return result.substring(result.indexOf('$')+1);
		}
	}
	
	/* Here are four simple strategies: */
	
	class NicePlayer extends Player {
		//NicePlayer always cooperates
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return 0; 
		}
	}
	
	class NastyPlayer extends Player {
		//NastyPlayer always defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return 1; 
		}
	}
	
	class RandomPlayer extends Player {
		//RandomPlayer randomly picks his action each time
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (Math.random() < 0.5)
				return 0;  //cooperates half the time
			else
				return 1;  //defects half the time
		}
	}
	
	class TolerantPlayer extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			int opponentCoop = 0;
			int opponentDefect = 0;
			for (int i=0; i<n; i++) {
				if (oppHistory1[i] == 0)
					opponentCoop = opponentCoop + 1;
				else
					opponentDefect = opponentDefect + 1;
			}
			for (int i=0; i<n; i++) {
				if (oppHistory2[i] == 0)
					opponentCoop = opponentCoop + 1;
				else
					opponentDefect = opponentDefect + 1;
			}
			if (opponentDefect > opponentCoop)
				return 1;
			else
				return 0;
		}
	}
	
	class FreakyPlayer extends Player {
		//FreakyPlayer determines, at the start of the match, 
		//either to always be nice or always be nasty. 
		//Note that this class has a non-trivial constructor.
		int action;
		FreakyPlayer() {
			if (Math.random() < 0.5)
				action = 0;  //cooperates half the time
			else
				action = 1;  //defects half the time
		}
		
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return action;
		}	
	}

	class T4TPlayer extends Player {
		//Picks a random opponent at each play, 
		//and uses the 'tit-for-tat' strategy against them 
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (n==0) return 0; //cooperate by default
			if (Math.random() < 0.5)
				return oppHistory1[n-1];
			else
				return oppHistory2[n-1];
		}	
	}

	class SoftT4TPlayer extends Player {
        //Play tit for tat and defect only if both opponents defect
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0; //cooperate by default
            if ((oppHistory1[n-1]==0) || (oppHistory2[n-1]==0))
                return 0;
            else
                return 1;
        }
    }

    class HardT4TPlayer extends Player {
        //Play tit for tat and defect if either opponent defect
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0; //cooperate by default
            if ((oppHistory1[n-1]==0) && (oppHistory2[n-1]==0))
                return 0;
            else
                return 1;
        }
    }

	class WinStayLoseShift extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

            if (n==0) return 0; //cooperate on first turn

			//Extract last action of my agent and opponents
            int my_lastAction = myHistory[n-1];
            int opp1_lastAction = oppHistory1[n-1];
            int opp2_lastAction = oppHistory2[n-1];

			// If my last action resulted in a reward of >=6 (top 2 possible rewards), 
			// then continue with same action otherwise 
			//change action
            if (payoff[my_lastAction][opp1_lastAction][opp2_lastAction]>=6) return my_lastAction;
            else return my_lastAction==1?0:1;
        }
    }

	class GrimTriggerPlayer extends Player {

		// track if either opponent has defected

		boolean grimTrigger = false;
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (!grimTrigger) {
				for (int i = 0; i < n; i++) {
					if (oppHistory1[i] == 1 || oppHistory2[i] == 1) {
						// Cooperate only until opponent cooperates
						grimTrigger = true; 
						break;
					}
				}
			}
			if (grimTrigger) {
				// If opponent defects even once, defect
				return 1; 
			} else {
				// Cooperate is opponent cooperates
				return 0; 
			}
		}
	}
	
	class Agarwal_Anusha_Player extends Player {

		int myTotalPayoff=0, opp1TotalPayoff=0, opp2TotalPayoff=0;
		int countOpp1C = 0; int countOpp2C = 0;

		final double THRESHOLD = 0.9; 

		//Check if I have the highest payoff score
		private int isWinning() {
			if (myTotalPayoff>=opp1TotalPayoff && myTotalPayoff>=opp2TotalPayoff) return 0;
			return 1;
		}

		//Helper function to return the inverse of the input action 
		//i.e. 0->1 and 1->0
		private int invertAction(int action) {
			if (action == 1) return 0;
			return 1;
		}

		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			/**
			RULES:
			[#0] Balance between being trustworthy as well as unpredictable
			[#1] Prioritize self-protection
			[#2] Reciprocate cooperation
			[#3] Transform losing scenario to mutual loss
			*/

			// Always cooperate in the first round
			if (n==0) return 0;

			// Updating Previous actions and payoffs for each player 
			int my_prevAction = myHistory[n-1];
			int opp1_prevAction = oppHistory1[n-1];
			int opp2_prevAction = oppHistory2[n-1];

			this.myTotalPayoff += payoff[my_prevAction][opp1_prevAction][opp2_prevAction];
			this.opp1TotalPayoff += payoff[opp1_prevAction][opp2_prevAction][my_prevAction];
			this.opp2TotalPayoff += payoff[opp2_prevAction][opp1_prevAction][my_prevAction];

			// Update opponent's cooperate record.
			if (n>0) {
				countOpp1C += invertAction(oppHistory1[n-1]);
				countOpp2C += invertAction(oppHistory2[n-1]);
			}
			// Calculate opponent's cooperate probability.
			double opp1_CProb = countOpp1C / oppHistory1.length;
			double opp2_CProb = countOpp2C / oppHistory2.length;

			//When almost towards the end of the tournament, take action according to opponent past behaviour
			//If opponents cooperate < 90% of the time then defect 
			if ((n>100) && (opp1_CProb<THRESHOLD && opp1_CProb<THRESHOLD)) {
				if (Math.random()<0.99) return 1;
				else return 1;
			}

			/** [RECIPROCATE COOPERATION]
			 * If the opponents usually cooperate, with a probability higher than 0.9 
			 * Also check if both opponents cooperated in the last turn 
			 * Then continue to cooperate with 99% probability and 1% unpredictability
			*/
			if ((opp1_prevAction+opp2_prevAction ==0)&&(opp1_CProb>THRESHOLD && opp2_CProb>THRESHOLD)) {
				if (Math.random()<0.99) return 0;
				else return 1;
			}
			else
				/**[PRIORITIZE SELF PROTECTION]
				 * [IF I AM LOSING TRANSFORM SITUATION TO MUTUAL LOSS - DEFECT]
				 * If the opponent's cooperation probability if low, then check if I have the highest payoff
				 * If yes, then continue to cooperate, otherwise defect
				*/
				return isWinning();
		}
	}
	
	/* In our tournament, each pair of strategies will play one match against each other. 
	 This procedure simulates a single match and returns the scores. */
	float[] scoresOfMatch(Player A, Player B, Player C, int rounds) {
		int[] HistoryA = new int[0], HistoryB = new int[0], HistoryC = new int[0];
		float ScoreA = 0, ScoreB = 0, ScoreC = 0;
		
		for (int i=0; i<rounds; i++) {
			int PlayA = A.selectAction(i, HistoryA, HistoryB, HistoryC);
			int PlayB = B.selectAction(i, HistoryB, HistoryC, HistoryA);
			int PlayC = C.selectAction(i, HistoryC, HistoryA, HistoryB);
			ScoreA = ScoreA + payoff[PlayA][PlayB][PlayC];
			ScoreB = ScoreB + payoff[PlayB][PlayC][PlayA];
			ScoreC = ScoreC + payoff[PlayC][PlayA][PlayB];
			HistoryA = extendIntArray(HistoryA, PlayA);
			HistoryB = extendIntArray(HistoryB, PlayB);
			HistoryC = extendIntArray(HistoryC, PlayC);
		}
		float[] result = {ScoreA/rounds, ScoreB/rounds, ScoreC/rounds};
		return result;
	}
	
//	This is a helper function needed by scoresOfMatch.
	int[] extendIntArray(int[] arr, int next) {
		int[] result = new int[arr.length+1];
		for (int i=0; i<arr.length; i++) {
			result[i] = arr[i];
		}
		result[result.length-1] = next;
		return result;
	}
	
	/* The procedure makePlayer is used to reset each of the Players 
	 (strategies) in between matches. When you add your own strategy,
	 you will need to add a new entry to makePlayer, and change numPlayers.*/
	
	int numPlayers = 7;
	Player makePlayer(int which) {
		switch (which) {
		case 0: return new NicePlayer();
		case 1: return new NastyPlayer();
		case 2: return new RandomPlayer();
		case 3: return new TolerantPlayer();
		case 4: return new FreakyPlayer();
		case 5: return new T4TPlayer();
		// case 6: return new SoftT4TPlayer();
		// case 7: return new HardT4TPlayer();
		// case 8: return new WinStayLoseShift();
		// case 9: return new GrimTriggerPlayer();
		case 6: return new Agarwal_Anusha_Player();
		}
		throw new RuntimeException("Bad argument passed to makePlayer");
	}
	
	/* Finally, the remaining code actually runs the tournament. */
	
	public static void main (String[] args) {
		ThreePrisonersDilemma instance = new ThreePrisonersDilemma();
		// for (int x=0;x<25;x++)
			instance.runTournament();
	}
	
	boolean verbose = true; // set verbose = false if you get too much text output
	
	void runTournament() {
		float[] totalScore = new float[numPlayers];

		// This loop plays each triple of players against each other.
		// Note that we include duplicates: two copies of your strategy will play once
		// against each other strategy, and three copies of your strategy will play once.

		for (int i=0; i<numPlayers; i++) for (int j=i; j<numPlayers; j++) for (int k=j; k<numPlayers; k++) {

			Player A = makePlayer(i); // Create a fresh copy of each player
			Player B = makePlayer(j);
			Player C = makePlayer(k);
			int rounds = 90 + (int)Math.rint(20 * Math.random()); // Between 90 and 110 rounds
			float[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
			totalScore[i] = totalScore[i] + matchResults[0];
			totalScore[j] = totalScore[j] + matchResults[1];
			totalScore[k] = totalScore[k] + matchResults[2];
			if (verbose)
				System.out.println(A.name() + " scored " + matchResults[0] +
						" points, " + B.name() + " scored " + matchResults[1] + 
						" points, and " + C.name() + " scored " + matchResults[2] + " points.");
		}
		int[] sortedOrder = new int[numPlayers];
		// This loop sorts the players by their score.
		for (int i=0; i<numPlayers; i++) {
			int j=i-1;
			for (; j>=0; j--) {
				if (totalScore[i] > totalScore[sortedOrder[j]]) 
					sortedOrder[j+1] = sortedOrder[j];
				else break;
			}
			sortedOrder[j+1] = i;
		}
		
		// Finally, print out the sorted results.
		if (verbose) System.out.println();
		System.out.println("Tournament Results");
		for (int i=0; i<numPlayers; i++) 
			System.out.println(makePlayer(sortedOrder[i]).name() + ": " 
				+ totalScore[sortedOrder[i]] + " points.");
			// System.out.println(makePlayer(sortedOrder[i]).name() + "," 
				// + totalScore[sortedOrder[i]]);
		
	} // end of runTournament()
	
} // end of class PrisonersDilemma

