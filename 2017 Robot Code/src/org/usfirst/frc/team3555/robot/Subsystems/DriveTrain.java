package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Autonomous.Action;
import org.usfirst.frc.team3555.robot.Input.ExponentialJoystick;
import org.usfirst.frc.team3555.robot.Input.JoystickMappings;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <h1>Drive Train</h1>
 * Hold the power of driving the robot around.
 * This contains all the speed controllers and the drive controllers.
 * Has methods for different drive control modes (Example: Arcade Drive, Tank Drive).
 * This also has generator methods for different basic actions that the drive train can do (Example: Spin Left, Turn Right...)
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
		
		setLeftPositionPIDF(0.8, 0, 0, 0);
		setLeftVelocityPIDF(0.85, 0.01, 0.2, 0);
		
		setRightPositionPIDF(0.9, 0, 0, 0);
		setRightVelocityPIDF(0.85, 0.01, 0.2, 0);

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
	
	/**
	 * Reads the state of the joysticks, and will call the corresponding drive control mode to interpret joy positions.
	 */
	public void update() {
		scaleFactor = ((joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Slider) * - 1) + 1) / 2;//Uses the slider at the bottom of the joystick to act as a throttle to give driver more control over speed 
		
		if(scaleFactor < scaleFactorLimit)//Caps the factor to a minimum amount
			scaleFactor = scaleFactorLimit;//Prevents it from becoming 0

		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_7))//Switch Control Mode
			currentControlMode = DriveModes.ARCADE_DRIVE;
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_6))
			currentControlMode = DriveModes.TANK_DRIVE;
		
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Top_Lower) && !buttonPressed) {//Invert the controls (Change which way is the front) 
			invertedDrive *= -1;//Invert controls
			buttonPressed = true;
		} else if(!joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Top_Lower)) {//Boolean logic to only call this once when a button is pressed
			buttonPressed = false;
		}
			
		if(currentControlMode == DriveModes.ARCADE_DRIVE)//Call the corresponding method to interpret joystick position
			arcadeDrive();
		else if(currentControlMode == DriveModes.TANK_DRIVE)
			tankDrive();
		
		/********************************* Print Stuff *********************************/ 
		SmartDashboard.putString("Drive Mode: ", String.valueOf(currentControlMode));
		SmartDashboard.putString("Controls Inverted: ", invertedDrive == 1 ? "Gear Handler Front" : "Shooter Front");
		SmartDashboard.putNumber("Left Encoder Count: ", leftRear.getEncPosition());
		SmartDashboard.putNumber("Right Encoder Count: ", rightRear.getEncPosition());
		
		SmartDashboard.putNumber("LR, 43 Current: ", leftRear.getOutputCurrent());
		SmartDashboard.putNumber("LF, 41 Current: ", leftFront.getOutputCurrent());
		SmartDashboard.putNumber("RF, 42 current: ", rightRear.getOutputCurrent());
		SmartDashboard.putNumber("RR, 44 Current: ", rightFront.getOutputCurrent());
	}
	
	//***************************** DRIVE MODES ***********************************//
	
	/**
	 * Interprets joystick position to drive the motor controllers in the arcade fashion
	 * This the joyStickRight is the main joystick used for arcade drive
	 */
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
	
	/**
	 * Interprets the position of both joysticks to drive the robot in a tank formation
	 */
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
	
	//***************************** Actions ***********************************//
	
	/**
	 * Create am action object to drive the robot at certain rpm on each side for a certain amount of seconds. 
	 * Battery / Signal light is the front.
	 * 
	 * @param speedLeft - Speed in RPM
	 * @param speedRight - Speed in RPM
	 * @param seconds - Seconds for this to take (Keep this reasonable)
	 * @return - The Action object to be used in autonomous
	 */
	public Action getAutoDriveAction(double speedLeft, double speedRight, double seconds) {
		return new Action(() -> {//Start
			leftRear.changeControlMode(CANTalon.TalonControlMode.Speed);
			rightRear.changeControlMode(CANTalon.TalonControlMode.Speed);
			
			leftRear.setPID(leftVelP, leftVelI, leftVelD);
			rightRear.setPID(rightVelP, rightVelI, rightVelD);
			
			leftRear.set(-speedLeft);
			rightRear.set(speedRight);
			leftFront.set(leftRear.getDeviceID());
			rightFront.set(rightRear.getDeviceID());
		}, (startTime) -> {//Update
			if(System.currentTimeMillis() >= (seconds * 1000) + startTime)
				return true;
			return false;
		}, () -> {//Clean Up
			leftRear.set(0);
			rightRear.set(0);
			leftFront.set(leftRear.getDeviceID());
			rightFront.set(rightRear.getDeviceID());
			
			leftRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			rightRear.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		});
	}
	
	/**
	 * Creates an action object that will drive each side their designated distance in the allocated time.
	 * 
	 * @param metersLeftSide - Meters for the left side to drive 
	 * @param metersRightSide - Meters for the right side to drive
	 * @param seconds - Seconds for this to be completed in (Keep this reasonable)
	 * @return - The action object that can be added to the queue
	 */
	public Action drive(double metersLeftSide, double metersRightSide, double seconds) {
		double metersPerSecondLeft = metersLeftSide / seconds;
		double metersPerSecondRight = metersRightSide / seconds;
		
		double rpmLeft = (metersPerSecondLeft / wheelRadius) * (60 / (Math.PI * 2)); 
		double rpmRight = (metersPerSecondRight / wheelRadius) * (60 / (Math.PI * 2)); 
		
		return getAutoDriveAction(rpmLeft, rpmRight, seconds);
	}
	
	/**
	 * Creates an action object that will drive both sides the designated distance in the allocated time
	 * 
	 * @param meters - Distance in meters for both sides to drive
	 * @param seconds - Seconds that this is to be done in
	 * @return - The Action object that can be added to the autonomous queue
	 */
	public Action drive(double meters, double seconds) { return drive(meters, meters, seconds); }
	
	/**
	 * Turn a certain degrees to the right.
	 * This will only turn the left wheel, the right will remain stationary.
	 * 
	 * @param degrees - Degrees to turn to the right
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnRightDegrees(double degrees, double seconds) { return turnRightRadians(Math.toRadians(degrees), seconds); }
	
	/**
	 * Turn a certain radians to the right.
	 * This will only turn the left wheel, the right will remain stationary.
	 * 
	 * @param radians - Radians to turn to the right
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnRightRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		return drive(distance, 0, seconds);
	}

	/**
	 * Turn a certain degrees to the right.
	 * This will turn both wheels in opposite directions, causing it to turn on a point (roughly)
	 * 
	 * @param degrees - Degrees to turn to the right
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnRightOnDimeDegrees(double degrees, double seconds) { return turnRightOnDimeRadians(Math.toRadians(degrees), seconds); }

	/**
	 * Turn a certain radians to the right.
	 * This will turn both wheels in opposite directions, causing it to turn on a point (roughly)
	 * 
	 * @param radians - Radians to turn to the right
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnRightOnDimeRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		return drive(distance, -distance);
	}
	
	/**
	 * Turn a certain degrees to the left.
	 * This will only turn the right wheel, the left will remain stationary.
	 * 
	 * @param degrees - Degrees to turn to the left
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnLeftDegrees(double degrees, double seconds) { return turnLeftRadians(Math.toRadians(degrees), seconds); }
	
	/**
	 * Turn a certain radians to the left.
	 * This will only turn the right wheel, the left will remain stationary.
	 * 
	 * @param radians - Radians to turn to the left
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnLeftRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		return drive(0, distance, seconds);
	}
	
	/**
	 * Turn a certain degrees to the Left.
	 * This will turn both wheels in opposite directions, causing it to turn on a point (roughly)
	 * 
	 * @param degrees - Degrees to turn to the left
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnLeftOnDimeDegrees(double degrees, double seconds) { return turnLeftOnDimeRadians(Math.toRadians(degrees), seconds); }
	
	/**
	 * Turn a certain radians to the Left.
	 * This will turn both wheels in opposite directions, causing it to turn on a point (roughly)
	 * 
	 * @param radians - Radians to turn to the left
	 * @param seconds - Seconds that this action should take
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action turnLeftOnDimeRadians(double radians, double seconds) {
		double distance = radians * distanceBetweenWheels;
		return drive(-distance, distance);
	}
	
	/**
	 * Spins the robot on a dime, rotations in terms of the amount of time to turn the robot 360 degrees (2pi radians)
	 * 
	 * @param rotations - Amount of full spins of the robot
	 * @param seconds - Seconds for this action to complete
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action spinLeft(double rotations, double seconds) {
		return turnLeftOnDimeDegrees(rotations * 360, seconds);
	}
	
	/**
	 * Spins the robot on a dime, rotations in terms of the amount of time to turn the robot 360 degrees (2pi radians)
	 * 
	 * @param rotations - Amount of full spins of the robot
	 * @param seconds - Seconds for this action to complete
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action spinRight(double rotations, double seconds) {
		return turnRightOnDimeDegrees(rotations * 360, seconds);
	}
	
	/**
	 * Drive the robot in terms of wheel rotations
	 * 
	 * @param rotationsLeft - Amount rotations of the left wheels
	 * @param rotationsRight - Amount of rotations of the right wheels
	 * @param seconds - Seconds for this Action to complete
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action driveRotations(double rotationsLeft, double rotationsRight, double seconds) { 
		return drive(wheelCircumference * rotationsLeft, wheelCircumference * rotationsRight, seconds); 
	}
	
	/**
	 * Drive the robot in terms of wheel rotations
	 * 
	 * @param rotations - Amount of rotations for all wheels to turn
	 * @param seconds - Seconds for this Action to complete
	 * @return - Action object to be added to the autonomous queue
	 */
	public Action driveRotations(double rotations, double seconds) { return drive(wheelCircumference * rotations, wheelCircumference * rotations, seconds); }
	
	/*********************************** Set Left PIDF ********************************/
	
	/**
	 * Change the PID values of the velocity loop on the left side
	 * 
	 * @param leftVelP - Left Velocity P Value
	 * @param leftVelI - Left Velocity I Value
	 * @param leftVelD - Left Velocity D Value
	 */
	public void setLeftVelocityPID(double leftVelP, double leftVelI, double leftVelD) {
		this.leftVelP = leftVelP;
		this.leftVelI = leftVelI;
		this.leftVelD = leftVelD;
	}
	
	/**
	 * Change the PIDF values of the velocity loop on the left side
	 * 
	 * @param leftVelP - Left Velocity P Value
	 * @param leftVelI - Left Velocity I Value
	 * @param leftVelD - Left Velocity D Value
	 * @param leftVelF - Left Velocity F Value
	 */
	public void setLeftVelocityPIDF(double leftVelP, double leftVelI, double leftVelD, double leftVelF) {
		setLeftVelocityPID(leftVelP, leftVelI, leftVelD);
		this.leftVelF = leftVelF;
	}
	
	/**
	 * Change PID values of the position loop on the left side
	 * 
	 * @param leftPosP - Left Position P Value
	 * @param leftPosI - Left Position I Value
	 * @param leftPosD - Left Position D Value
	 */
	public void setLeftPositionPID(double leftPosP, double leftPosI, double leftPosD) {
		this.leftPosP = leftPosP;
		this.leftPosI = leftPosI;
		this.leftPosD = leftPosD;
	}
	
	/**
	 * Change PIDF values of the position loop on the left side
	 * 
	 * @param leftPosP - Left Position P Value
	 * @param leftPosI - Left Position I Value
	 * @param leftPosD - Left Position D Value
	 * @param leftPosF - Left Position F Value
	 */
	public void setLeftPositionPIDF(double leftPosP, double leftPosI, double leftPosD, double leftPosF) {
		setLeftPositionPID(leftPosP, leftPosI, leftPosD);
		this.leftPosF = leftPosF;
	}
	
	/*********************************** Set Right PIDF ******************************/
	
	/**
	 * Change the PID values of the velocity loop on the right side
	 * 
	 * @param rightVelP - Right Velocity P Value
	 * @param rightVelI - Right Velocity I Value
	 * @param rightVelD - Right Velocity D Value
	 */
	public void setRightVelocityPID(double rightVelP, double rightVelI, double rightVelD) {
		this.rightVelP = rightVelP;
		this.rightVelI = rightVelI;
		this.rightVelD = rightVelD;
	}
	
	/**
	 * Change the PIDF values of the velocity loop on the right side
	 * 
	 * @param rightVelP - Right Velocity P Value
	 * @param rightVelI - Right Velocity I Value
	 * @param rightVelD - Right Velocity D Value
	 * @param rightVelF - Right Velocity F Value
	 */
	public void setRightVelocityPIDF(double rightVelP, double rightVelI, double rightVelD, double rightVelF) {
		setRightVelocityPID(rightVelP, rightVelI, rightVelD);
		this.rightVelF = rightVelF;
	}
	
	/**
	 * Change the PID values of the position loop on the right side
	 * 
	 * @param rightPosP - Right Position P Value
	 * @param rightPosI - Right Position I Value
	 * @param rightPosD - Right Position D Value
	 */
	public void setRightPositionPID(double rightPosP, double rightPosI, double rightPosD) {
		this.rightPosP = rightPosP;
		this.rightPosI = rightPosI;
		this.rightPosD = rightPosD;
	}
	
	/**
	 * Change the PIDF values of the position loop on the right side
	 * 
	 * @param rightPosP - Right Position P Value
	 * @param rightPosI - Right Position I Value
	 * @param rightPosD - Right Position D Value
	 * @param rightPosF - Right Position F Value
	 */
	public void setRightPositionPIDF(double rightPosP, double rightPosI, double rightPosD, double rightPosF) {
		setRightPositionPID(rightPosP, rightPosI, rightPosD);
		this.rightPosF = rightPosF;
	}
}