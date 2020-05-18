package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "test3")
public class test3 extends LinearOpMode {
    //Declare OpMode members.
    //private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFwd = null;
    private DcMotor leftAft = null;
    private DcMotor rightFwd = null;
    private DcMotor rightAft = null;
    private Servo servo = null;
    //个性化控制参数
    private double servoIncrement = 0.005;

    @Override
    public void runOpMode() {
        leftFwd = hardwareMap.dcMotor.get("leftFwd");
        leftAft = hardwareMap.dcMotor.get("leftAft");
        rightFwd = hardwareMap.dcMotor.get("rightFwd");
        rightAft = hardwareMap.dcMotor.get("rightAft");
        leftFwd.setDirection(DcMotorSimple.Direction.REVERSE);
        leftAft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFwd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftAft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFwd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightAft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFwd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftAft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFwd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightAft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFwd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftAft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFwd.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightAft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        servo = hardwareMap.servo.get("servo");

        waitForStart();
        while (opModeIsActive()) {
            //若y不为0，trigger制动转向，否则差速转向
            //Servo control start
            if (gamepad1.left_bumper) {
                servo.setPosition(servo.getPosition() + servoIncrement);
            } else if (gamepad1.right_bumper) {
                servo.setPosition(servo.getPosition() - servoIncrement);
            }
        }
    }
}

/*if 0<=stick.x<=0.5
    x = 2x^2
    else
    x = x
 */