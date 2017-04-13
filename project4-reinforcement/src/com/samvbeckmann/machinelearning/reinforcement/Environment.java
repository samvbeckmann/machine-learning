package com.samvbeckmann.machinelearning.reinforcement;

import java.util.Random;

public class Environment {
    //QAgents should NEVER EVER NEED TO USE ANYTHING IN HERE AT ALL

    //Reward values
    public static final int WIN_REWARD = 100;
    public static final int LOSE_REWARD = -100;
    public static final int DRAW_REWARD = 0;
    public static final double GOOD_REWARD = 50.0;
    public static final double BAD_REWARD = -50.0;

    public static void main(String[] args) {
        //Defaults - feel free to modify these as you see fit
        double prob = 0; //Probability of getting intermediate reward
        String player1 = "RandomPlayer"; //Player1's Class
        String player2 = "RandomPlayer"; //Player2's Class
        int rounds = 1; //The number of rounds to run
        boolean print = false; //Whether to print games
        boolean stats = false; //Whether to print stats
        long seed = System.currentTimeMillis(); //Random seed

        //Our optimal player, for comparison
        TicTacToePlayer optimusPrime = new RandomMinimaxTicTacToePlayer();

        //Process command line args
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-prob"))
                prob = Double.parseDouble(args[++i]);
            else if (args[i].equals("-player1"))
                player1 = args[++i];
            else if (args[i].equals("-player2"))
                player2 = args[++i];
            else if (args[i].equals("-rounds"))
                rounds = Integer.parseInt(args[++i]);
            else if (args[i].equals("-print"))
                print = true;
            else if (args[i].equals("-stats"))
                stats = true;
            else if (args[i].equals("-seed"))
                seed = Long.parseLong(args[++i]);
        }

        //Stats
        int xWinNum = 0;
        int oWinNum = 0;
        int catWinNum = 0;
        int[] winArray = new int[rounds];
        Random r = new Random(seed);

        for (int i = 0; i < rounds; i++)
            winArray[i] = 0;

        //Now, we load in the two players
        //This will probably me modified later to be more flexible
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        TicTacToePlayer p1 = null, p2 = null;
        try {
            p1 = (TicTacToePlayer) (cl.loadClass(player1).newInstance());
            p2 = (TicTacToePlayer) (cl.loadClass(player2).newInstance());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Could not find a class you specified.");
            System.exit(1);
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            System.out.println("Could not instantiate a class you specified.");
            System.exit(1);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.out.println("Illegal access.");
            System.exit(1);
        }

        //Then, we start the game loop
        for (int i = 0; i < rounds; i++) {
            int player = -1; //X goes first
            int move;
            double p1R = 0;
            double p2R = 0;
            int[] state = new int[9];
            while (!endState(state)) {
                if (player == -1)
                    move = p1.interact(getID(state), player, getActions(state), p1R);
                else
                    move = p2.interact(getID(state), player, getActions(state), p2R);

                //Validate move
                if (state[move] != 0) {
                    System.out.println("Invalid move from player: " + (player == -1 ? "X" : "O") + " " + move +
                            ". Ending simulation.");
                    System.exit(1);
                }

                //Update move
                state[move] = player;
                if (print) {
                    System.out.println("Player: " + (player == -1 ? "X" : "O") + " played: " + move + ".");
                    printBoard(state);
                    System.out.println();
                }

                if (r.nextDouble() < prob) {
                    //Get optimal move
                    int optimalMove = optimusPrime.interact(getID(state), player, getActions(state), 0.0);
                    if (player == -1) {
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

                player = (player == -1 ? 1 : -1);
            }

            //Give out the final rewards
            if (xWins(state)) {
                p1.interact(getID(state), player, getActions(state), WIN_REWARD);
                p2.interact(getID(state), player, getActions(state), LOSE_REWARD);
                if (print)
                    System.out.println("X Wins!");
                xWinNum++;
                winArray[i] = -1;
            } else if (oWins(state)) {
                p1.interact(getID(state), player, getActions(state), LOSE_REWARD);
                p2.interact(getID(state), player, getActions(state), WIN_REWARD);
                if (print)
                    System.out.println("O Wins!");
                oWinNum++;
                winArray[i] = 1;
            } else {
                p1.interact(getID(state), player, getActions(state), DRAW_REWARD);
                p2.interact(getID(state), player, getActions(state), DRAW_REWARD);
                if (print)
                    System.out.println("Cat Wins!");
                catWinNum++;
                winArray[i] = 0;
            }
            if (print)
                printBoard(state);
        }

        if (stats) {
            System.out.println("X: " + xWinNum + " O: " + oWinNum + " Cat: " + catWinNum);
            System.out.println("Game-by-game breakdown:");
            for (int i = 0; i < rounds; i++)
                System.out.print("" + winArray[i] + " ");
            System.out.println();
        }
    }

    //The implementations of the helper functions
    //These take a board array, and should not be used by QAgents
    public static boolean endState(int[] board) {
        return xWins(board) || oWins(board) || catWins(board);
    }

    public static boolean xWins(int[] board) {
        //Check rows
        if (((board[0] + board[1] + board[2]) == -3) ||
                ((board[3] + board[4] + board[5]) == -3) ||
                ((board[6] + board[7] + board[8]) == -3))
            return true;

        //Check cols
        if (((board[0] + board[3] + board[6]) == -3) ||
                ((board[1] + board[4] + board[7]) == -3) ||
                ((board[2] + board[5] + board[8]) == -3))
            return true;

        //Check diags
        if (((board[0] + board[4] + board[8]) == -3) ||
                ((board[2] + board[4] + board[6]) == -3))
            return true;
        return false;
    }

    public static boolean oWins(int[] board) {
        //Check rows
        if (((board[0] + board[1] + board[2]) == 3) ||
                ((board[3] + board[4] + board[5]) == 3) ||
                ((board[6] + board[7] + board[8]) == 3))
            return true;

        //Check cols
        if (((board[0] + board[3] + board[6]) == 3) ||
                ((board[1] + board[4] + board[7]) == 3) ||
                ((board[2] + board[5] + board[8]) == 3))
            return true;

        //Check diags
        if (((board[0] + board[4] + board[8]) == 3) ||
                ((board[2] + board[4] + board[6]) == 3))
            return true;

        return false;
    }

    public static boolean catWins(int[] board) {
        if (xWins(board) || oWins(board)) return false;
        for (int i = 0; i < board.length; i++)
            if (board[i] == 0) return false;

        return true;
    }

    public static int getID(int[] board) {
        int ret = 0;
        for (int i = 0; i < board.length; i++) {
            ret *= 10;
            ret = ret + board[i] + 1;
        }
        return ret;
    }

    public static int[] getBoard(int id) {
        int[] ret = new int[9];
        for (int i = ret.length - 1; i >= 0; i--) {
            ret[i] = id % 10 - 1;
            id /= 10;
        }

        return ret;
    }

    public static int[] getActions(int[] board) {
        int l = 0;
        for (int i = 0; i < board.length; i++)
            if (board[i] == 0) l++;

        if (l == 0) return null;

        int[] ret = new int[l];
        l = 0;
        for (int i = 0; i < board.length; i++)
            if (board[i] == 0) {
                ret[l] = i;
                l++;
            }

        return ret;

    }

    public static void printBoard(int[] board) {
        for (int i = 0; i < board.length; i++) {
            if (i % 3 == 0) System.out.println();
            if (board[i] == -1)
                System.out.print("X");
            else if (board[i] == 1)
                System.out.print("O");
            else System.out.print(" ");
        }
        System.out.println();
    }

    public static int[] getNewBoard(int[] board, int move, int player) {
        //System.out.print("GetNewBoard: ");
        //System.out.println("Moving: " + move + " Player: " + player + " Board: ");
        //for(int j=0; j<board.length; j++)
        //    System.out.print("" + board[j] + " ");
        //System.out.println();
        if (board[move] != 0) {
            System.out.println("Illegal move.");
            return null;
        }
        int[] newBoard = new int[board.length];
        for (int i = 0; i < newBoard.length; i++)
            newBoard[i] = board[i];
        newBoard[move] = player;
        return newBoard;
    }

    //Some useful helper functions
    //These take a board id, and are all that a QLearner should need
    public static boolean xWins(int id) {
        return xWins(getBoard(id));
    }

    public static boolean oWins(int id) {
        return oWins(getBoard(id));
    }

    public static boolean catWins(int id) {
        return catWins(getBoard(id));
    }

    public static boolean endState(int id) {
        return endState(getBoard(id));
    }

    public static int[] getActions(int id) {
        return getActions(getBoard(id));
    }

    public static int[] getNewBoard(int id, int move, int player) {
        return getNewBoard(getBoard(id), move, player);
    }
}
