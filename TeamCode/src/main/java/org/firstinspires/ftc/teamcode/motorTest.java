package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "motorTest")
//@Disabled
//Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
public class motorTest extends LinearOpMode {
    //maneuver
    private DcMotor leftFwd;
    private DcMotor leftAft;
    private DcMotor rightFwd;
    private DcMotor rightAft;
    //rollers
    private DcMotor leftRoller;
    private DcMotor rightRoller;
    //elevator
    private DcMotor elev;
    private DcMotor leftPliers;
    private DcMotor rightPliers;
    private DcMotor pushBrick;


    @Override
    public void runOpMode() {
        //maneuver
        leftFwd = hardwareMap.dcMotor.get("leftFwd");
        leftAft = hardwareMap.dcMotor.get("leftAft");
        rightFwd = hardwareMap.dcMotor.get("rightFwd");
        rightAft = hardwareMap.dcMotor.get("rightAft");
        rightFwd.setDirection(DcMotorSimple.Direction.REVERSE); //Exclusively for left side motors,
        rightAft.setDirection(DcMotorSimple.Direction.REVERSE); //Do Not merge into DcMotor[] motors!
        leftRoller = hardwareMap.dcMotor.get("leftRoller");
        rightRoller = hardwareMap.dcMotor.get("rightRoller");
        rightRoller.setDirection(DcMotorSimple.Direction.REVERSE);
        DcMotor[] motors = {leftFwd, leftAft, rightFwd, rightAft/*, elev*/};
        for (DcMotor runMode : motors) {
            runMode.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            runMode.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            runMode.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        /*
        rollers
        leftRoller = hardwareMap.dcMotor.get("leftRoller");
        rightRoller = hardwareMap.dcMotor.get("rightRoller");
        rightRoller.setDirection(DcMotorSimple.Direction.REVERSE);
        elevator
        elev = hardwareMap.dcMotor.get("elev");
        leftPliers = hardwareMap.dcMotor.get("leftPliers");
        rightPliers = hardwareMap.dcMotor.get("rightPliers");
        leftPliers.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightPliers.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        */

        waitForStart();
        while (opModeIsActive()) {
            //maneuver
            leftFwd.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_y - gamepad1.right_stick_x);
            leftAft.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_y + gamepad1.right_stick_x);
            rightFwd.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_y + gamepad1.right_stick_x);
            rightAft.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_y - gamepad1.right_stick_x);

            //rollers
            leftRoller.setPower(0.5);
            rightRoller.setPower(0.5);
        }
    }

    public void elevMountBricks() {
        pliersOpen();
        elev.setTargetPosition(elev.getCurrentPosition() + 100);
        elev.setPower(0.5);
        while (elev.isBusy()) {
        } //Don't do anything
        elev.setPower(0);

        elev.setTargetPosition(elev.getCurrentPosition() - 90);
        elev.setPower(0.5);
        while (elev.isBusy()) {
        } //Don't do anything
        elev.setPower(0);
    }

    public void pliersOpen() {

    }

    public void pliersClose() {

    }

}