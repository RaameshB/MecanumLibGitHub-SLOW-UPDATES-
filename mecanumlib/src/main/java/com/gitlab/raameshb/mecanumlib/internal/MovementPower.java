package com.gitlab.raameshb.mecanumlib.internal;

import org.ejml.simple.SimpleMatrix;

public class MovementPower {
    static final double[][] rotationValues = {{1, -1, 1, -1}};
    static final double[][] forwardValues = {{1, 1, 1, 1}};
    static final double[][] strafeValues = {{-1, 1, 1, -1}};
    static final SimpleMatrix rotationMatrix = new SimpleMatrix(rotationValues);
    static final SimpleMatrix forwardMatrix = new SimpleMatrix(forwardValues);
    static final SimpleMatrix strafeMatrix = new SimpleMatrix(strafeValues);
}
