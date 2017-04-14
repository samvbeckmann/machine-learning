package com.samvbeckmann.machinelearning.reinforcement;

import java.util.List;
import java.util.Random;

/**
 * Abstract Minimax TicTacToe Player designed to share code between the two minimax players.
 *
 * @author Sam Beckmann
 */
abstract public class AbstractMinimaxPlayer implements TicTacToePlayer {
    private Random rnd = new Random();
    protected Board.BoardState playerID;
    protected boolean randomSelection;

    double minimax(Board board, Board.BoardState currentPlayer, boolean firstLevel) {
        List<Integer> actions = board.getAvailableActions();

//        System.out.println("Checking: ");
//        System.out.print(board.toString());
//        System.out.println("Actions: " +actions.size());
//        for (Integer action : actions) System.out.print("" + action + " ");
//        System.out.println();
        //Base cases
        if (board.playerWins(Board.BoardState.X_PLAYER)) {
//            System.out.println("XWins!");
            return Environment.WIN_REWARD;
        }

        if (board.playerWins(Board.BoardState.O_PLAYER)) {
//            System.out.println("OWins!");
            return Environment.LOSE_REWARD;
        }

        if (board.gameState() == 3) {
//            System.out.println("cat");
            return Environment.DRAW_REWARD;
        }

        boolean isXPlayer = currentPlayer == Board.BoardState.X_PLAYER;
        Board.BoardState nextPlayer = isXPlayer ? Board.BoardState.O_PLAYER : Board.BoardState.X_PLAYER;

        //Iterate through the kids and return the best
        double max[] = new double[actions.size()];
        for (int i = 0; i < actions.size(); i++) {
            Board newBoard = board.simulateMove(currentPlayer, actions.get(i));
            max[i] = minimax(newBoard, nextPlayer, false);
        }

        //Find the best
        double best = (isXPlayer ? Environment.LOSE_REWARD * 2 : Environment.WIN_REWARD * 2);
        int count = 0;
        int loc = 0;
        for (int i = 0; i < max.length; i++) {
            if (isXPlayer) {
                if (max[i] > best) {
                    best = max[i];
                    count = 0;
                    loc = i;
                } else if (max[i] == best) count++;
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
