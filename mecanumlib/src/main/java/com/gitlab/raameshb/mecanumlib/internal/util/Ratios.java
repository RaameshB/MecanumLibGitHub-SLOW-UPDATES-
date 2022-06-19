package com.gitlab.raameshb.mecanumlib.internal.util;

public class Ratios {
    double[] ratioArray = new double[] {0,0,0};
    public Ratios(double val0, double val1, double val2) {
        ratioArray[0] = val0;
        ratioArray[1] = val1;
        ratioArray[2] = val2;
    }
    public double[] getRatioArray() {
        return ratioArray;
    }
}
