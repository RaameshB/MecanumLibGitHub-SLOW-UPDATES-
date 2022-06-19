package com.gitlab.raameshb.mecanumlib.internal;

import androidx.annotation.NonNull;
import androidx.renderscript.RSInvalidStateException;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.gitlab.raameshb.mecanumlib.internal.rrUtil.Encoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Config
public class RoadRunnerPositionTracking extends ThreeTrackingWheelLocalizer {

    //TODO: Change x1 and x2 to y1 and y2 and then change y to x

    static double TICKS_PER_REV = 8192;
    static double WHEEL_RADIUS = 2.5/2;
    static double GEAR_RATIO = 1;

    static double LATERAL_DISTANCE = 10;
    static double FORWARD_OFFSET = 0;

    static double X_MULTIPLIER = 1;
    static double Y_MULTIPLIER = 1;


    public RoadRunnerPositionTracking() {
        super(Arrays.asList(
           new Pose2d(0, LATERAL_DISTANCE / 2, 0),
           new Pose2d(0, -LATERAL_DISTANCE / 2, 0),
           new Pose2d(FORWARD_OFFSET, 0, 0)
        ));

        initHashMap();

    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(MecanumChassis.xEncoder1.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(MecanumChassis.xEncoder2.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(MecanumChassis.yEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    boolean highVelocEncoders = false;

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        if (highVelocEncoders) {
            return Arrays.asList(
                    encoderTicksToInches(MecanumChassis.xEncoder1.getCorrectedVelocity()) * X_MULTIPLIER,
                    encoderTicksToInches(MecanumChassis.xEncoder2.getCorrectedVelocity()) * X_MULTIPLIER,
                    encoderTicksToInches(MecanumChassis.yEncoder.getCorrectedVelocity()) * Y_MULTIPLIER
            );
        } else {
            return Arrays.asList(
                    encoderTicksToInches(MecanumChassis.xEncoder1.getRawVelocity()) * X_MULTIPLIER,
                    encoderTicksToInches(MecanumChassis.xEncoder2.getRawVelocity()) * X_MULTIPLIER,
                    encoderTicksToInches(MecanumChassis.yEncoder.getRawVelocity()) * Y_MULTIPLIER
            );
        }
    }

    public void getRotation() {

    }


    boolean funcChecker = false;
    public void extraArgs(args arg1) {
        funcChecker = true;
        extraArgsInternal(argsMap.get(arg1));
    }
    public void extraArgs(args arg1, args arg2) {
        funcChecker = true;
        extraArgsInternal(argsMap.get(arg1) + argsMap.get(arg2));
    }
    public void extraArgs(args arg1, args arg2, args arg3) {
        funcChecker = true;
        extraArgsInternal(argsMap.get(arg1) + argsMap.get(arg2) + argsMap.get(arg3));
    }
    public void extraArgs(args arg1, args arg2, args arg3, args arg4) {
        funcChecker = true;
        extraArgsInternal(argsMap.get(arg1) + argsMap.get(arg2) + argsMap.get(arg3) + argsMap.get(arg4));
    }
    void extraArgsInternal(int argsInt) {
        if (!funcChecker) {
            throw new RSInvalidStateException("How and why are you running this function directly?");
        }
        funcChecker = false;
        if (argsInt >= 1000) {
            highVelocEncoders = true;
            argsInt -= 1000;
        }
        if (argsInt >= 100) {
            MecanumChassis.yEncoder.setDirection(Encoder.Direction.REVERSE);
            argsInt -= 100;
        }
        if (argsInt >= 10) {
            MecanumChassis.xEncoder2.setDirection(Encoder.Direction.REVERSE);
            argsInt -= 10;
        }
        if (argsInt >= 1) {
            MecanumChassis.xEncoder1.setDirection(Encoder.Direction.REVERSE);
        }
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }


    public enum args {
        x1Reverse,
        x2Reverse,
        yReverse,
        highVelocEncoders
    }

    static HashMap<args, Integer> argsMap = new HashMap<>();

    void initHashMap() {
        argsMap.put(args.x1Reverse, 1);
        argsMap.put(args.x2Reverse, 10);
        argsMap.put(args.yReverse, 100);
        argsMap.put(args.highVelocEncoders, 1000);
    }
}
