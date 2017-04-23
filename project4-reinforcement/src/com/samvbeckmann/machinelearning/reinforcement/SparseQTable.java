package com.samvbeckmann.machinelearning.reinforcement;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;

import java.util.HashMap;

/**
 * Defines a Q-Table that provides a Q function for use in Q-Learning.
 * Essentially a wrapper around a {@link HashMap}, the SparseQTable hashes a state and an action in a new
 * {@link StateAction}, and maps that to an appropriate Q-value.
 */
public class SparseQTable {

    private final HashMap<StateAction, PairInfo> table;
    private final PairInfo DEFAULT_INFO = new PairInfo(0, 0, 0);

    public SparseQTable() {
        table = new HashMap<>();
    }

    private PairInfo getCorrectInfoPair(Board state, int action) {
        return getCorrectInfoPair(new StateAction(state, action));
    }

    private PairInfo getCorrectInfoPair(StateAction pair) {
        PairInfo info = table.get(pair);
        if (info == null) {
            info = new PairInfo(DEFAULT_INFO);
        }
        return info;
    }

    /**
     * Gets the Q-Value for a given state and action.
     *
     * @param state  State of the q-value.
     * @param action Action of the q-value.
     * @return The q-value for the provided state-action pair.
     */
    public double getQValue(Board state, int action) {
        return getCorrectInfoPair(state, action).qValue;
    }

    /**
     * Takes a given value, and set the q-value of the state-action pair to that value.
     *
     * @param state  State of the q-value.
     * @param action Action of the q-value.
     * @param val    The value to set the state-action pair to.
     */
    public void setQValue(Board state, int action, double val) {
        StateAction sa = new StateAction(state, action);
        PairInfo info = getCorrectInfoPair(sa);
        info.qValue = val;
        table.put(sa, info);
    }

    public void incrementAlpha(Board state, int action) {
        StateAction sa = new StateAction(state, action);
        PairInfo info = getCorrectInfoPair(sa);
        info.timesVisited += 1;
        table.put(sa, info);
    }

    public double alphaCalc(Board state, int action) {
        PairInfo info = getCorrectInfoPair(state, action);
        return 1 / info.timesVisited;
    }

    public void setEligibility(Board state, int action, double eligibility) {
        StateAction sa = new StateAction(state, action);
        PairInfo info = getCorrectInfoPair(sa);
        info.eligibility = eligibility;
        table.put(sa, info);
    }

    public double getEligibility(Board state, int action) {
        return getCorrectInfoPair(state, action).eligibility;
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

    class PairInfo {
        private double qValue;
        private int timesVisited;
        private double eligibility;

        PairInfo(double qValue, int timesVisited, double eligibility) {
            this.qValue = qValue;
            this.timesVisited = timesVisited;
            this.eligibility = eligibility;
        }

        PairInfo(PairInfo pi) {
            this(pi.qValue, pi.timesVisited, pi.eligibility);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PairInfo pairInfo = (PairInfo) o;

            return Double.compare(pairInfo.qValue, qValue) == 0 && timesVisited == pairInfo.timesVisited &&
                    Double.compare(pairInfo.eligibility, eligibility) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(qValue);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + timesVisited;
            temp = Double.doubleToLongBits(eligibility);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}