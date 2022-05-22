package com.raameshb.mecanumlib.internal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumChassis {

    public static boolean isInitialized = false;
    public static DcMotorEx frontRight, frontLeft, backRight, backLeft;
    public static DcMotor xEncoder1, xEncoder2, yEncoder;

    public static LinearOpMode linearOpMode;
    static boolean usingSeparateEncoders = false;

    public static void MecanumChassisConfig(String frontRightName, String frontLeftName, String backRightName, String backLeftName, HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get(frontRightName);
        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get(frontLeftName);
        backRight = (DcMotorEx) hardwareMap.dcMotor.get(backRightName);
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get(backLeftName);

        defaultMotors();

        linearOpMode = linearOpMode;

        isInitialized = true;

    }

    public static void MecanumChassisConfig(String frontRightName, String frontLeftName, String backRightName, String backLeftName, String xEncoder1Name, String xEncoder2Name, String yEncoderName, HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get(frontRightName);
        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get(frontLeftName);
        backRight = (DcMotorEx) hardwareMap.dcMotor.get(backRightName);
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get(backLeftName);

        xEncoder1 = hardwareMap.dcMotor.get(xEncoder1Name);
        xEncoder2 = hardwareMap.dcMotor.get(xEncoder2Name);
        yEncoder = hardwareMap.dcMotor.get(yEncoderName);

        usingSeparateEncoders = true;

        defaultMotors();

        linearOpMode = linearOpMode;

        isInitialized = true;


    }
    public static void defaultMotors() {
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public static void reversedMotors() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }



}
