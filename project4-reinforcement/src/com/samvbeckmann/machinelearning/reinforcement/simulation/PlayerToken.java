package com.samvbeckmann.machinelearning.reinforcement.simulation;

/**
 * Defines player tokens for use in the board and in keeping track of statistics.
 * The OPEN token doubles as an empty space when used on a board, and a tie game when collecting statistics.
 *
 * @author Sam Beckmann
 */
public enum PlayerToken {
    X_PLAYER, O_PLAYER, OPEN;

    @Override
    public String toString() {
        switch (this) {
            case O_PLAYER:
                return "O";
            case X_PLAYER:
                return "X";
            case OPEN:
                return " ";
            default:
                return "ERROR";
        }
    }

    /**
     * Gets a character for each token for use in rendering stats.
     * Differs from {@link #toString()} in that an empty space is not return for the {@link #OPEN} value.
     *
     * @return A one-character string that represents the player token when rendering stats.
     */
    public String statsSymbol() {
        switch (this) {
            case X_PLAYER:
                return "X";
            case O_PLAYER:
                return "O";
            case OPEN:
                return "-";
            default:
                return "ERROR";
        }
    }
}
