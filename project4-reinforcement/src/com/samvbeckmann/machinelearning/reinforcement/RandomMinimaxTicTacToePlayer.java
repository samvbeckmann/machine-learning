package com.samvbeckmann.machinelearning.reinforcement;

import java.util.List;

// An optimal player for TTT
public class RandomMinimaxTicTacToePlayer extends AbstractMinimaxPlayer {

    public RandomMinimaxTicTacToePlayer() {
        this.randomSelection = true;
    }

    @Override
    public void init(Board.BoardState playerID) {
        this.playerID = playerID;
    }

    @Override
    public int interact(Board board, double reward) {
//        int[] board = Environment.getBoard(boardID);
        // It's our turn

        /*
        System.out.println("Given: ");
        System.out.println("Board: ");
        for(int j=0; j<board.length; j++)
            System.out.print(""+board[j]+" ");
        System.out.println("Actions: ");
        for(int j=0; j<actions.length; j++)
            System.out.print(""+actions[j]+" ");
        */

        // Do we have moves?
        List<Integer> actions = board.getAvailableActions();
        if (actions.size() == 0) return -1;

        return (int) minimax(board, playerID, true);
    }
}
