package com.samvbeckmann.machinelearning.reinforcement;

import java.util.Random;

public class RandomPlayer implements TicTacToePlayer {
    private Random r;

    public RandomPlayer(long seed) {
        r = new Random(seed);
    }

    public RandomPlayer() {
        r = new Random();
    }

    public int interact(int boardID, int myPlayerNum, int[] actions, double reward) {
        int[] moves = Environment.getActions(boardID);
        if (moves == null) return -1;
        if (moves.length == 0) return -1;
        return moves[r.nextInt(moves.length)];
    }
}
