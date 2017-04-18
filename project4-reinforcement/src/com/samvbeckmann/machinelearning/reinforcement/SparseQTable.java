package com.samvbeckmann.machinelearning.reinforcement;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;

import java.util.HashMap;

/**
 * Defines a Q-Table that provides a Q function for use in Q-Learning.
 * Essentially a wrapper around a {@link HashMap}, the SparseQTable hashes a state and an action in a new
 * {@link StateAction}, and maps that to an appropriate Q-value.
 */
public class SparseQTable {

    private final HashMap<StateAction, Double> table;
    private double def = 0;

    public SparseQTable() {
        this(0);
    }

    public SparseQTable(double def) {
        this.def = def;
        table = new HashMap<>();
    }

    /**
     * Gets the Q-Value for a given state and action.
     *
     * @param state  State of the q-value.
     * @param action Action of the q-value.
     * @return The q-value for the provided state-action pair.
     */
    public double getQValue(Board state, int action) {
        return table.getOrDefault(new StateAction(state, action), def);
    }

    /**
     * Takes a given value, and set the q-value of the state-action pair to that value.
     *
     * @param state  State of the q-value.
     * @param action Action of the q-value.
     * @param val    The value to set the state-action pair to.
     */
    public void setQValue(Board state, int action, double val) {
        table.put(new StateAction(state, action), val);
    }

    /**
     * Sets the q-value of a given stateAction to the given value.
     * @param stateAction State-Action pair to set value for.
     * @param val Value to set the State-Action pair to.
     */
    public void setQValue(StateAction stateAction, double val) {
        table.put(stateAction, val);
    }

    /**
     * Wrapper around a State-Action pair that provides a unique hash and equality.
     */
    class StateAction {
        private final Board state;
        private final int action;

        StateAction(Board s, int a) {
            state = s;
            action = a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateAction that = (StateAction) o;

            return action == that.action && state.equals(that.state);
        }

        @Override
        public int hashCode() {
            int result = state.hashCode();
            result = 31 * result + action;
            return result;
        }
    }
}