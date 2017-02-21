package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/*
 * Reserved project for random testing
 */
public class Robot extends SampleRobot {
	private CANTalon talon = new CANTalon(0);
	private Joystick joy = new Joystick(2);
	
	public Robot(){
		talon.reset();
		talon.setPosition(0);
		talon.changeControlMode(CANTalon.TalonControlMode.Speed);
//		talon.changeControlMode(CANTalon.TalonControlMode.Position);
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//		talon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		talon.configEncoderCodesPerRev(500);
		talon.configNominalOutputVoltage(+0.0f, -0.0f);
        talon.configPeakOutputVoltage(+12.0f, -12.0f);
		talon.setF(0);
//		talon.setPID(12.8, 0, 0); // position mode on the gear handler
//		talon.setPID(.6, .0001, .15); // this is for the position mode
		talon.setPID(.2, .00062, .03); // this is for the speed mode
		
		talon.reverseSensor(false);
		talon.enableControl();
		talon.set(4000);
	}
	
	public void operatorControl() {
		while(isOperatorControl() && isEnabled()) {
//			if(Math.abs(joy.getRawAxis(1)) > .1)
//				talon.set(joy.getRawAxis(1));
//			else
				
//			if(joy.getRawButton(9))
//				talon.set(460);
//			if(joy.getRawButton(10))
//				talon.set(600);
//			if(joy.getRawButton(11))
//				talon.set(720);
//			talon.set(1); //for position mode, tells it to turn once (test for pid constants) the constants above seems to work well
//			talon.set(100); //for speed mode, tells it to turn at 100 rpm (test for pid constants) the constants above seems to work well 
			
//			SmartDashboard.putNumber("Talon Pot: ", talon.getAnalogInPosition());
			SmartDashboard.putNumber("Talon Pos: ", talon.getPosition());
//			SmartDashboard.putNumber("Talon Encoder Pos: ", talon.getEncPosition());
			SmartDashboard.putNumber("Talon rpm: ", talon.getSpeed());
			SmartDashboard.putNumber("Talon Voltage: ", talon.getOutputVoltage());
			SmartDashboard.putNumber("Talon current: ", talon.getOutputCurrent());
			
			Timer.delay(0.005); 
		}
		talon.setPosition(0);
	}
}
