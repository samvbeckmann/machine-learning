package com.samvbeckmann.machinelearning.reinforcement.simulation;

import com.samvbeckmann.machinelearning.reinforcement.players.MinimaxTicTacToePlayer;
import com.samvbeckmann.machinelearning.reinforcement.players.RandomMinimaxTicTacToePlayer;
import com.samvbeckmann.machinelearning.reinforcement.players.TicTacToePlayer;

import java.util.Random;

/**
 * Defines the environment for the tic-tac-toe simulation.
 *
 * @author Sam Beckmann
 */
class Environment {

    // Reward values
    private static final double WIN_REWARD = 100.0;
    private static final double LOSE_REWARD = -100.0;
    private static final double DRAW_REWARD = 0.0;
    private static final double GOOD_REWARD = 50.0;
    private static final double BAD_REWARD = -50.0;

    // Variables
    private final TicTacToePlayer xPlayer;
    private final TicTacToePlayer oPlayer;
    private final int rounds;
    private final double prob;
    private final boolean print;
    private final boolean stats;
    private final PlayerToken[] winArray;
    private int xWins;
    private int oWins;
    private int ties;
    private final Random rnd;

    /**
     * Creates a new simulation environment with all variables.
     *
     * @param xPlayer Player using the "X" token ({@link PlayerToken#X_PLAYER}).
     * @param oPlayer Player using the "O" token ({@link PlayerToken#O_PLAYER}).
     * @param rounds  Number of rounds of tic-tac-toe to be played in the simulation.
     * @param prob    Probability of receiving an intermediate reward during the game.
     *                Setting this variable to 0 only gives rewards at the end of the game.
     *                Setting this variable to 1 gives rewards after every move.
     * @param print   Flag that determines if boards are printed after each move.
     * @param stats   Flag that determines if statistics are printed at the end of the simulation.
     * @param seed    Seed for the random number generator.
     */
    private Environment(TicTacToePlayer xPlayer,
                        TicTacToePlayer oPlayer,
                        int rounds,
                        double prob,
                        boolean print,
                        boolean stats,
                        long seed) {
        this.xPlayer = xPlayer;
        this.xPlayer.setPlayer(PlayerToken.X_PLAYER);
        this.oPlayer = oPlayer;
        this.oPlayer.setPlayer(PlayerToken.O_PLAYER);
        this.rounds = rounds;
        this.prob = prob;
        this.print = print;
        this.stats = stats;
        this.rnd = new Random(seed);
        this.winArray = new PlayerToken[rounds];
        this.xWins = 0;
        this.oWins = 0;
        this.ties = 0;
    }

    /**
     * Driver method for the simulation.
     *
     * @param args String arguments. See the user manual for a breakdown of specific arguments.
     */
    public static void main(String[] args) {
        double prob = 0;
        String xPlayerString = "RandomPlayer";
        String oPlayerString = "RandomPlayer";
        int rounds = 1;
        boolean print = false;
        boolean stats = false;
        long seed = System.currentTimeMillis();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-prob":
                    prob = Double.parseDouble(args[++i]);
                    break;
                case "-xplayer":
                    xPlayerString = args[++i];
                    break;
                case "-oplayer":
                    oPlayerString = args[++i];
                    break;
                case "-rounds":
                    rounds = Integer.parseInt(args[++i]);
                    break;
                case "-print":
                    print = true;
                    break;
                case "-stats":
                    stats = true;
                    break;
                case "-seed":
                    seed = Long.parseLong(args[++i]);
                    break;
            }
        }

        // Load the players from class files
        // All player classes must be located in the "com.samvbeckmann.machinelearning.reinforcement,players" package.
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        TicTacToePlayer xPlayer = null, oPlayer = null;
        final String packagePath = "com.samvbeckmann.machinelearning.reinforcement.players.";
        try {
            xPlayer = (TicTacToePlayer) (cl.loadClass(packagePath + xPlayerString).newInstance());
            oPlayer = (TicTacToePlayer) (cl.loadClass(packagePath + oPlayerString).newInstance());
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

        Environment env = new Environment(xPlayer, oPlayer, rounds, prob, print, stats, seed);
        env.runSimulation();
    }

    /**
     * Runs the tic-tac-toe simulation using the environment variables.
     */
    private void runSimulation() {
//        for (int i = 0; i < rounds; i++) {
//            winArray[i] = runGame(new MinimaxTicTacToePlayer());
//        }
//
//        if (stats) System.out.print(getStatsString());

        while (true) {
            for (int i = 0; i < rounds; i++) {
                winArray[i] = runGame(new MinimaxTicTacToePlayer());
            }
            if (stats) System.out.print(getStatsString());
            xWins = oWins = ties = 0;
        }
    }

    /**
     * Runs a single game of the tic-tac-toe simulation.
     *
     * @param optimalPlayer An optimal tic-tac-toe player, used for generating reward values.
     * @return The token of the player who won the game. The {@link PlayerToken#OPEN} token represents a tie game.
     */
    private PlayerToken runGame(TicTacToePlayer optimalPlayer) {

        PlayerToken player = PlayerToken.X_PLAYER; // X goes first
        Board board = new Board();
        int move;
        double xPlayerReward = 0;
        double oPlayerReward = 0;

        while (board.getGameState() == 0) {
            if (player == PlayerToken.X_PLAYER) {
                xPlayer.giveReward(board, xPlayerReward);
                move = xPlayer.interact(board);
            } else {
                oPlayer.giveReward(board, oPlayerReward);
                move = oPlayer.interact(board);
            }

            if (print) {
                System.out.println("Player: " + player.toString() + " played: " + move + ".");
                System.out.print(board.toString());
                System.out.println();
            }

            if (rnd.nextDouble() < prob) {
                //Get optimal move
                optimalPlayer.setPlayer(player);
                int optimalMove = optimalPlayer.interact(board);
                if (player == PlayerToken.X_PLAYER) {
                    if (move == optimalMove)
                        xPlayerReward = GOOD_REWARD;
                    else
                        xPlayerReward = BAD_REWARD;
                } else {
                    if (move == optimalMove)
                        oPlayerReward = GOOD_REWARD;
                    else
                        oPlayerReward = BAD_REWARD;
                }
            }

            board.move(player, move);

            player = player == PlayerToken.X_PLAYER ? PlayerToken.O_PLAYER : PlayerToken.X_PLAYER;
        }

        if (print) System.out.print(board.toString());

        //Give out the final rewards
        if (board.playerWins(PlayerToken.X_PLAYER)) {
            xPlayer.giveReward(board, WIN_REWARD);
            oPlayer.giveReward(board, LOSE_REWARD);
            if (print)
                System.out.println("X Wins!");
            xWins++;
            return PlayerToken.X_PLAYER;
        } else if (board.playerWins(PlayerToken.O_PLAYER)) {
            xPlayer.giveReward(board, LOSE_REWARD);
            oPlayer.giveReward(board, WIN_REWARD);
            if (print)
                System.out.println("O Wins!");
            oWins++;
            return PlayerToken.O_PLAYER;
        } else {
            xPlayer.giveReward(board, DRAW_REWARD);
            oPlayer.giveReward(board, DRAW_REWARD);
            if (print)
                System.out.println("Cat Wins!");
            ties++;
            return PlayerToken.OPEN;
        }
    }

    /**
     * Generates a string containing the stats for this simulation.
     *
     * @return A formatted string with the statistics for who one each round.
     */
    private String getStatsString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("X wins:%5d\n", xWins));
        result.append(String.format("O wins:%5d\n", oWins));
        result.append(String.format("Ties:%7d\n", ties));
        result.append("Game-by-game breakdown:\n");
        for (int i = 0; i < rounds; i++) {
            result.append(winArray[i].statsSymbol()).append(" ");
        }
        return result.toString();
    }
}
