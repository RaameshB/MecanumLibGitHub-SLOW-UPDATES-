package com.gitlab.raameshb.mecanumlib.internal;

import org.ejml.simple.SimpleMatrix;

public class MovementPower {

    /*
    Format for matrixes: {{frontLeft, frongRight, backLeft, backRight}}
     */

    /*
     When rotating clockwise the frontLeft and backLeft wheels are given forward power and the right side wheels are
     given negative power.
     */
    static final double[][] rotationValues = {{1, -1, 1, -1}};

    /*
    All wheels are given forward power when moving forwards
     */
    static final double[][] forwardValues = {{1, 1, 1, 1}};
    /*
    When strafing the frontLeft and backRight wheels are given negative power and the frontRight and backLeft wheels
    are given negative power
     */
    static final double[][] strafeValues = {{-1, 1, 1, -1}};
    static final SimpleMatrix rotationMatrix = new SimpleMatrix(rotationValues);
    static final SimpleMatrix forwardMatrix = new SimpleMatrix(forwardValues);
    static final SimpleMatrix strafeMatrix = new SimpleMatrix(strafeValues);
}
