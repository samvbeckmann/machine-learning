package com.samvbeckmann.machinelearning.reinforcement;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines an alpha table, which keeps track of how often certain states have been visited.
 */
public class AlphaTable {
    private final Map<Board, Integer> alphaMap;

    public AlphaTable() {
        alphaMap = new HashMap<>();
    }

    /**
     * Adds one to the count of the given state.
     * If the given state is not in the table, it is added to the table with a value of 1.
     *
     * @param state State to increment counter for.
     */
    public void updateState(Board state) {
        int prev = alphaMap.getOrDefault(state, 0);
        alphaMap.put(state, prev + 1);
    }

    /**
     * Gets the number of times a given state has been visited.
     *
     * @param state The state to consider.
     * @return The number of times the given state has been visited.
     */
    public int getStateValue(Board state) {
        return alphaMap.getOrDefault(state, 0);
    }

    /**
     * Performs the alpha calculation for the given state.
     *
     * @param state State to perform alpha calculation for.
     * @return Alpha weight to use in the Q-Learning update rule.
     */
    public double alphaCalc(Board state) {
        return 1 / getStateValue(state);
    }
}
