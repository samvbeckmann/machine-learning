package com.samvbeckmann.machinelearning.reinforcement.players;

// An optimal player for TTT

/**
 * A minimax tic-tac-toe player that chooses randomly between equally optimal moves.
 *
 * @author Sam Beckmann
 */
public class RandomMinimaxTicTacToePlayer extends AbstractMinimaxPlayer {

    public RandomMinimaxTicTacToePlayer() {
        this.randomSelection = true;
    }

}
