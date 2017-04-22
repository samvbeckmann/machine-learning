package com.samvbeckmann.machinelearning.reinforcement.players;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;
import com.samvbeckmann.machinelearning.reinforcement.simulation.PlayerToken;

import java.util.List;
import java.util.Random;

/**
 * A tic-tac-toe player that randomly chooses between all available moves.
 *
 * @author Sam Beckmann
 */
@SuppressWarnings("unused")
public class RandomPlayer implements TicTacToePlayer {
    private final Random rnd;

    public RandomPlayer(long seed) {
        rnd = new Random(seed);
    }

    public RandomPlayer() {
        rnd = new Random();
    }

    @Override
    public void setPlayer(PlayerToken playerID) {
        // NOOP
    }

    @Override
    public void giveReward(Board board, double reward, boolean terminal) {
        // NOOP
    }

    @Override
    public int interact(Board board) {
        List<Integer> moves = board.getAvailableActions();
        if (moves.size() == 0) return -1;
        return moves.get(rnd.nextInt(moves.size()));
    }
}
