package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "test")
//@Disabled
//Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
public class test extends LinearOpMode {
    //Declare OpMode members.
    //private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFwd = null;
    private DcMotor leftAft = null;
    private DcMotor rightFwd = null;
    private DcMotor rightAft = null;
    private Servo servo = null;
    //个性化控制参数
    private double pivotTurnActiveThreshold = 0.0;
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
            //Bogie control start
            if (Math.abs(gamepad1.left_stick_y) <= pivotTurnActiveThreshold) { //纵轴杆量少于一定值时进入原地转向模式 //按需修改
                leftFwd.setPower(gamepad1.left_stick_x);
                leftAft.setPower(gamepad1.left_stick_x);
                rightFwd.setPower(-gamepad1.left_stick_x);
                rightAft.setPower(-gamepad1.left_stick_x);
            } else if (gamepad1.left_stick_x < 0) { //若不在原地转向模式，则判断杆输入的方向并转向，此处为左转条件 //转向方案为纵横相加
                leftFwd.setPower(gamepad1.left_stick_y);
                leftAft.setPower(gamepad1.left_stick_y);
                rightFwd.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
                rightAft.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
            } else { //否则右转 //无转向输入则直行
                leftFwd.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
                leftAft.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);
                rightFwd.setPower(gamepad1.left_stick_y);
                rightAft.setPower(gamepad1.left_stick_y);

            //Servo control start
            if (gamepad1.left_bumper) {
                servo.setPosition(servo.getPosition() + servoIncrement);
            } else if (gamepad1.right_bumper) {
                servo.setPosition(servo.getPosition() - servoIncrement);
            }
        }
    }

    //private void pingyi(float power) {

    }
}
