package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.android.AndroidOrientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone;

import java.util.List;

@Autonomous(name = "SeekSkyStone (Blocks to Java)", group = "")
public class SeekSkyStone extends LinearOpMode {

    private DcMotor leftFwd;
    private DcMotor leftAft;
    private DcMotor rightFwd;
    private DcMotor rightAft;
    private VuforiaSkyStone vuforiaSkyStone;
    private TfodSkyStone tfodSkyStone;
    private AndroidOrientation androidOrientation;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        leftFwd = hardwareMap.dcMotor.get("leftFwd");
        leftAft = hardwareMap.dcMotor.get("leftAft");
        rightFwd = hardwareMap.dcMotor.get("rightFwd");
        rightAft = hardwareMap.dcMotor.get("rightAft");
        //UpperServo = hardwareMap.servo.get("UpperServo");
        //LowerServo = hardwareMap.servo.get("LowerServo");
        vuforiaSkyStone = new VuforiaSkyStone();
        tfodSkyStone = new TfodSkyStone();
        androidOrientation = new AndroidOrientation();


        // Initialization
        telemetry.addData("Init ", "started");
        telemetry.update();
        leftFwd.setDirection(DcMotorSimple.Direction.REVERSE);
        leftAft.setDirection(DcMotorSimple.Direction.REVERSE);
        //UpperServo.setPosition(1);
        //LowerServo.setPosition(0);
        // Init Vuforia because Tensor Flow needs it.
        vuforiaSkyStone.initialize(
                "", // vuforiaLicenseKey
                VuforiaLocalizer.CameraDirection.BACK, // cameraDirection
                false, // useExtendedTracking
                true, // enableCameraMonitoring
                VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES, // cameraMonitorFeedback
                0, // dx
                0, // dy
                0, // dz
                0, // xAngle
                0, // yAngle
                0, // zAngle
                false); // useCompetitionFieldTargetLocations
        telemetry.addData("Vuforia", "initialized");
        telemetry.update();
        // Let's use 70% minimum confidence and
        // and no object tracker.
        tfodSkyStone.initialize(vuforiaSkyStone, 0.7F, true, true);
        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        // Set target ratio of object height to image
        // height value corresponding to the length
        // of the robot's neck.
        double TargetHeightRatio = 0.8;
        waitForStart();
        tfodSkyStone.activate();
        // We'll loop until gold block captured or time is up
        boolean SkystoneFound = false;
        while (opModeIsActive() && !SkystoneFound) {
            // Get list of current recognitions.
            List<Recognition> recognitions = tfodSkyStone.getRecognitions();
            // Report number of recognitions.
            telemetry.addData("Objects Recognized", recognitions.size());
            // If some objects detected...
            if (recognitions.size() > 0) {
                // ...let's count how many are gold.
                int SkystoneCount = 0;
                // Step through the stones detected.
                // TODO: Enter the type for variable named recognition
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals("Skystone")) {
                        // A Skystone has been detected.
                        SkystoneCount = SkystoneCount + 1;
                        // We can assume this is the first Skystone
                        // because we break out of this loop below after
                        // using the information from the first Skystone.
                        // We don't need to calculate turn angle to Skystone
                        // because TensorFlow has estimated it for us.
                        double ObjectAngle = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                        // Negative angle means Skystone is left, else right.
                        telemetry.addData("Estimated Angle", ObjectAngle);
                        if (ObjectAngle > 0) {
                            telemetry.addData("Direction", "Right");
                        } else {
                            telemetry.addData("Direction", "Left");
                        }
                        // Calculate power levels for turn toward Skystone.
                        double LeftPower = 0.5 * (ObjectAngle / 45);
                        double RightPower = -0.5 * (ObjectAngle / 45);
                        // We'll be comparing the Skystone height
                        // to the height of the video image to estimate
                        // how close the robot is to the Skystone.
                        double ImageHeight = recognition.getImageHeight();
                        double ObjectHeight = recognition.getHeight();
                        // Calculate height of Skystone relative to image height.
                        // Larger ratio means robot is closer to Skystone.
                        double ObjectHeightRatio = ObjectHeight / ImageHeight;
                        telemetry.addData("HeightRatio", ObjectHeightRatio);
                        // Use height ratio to determine distance.
                        // If height ratio larger than (target - tolerance)...
                        if (ObjectHeightRatio < TargetHeightRatio - 0.05) {
                            // ...not close enough yet.
                            telemetry.addData("Distance", "Not close enough");
                            // If sum of turn powers are small
                            if (Math.abs(LeftPower) + Math.abs(RightPower) < 0.05) {
                                // ...don't really need to turn.  Move forward.
                                telemetry.addData("Action", "Forward");
                                // Go forward by setting power proportional to how
                                // far from target distance.
                                LeftPower = 0.1 + 0.5 * ((TargetHeightRatio - 0.05) - ObjectHeightRatio);
                                RightPower = LeftPower;
                            } else {
                                // Else we'll turn to Skystone with current power levels.
                                telemetry.addData("Action", "Turn");
                            }
                            // Else if height ratio more than (target+tolerance)...
                        } else if (ObjectHeightRatio > TargetHeightRatio + 0.01) {
                            // ...robot too close to Skystone.
                            telemetry.addData("Distance", "Too close");
                            // If calculated turn power levels are small...
                            if (Math.abs(LeftPower) + Math.abs(RightPower) < 0.12) {
                                // ...don't need to turn.  Backup instead by setting
                                // power proportional to how far past target ratio
                                telemetry.addData("Action", "Back up");
                                LeftPower = -0.05 + -0.5 * ((TargetHeightRatio + 0.05) - TargetHeightRatio);
                                RightPower = LeftPower;
                            } else {
                                // Else use current power levels to turn to Skystone
                                telemetry.addData("Action", "Turn");
                            }
                        } else {
                            // Skystone is about one neck length away.
                            telemetry.addData("Distance", "Correct");
                            // If calculated turn power levels are small...
                            if (Math.abs(LeftPower) + Math.abs(RightPower) < 0.12) {
                                // ...robot is centered on the Skystone.
                                telemetry.addData("Action", "Motors off, hit the Skystone");
                                // Turn motors off by setting power to 0.
                                LeftPower = 0;
                                RightPower = 0;
                                // Lower neck and open jaw.
                                //LowerServo.setPosition(0.5);
                                //UpperServo.setPosition(0.5);
                                SkystoneFound = true;
                            } else {
                                // Otherwise use current power levels to turn
                                // to better center on gold.
                                telemetry.addData("Action", "Turn");
                            }
                        }
                        telemetry.addData("Left Power", LeftPower);
                        telemetry.addData("Right Power", RightPower);
                        // Set power levels to get closer to Skystone.
                        leftFwd.setPower(LeftPower);
                        leftAft.setPower(LeftPower);
                        rightFwd.setPower(RightPower);
                        rightAft.setPower(RightPower);
                        // We've found a Skystone so we don't have
                        // to look at rest of detected objects.
                        // Break out of For-each-recognition.
                        break;
                    }
                }
                // If no Skystones detected...
                if (SkystoneCount == 0) {
                    telemetry.addData("Status", "No Skystone");
                    telemetry.addData("Action", "Back up");
                    // Back up slowly hoping to bring Skystone in view.
                    leftFwd.setPower(-0.1);
                    leftAft.setPower(-0.1);
                    rightFwd.setPower(-0.1);
                    rightAft.setPower(-0.1);
                }
            } else {
                // No objects detected
                telemetry.addData("Status", "No objects detected");
                telemetry.addData("Action", "Back up");
                // Back up slowly hoping to bring objects in view.
                leftFwd.setPower(-1);
                leftAft.setPower(-1);
                rightFwd.setPower(-1);
                rightAft.setPower(-1);
            }
            telemetry.update();
        }
        // Skystone found, time is up or stop was requested.
        tfodSkyStone.deactivate();
        leftFwd.setPower(0);
        leftAft.setPower(0);
        rightFwd.setPower(0);
        rightAft.setPower(0);
        // Pause to let driver station to see last telemetry.
        sleep(2000);

        vuforiaSkyStone.close();
        tfodSkyStone.close();
        double azimuth = androidOrientation.getAzimuth();
    }
}