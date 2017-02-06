package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
/*
 * Reserved project for random testing
 */
public class Robot extends SampleRobot {
	private CANTalon talon = new CANTalon(44);
//	private Joystick joy = new Joystick(2);
	
	public Robot(){
		talon.reset();
		talon.setPosition(0);
//		talon.changeControlMode(CANTalon.TalonControlMode.Speed);
//		talon.changeControlMode(CANTalon.TalonControlMode.Position);
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		talon.configEncoderCodesPerRev(360);
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0f, -12.0f);
		talon.setF(0);
//		talon.setPID(.25, .00062, .3); // this is for the position mode
//		talon.setPID(.2, .00062, 0); // this is for the speed mode
		
		talon.reverseSensor(true);
		talon.enableControl();
	}
	
	public void operatorControl() {
		while(isOperatorControl() && isEnabled()) {
//			talon.set(1); //for position mode, tells it to turn once (test for pid constants) the constants above seems to work well
//			talon.set(100); //for speed mode, tells it to turn at 100 rpm (test for pid constants) the constants above seems to work well 
			Timer.delay(0.005); 
		}
	}
}
