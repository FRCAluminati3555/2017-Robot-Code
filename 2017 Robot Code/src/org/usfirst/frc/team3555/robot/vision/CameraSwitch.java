package org.usfirst.frc.team3555.robot.vision;

import org.opencv.core.Mat;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

/*
 * This was taken from the example programs (intermediate vision)
 * and re-purposed for camera switching with a joystick
 */
public class CameraSwitch {
	Thread visionThread;
	Joystick joy;

	public CameraSwitch(Joystick joy) {
		/*
		 * makes the field joystick the passed in joystick
		 */
		this.joy = joy;
		
		/*
		 * this creates a new thread for the robot to run
		 * this thread will just put camera output to the dash
		 * and switch cameras at the press of a button on the joystick
		 */
		visionThread = new Thread(() -> {
			/*
			 * create the cameras here
			 */
			AxisCamera cam0 = new AxisCamera("cam0", "169.254.78.174");
			UsbCamera cam1 = new UsbCamera("cam1", 0);
			
			/*
			 * config them however here
			 */
			cam0.setResolution(640, 480);
			cam1.setResolution(640, 480);
			
			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo(cam0);
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from a camera and put it in the source mat.
				// If there is an error notify the output.
				if(joy.getRawButton(2))
					cvSink = CameraServer.getInstance().getVideo(cam0);
				if(joy.getRawButton(3))
					cvSink = CameraServer.getInstance().getVideo(cam1);
				
				
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
	}
}
