import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

class Agarwal_Anusha_Player extends Player {

    int[][][] payoff = {
            {{6, 3},     //payoffs when first and second players cooperate
            {3, 0}},     //payoffs when first player coops, second defects
            {{8, 5},     //payoffs when first player defects, second coops
            {5, 2}}};    //payoffs when first and second players defect

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