package com.gitlab.raameshb.mecanumlib.internal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.*;

public class PositionTracking implements Runnable {


    double globalX = 0;
    double globalY = 0;
    double globalAngleDeg = 0;
    double globalAngleRad = 0;

    double TICKS_PER_ROTATION;
    double COUNTS_PER_INCH;

    double yEncoder1DistanceXFromCenter; double yEncoder1DistanceYFromCenter;
    double yEncoder2DistanceXFromCenter; double yEncoder2DistanceYFromCenter;
    double xEncoderDistanceXFromCenter; double xEncoderDistanceYFromCenter;

    double yEncoder1Multiplier; double yEncoder2Multiplier; double xEncoderMultiplier;

    double yEncoder1Radius; double yEncoder2Radius; double xEncoderRadius;

    DcMotorEx yEncoder1;
    DcMotorEx yEncoder2;
    DcMotorEx xEncoder;

    public PositionTracking(String yEncoder1Name, double yEncoder1DistanceXFromCenter, double yEncoder1DistanceYFromCenter,
                            String yEncoder2Name, double yEncoder2DistanceXFromCenter, double yEncoder2DistanceYFromCenter,
                            String xEncoderName, double xEncoderDistanceXFromCenter, double xEncoderDistanceYFromCenter,
                            int ticksPerRotation, double yEncoder1WheelRadius, double yEncoder2WheelRadius, double xEncoderWheelRadius) {

        if (!MecanumChassis.isInitialized) {
            throw new RuntimeException("MecanumChassis is not initialized");
        }

        yEncoder1 = MecanumChassis.hardwareMap.dcMotor.get(yEncoder1Name);
        yEncoder1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yEncoder1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.yEncoder1DistanceXFromCenter = yEncoder1DistanceXFromCenter;
        this.yEncoder1DistanceYFromCenter = yEncoder1DistanceYFromCenter;

        yEncoder2 = MecanumChassis.hardwareMap.dcMotor.get(yEncoder2Name);
        yEncoder2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        yEncoder2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.yEncoder2DistanceXFromCenter = yEncoder2DistanceXFromCenter;
        this.yEncoder2DistanceYFromCenter = yEncoder2DistanceYFromCenter;

        xEncoder = MecanumChassis.hardwareMap.dcMotor.get(xEncoderName);
        xEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        xEncoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.xEncoderDistanceXFromCenter = xEncoderDistanceXFromCenter;
        this.xEncoderDistanceYFromCenter = xEncoderDistanceYFromCenter;

        yEncoder1Radius = Math.sqrt(Math.pow(yEncoder1DistanceXFromCenter, 2) + Math.pow(yEncoder1DistanceYFromCenter, 2));
        yEncoder2Radius = Math.sqrt(Math.pow(yEncoder2DistanceXFromCenter, 2) + Math.pow(yEncoder2DistanceYFromCenter, 2));
        xEncoderRadius = Math.sqrt(Math.pow(xEncoderDistanceXFromCenter, 2) + Math.pow(xEncoderDistanceYFromCenter, 2));

        yEncoder1Multiplier = yEncoder1Radius / yEncoder1DistanceXFromCenter;
        yEncoder2Multiplier = yEncoder2Radius / yEncoder2DistanceXFromCenter;

        TICKS_PER_ROTATION = ticksPerRotation;

        Thread trackingThread = new Thread(this);
        trackingThread.start();
    }

    protected class Tracker extends Thread {
        public void run() {
            try {

//                double robotX = 0;
//                double robotY = 0;

                double lastEncoderX = 0;
                double lastEncoderY = 0;

                double deltaEncoderX;
                double deltaEncoderY;

                double deltaX;
                double deltaY;

                double lastVelX = 0;
                double lastVelY = 0;

                ElapsedTime time = new ElapsedTime();

                final double encoderRadius = 13;

                int loopEvent = 0;

                while(!Thread.interrupted() && MecanumChassis.linearOpMode.opModeIsActive()) {

                    currentAngle = (encoderDistanceY2()*2/3 - encoderDistanceY1()*2/3)/(encoderRadius) + (PI / 2);

                    deltaEncoderX = encoderDistanceX() - lastEncoderX;
                    deltaEncoderY = encoderDistanceY() - lastEncoderY;

                    deltaY = sin(currentAngle) * deltaEncoderY + sin(currentAngle - PI/2) * deltaEncoderX;
                    deltaX = cos(currentAngle) * deltaEncoderY + cos(currentAngle - PI/2) * deltaEncoderX;

                    globalX += deltaX;
                    globalY += deltaY;
//                    robot.setPoint(robotX, robotY);

                    if(loopEvent==5) {
                        velX = (globalX - lastVelX) / time.seconds();
                        velY = (globalY - lastVelY) / time.seconds();
                        time.reset();
                        lastVelX = robot.x;
                        lastVelY = robot.y;
                        loopEvent = 0;
                    }

                    lastEncoderX = encoderDistanceX();
                    lastEncoderY = encoderDistanceY();

                    loopEvent++;

//                    opMode.telemetry.addData("robot x", robotX);
//                    opMode.telemetry.addData("robot y", robotY);
//                    opMode.telemetry.addData("robot.x", robot.x);
//                    opMode.telemetry.addData("robot.y", robot.y);
//                    opMode.telemetry.addData("currntAngle", currentAngle);
//                    opMode.telemetry.addData("angle degrees", getAngle());
//                    opMode.telemetry.update();

                    try {
                        Thread.sleep(10);
                    } catch(InterruptedException e) {
                        return;
                    }

                    Thread.yield();
                }

            } catch(Exception e) {
                //exception!
            }
        }
    }

    public double getGlobalX() {
        return globalX;
    }

    public double getGlobalY() {
        return globalY;
    }

    public double getGlobalAngleDeg() {
        return globalAngleDeg;
    }

    public double getGlobalAngleRad() {
        return globalAngleRad;
    }

}
