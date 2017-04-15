package com.samvbeckmann.machinelearning.reinforcement.players;

/**
 * A tic-tac-toe player that uses the Minimax algorithm to determine its moves.
 * As the algorithm has no depth limit, it fully explores the game tree and thus is an optimal player.
 *
 * @author Sam Beckmann
 */
@SuppressWarnings("unused")
public class MinimaxTicTacToePlayer extends AbstractMinimaxPlayer {

    public MinimaxTicTacToePlayer() {
        this.randomSelection = false;
    }

}