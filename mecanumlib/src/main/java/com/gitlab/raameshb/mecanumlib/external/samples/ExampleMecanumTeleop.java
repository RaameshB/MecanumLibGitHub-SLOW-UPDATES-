package com.gitlab.raameshb.mecanumlib.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.gitlab.raameshb.mecanumlib.internal.MecanumChassis;
import com.gitlab.raameshb.mecanumlib.internal.Movement;

@Disabled //comment out this line to have the teleop show up

@TeleOp
public class ExampleMecanumTeleop extends LinearOpMode {

    Movement movementController;
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumChassis.MecanumChassisInit(hardwareMap, this);
        MecanumChassis.configMotors("front_right", "front_left", "back_right", "back_left");
        movementController = new Movement();

        waitForStart();
        while (opModeIsActive()) {
            movementController.move(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        }
    }
}
