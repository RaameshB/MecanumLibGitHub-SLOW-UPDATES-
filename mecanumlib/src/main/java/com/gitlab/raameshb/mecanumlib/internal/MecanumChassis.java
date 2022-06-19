package com.gitlab.raameshb.mecanumlib.internal;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.gitlab.raameshb.mecanumlib.internal.rrUtil.Encoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MecanumChassis {

    public static boolean isInitialized = false;
    public static boolean areMotorsInitialized = false;
    public static boolean areEncodersInitialized = false;
    public static DcMotorEx frontRight, frontLeft, backRight, backLeft;
    public static Encoder xEncoder1, xEncoder2, yEncoder;

    public static LinearOpMode linearOpMode;
    // static boolean usingSeparateEncoders = false;

    public static HardwareMap hardwareMap;

    public static void MecanumChassisInit(HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        MecanumChassis.hardwareMap = hardwareMap;

        MecanumChassis.linearOpMode = linearOpMode;

        isInitialized = true;
    }

    public static void configMotors(String frontRightName, String frontLeftName, String backRightName, String backLeftName) {
        frontRight = (DcMotorEx) hardwareMap.dcMotor.get(frontRightName);
        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get(frontLeftName);
        backRight = (DcMotorEx) hardwareMap.dcMotor.get(backRightName);
        backLeft = (DcMotorEx) hardwareMap.dcMotor.get(backLeftName);

        defaultMotors();

        areMotorsInitialized = true;
    }
    public static void configEncoders(String x1EncoderName, String x2EncoderName, String yEncoderName) {
        xEncoder1 = new Encoder(hardwareMap.get(DcMotorEx.class, x1EncoderName));
        xEncoder2 = new Encoder(hardwareMap.get(DcMotorEx.class, x2EncoderName));
        yEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, yEncoderName));

        areEncodersInitialized = true;
    }


    public static void setMotorDirections(DcMotorSimple.Direction frontRightDir, DcMotorSimple.Direction frontLeftDir, DcMotorSimple.Direction backRightDir, DcMotorSimple.Direction backLeftDir) {
        frontRight.setDirection(frontRightDir);
        frontLeft.setDirection(frontLeftDir);
        backRight.setDirection(backRightDir);
        backLeft.setDirection(backLeftDir);
    }

//    public static void MecanumChassisConfig(String frontRightName, String frontLeftName, String backRightName, String backLeftName, String xEncoder1Name, String xEncoder2Name, String yEncoderName, HardwareMap hardwareMap, LinearOpMode linearOpMode) {
//        frontRight = (DcMotorEx) hardwareMap.dcMotor.get(frontRightName);
//        frontLeft = (DcMotorEx) hardwareMap.dcMotor.get(frontLeftName);
//        backRight = (DcMotorEx) hardwareMap.dcMotor.get(backRightName);
//        backLeft = (DcMotorEx) hardwareMap.dcMotor.get(backLeftName);
//
//        xEncoder1 = hardwareMap.dcMotor.get(xEncoder1Name);
//        xEncoder2 = hardwareMap.dcMotor.get(xEncoder2Name);
//        yEncoder = hardwareMap.dcMotor.get(yEncoderName);
//
//        usingSeparateEncoders = true;
//
//        defaultMotors();
//
//        linearOpMode = linearOpMode;
//
//        isInitialized = true;
//
//
//    }
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
