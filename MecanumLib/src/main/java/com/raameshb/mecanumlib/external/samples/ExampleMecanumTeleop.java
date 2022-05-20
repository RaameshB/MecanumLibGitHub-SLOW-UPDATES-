package com.raameshb.mecanumlib.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.raameshb.mecanumlib.internal.MecanumChassis;
import com.raameshb.mecanumlib.internal.Movement;

@Disabled //comment out this line to have the teleop show up

@TeleOp
public class ExampleMecanumTeleop extends LinearOpMode {

    Movement movementController;
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumChassis.MecanumChassisConfig("front_right", "front_left", "back_right", "back_left", hardwareMap, this);
        movementController = new Movement();

        waitForStart();
        while (opModeIsActive()) {
            movementController.move(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        }
    }
}
