package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.SparseQTable;
import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Defines a tic-tac-toe player that uses Q-Learning to play.
 */
@SuppressWarnings("unused")
public class QLearnerLambdaPlayer implements TicTacToePlayer {

    private SparseQTable qTable;
    private Board lastState;
    private int lastAction;
    private int nextAction;
    private double gamma;
    private Random rnd;
    private double epsilon;
    private double lambda;

    private final static double GAMMA = 0.9;

    public QLearnerLambdaPlayer() {
        this.qTable = new SparseQTable();
        this.lastState = null;
        this.lastAction = -1;
        this.rnd = new Random();
        this.epsilon = 0.3;
        this.lambda = 0.9;
    }

    @Override
    public int interact(Board board) {
        lastState = new Board(board);
        qTable.incrementAlpha(lastState, nextAction);
        lastAction = nextAction;
        return nextAction;
    }

    @Override
    public void giveReward(Board board, double reward, boolean terminal) {
        nextAction = selectAction(board);
        if (lastState == null) return;

        List<Integer> possibleActions = board.getAvailableActions();

        int bestAction = getOptimalAction(board);

        double delta = reward + GAMMA * qTable.getQValue(board, nextAction) - qTable.getQValue(lastState, lastAction);
        qTable.setEligibility(lastState, lastAction, 1);

        Set<SparseQTable.StateAction> pairs = qTable.getAllPairs();
        for (SparseQTable.StateAction pair : pairs) {
            qTable.setQValue(pair, qTable.getQValue(pair) + qTable.alphaCalc(pair) * delta * qTable.getEligibility(pair));
            if (bestAction == nextAction) {
                qTable.setEligibility(pair, GAMMA * lambda * qTable.getEligibility(pair));
            } else {
                qTable.setEligibility(pair, 0);
            }
        }
//
//
//        return currentBestAction;
//        double maxNextUtility = -Double.MAX_VALUE;
//        if (possibleActions.size() > 0) {
//            qTable.getQValue(board, possibleActions.get(0));
//            for (int action : possibleActions) {
//                maxNextUtility = Math.max(qTable.getQValue(board, action), maxNextUtility);
//            }
//        } else {
//            maxNextUtility = 0;
//        }
//
//
//
//        double qDelta = qTable.alphaCalc(lastState, lastAction) * (reward + GAMMA * maxNextUtility - qTable.getQValue(lastState, lastAction));
//        qTable.setQValue(lastState, lastAction, qTable.getQValue(lastState, lastAction) + qDelta);

        if (terminal) {
            lastState = null;
            epsilon *= .99;
        }
    }

    @Override
    public void setPlayer(PlayerToken playerID) {
        // NOOP
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
        if (possibleActions.size() == 0) {
            return -1;
        } else if (rnd.nextDouble() < epsilon) {
            return possibleActions.get(rnd.nextInt(possibleActions.size()));
        } else {
            return  getOptimalAction(state);
        }
    }

    private int getOptimalAction(Board state) {
        List<Integer> possibleActions = state.getAvailableActions();
        if (possibleActions.size() == 0) {
            return -1;
        }
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
