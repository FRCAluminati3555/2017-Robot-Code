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
	
	private double leftPosP, leftPosI, leftPosD, leftPosF;
	private double rightPosP, rightPosI, rightPosD, rightPosF;
	
	private double leftVelP, leftVelI, leftVelD, leftVelF;
	private double rightVelP, rightVelI, rightVelD, rightVelF;
	
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
		
//		leftPosP = .8;
//		rightPosP = .9;
		
		
		setLeftPositionPIDF(0.8, 0, 0, 0);
		setLeftVelocityPIDF(0.85, 0.01, 0.2, 0);
		
		setRightPositionPIDF(0.9, 0, 0, 0);
		setRightVelocityPIDF(0.85, 0.01, 0.2, 0);
		
//		leftVelP = 0.85;
//		leftVelI = 0.01;
//		leftVelD = 0.2;
		
//		rightVelP = 0.85;
//		rightVelI = 0.01;
//		rightVelD = 0.2;
				
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
	
	public void drive(double metersLeftSide, double metersRightSide, double seconds) {
		double metersPerSecondLeft = metersLeftSide / seconds;
		double metersPerSecondRight = metersRightSide / seconds;
		
		double rpmLeft = (metersPerSecondLeft / wheelRadius) * (60 / (Math.PI * 2)); 
		double rpmRight = (metersPerSecondRight / wheelRadius) * (60 / (Math.PI * 2)); 
		
		autoDrive(rpmLeft, rpmRight, seconds);
	}
	
	public void drive(double meters, double seconds) { drive(meters, meters, seconds); }
	
	public void turnLeftDegrees(double degrees, double seconds) { turnLeftDegrees(Math.toRadians(degrees), seconds); }
	public void turnRightDegrees(double degrees, double seconds) { turnRightDegrees(Math.toRadians(degrees), seconds); }
	
	/*
	 * Turns robot to the right with one wheel stationary
	 */
	public void turnRightRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(distance, 0, seconds);
	}

	/**
	 * Turn the robot with both wheels
	 * 
	 * @param degrees
	 * @param seconds
	 */
	public void turnRightOnDimeDegrees(double degrees, double seconds) { turnRightOnDimeRadians(Math.toRadians(degrees), seconds); }
	
	/**
	 * Turn the robot with both wheels
	 * 
	 * @param radians
	 * @param seconds
	 */
	public void turnRightOnDimeRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(distance, -distance);
	}
	
	/*
	 * Turns robot to the left with one wheel stationary
	 */
	public void turnLeftRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(0, distance, seconds);
	}
	
	public void turnLeftOnDimeDegrees(double degrees, double seconds) { turnLeftOnDimeRadians(Math.toRadians(degrees), seconds); }
	public void turnLeftOnDimeRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		drive(-distance, distance);
	}
	
	/**
	 * Don't question it
	 * 
	 * @param seconds
	 */
	public void spin(double seconds) {
		autoDrive(100, 100, seconds);
	}
	
	/**
	 * Spins the robot on a dime, rotations in terms of the amount of time to turn the robot 360 degrees (2pi radians)
	 * 
	 * @param rotations of the robot
	 * @param seconds
	 */
	public void spinLeft(double rotations, double seconds) {
		turnLeftOnDimeDegrees(rotations * 360, seconds);
	}
	
	/**
	 * Spins the robot on a dime, rotations in terms of the amount of time to turn the robot 360 degrees (2pi radians)
	 * 
	 * @param rotations of the robot
	 * @param seconds
	 */
	public void spinRight(double rotations, double seconds) {
		turnRightOnDimeDegrees(rotations * 360, seconds);
	}
	
	/**
	 * Drive the robot in terms of wheel rotations
	 * 
	 * @param rotationsLeft - rotations of the left wheels
	 * @param rotationsRight - rotation of the right wheels
	 */
	public void driveRotations(double rotationsLeft, double rotationsRight, double seconds) { drive(wheelCircumference * rotationsLeft, wheelCircumference * rotationsRight, seconds); }
	
	/**
	 * Drive the robot in terms of wheel rotations
	 * 
	 * @param rotations
	 * @param seconds
	 */
	public void driveRotations(double rotations, double seconds) { drive(wheelCircumference * rotations, wheelCircumference * rotations, seconds); }
	
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
	
	
	/*********************************** Set Left PIDF ********************************/
	public void setLeftVelocityPID(double leftVelP, double leftVelI, double leftVelD) {
		this.leftVelP = leftVelP;
		this.leftVelI = leftVelI;
		this.leftVelD = leftVelD;
	}
	
	public void setLeftVelocityPIDF(double leftVelP, double leftVelI, double leftVelD, double leftVelF) {
		setLeftVelocityPID(leftVelP, leftVelI, leftVelD);
		this.leftVelF = leftVelF;
	}
	
	public void setLeftPositionPID(double leftPosP, double leftPosI, double leftPosD) {
		this.leftPosP = leftPosP;
		this.leftPosI = leftPosI;
		this.leftPosD = leftPosD;
	}
	
	public void setLeftPositionPIDF(double leftPosP, double leftPosI, double leftPosD, double leftPosF) {
		setLeftPositionPID(leftPosP, leftPosI, leftPosD);
		this.leftPosF = leftPosF;
	}
	
	/*********************************** Set Right PIDF ******************************/
	public void setRightVelocityPID(double rightVelP, double rightVelI, double rightVelD) {
		this.rightVelP = rightVelP;
		this.rightVelI = rightVelI;
		this.rightVelD = rightVelD;
	}
	
	public void setRightVelocityPIDF(double rightVelP, double rightVelI, double rightVelD, double rightVelF) {
		setRightVelocityPID(rightVelP, rightVelI, rightVelD);
		this.rightVelF = rightVelF;
	}
	
	public void setRightPositionPID(double rightPosP, double rightPosI, double rightPosD) {
		this.rightPosP = rightPosP;
		this.rightPosI = rightPosI;
		this.rightPosD = rightPosD;
	}
	
	public void setRightPositionPIDF(double rightPosP, double rightPosI, double rightPosD, double rightPosF) {
		setRightPositionPID(rightPosP, rightPosI, rightPosD);
		this.rightPosF = rightPosF;
	}
	
	public void updateFollowers() { leftFront.set(leftRear.getDeviceID()); rightFront.set(rightRear.getDeviceID()); }
	public void invertControls(){invertedDrive *= -1;}
}