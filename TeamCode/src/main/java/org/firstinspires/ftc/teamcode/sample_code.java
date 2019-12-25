package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.SerialNumber;

import dalvik.system.DelegateLastClassLoader;

@TeleOp(name = "Something", group = "Some group")
//group is not required, name property is the name of this teleop showed on the screen of the driver station
//@Disabled
//To stop showing this op mode on driver station, uncomment the above line
public class sample_code extends LinearOpMode {
    //Declaration Part
    private ElapsedTime runtime = new ElapsedTime(); //计时器
    private DcMotor motor = null; //motor is the name
    private Servo servo = null; // servo is the name

    @Override
    public void runOpMode() throws InterruptedException {
        // Init
        // Init your hardware before starting, check the names!
        //Init Motor
        motor = hardwareMap.dcMotor.get("Motor_Name"); //Motor_Name is the name you set on the phone
        //Init Servo
        servo = hardwareMap.servo.get("Servo_Name");

        //timer operation
        //Timer runs immediately after you reset, it resets itself when declared
        runtime.reset(); // 重置计时器
        runtime.milliseconds(); //获取计时器总毫秒数

        //Motor Run mode
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Do every time you want to reset encoder(degree counter)
        //!Important: Don't forget to set run mode after reset
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //power = actual speed(%)
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //power = voltage(%)
        //Motor operation
        motor.setPower(0.1);
        motor.setDirection(DcMotorSimple.Direction.FORWARD); //Set Motor direction as FORWARD
        motor.setDirection(DcMotorSimple.Direction.REVERSE); //Set Motor direction as REVERSED
        motor.getCurrentPosition(); //Get encoder reading
        motor.setTargetPosition(motor.getCurrentPosition() + 123); //Make motor run to this position
        //Notice: It is accumulative, use +/- to move relatively↑, remember to set power
        motor.isBusy(); //Use this accompanied with setTargetPosition, Example below
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //Break when power set to 0, Float otherwise
        //Motor Run to target EXAMPLE
        motor.setTargetPosition(motor.getCurrentPosition() + 1234);
        motor.setPower(1.0);
        while (motor.isBusy()) {
        } //Don't do anything
        motor.setPower(0);

        //Servo operation
        servo.setPosition(0.1); //Set servo to target position, in percentage(0.0-1.0)
        servo.getPosition(); //Get position last servo was told to turn. Caution! not current position
        //There is no way to know its absolute current position unless a motor is accompanied

        //Telemetry
        telemetry.addData("Caption", "Content");
        telemetry.update(); //Don't forget to update
        //telemetry show status of the robot on the driver station phone screen

        //CODE EXAMPLES
        //Use a key as a switch(a normal button)
        ElapsedTime keyPressTimer = new ElapsedTime(); //Declare this on Declaration part
        if (keyPressTimer.milliseconds() > 300) { //Change 300 if it is not sensitive enough
            keyPressTimer.reset(); //reset it
            //Do whatever you need
        } //Won't active if duration is < 300

        //Run motor until target encoder reading is reached but not having linear speed
        int targetPosition = 100; //Relative
        int initialPosition = motor.getCurrentPosition(); //Log current position
        while (motor.getCurrentPosition() < initialPosition + targetPosition) {
            int readingsLeft = initialPosition + targetPosition - motor.getCurrentPosition();

            //Change speed according to readings
            if (readingsLeft > 0.5 * targetPosition) {
                motor.setPower(0.5); //TODO: Change the value to what you want
            } else if (readingsLeft > 0.3 * targetPosition) {
                motor.setPower(0.3); //TODO: Change the value
            } //TODO: Add as many as you want to adjust the speed
        }


        //Wait for start, Don't forget this, fatal error occurs when forgotten
        waitForStart();
        //Running
        while (opModeIsActive()) {
            //This is where you put your running code
            //This runs many times in a second, number is not sure
            //GOOD LUCK!
        }
    }
}
