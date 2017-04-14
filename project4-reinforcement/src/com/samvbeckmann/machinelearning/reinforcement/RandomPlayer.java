package com.samvbeckmann.machinelearning.reinforcement;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements TicTacToePlayer {
    private Random r;

    public RandomPlayer(long seed) {
        r = new Random(seed);
    }

    public RandomPlayer() {
        r = new Random();
    }

    @Override
    public void init(Board.BoardState playerID) {
        // NOOP
    }

    @Override
    public int interact(Board board, double reward) {
        List<Integer> moves = board.getAvailableActions();
        if (moves.size() == 0) return -1;
        return moves.get(r.nextInt(moves.size()));
    }
}
