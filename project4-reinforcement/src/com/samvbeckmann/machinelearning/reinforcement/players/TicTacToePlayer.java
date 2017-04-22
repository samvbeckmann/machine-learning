package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

/**
 * The interface for a tic-tac-toe player. A trivial implementation example can be found in {@link RandomPlayer}.
 * A more complex example, using the minimax algorithm to choose moves, can be found in {@link MinimaxTicTacToePlayer}.
 * <p>
 * Tic-tac-toe players can keep track of the game from the contract that the {@link #interact(Board)} method is called
 * precisely one time on each turn.
 *
 * @author Sam Beckmann
 */
public interface TicTacToePlayer {

    /**
     * Method for an agent to interact with the Tic-Tac-Toe board.
     * This method is called precisely one time on each turn. An agent can determine where the other player moved by
     * storing the previous board state and comparing it to the current board state.
     *
     * @param board Current board state
     * @return Location that the agent wishes to move this turn.
     */
    int interact(Board board);

    /**
     * Gives a reward value to the agent for its most recent move. This method will be called a maximum of one time per
     * turn, although it may not be called at all. The reward is always in respect to the most recently made movement.
     *  @param board
     * @param reward Value of the reward for the agent's last move. Higher rewards are better.
     * @param terminal
     */
    void giveReward(Board board, double reward, boolean terminal);

    /**
     * Update the token that represents the player.
     * This method is called at the initialization of each player, and again if the player's token changes.
     * For most players, this method is only called once.
     * However, in special cases, such as an agent designed to give out rewards or test certain moves, this method may
     * be called multiple times.
     * Agents should be designed to accommodate this method being called multiple times over their lifetimes.
     *
     * @param playerID The token that represents this player. Either {@link PlayerToken#X_PLAYER} or
     *                 {@link PlayerToken#O_PLAYER}.
     */
    void setPlayer(PlayerToken playerID);
}
