package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

import java.util.List;
import java.util.Random;

/**
 * Abstract tic-tac-toe player designed to share code between the two minimax players.
 *
 * @author Sam Beckmann
 */
abstract class AbstractMinimaxPlayer implements TicTacToePlayer {
    private final static double X_WIN_REWARD = 1;
    private final static double O_WIN_REWARD = -1;
    private final static double DRAW_REWARD = 0;

    private PlayerToken playerID;
    boolean randomSelection;
    private final Random rnd = new Random();

    @Override
    public void giveReward(Board board, double reward, boolean terminal) {
        // NOOP
    }

    @Override
    public void setPlayer(PlayerToken playerID) {
        this.playerID = playerID;
    }

    @Override
    public int interact(Board board) {
        if (board.getAvailableActions().size() == 0) {
            return -1;
        } else {
            return (int) minimax(board, playerID, true);
        }
    }

    /**
     * Runs the minimax algorithm to determine the optimal move.
     * This method recursively calls itself it iterate through the game tree.
     *
     * @param board         Current state of the board at this point in the algorithm.
     * @param currentPlayer The token of the player that is making a move at this level of the algorithm.
     * @param firstLevel    Only true for the top level, when this method is not called recursively. Used to determine
     *                      the return value of the method.
     * @return If firstLevel is true, returns the location of the optimal move. Otherwise, returns the maximized
     * utility of the subtree from the current board.
     */
    private double minimax(Board board, PlayerToken currentPlayer, boolean firstLevel) {

        // Handle base cases
        switch (board.getGameState()) {
            case 1:
                return X_WIN_REWARD;
            case 2:
                return O_WIN_REWARD;
            case 3:
                return DRAW_REWARD;
        }

        List<Integer> actions = board.getAvailableActions();
        boolean isXPlayer = currentPlayer == PlayerToken.X_PLAYER;
        PlayerToken nextPlayer = isXPlayer ? PlayerToken.O_PLAYER : PlayerToken.X_PLAYER;

        //Iterate through the kids and return the best
        double max[] = new double[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            Board newBoard = board.simulateMove(currentPlayer, actions.get(i));
            max[i] = minimax(newBoard, nextPlayer, false);
        }

        //Find the best
        double best = (isXPlayer ? O_WIN_REWARD * 2 : X_WIN_REWARD * 2);
        int count = 0;
        int loc = 0;
        for (int i = 0; i < max.length; i++) {
            if (isXPlayer) {
                if (max[i] > best) {
                    best = max[i];
                    count = 0;
                    loc = i;
                } else if (max[i] == best) {
                    count++;
                }
            } else {
                if (max[i] < best) {
                    best = max[i];
                    count = 0;
                    loc = i;
                } else if (max[i] == best) {
                    count++;
                }
            }
        }

        //If we're random...
        if (randomSelection && firstLevel && count > 1) {
            int choice = rnd.nextInt(count);
            int c = 0;
            for (int i = 0; i < max.length; i++) {
                if (max[i] == best) {
                    if (c == choice) {
                        return actions.get(i);
                    } else c++;
                }
            }
        }

        if (firstLevel)
            return actions.get(loc);
        return max[loc];
    }
}
