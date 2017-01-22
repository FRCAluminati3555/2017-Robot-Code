package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {

			Timer.delay(0.005); 
		}
	}
}
