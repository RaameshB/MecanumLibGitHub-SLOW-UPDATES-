package com.gitlab.raameshb.mecanumlib.internal;

import androidx.renderscript.RSRuntimeException;

import com.gitlab.raameshb.mecanumlib.internal.util.Ratios;

import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.Objects;


public class Movement {

    public Movement() {
        if (!(MecanumChassis.isInitialized && MecanumChassis.areMotorsInitialized)) {
            throw new RuntimeException("MecanumChassis is not properly initialized, check if you've run MecanumChassis.MecanumChassisInit() and MecanumChassis.configMotors()");
        }
        initHashmap();
        Thread movementThread = new Thread(new powerSetter());
        movementThread.start();
    }

    static double frontLeftPowerFinal = 0;
    static double frontRightPowerFinal = 0;
    static double backLeftPowerFinal = 0;
    static double backRightPowerFinal = 0;

    public void move(double power, double[] ratios) {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
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

    public void move(double power, Ratios ratiosClass) {
        double[] ratios = ratiosClass.getRatioArray();
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
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

    public void moveDebug(double power, double[] ratios) {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (MovementPower.forwardMatrix.scale(ratios[0]))
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[1]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[2]))
        ).scale(power);

        MecanumChassis.linearOpMode.telemetry.addData("frontLeftPowerFinal", movementMatrix.get(0, 0));
        MecanumChassis.linearOpMode.telemetry.addData("frontRightPowerFinal", movementMatrix.get(0, 1));
        MecanumChassis.linearOpMode.telemetry.addData("backLeftPowerFinal", movementMatrix.get(0, 2));
        MecanumChassis.linearOpMode.telemetry.addData("backRightPowerFinal", movementMatrix.get(0, 3));
        MecanumChassis.linearOpMode.telemetry.update();
    }

    public void moveDebug(double power, Ratios ratiosClass) {
        double[] ratios = ratiosClass.getRatioArray();
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (MovementPower.forwardMatrix.scale(ratios[0]))
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[1]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[2]))
        ).scale(power);

        MecanumChassis.linearOpMode.telemetry.addData("frontLeftPowerFinal", movementMatrix.get(0, 0));
        MecanumChassis.linearOpMode.telemetry.addData("frontRightPowerFinal", movementMatrix.get(0, 1));
        MecanumChassis.linearOpMode.telemetry.addData("backLeftPowerFinal", movementMatrix.get(0, 2));
        MecanumChassis.linearOpMode.telemetry.addData("backRightPowerFinal", movementMatrix.get(0, 3));
        MecanumChassis.linearOpMode.telemetry.update();
    }

    public enum format {
        FORWARD_STRAFE_ROATATE,
        STRAFE_FORWARD_ROTATE,
        FORWARD_ROTATE_STRAFE,
        ROTATE_FORWARD_STRAFE,
        STRAFE_ROTATE_FORWARD,
        ROTATE_STRAFE_FORWARD
    }

    static HashMap<format, int[]> movementMap = new HashMap<>();

    public static void initHashmap() {
        //0 is forward, 1 is strafe, 2 is rotate
        movementMap.put(format.FORWARD_STRAFE_ROATATE, new int[]{0, 1, 2});
        movementMap.put(format.STRAFE_FORWARD_ROTATE, new int[]{1, 0, 2});
        movementMap.put(format.FORWARD_ROTATE_STRAFE, new int[]{0, 2, 1});
        movementMap.put(format.ROTATE_FORWARD_STRAFE, new int[]{1, 2, 0});
        movementMap.put(format.STRAFE_ROTATE_FORWARD, new int[]{2, 0, 1});
        movementMap.put(format.ROTATE_STRAFE_FORWARD, new int[]{2, 1, 0});
    }

    public void move(double power, double[] ratios, format format) {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (
                        MovementPower.forwardMatrix.scale(
                                ratios[
                                        Objects.requireNonNull(
                                                movementMap.get(format)
                                        )
                                                [0]
                                        ]
                        )
                )
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[1]]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[2]]))
        ).scale(power);

        frontLeftPowerFinal = movementMatrix.get(0, 0);
        frontRightPowerFinal = movementMatrix.get(0, 1);
        backLeftPowerFinal = movementMatrix.get(0, 2);
        backRightPowerFinal = movementMatrix.get(0, 3);
    }

    public void move(double power, Ratios ratiosClass, format format) {
        double[] ratios = ratiosClass.getRatioArray();
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (
                        MovementPower.forwardMatrix.scale(
                                ratios[
                                        Objects.requireNonNull(
                                                movementMap.get(format)
                                        )
                                                [0]
                                        ]
                        )
                )
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[1]]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[2]]))
        ).scale(power);

        frontLeftPowerFinal = movementMatrix.get(0, 0);
        frontRightPowerFinal = movementMatrix.get(0, 1);
        backLeftPowerFinal = movementMatrix.get(0, 2);
        backRightPowerFinal = movementMatrix.get(0, 3);
    }

    public void moveDebug(double power, double[] ratios, format format) {
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (
                        MovementPower.forwardMatrix.scale(
                                ratios[
                                        Objects.requireNonNull(
                                                movementMap.get(format)
                                        )
                                                [0]
                                        ]
                        )
                )
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[1]]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[2]]))
        ).scale(power);

        MecanumChassis.linearOpMode.telemetry.addData("frontLeftPowerFinal", movementMatrix.get(0, 0));
        MecanumChassis.linearOpMode.telemetry.addData("frontRightPowerFinal", movementMatrix.get(0, 1));
        MecanumChassis.linearOpMode.telemetry.addData("backLeftPowerFinal", movementMatrix.get(0, 2));
        MecanumChassis.linearOpMode.telemetry.addData("backRightPowerFinal", movementMatrix.get(0, 3));
        MecanumChassis.linearOpMode.telemetry.update();


    }

    public void moveDebug(double power, Ratios ratiosClass, format format) {
        double[] ratios = ratiosClass.getRatioArray();
        if (ratios.length != 3 || (Math.abs(ratios[0]) + Math.abs(ratios[1]) + Math.abs(ratios[2])) != 1) {
            throw new RSRuntimeException("Invalid Ratio");
        }

        SimpleMatrix movementMatrix = (
                (
                        MovementPower.forwardMatrix.scale(
                                ratios[
                                        Objects.requireNonNull(
                                                movementMap.get(format)
                                        )
                                                [0]
                                        ]
                        )
                )
                        .plus
                                (MovementPower.strafeMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[1]]))
                        .plus
                                (MovementPower.rotationMatrix.scale(ratios[Objects.requireNonNull(movementMap.get(format))[2]]))
        ).scale(power);

        MecanumChassis.linearOpMode.telemetry.addData("frontLeftPowerFinal", movementMatrix.get(0, 0));
        MecanumChassis.linearOpMode.telemetry.addData("frontRightPowerFinal", movementMatrix.get(0, 1));
        MecanumChassis.linearOpMode.telemetry.addData("backLeftPowerFinal", movementMatrix.get(0, 2));
        MecanumChassis.linearOpMode.telemetry.addData("backRightPowerFinal", movementMatrix.get(0, 3));
        MecanumChassis.linearOpMode.telemetry.update();


    }

    public void move(double rightY, double leftY, double rightX, double leftX) {
        double side = (rightX + leftX)/2;
        backLeftPowerFinal = leftY + side;
        frontRightPowerFinal = rightY + side;
        frontLeftPowerFinal = leftY + -side;
        backRightPowerFinal = rightY + -side;
    }

    public void stop() {
        backLeftPowerFinal = 0;
        frontRightPowerFinal = 0;
        frontLeftPowerFinal = 0;
        backRightPowerFinal = 0;
    }

    static class powerSetter implements Runnable {
        @Override
        public void run() {
            while (!MecanumChassis.linearOpMode.isStopRequested()) {
                MecanumChassis.frontLeft.setPower(frontLeftPowerFinal);
                MecanumChassis.frontRight.setPower(frontRightPowerFinal);
                MecanumChassis.backLeft.setPower(backLeftPowerFinal);
                MecanumChassis.backRight.setPower(backRightPowerFinal);
                Thread.yield();
            }
        }
    }
}
