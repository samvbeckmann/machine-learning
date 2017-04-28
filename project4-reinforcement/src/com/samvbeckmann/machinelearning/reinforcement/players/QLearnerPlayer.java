package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.SparseQTable;
import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

import java.util.List;
import java.util.Random;

/**
 * Defines a tic-tac-toe player that uses Q-Learning to play.
 */
@SuppressWarnings("unused")
public class QLearnerPlayer implements TicTacToePlayer {

    private SparseQTable qTable;
    private Board lastState;
    private int lastAction;
    private double gamma;
    private Random rnd;
    private double epsilon;

    private final static double GAMMA = 0.9;
    private final static double UPDATE_RULE = 0.999;

    public QLearnerPlayer() {
        this.qTable = new SparseQTable();
        this.lastState = null;
        this.lastAction = -1;
        this.rnd = new Random();
        this.epsilon = 1;
    }

    @Override
    public int interact(Board board) {
        lastState = new Board(board);
        int action = selectAction(board);
        qTable.incrementAlpha(lastState, action);
        lastAction = action;
        return action;
    }

    @Override
    public void giveReward(Board board, double reward, boolean terminal) {
        if (lastState == null) return;
        List<Integer> possibleActions = board.getAvailableActions();
        double maxNextUtility = -Double.MAX_VALUE;
        if (possibleActions.size() > 0) {
            qTable.getQValue(board, possibleActions.get(0));
            for (int action : possibleActions) {
                maxNextUtility = Math.max(qTable.getQValue(board, action), maxNextUtility);
            }
        } else {
            maxNextUtility = 0;
        }

        double qDelta = qTable.alphaCalc(lastState, lastAction) * (reward + GAMMA * maxNextUtility - qTable.getQValue(lastState, lastAction));
        qTable.setQValue(lastState, lastAction, qTable.getQValue(lastState, lastAction) + qDelta);

        if (terminal) {
            lastState = null;
            epsilon *= UPDATE_RULE;
        }
    }

    @Override
    public void setPlayer(PlayerToken playerID) {
        // NOOP
    }

    // ε-greedy
    private int selectAction(Board state) {
        List<Integer> possibleActions = state.getAvailableActions();
        if (rnd.nextDouble() < epsilon) {
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
