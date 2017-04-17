package com.samvbeckmann.machinelearning.reinforcement;

import com.samvbeckmann.machinelearning.reinforcement.simulation.Board;

import java.util.HashMap;

public class SparseQTable {
    private HashMap<StateAction, Double> table;
    private double def = 0;

    public SparseQTable() {
        this(0);
    }

    public SparseQTable(double def) {
        this.def = def;
        table = new HashMap<>();
    }

    //A simple test
//    public static void main(String[] args) {
//        SparseQTable q1 = new SparseQTable();
//
//        q1.setQValue(3, 5, 1.0);
//        q1.setQValue(7, 8, 2.0);
//
//        q1.setQValue(7, 8, 3.0);
//
//        double threefive = q1.getQValue(3, 5);
//        double seveneight = q1.getQValue(7, 8);
//        double none = q1.getQValue(-1, -1);
//
//        if (threefive == 1.0 && seveneight == 3.0 && none == 0) System.out.println("Test passed!");
//        else System.out.println("Test failed!");
//    }

    public double getQValue(Board state, int action) {
        Double d = table.get(new StateAction(state, action));
        if (d == null) return def;
        return d;
    }

    public void setQValue(Board state, int action, double val) {
        table.put(new StateAction(state, action), val);
    }

    public void setQValue(StateAction stateAction, double val) {
        table.put(stateAction, val);
    }

    class StateAction {
        private Board state;
        private int action;

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