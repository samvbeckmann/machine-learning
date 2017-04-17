package com.samvbeckmann.machinelearning.reinforcement;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sam on 4/17/17.
 */
public class AlphaTable {
    private Map<Board, Integer> alphaMap;

    public AlphaTable() {
        alphaMap = new HashMap<>();
    }

    public void updateState(Board state) {
        Integer prev = alphaMap.get(state);
        if (prev == null) prev = 0;
        alphaMap.put(state, prev + 1);
    }

    public int getStateValue(Board state) {
        Integer val = alphaMap.get(state);
        return val == null ? 0 : val;
    }

    public double alphaCalc(Board state) {
        return 1 / getStateValue(state);
    }
}
