package com.samvbeckmann.machinelearning.reinforcement.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Defines a tic-tac-toe board.
 * Locations on the board are defined by a number representing that location.
 * The mapping of numbers to locations is defined below:
 * 0|1|2
 * -+-+-
 * 3|4|5
 * -+-+-
 * 6|7|8
 *
 * @author Sam Beckmann
 */
public class Board {

    private final PlayerToken[] board = new PlayerToken[9];

    /**
     * Basic constructor of a board. Boards initially begin completely empty.
     */
    @SuppressWarnings("WeakerAccess")
    public Board() {
        Arrays.fill(board, PlayerToken.OPEN);
    }

    /**
     * Deep copy constructor for a board.
     *
     * @param passedBoard Board to be copied.
     */
    public Board(Board passedBoard) {
        System.arraycopy(passedBoard.board, 0, this.board, 0, board.length);
    }

    /**
     * Gets the current state of a location in the board.
     *
     * @param location Square to get the state from.
     * @return The current state of the location.
     */
    @SuppressWarnings("WeakerAccess")
    public PlayerToken getState(int location) {
        if (location < 0 || location > 8) {
            throw new IllegalArgumentException("location must be in range [0,8]");
        }
        return board[location];
    }

    /**
     * Validates if a given move would be legal on the current board.
     *
     * @param location Number of square that would be the target of the move.
     * @return True if the move is valid, else false.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean validateMove(int location) {
        if (location < 0 || location > 8) {
            throw new IllegalArgumentException("location must be in range [0,8]");
        }
        return board[location] == PlayerToken.OPEN;
    }

    /**
     * Update the board by having the given player move in the given location.
     * This method verifies the move is valid before making it, and gracefully handles invalid moves.
     * Tic-tac-toe agents should never call this method, as it affects the base board state.
     * The {@link #simulateMove(PlayerToken, int)} method should be used instead, as it is non-destructive.
     *
     * @param player   Player that is making this move. This determines what token is placed onto the board.
     * @param location Board location to make the move. Uses standard the standard board/int mapping.
     * @return True if the move was successfully made, else false.
     */
    boolean move(PlayerToken player, int location) {
        if (!validateMove(location) || (player != PlayerToken.X_PLAYER && player != PlayerToken.O_PLAYER)) {
            return false;
        } else {
            board[location] = player;
            return true;
        }
    }

    /**
     * Generates a new board, which simulates a move by the given player in the given location.
     * This method is used by players that wish to "look ahead" at a possible path, without committing to a move.
     *
     * @param player   Player to make move in simulation.
     * @param location Location to simulate a move.
     * @return A new board, the result of a simulated move in the given location.
     */
    public Board simulateMove(PlayerToken player, int location) {
        Board newBoard = new Board(this);
        if (newBoard.move(player, location)) {
            return newBoard;
        } else {
            throw new IllegalStateException("Move location must be an empty square");
        }
    }

    /**
     * Generates the list of available actions given the current board state.
     * This list is equivalent to the locations with {@link PlayerToken#OPEN} in the current board.
     * This method is O(n), so caching the result may be beneficial.
     *
     * @return A list of the locations which are currently open and can be used for moves.
     * In the event of no open locations, returns an empty list.
     */
    public List<Integer> getAvailableActions() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (getState(i) == PlayerToken.OPEN) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Determines the current state of the game.
     *
     * @return 0 if game is still ongoing
     * 1 if the x-player won
     * 2 if the o-player won
     * 3 if the match is a tie
     */
    public int getGameState() {
        if (playerWins(PlayerToken.X_PLAYER))
            return 1;
        else if (playerWins(PlayerToken.O_PLAYER))
            return 2;
        else if (this.getAvailableActions().size() == 0) {
            return 3;
        } else {
            return 0;
        }
    }

    /**
     * Determines if the given player wins in the current board state.
     *
     * @param player The token of the player to test win condition for.
     * @return True if the player has won, else false.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean playerWins(PlayerToken player) {
        return ((board[0] == player && board[1] == player && board[2] == player) || // row 1
                (board[3] == player && board[4] == player && board[5] == player) || // row 2
                (board[6] == player && board[7] == player && board[8] == player) || // row 3
                (board[0] == player && board[3] == player && board[6] == player) || // col 1
                (board[1] == player && board[4] == player && board[7] == player) || // col 2
                (board[2] == player && board[5] == player && board[8] == player) || // col 3
                (board[0] == player && board[4] == player && board[8] == player) || // diagonal right
                (board[2] == player && board[4] == player && board[6] == player));  // diagonal left
    }

    @Override
    public String toString() {
        String result = "";
        result += board[0].toString() + "|" + board[1].toString() + "|" + board[2].toString() + "\n-+-+-\n";
        result += board[3].toString() + "|" + board[4].toString() + "|" + board[5].toString() + "\n-+-+-\n";
        result += board[6].toString() + "|" + board[7].toString() + "|" + board[8].toString() + "\n";
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board1 = (Board) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
