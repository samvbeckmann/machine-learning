package com.samvbeckmann.machinelearning.reinforcement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Defines a tic-tac-toe board.
 *
 */
public class Board {

    enum BoardState {
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
    }

    private BoardState[] board = new BoardState[9];

    public Board() {
        Arrays.fill(board, BoardState.OPEN);
    }

    public Board(Board passedBoard) {
        for (int i = 0; i < board.length; i++) {
            this.board[i] = passedBoard.getState(i);
        }
    }

    /**
     * Gets the current state of a location in the board.
     *
     * @param location Square to get the state from.
     * @return The current state of the location.
     */
    public BoardState getState(int location) {
        if (location < 0 || location > 8) {
            throw new IllegalArgumentException("location must be in range [0,8]");
        }
        return board[location];
    }

    /**
     * Validates if a given move would be legal on the current board.
     *
     * @param location number of square that would be the target of the move.
     * @return true if the move is valid, else false.
     */
    public boolean validateMove(int location) {
        if (location < 0 || location > 8) {
            throw new IllegalArgumentException("location must be in range [0,8]");
        }
        return board[location] == BoardState.OPEN;
    }

    /**
     *
     * @param player
     * @param location
     * @return
     */
    public boolean move(BoardState player, int location) {
        if (!validateMove(location)) {
            return false;
        } else {
            board[location] = player;
            return true;
        }
    }

    /**
     * Generates a new board, which simulates a move by the given player in the given location.
     *
     * @param player Player to make move in simulation.
     * @param location Location to simulate a move.
     * @return A new board, the result of a simulated move in the given location.
     */
    public Board simulateMove(BoardState player, int location){
        Board newBoard = new Board(this);
        if (newBoard.move(player, location)) {
            return newBoard;
        } else {
            throw new IllegalStateException("Move location must be an empty square");
        }
    }

    public List<Integer> getAvailableActions() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if (board[i] == BoardState.OPEN) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Determines the current state of the game.
     *
     * @return 0 if game is still ongoing
     *         1 if the x-player won
     *         2 if the o-player won
     *         3 if the match is a tie
     */
    public int gameState() {
        if (playerWins(BoardState.X_PLAYER))
            return 1;
        else if (playerWins(BoardState.O_PLAYER))
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
     * @param player Player to test winning condition.
     * @return True if the player has won, else false.
     */
    public boolean playerWins(BoardState player) {
        return ((board[0] == player && board[1] == player && board[2] == player) || // row 1
                (board[3] == player && board[4] == player && board[5] == player) || // row 2
                (board[6] == player && board[7] == player && board[8] == player) || // row 3
                (board[0] == player && board[3] == player && board[6] == player) || // col 1
                (board[1] == player && board[4] == player && board[7] == player) || // col 2
                (board[2] == player && board[5] == player && board[8] == player) || // col 3
                (board[0] == player && board[4] == player && board[8] == player) || // diag right
                (board[2] == player && board[4] == player && board[6] == player));  // diag left
    }

    @Override
    public String toString() {
        String result = "";
        result += board[0].toString() + "|" + board[1].toString() + "|" + board[2].toString() + "\n-+-+-\n";
        result += board[3].toString() + "|" + board[4].toString() + "|" + board[5].toString() + "\n-+-+-\n";
        result += board[6].toString() + "|" + board[7].toString() + "|" + board[8].toString() + "\n";
        return result;
    }
}
