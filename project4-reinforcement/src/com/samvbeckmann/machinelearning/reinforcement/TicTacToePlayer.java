package com.samvbeckmann.machinelearning.reinforcement;

public interface TicTacToePlayer {
    //Given: a board, and which integer represents me, and the reward for my last action
    //Return: which square I'd like to move in

    /**
     * Method for an agent to interact with the Tic-Tac-Toe board.
     *
     * @param board  Current board state
     * @param reward Reward for last action taken
     * @return Square to move in
     */
    int interact(Board board, double reward);

    /**
     * Initialization method that passes an ID to the player.
     *
     * @param playerID X_PLAYER or O_PLAYER, representing the player's ID in the game.
     */
    void init(Board.BoardState playerID);
}
