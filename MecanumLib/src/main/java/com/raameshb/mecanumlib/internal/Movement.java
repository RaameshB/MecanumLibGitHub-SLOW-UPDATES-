package com.raameshb.mecanumlib.internal;

import androidx.renderscript.RSInvalidStateException;
import org.ejml.simple.SimpleMatrix;
import java.util.HashMap;
import java.util.Objects;

public class Movement {

    public Movement() {
        if (!MecanumChassis.isInitialized) {
            throw new RuntimeException("MecanumChassis is not initialized");
        }
        initHashmap();
        Thread movementThread = new Thread(new powerSetter());
        movementThread.start();
    }

    static double frontLeftPowerFinal = 0;
    static double frontRightPowerFinal = 0;
    static double backLeftPowerFinal = 0;
    static double backRightPowerFinal = 0;

    public void move(double power, double[] ratios) throws Exception {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new Exception("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (MovementPower.forwardMatrix.scale(ratios[0]))
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[1]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[2]))
        ).scale(power);

        frontLeftPowerFinal = movementMatrix.get(0, 0);
        frontRightPowerFinal = movementMatrix.get(0, 1);
        backLeftPowerFinal = movementMatrix.get(0, 2);
        backRightPowerFinal = movementMatrix.get(0, 3);
    }

    enum format {
        FORWARD_STRAFE_ROATATE,
        STRAFE_FORWARD_ROTATE,
        FORWARD_ROTATE_STRAFE,
        ROTATE_FORWARD_STRAFE,
        STRAFE_ROTATE_FORWARD,
        ROTATE_STRAFE_FORWARD
    }

    static HashMap<format, double[]> movementMap = new HashMap<>();

    public static void initHashmap() {
        movementMap.put(format.FORWARD_STRAFE_ROATATE, new double[]{0, 1, 2});
        movementMap.put(format.STRAFE_FORWARD_ROTATE, new double[]{1, 0, 2});
        movementMap.put(format.FORWARD_ROTATE_STRAFE, new double[]{0, 2, 1});
        movementMap.put(format.ROTATE_FORWARD_STRAFE, new double[]{2, 0, 1});
        movementMap.put(format.STRAFE_ROTATE_FORWARD, new double[]{1, 2, 0});
        movementMap.put(format.ROTATE_STRAFE_FORWARD, new double[]{2, 1, 0});
    }

    public void move(double power, double[] ratios, format format) throws Exception {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new Exception("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (MovementPower.forwardMatrix.scale(Objects.requireNonNull(movementMap.get(format))[0]))
                        .plus
                                (MovementPower.strafeMatrix.scale(Objects.requireNonNull(movementMap.get(format))[1]))
                        .plus
                                (MovementPower.rotationMatrix.scale(Objects.requireNonNull(movementMap.get(format))[2]))
        ).scale(power);

        frontLeftPowerFinal = movementMatrix.get(0, 0);
        frontRightPowerFinal = movementMatrix.get(0, 1);
        backLeftPowerFinal = movementMatrix.get(0, 2);
        backRightPowerFinal = movementMatrix.get(0, 3);
    }

    public void move(double rightY, double leftY, double rightX, double leftX) {
        double side = (rightX + leftX)/2;
        backLeftPowerFinal = rightY + side;
        frontRightPowerFinal = rightY + side;
        frontLeftPowerFinal = leftY + -side;
        backRightPowerFinal = leftY + -side;
    }

    static class powerSetter implements Runnable {
        @Override
        public void run() {
            while (!MecanumChassis.linearOpMode.isStopRequested()) {
                MecanumChassis.frontLeft.setPower(frontLeftPowerFinal);
                MecanumChassis.frontRight.setPower(frontRightPowerFinal);
                MecanumChassis.backLeft.setPower(backLeftPowerFinal);
                MecanumChassis.backRight.setPower(backRightPowerFinal);
            }
        }
    }
}
