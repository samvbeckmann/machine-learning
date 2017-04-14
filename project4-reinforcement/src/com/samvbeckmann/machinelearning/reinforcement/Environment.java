package com.samvbeckmann.machinelearning.reinforcement;

import java.util.Random;

public class Environment {
    //QAgents should NEVER EVER NEED TO USE ANYTHING IN HERE AT ALL

    //Reward values
    public static final double WIN_REWARD = 100.0;
    public static final double LOSE_REWARD = -100.0;
    public static final double DRAW_REWARD = 0.0;
    public static final double GOOD_REWARD = 50.0;
    public static final double BAD_REWARD = -50.0;

    // variables
    private TicTacToePlayer xPlayer;
    private TicTacToePlayer oPlayer;
    private int rounds;
    private Board.BoardState[] winArray;
    private int xWins;
    private int oWins;
    private int ties;

    public static void main(String[] args) {
        Environment env = new Environment(new RandomMinimaxTicTacToePlayer(), new RandomMinimaxTicTacToePlayer(), 10);
        env.runSimulation();
    }


    public Environment(TicTacToePlayer xPlayer, TicTacToePlayer oPlayer, int rounds) {
        this.xPlayer = xPlayer;
        this.xPlayer.init(Board.BoardState.X_PLAYER);
        this.oPlayer = oPlayer;
        this.oPlayer.init(Board.BoardState.O_PLAYER);
        this.rounds = rounds;
        this.winArray = new Board.BoardState[rounds];
        this.xWins = 0;
        this.oWins = 0;
        this.ties = 0;
    }

    private void runSimulation() {
        double prob = 0; //Probability of getting intermediate reward
//        String player1 = "RandomPlayer"; //Player1's Class
//        String player2 = "RandomPlayer"; //Player2's Class
//        int rounds = 10; //The number of rounds to run
        boolean print = false; //Whether to print games
        boolean stats = true; //Whether to print stats
        long seed = System.currentTimeMillis(); //Random seed

        //Our optimal player, for comparison
        TicTacToePlayer optimusPrime = new RandomMinimaxTicTacToePlayer();

        //Process command line args
//        for (int i = 0; i < args.length; i++) {
//            if (args[i].equals("-prob"))
//                prob = Double.parseDouble(args[++i]);
//            else if (args[i].equals("-player1"))
//                player1 = args[++i];
//            else if (args[i].equals("-player2"))
//                player2 = args[++i];
//            else if (args[i].equals("-rounds"))
//                rounds = Integer.parseInt(args[++i]);
//            else if (args[i].equals("-print"))
//                print = true;
//            else if (args[i].equals("-stats"))
//                stats = true;
//            else if (args[i].equals("-seed"))
//                seed = Long.parseLong(args[++i]);
//        }

        //Stats
        Random rnd = new Random(seed);

        for (int i = 0; i < rounds; i++) {
            winArray[i] = runGame(rnd, prob, print, optimusPrime);
        }
        if (stats) System.out.print(getStats());
    }

    private Board.BoardState runGame(Random rnd, double prob, boolean print, TicTacToePlayer optimusPrime) {
        Board.BoardState player = Board.BoardState.X_PLAYER;
        int move;
        double p1R = 0;
        double p2R = 0;
//        int[] state = new int[9];
        Board board = new Board();
        while (board.gameState() == 0) {
            if (player == Board.BoardState.X_PLAYER)
                move = xPlayer.interact(board, p1R);
            else
                move = oPlayer.interact(board, p2R);

            board.move(player, move);

            if (print) {
                System.out.println("Player: " + player.toString() + " played: " + move + ".");
                System.out.print(board.toString());
                System.out.println();
            }

            if (rnd.nextDouble() < prob) {
                //Get optimal move
                optimusPrime.init(player);
                int optimalMove = optimusPrime.interact(board, 0.0);
                if (player == Board.BoardState.X_PLAYER) {
                    if (move == optimalMove)
                        p1R = GOOD_REWARD;
                    else
                        p1R = BAD_REWARD;
                } else {
                    if (move == optimalMove)
                        p2R = GOOD_REWARD;
                    else
                        p2R = BAD_REWARD;
                }
            }

            player = player == Board.BoardState.X_PLAYER ? Board.BoardState.O_PLAYER : Board.BoardState.X_PLAYER;
        }

        if (print)
            System.out.print(board.toString());

        //Give out the final rewards
        if (board.playerWins(Board.BoardState.X_PLAYER)) {
            xPlayer.interact(board, WIN_REWARD);
            oPlayer.interact(board, LOSE_REWARD);
            if (print)
                System.out.println("X Wins!");
            xWins++;
            return Board.BoardState.X_PLAYER;
        } else if (board.playerWins(Board.BoardState.O_PLAYER)) {
            xPlayer.interact(board, LOSE_REWARD);
            oPlayer.interact(board, WIN_REWARD);
            if (print)
                System.out.println("O Wins!");
            oWins++;
            return Board.BoardState.O_PLAYER;
        } else {
            xPlayer.interact(board, DRAW_REWARD);
            oPlayer.interact(board, DRAW_REWARD);
            if (print)
                System.out.println("Cat Wins!");
            ties++;
            return Board.BoardState.OPEN;
        }

    }

    private String getStats() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("X wins:%5d\n", xWins));
        result.append(String.format("O wins:%5d\n", oWins));
        result.append(String.format("Ties:%7d\n", ties));
        result.append("Game-by-game breakdown:\n");
        for (int i = 0; i < rounds; i++) {
            result.append(winArray[i]).append(" ");
        }
        return result.toString();
    }

    //The implementations of the helper functions
    //These take a board array, and should not be used by QAgents
//    public static boolean endState(int[] board) {
//        return xWins(board) || oWins(board) || catWins(board);
//    }
//
//    public static boolean xWins(int[] board) {
//        //Check rows
//        if (((board[0] + board[1] + board[2]) == -3) ||
//                ((board[3] + board[4] + board[5]) == -3) ||
//                ((board[6] + board[7] + board[8]) == -3))
//            return true;
//
//        //Check cols
//        if (((board[0] + board[3] + board[6]) == -3) ||
//                ((board[1] + board[4] + board[7]) == -3) ||
//                ((board[2] + board[5] + board[8]) == -3))
//            return true;
//
//        //Check diags
//        if (((board[0] + board[4] + board[8]) == -3) ||
//                ((board[2] + board[4] + board[6]) == -3))
//            return true;
//        return false;
//    }
//
//    public static boolean oWins(int[] board) {
//        //Check rows
//        if (((board[0] + board[1] + board[2]) == 3) ||
//                ((board[3] + board[4] + board[5]) == 3) ||
//                ((board[6] + board[7] + board[8]) == 3))
//            return true;
//
//        //Check cols
//        if (((board[0] + board[3] + board[6]) == 3) ||
//                ((board[1] + board[4] + board[7]) == 3) ||
//                ((board[2] + board[5] + board[8]) == 3))
//            return true;
//
//        //Check diags
//        if (((board[0] + board[4] + board[8]) == 3) ||
//                ((board[2] + board[4] + board[6]) == 3))
//            return true;
//
//        return false;
//    }
//
//    public static boolean catWins(int[] board) {
//        if (xWins(board) || oWins(board)) return false;
//        for (int i = 0; i < board.length; i++)
//            if (board[i] == 0) return false;
//
//        return true;
//    }

//    public static int getID(int[] board) {
//        int ret = 0;
//        for (int i = 0; i < board.length; i++) {
//            ret *= 10;
//            ret = ret + board[i] + 1;
//        }
//        return ret;
//    }

//    public static int[] getBoard(int id) {
//        int[] ret = new int[9];
//        for (int i = ret.length - 1; i >= 0; i--) {
//            ret[i] = id % 10 - 1;
//            id /= 10;
//        }
//
//        return ret;
//    }

//    public static int[] getActions(int[] board) {
//        int l = 0;
//        for (int i = 0; i < board.length; i++)
//            if (board[i] == 0) l++;
//
//        if (l == 0) return null;
//
//        int[] ret = new int[l];
//        l = 0;
//        for (int i = 0; i < board.length; i++)
//            if (board[i] == 0) {
//                ret[l] = i;
//                l++;
//            }
//
//        return ret;
//
//    }

//    public static void printBoard(int[] board) {
//        for (int i = 0; i < board.length; i++) {
//            if (i % 3 == 0) System.out.println();
//            if (board[i] == -1)
//                System.out.print("X");
//            else if (board[i] == 1)
//                System.out.print("O");
//            else System.out.print(" ");
//        }
//        System.out.println();
//    }

//    public static int[] getNewBoard(int[] board, int move, int player) {
//        //System.out.print("GetNewBoard: ");
//        //System.out.println("Moving: " + move + " Player: " + player + " Board: ");
//        //for(int j=0; j<board.length; j++)
//        //    System.out.print("" + board[j] + " ");
//        //System.out.println();
//        if (board[move] != 0) {
//            System.out.println("Illegal move.");
//            return null;
//        }
//        int[] newBoard = new int[board.length];
//        for (int i = 0; i < newBoard.length; i++)
//            newBoard[i] = board[i];
//        newBoard[move] = player;
//        return newBoard;
//    }

    //Some useful helper functions
    //These take a board id, and are all that a QLearner should need
//    public static boolean xWins(int id) {
//        return xWins(getBoard(id));
//    }
//
//    public static boolean oWins(int id) {
//        return oWins(getBoard(id));
//    }
//
//    public static boolean catWins(int id) {
//        return catWins(getBoard(id));
//    }
//
//    public static boolean endState(int id) {
//        return endState(getBoard(id));
//    }
//
//    public static int[] getActions(int id) {
//        return getActions(getBoard(id));
//    }
//
//    public static int[] getNewBoard(int id, int move, int player) {
//        return getNewBoard(getBoard(id), move, player);
//    }
}
