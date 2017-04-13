package com.samvbeckmann.machinelearning.reinforcement;

import java.util.Random;

/**
 * Abstract Minimax TicTacToe Player designed to share code between the two minimax players.
 *
 * @author Sam Beckmann
 */
abstract public class AbstractMinimaxPlayer implements TicTacToePlayer {
    private Random rnd = new Random();

    abstract public int interact(int boardID, int myNum, int[] actions, double reward);

    int minimax(int[] board, int[] actions, int myNum, boolean m, boolean first, boolean random) {
        //System.out.println("Checking: "); Environment.printBoard(board);
        //System.out.println("Actions: " + actions.length);
        //for(int i=0; i<actions.length; i++)
        //    System.out.print("" + actions[i] + " ");
        //System.out.println();
        //Base cases
        if (Environment.xWins(board)) {
            //System.out.println("XWins!");
            return Environment.WIN_REWARD;
        }

        if (Environment.oWins(board)) {
            //System.out.println("OWins!");
            return Environment.LOSE_REWARD;
        }

        if (Environment.catWins(board)) {
            //System.out.println("cat");
            return Environment.DRAW_REWARD;
        }

        //Iterate through the kids and return the best
        double max[] = new double[actions.length];
        for (int i = 0; i < actions.length; i++) {
            int[] newBoard = Environment.getNewBoard(board, actions[i], myNum);
            if (newBoard == null) {
                System.out.println("Bad board.");
                System.out.println("Index: " + i);
                System.out.println("Actions: ");
                for (int j = 0; j < actions.length; j++)
                    System.out.print("" + actions[j] + " ");
                System.out.println("\nBoard: ");
                for (int j = 0; j < board.length; j++)
                    System.out.print("" + board[j] + " ");
                System.out.println();
            }
            int[] newActions = Environment.getActions(newBoard);
            max[i] = minimax(newBoard, newActions, (myNum == -1 ? 1 : -1), !m, false, random);
        }

        //Find the best
        double best = (m ? Environment.LOSE_REWARD * 2 : Environment.WIN_REWARD * 2);
        int count = 0;
        int loc = 0;
        for (int i = 0; i < max.length; i++) {
            if (m) {
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
                } else if (max[i] == best) count++;
            }
        }

        //If we're random...
        if (random && count > 1 && first) {
            int choice = rnd.nextInt(count);
            int c = 0;
            for (int i = 0; i < max.length; i++) {
                if (max[i] == best) {
                    if (c == choice)
                        if (first) return actions[i];
                        else return (int) max[i];
                    else c++;
                }
            }
        }

        if (first) return actions[loc];
        return (int) max[loc];
    }
}
