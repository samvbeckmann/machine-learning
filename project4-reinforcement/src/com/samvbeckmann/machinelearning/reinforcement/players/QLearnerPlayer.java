package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.AlphaTable;
import com.samvbeckmann.machinelearning.reinforcement.SparseQTable;
import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

import java.util.List;
import java.util.Random;

/**
 * Created by sam on 4/17/17.
 */
@SuppressWarnings("unused")
public class QLearnerPlayer implements TicTacToePlayer {

    private PlayerToken player;
    private SparseQTable qTable;
    private AlphaTable alpha;
    private Board lastState;
    private int lastAction;
    private double gamma;
    private Random rnd;

    private final static double GAMMA = 0.9;

    public QLearnerPlayer() {
        this.qTable = new SparseQTable();
        this.alpha = new AlphaTable();
        this.lastState = null;
        this.lastAction = -1;
        this.rnd = new Random();
    }

    @Override
    public int interact(Board board) {
        lastState = new Board(board);
        alpha.updateState(lastState);
        int action = selectAction(board);
        lastAction = action;
        return action;
    }

    @Override
    public void giveReward(Board board, double reward) {
        if (lastState == null) return;
        List<Integer> possibleActions = board.getAvailableActions();
        double maxNextUtility = -Double.MAX_VALUE;
        if (possibleActions.size() > 0) {
            qTable.getQValue(board, possibleActions.get(0));
            for (int action : possibleActions) {
                maxNextUtility = Math.max(qTable.getQValue(board, action), maxNextUtility);
            }
        } else {
            maxNextUtility = reward;
        }
        double qDelta = alpha.alphaCalc(lastState) * (reward + GAMMA * maxNextUtility - qTable.getQValue(lastState, lastAction));
        qTable.setQValue(lastState, lastAction, qTable.getQValue(lastState, lastAction) + qDelta);
    }

    @Override
    public void setPlayer(PlayerToken playerID) {
        this.player = playerID;
    }

//    private int selectAction(Board state) {
//        double temp = 1 / Math.log(alpha.getStateValue(state));
//        List<Integer> actions = state.getAvailableActions();
//        double[] probabilities = new double[actions.size()];
//        double sum = 0;
//        for (int i = 0; i < actions.size(); i++) {
//            probabilities[i] = Math.exp(qTable.getQValue(state, actions.get(i)) / temp);
//            sum += probabilities[i];
//        }
//        double runningTotal = 0;
//        for (int i = 0; i < probabilities.length; i++) {
//            probabilities[i] = probabilities[i] / sum + runningTotal;
//            runningTotal += probabilities[i];
//        }
//
//        double choiceSelector = rnd.nextDouble();
//        for (int i = 0; i < probabilities.length;  i++) {
//            if (choiceSelector < probabilities[i]) {
//                return actions.get(i);
//            }
//        }
//        return -1;
//    }

    // ε-greedy
    private int selectAction(Board state) {
        List<Integer> possibleActions = state.getAvailableActions();
        if (rnd.nextDouble() > 0.9) {
            return possibleActions.get(rnd.nextInt(possibleActions.size()));
        } else {
            double currentMax = qTable.getQValue(state, possibleActions.get(0));
            int currentBestAction = possibleActions.get(0);
            for (int action : possibleActions) {
                if (qTable.getQValue(state, action) > currentMax) {
                    currentMax = qTable.getQValue(state, action);
                    currentBestAction = action;
                }
            }
            return currentBestAction;
        }
    }
}
