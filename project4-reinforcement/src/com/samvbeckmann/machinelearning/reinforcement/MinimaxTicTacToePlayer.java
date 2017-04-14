package com.samvbeckmann.machinelearning.reinforcement;

//An optimal player for TTT
public class MinimaxTicTacToePlayer extends AbstractMinimaxPlayer {

    public MinimaxTicTacToePlayer() {
        this.randomSelection = false;
    }

    @Override
    public void init(Board.BoardState playerID) {
        this.playerID = playerID;
    }

    public int interact(Board board, double reward) {
//        int[] board = Environment.getBoard(boardID);
        //It's our turn

        /*
        System.out.println("Given: ");
        System.out.println("Board: ");
        for(int j=0; j<board.length; j++)
            System.out.print(""+board[j]+" ");
        System.out.println("Actions: ");
        for(int j=0; j<actions.length; j++)
            System.out.print(""+actions[j]+" ");
        */

        //Do we have moves?
        if (board.getAvailableActions().size() == 0)
            return -1;

        return (int) minimax(board, playerID, true);
    }
}
