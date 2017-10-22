package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.ExponentialJoystick;
import org.usfirst.frc.team3555.robot.Input.JoystickMappings;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <h1>Drive Train</h1>
 * 
 * @author Sam Secondo
 */
public class DriveTrain implements SubSystem{
	public static enum DriveModes {
		ARCADE_DRIVE, TANK_DRIVE
	}
	
	private DriveModes currentControlMode;
	private ExponentialJoystick joyStickLeft, joyStickRight;
	private CANTalon leftRear, leftFront, rightRear, rightFront;
	
	private double leftPosP, leftPosI, leftPosD;
	private double rightPosP, rightPosI, rightPosD;
	
	private double leftVelP, leftVelI, leftVelD;
	private double rightVelP, rightVelI, rightVelD;
	
	private double scaleFactor;
	private double scaleFactorLimit;
	
	private int invertedDrive;
	private boolean buttonPressed;
	
	private int codesPerRev;
	private double wheelCircumference; // <- meters
	private double wheelRadius; // <- meters
	private double distanceBetweenWheels; // <- meters

	public DriveTrain(ExponentialJoystick joyLeft, ExponentialJoystick joyRight) {
		this.joyStickLeft = joyLeft;
		this.joyStickRight = joyRight;
		
		currentControlMode = DriveModes.ARCADE_DRIVE;
		
		leftRear = new CANTalon(43);
		leftFront = new CANTalon(41); 
		
		rightRear = new CANTalon(44);
		rightFront = new CANTalon(42);
		
		leftRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		leftRear.set(0);
		rightRear.set(0);
		
		leftFront.set(0);
		rightFront.set(0);
		
		leftPosP = .8;
		rightPosP = .9;
		
		leftVelP = 0.85;
		leftVelI = 0.01;
		leftVelD = 0.2;
		
		rightVelP = 0.85;
		rightVelI = 0.01;
		rightVelD = 0.2;
				
		codesPerRev = 360;
		wheelRadius = 0.0762;
		wheelCircumference = 2 * Math.PI * wheelRadius;
		distanceBetweenWheels = .51; 
	
		leftRear.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		rightRear.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		
		leftRear.configEncoderCodesPerRev(codesPerRev);
		rightRear.configEncoderCodesPerRev(codesPerRev);
		
		rightFront.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftFront.changeControlMode(CANTalon.TalonControlMode.Follower);
		
		scaleFactor = 1;
		scaleFactorLimit = .3;
		
		invertedDrive = -1;
		
		leftFront.enableControl();
		leftRear.enableControl();
		rightFront.enableControl();
		rightRear.enableControl();
	}
	
	public void update() {
		scaleFactor = ((joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Slider) * - 1) + 1)/2;
		
		if(scaleFactor < scaleFactorLimit)
			scaleFactor = scaleFactorLimit;

		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_7))
			currentControlMode = DriveModes.ARCADE_DRIVE;
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_6))
			currentControlMode = DriveModes.TANK_DRIVE;
		
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Top_Lower) && !buttonPressed) { 
			invertControls();
			buttonPressed= true;
		} else if(!joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Top_Lower)) {
			buttonPressed = false;
		}
			
		if(currentControlMode == DriveModes.ARCADE_DRIVE)
			arcadeDrive();
		else if(currentControlMode == DriveModes.TANK_DRIVE)
			tankDrive();
		
		SmartDashboard.putString("Drive Mode: ", String.valueOf(currentControlMode));
		SmartDashboard.putString("Controls Inverted: ", invertedDrive == 1 ? "Gear Handler Front" : "Shooter Front");
		SmartDashboard.putNumber("Left Encoder Count: ", leftRear.getEncPosition());
		SmartDashboard.putNumber("Right Encoder Count: ", rightRear.getEncPosition());
		
		SmartDashboard.putNumber("LR, 43 Current: ", leftRear.getOutputCurrent());
		SmartDashboard.putNumber("LF, 41 Current: ", leftFront.getOutputCurrent());
		SmartDashboard.putNumber("RF, 42 current: ", rightRear.getOutputCurrent());
		SmartDashboard.putNumber("RR, 44 Current: ", rightFront.getOutputCurrent());
	}
	
	/**
	 * 
	 * Drive the robot at certain rpm on each side for a certain amount of seconds
	 * Battery / Signal light is the front
	 * 
	 * @param speedLeft - Speed in RPM
	 * @param speedRight - Speed in RPM
	 * @param seconds - Seconds to Run
	 */
	public void autoDrive(double speedLeft, double speedRight, double seconds) {
		leftRear.changeControlMode(CANTalon.TalonControlMode.Speed);
		rightRear.changeControlMode(CANTalon.TalonControlMode.Speed);
		
		leftRear.setPID(leftVelP, leftVelI, leftVelD);
		rightRear.setPID(rightVelP, rightVelI, rightVelD);
		
		leftRear.set(-speedLeft);
		rightRear.set(speedRight);
		leftFront.set(leftRear.getDeviceID());
		rightFront.set(rightRear.getDeviceID());
		
		Timer.delay(seconds);
		
		leftRear.set(0);
		rightRear.set(0);
		leftFront.set(leftRear.getDeviceID());
		rightFront.set(rightRear.getDeviceID());
		
		leftRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		rightRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}
	
	//TODO Finish this method for autonomus
	public void drive(double metersLeftSide, double metersRightSide, double seconds) {
		double MetersPerSecondLeft = metersLeftSide / seconds;
		double angularVelocityLeft = MetersPerSecondLeft * wheelRadius; // radians per second
		double revolutionsPerSecondLeft = angularVelocityLeft * 2 * Math.PI;
		double rpmLeft = revolutionsPerSecondLeft * revolutionsPerSecondLeft;
		
		double MetersPerSecondRight = metersRightSide / seconds;
		double angularVelocityRight = MetersPerSecondRight * wheelRadius; // radians per second
		double revolutionsPerSecondRight = angularVelocityRight * 2 * Math.PI;
		double rpmRight = revolutionsPerSecondRight * revolutionsPerSecondRight;
		
		autoDrive(rpmLeft, rpmRight, seconds);
	}
	
	public void drive(double meters, double seconds) { drive(meters, meters, seconds); }
	
	public void turnLeftDegrees(double degrees, double seconds) { turnLeftDegrees(Math.toRadians(degrees), seconds); }
	public void turnRightDegrees(double degrees, double seconds) { turnRightDegrees(Math.toRadians(degrees), seconds); }
	
	/*
	 * Turns robot to the left with one wheel stationary
	 */
	public void turnLeftRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(0, distance, seconds);
	}
	
	/*
	 * Turns robot to the right with one wheel stationary
	 */
	public void turnRightRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(distance, 0, seconds);
	}
	
	public void arcadeDrive() {
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
		leftSpeed = (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) + (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.X) * invertedDrive)) * scaleFactor;
		rightSpeed = (-joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) + (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.X) * invertedDrive)) * scaleFactor;
    	
    	leftRear.set(leftSpeed * invertedDrive);
    	rightRear.set(rightSpeed * invertedDrive);
    	leftFront.set(leftRear.getDeviceID());
    	rightFront.set(rightRear.getDeviceID());
	}
	
	public void tankDrive() {
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	leftSpeed = joyStickLeft.getValue(JoystickMappings.LogitechAttack3_Axis.Y) * scaleFactor;
		rightSpeed = -joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) * scaleFactor;
    	
    	if(invertedDrive == -1) {
    		leftRear.set(-leftSpeed);
    		rightRear.set(-rightSpeed);
    	} else {
    		leftRear.set(-rightSpeed);
    		rightRear.set(-leftSpeed);
    	}
    	
    	leftFront.set(leftRear.getDeviceID());
    	rightFront.set(rightRear.getDeviceID());
    }
	
	public void updateFollowers() { leftFront.set(leftRear.getDeviceID()); rightFront.set(rightRear.getDeviceID()); }
	public void invertControls(){invertedDrive *= -1;}
}