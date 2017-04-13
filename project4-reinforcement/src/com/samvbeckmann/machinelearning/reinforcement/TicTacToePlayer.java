package com.samvbeckmann.machinelearning.reinforcement;

public interface TicTacToePlayer {
    //Given: a board, and which integer represents me, and the reward for my last action
    //Return: which square I'd like to move in

    /**
     * Method for an agent to interact with the Tic-Tac-Toe board.
     *
     * @param board
     * @param myPlayerNum
     * @param actions
     * @param reward Reward for last action taken
     * @return Square to move in
     */
    int interact(int board, int myPlayerNum, int[] actions, double reward);
}
