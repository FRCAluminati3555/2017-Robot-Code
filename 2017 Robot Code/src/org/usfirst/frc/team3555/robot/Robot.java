package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	private Joystick joyLeft = new Joystick(1), joyRight = new Joystick(2), joyOP = new Joystick(0);
	
	private CANTalon left1 = new CANTalon(0), right1 = new CANTalon(0);
	
	private MotorGroup leftSideDrive = new MotorGroup(left1, new CANTalon(0)),
			rightSideDrive = new MotorGroup(right1, new CANTalon(0));
	
	private final double DEADZONE = .05;
	
	public void tankDrive(){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(joyLeft.getRawAxis(1)) >= DEADZONE){
        	leftSpeed = joyLeft.getRawAxis(1);
    	}
    	
    	if(Math.abs(joyRight.getRawAxis(1)) >= DEADZONE){
    		rightSpeed = -joyRight.getRawAxis(1);
    	}
    	
    	leftSideDrive.set(leftSpeed);
    	rightSideDrive.set(rightSpeed);
    }
	
	public void arcadeDrive(Joystick arcadeJoy){
    	double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(arcadeJoy.getRawAxis(1)) >= DEADZONE || Math.abs(arcadeJoy.getRawAxis(0)) >= DEADZONE){ 
    		leftSpeed = arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    		rightSpeed = -arcadeJoy.getRawAxis(1) - arcadeJoy.getRawAxis(0);
    	}
    	
    	leftSideDrive.set(leftSpeed);
    	rightSideDrive.set(rightSpeed);
    }
	
	public Robot(){
		left1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		right1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
	}
	
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			
			SmartDashboard.putNumber("Left Encoder: ", left1.getPosition()); // this needs to be tested more
			SmartDashboard.putNumber("Right Encoder: ", right1.getPosition());
			
			tankDrive();
			
			Timer.delay(0.005);
		}
	}
}
