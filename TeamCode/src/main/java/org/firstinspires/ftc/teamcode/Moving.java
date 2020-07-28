package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.android.AndroidOrientation;

public class Moving {

    private DcMotor leftFwd;
    private DcMotor leftAft;
    private DcMotor rightFwd;
    private DcMotor rightAft;
    private DcMotor[] motors;

    public Moving(HardwareMap hardwareMap) {
        this.leftFwd = hardwareMap.dcMotor.get("leftFwd");
        this.leftAft = hardwareMap.dcMotor.get("leftAft");
        this.rightFwd = hardwareMap.dcMotor.get("rightFwd");
        this.rightAft = hardwareMap.dcMotor.get("rightAft");
        motors = new DcMotor[]{this.leftFwd, this.leftAft, this.rightFwd, this.rightAft};
    }

    public void resetMotors() {
        for (DcMotor runMode : motors) {
            runMode.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void runWithEncoder() {
        for (DcMotor runMode : motors) {
            runMode.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void runWithoutEncoder() {
        for (DcMotor runMode : motors) {
            runMode.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void breakWhenStop() {
        for (DcMotor runMode : motors) {
            runMode.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void floatWhenStop() {
        for (DcMotor runMode : motors) {
            runMode.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public void panX(double power) {
        this.leftFwd.setPower(power);
        this.leftAft.setPower(-power);
        this.rightFwd.setPower(power);
        this.rightAft.setPower(-power);
    }

    public void panY(double power) {
        this.leftFwd.setPower(power);
        this.leftAft.setPower(power);
        this.rightFwd.setPower(power);
        this.rightAft.setPower(power);
    }

    public void turn(double degree, double power) {
        AndroidOrientation androidOrientation = new AndroidOrientation();
        this.leftAft.setPower(power);
        this.leftFwd.setPower(power);
        this.rightAft.setPower(-power);
        this.rightFwd.setPower(-power);
        while (Math.abs(androidOrientation.getAzimuth()) < degree) {

        }
        this.leftAft.setPower(0);
        this.leftFwd.setPower(0);
        this.rightAft.setPower(0);
        this.rightFwd.setPower(0);
    }

}
