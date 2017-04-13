package com.samvbeckmann.machinelearning.reinforcement;

import java.util.HashMap;

public class SparseQTable {
    private HashMap table;
    private double def=0;

    public SparseQTable() {
        table = new HashMap();
    }

    public SparseQTable(double def) {
        this.def=def;
    }

    public double getQValue(int state, int action) {
        Double d = (Double)table.get(new StateAction(state, action));
        if(d==null) return def;
        return d.doubleValue();
    }

    public void setQValue(int state, int action, double val) {
        table.put(new StateAction(state, action), new Double(val));
    }

    //A simple test
    public static void main(String[] args) {
        SparseQTable q1 = new SparseQTable();

        q1.setQValue(3, 5, 1.0);
        q1.setQValue(7, 8, 2.0);

        q1.setQValue(7, 8, 3.0);

        double threefive = q1.getQValue(3,5);
        double seveneight = q1.getQValue(7,8);
        double none = q1.getQValue(-1,-1);

        if(threefive==1.0 && seveneight==3.0 && none==0) System.out.println("Test passed!");
        else System.out.println("Test failed!");
    }

    class StateAction {
        private int state;
        private int action;

        public StateAction(int s, int a) {
            state=s; action=a;
        }

        public boolean equals(Object o) {
            StateAction sa = (StateAction)o;
            return sa.state==state && sa.action==action;
        }

        //A nasty hack...
        public int hashCode() {
            return state*10 + action;
        }
    }
}