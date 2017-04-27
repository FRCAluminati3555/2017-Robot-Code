package org.usfirst.frc.team3555.robot.subsystems;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings;
import org.usfirst.frc.team3555.robot.control.input.ExponentialJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain implements SubSystem{
	/*
	 * fields that take the place of the passed in variable so the rest of the class can see them
	 */
	private ExponentialJoystick joyStickLeft, joyStickRight;
	
	private XboxController xboxController;
	
	/*
	 * represents the deadzone on the joysticks to prevent the motors from moving at .005%
	 */
	private double deadzone;
	
	/*
	 * This multiplies to the value of the joystick, so that the driver can widen the range of precision in a 
	 * situation with slow and more precise movements (a value of 0 - 1 )  
	 */
	private double scaleFactor;
	
	/*
	 * This variable will set the lower bound limit to the scale factor so that the driver won't accidently put it to 0
	 * which would make the robot not move
	 */
	private double scaleFactorLimit;
	/*
	 * pid constants for the drive train CANTalons
	 */
	private double p = .65,i = 0,d = .1;
	
	/*
	 * represents whether or not the encoders are taken into account in the drive train controls
	 * aka controlled by RPM (uses encoders) or just percentage (does not use encoders)
	 */
	private boolean encoderDrive;
	
	private boolean controlWithXboxController;
	
	/*
	 * allows driver to reverse controls
	 * this is done by either having this variable being negative 1 or positive 1, then is multiplied to the 
	 * final speed output to either keep it the way it was, or multiply by -1
	 * to invert the controls
	 */
	private int invertedDrive;

	/*
	 * This is used to tell if the function with the button has already been pressed
	 * that way it doesn't get called many times over on 1 press
	 */
	private boolean invertButtonPressed;	
	/*
	 * CANTalon fields that represent the CANTalons on the drive train
	 * the naming convention is based on the fact that the two sides both have 2 speed controllers that control 1 shaft
	 */
	private CANTalon left1, left2, right1, right2;
	
	/*
	 * represents the max rpm of the motors on the robot
	 */
	private double maxRPM;
	
	/*
	 * an enumeration that contains the modes of the drive train
	 */
	public static enum DriveModes{
		ARCADE_DRIVE, TANK_DRIVE
	}
	
	/*
	 * represents the current mode that the drive 
	 */
	private DriveModes currentControlMode;
	
	/*
	 * Constructor for the Drive Train class
	 */
	public DriveTrain(double deadzone, DriveModes startingControlMode, ExponentialJoystick joyLeft, ExponentialJoystick joyRight, XboxController controller,
			CANTalon left1, CANTalon left2, CANTalon right1, CANTalon right2){
		
		scaleFactorLimit = .3;
		/*
		 * sets the fields of the class to the passed in parameters of the constructor
		 */
		this.deadzone = deadzone;
		this.joyStickLeft = joyLeft;
		this.joyStickRight = joyRight;
		this.xboxController = controller;
		
		currentControlMode = startingControlMode;
		
		this.left1 = left1;
		this.left2 = left2;
		this.right1 = right1;
		this.right2 = right2;
		
		maxRPM = 5330;
		
		scaleFactor = 1;
		
		/* 
		 * 1 is the standard orientation of the robot encoders
		 */
		invertedDrive = 1;
		
		/**
		 * the init of the CANTalons
		 */
		
		/*
		 * starts off by setting one controller of each side to speed mode
		 * that way they are controlled by rpm rather than a percentage of power available 
		 */
		left1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		right1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		/*
		 * sets these controllers to have a Quad Encoder to read from
		 */
		left1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		right1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		
		/*
		 * sets the amount of pulses, from the encoder, that equates to one revolution
		 */
		left1.configEncoderCodesPerRev(360);
		right1.configEncoderCodesPerRev(360);
		
		/*
		 * sets the PID of the two speed controllers
		 * an article explaining PID: http://www.flitetest.com/articles/pid-s-explained-in-simple-terms
		 * it's basically constatns that the controller will use to either speed itself up or lower itself
		 * to keep at a constant rate or position
		 */
		left1.setPID(p, i, d); //TODO test for these constants!
		right1.setPID(p, i, d);
		
		left1.reverseSensor(false);
		right1.reverseSensor(false);
		
		right1.setEncPosition(0);
		left1.setEncPosition(0);
		
		/*
		 * this sets the other two CANTalons to follower mode 
		 * this means that they will reflect the output of the Device ID that is passed into their set method
		 */
		left2.changeControlMode(CANTalon.TalonControlMode.Follower);
		right2.changeControlMode(CANTalon.TalonControlMode.Follower);

		/*
		 * this will enable the speed controllers to move
		 */
		left1.enableControl();
		left2.enableControl();
		right1.enableControl();
		right2.enableControl();
		
		left1.enable();
		left2.enable();
		right1.enable();
		right2.enable();
	}
	
	/*
	 * this is the update method specified by the subsystem interface
	 * this is the method that will check which drive mode that is chosen, 
	 * then it will do the math of the corresponding drive mode, and use that to control the robot
	 */
//	double tempRightPos;
//	double tempRightNeg;
//	double tempLeftPos;
//	double tempLeftNeg;
	
	public void update(){
		/*
		 * checks which side the slider is on, -1 is the top of the slider
		 * 1 is the bottom of the robot
		 * if it is at the bottom side, then the drive train will control with the controller
		 * if it is at the top side then the drive will control with the joysticks
		 */
//		if(joyStickRight.getRawAxis(2) > 0)
//			controlWithXboxController = true;
//		else if(joyStickRight.getRawAxis(2) < 0)
//			controlWithXboxController = false;
//
		
		/*
		 * (pot+1)/2
		 */
		scaleFactor = ((joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Slider) * -1) + 1)/2;
		
		if(scaleFactor < scaleFactorLimit){
			scaleFactor = scaleFactorLimit;
		}
		
		/*
		 * checks button 7 & 6 to switch drive mode
		 */
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_7))
			currentControlMode = DriveModes.ARCADE_DRIVE;
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Button_6))
			currentControlMode = DriveModes.TANK_DRIVE;
		
		/*
		 * inverts the controls
		 * meaning that the back is now the front 
		 * and the front is now the back
		 * the method will alternate from where it was
		 */
		if(joyStickRight.isButtonPressed(JoystickMappings.LogitechAttack3_Button.Top_Lower) && !invertButtonPressed){
			invertControls();
			invertButtonPressed = true;
		}
		else{
			invertButtonPressed = false;
		}
			
			
		/*
		 * depending on which control mode is the current
		 * it will call that modes logic method
		 */
		if(currentControlMode == DriveModes.ARCADE_DRIVE)
			arcadeDrive();
		else if(currentControlMode == DriveModes.TANK_DRIVE)
			tankDrive();
		
		/*
		 * Print Statements
		 */
		SmartDashboard.putString("Drive Mode: ", String.valueOf(currentControlMode));
		SmartDashboard.putString("Controls Inverted: ", invertedDrive == 1 ? "Gear Handler Front" : "Shooter Front");
		SmartDashboard.putNumber("Left Encoder Count: ", left1.getEncPosition());
		SmartDashboard.putNumber("Right Encoder Count: ", right1.getEncPosition());
	}
	
	/*
	 * changes the control mode of the drive to another drive mode, 
	 * hence why the method only takes in a DriveMode(the enum at the top)
	 * this sets the current control mode to the passed in control mode
	 */
	public void setDriveMode(DriveModes controlMode){
		currentControlMode = controlMode;
	}
	
	/*
	 * this is a method that can invert the controls of the robot
	 * this would be used when the robot faces towards the driver
	 */
	public void invertControls(){
		invertedDrive *= -1;
	}
	
	/*
	 * this is a method that will allow the user to change whether or not the encoders are used in the drive controls
	 * It will change properties of the SRX such as the control modes and the pid values
	 * The parameter is to tell whether or not to use the encoders in the drive
	 */
	public void useEncoderForDrive(boolean use){
		encoderDrive = use;
		
		if(use){
			left1.changeControlMode(CANTalon.TalonControlMode.Speed);
			right1.changeControlMode(CANTalon.TalonControlMode.Speed);
			
			left1.setPID(p, i, d);
			right1.setPID(p, i, d);
			
			left1.enableControl();
			right1.enableControl();
			left1.enable();
			right1.enable();
		}
		else{
			left1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			right1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			
			left1.setPID(p, i, d);
			right1.setPID(p, i, d);
			
			left1.enableControl();
			right1.enableControl();
		}
	}
	
	/*
	 * method that will automatically drive each side at a certain speed for a certain amount of time
	 * represented by the parameters
	 * currently left is getting right's speed because the robot's starting front is the gear handler
	 */
	public void autoDrive(double speedLeft, double speedRight, double seconds){
		left1.set(speedRight);
		right1.set(speedLeft);
		left2.set(left1.getDeviceID());
		right2.set(right1.getDeviceID());
		
		Timer.delay(seconds);
		
		left1.set(0);
		right1.set(0);
		left2.set(left1.getDeviceID());
		right2.set(right1.getDeviceID());
	}
	
	public void driveForRevs(int leftCount, int rightCount){
		int initialLeft = left1.getEncPosition(), initialRight = right1.getEncPosition();
		boolean leftRunning = true, rightRunning = true;
		
		while(leftRunning || rightRunning){
			if(Math.abs(left1.getEncPosition()) < leftCount + initialLeft){
				left1.set(-.3);
			}
			else{
				left1.set(0);
				leftRunning = false;
			}
			
			if(right1.getEncPosition() < rightCount + initialRight){
				right1.set(.3);
			}
			else{
				right1.set(0);
				rightRunning = false;
			}
			
			SmartDashboard.putNumber("Left Encoder Count: ", left1.getEncPosition());
			SmartDashboard.putNumber("Right Encoder Count: ", right1.getEncPosition());
		}
	}
	
	/*
	 * calculates the math for arcade drive
	 * starts off by creating 2 variables to store the to-be calculated speeds
	 * 
	 * the absolute value of the joystick's axises is taken to narrow down the amount of conditions to check
	 * this will check if the joystick is out side a theoretical deazone box on the joystick
	 * 
	 * general formula for arcade drive: 
	 * left = y_axis + x_axis
	 * right = y_axis - x_axis
	 * 
	 * however, inversion of motors, and their position and such messes with the formula
	 * 
	 * then the two first speed controllers are set to the corresponding side speed
	 * the other two follow the first of the side
	 * 
	 * the two variables are also multiplied by the scale factor (given by the slider on the drive joystick)
	 */ 
	public void arcadeDrive(){
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y)) >= deadzone || Math.abs(joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.X)) >= deadzone){ 
    		leftSpeed = (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) + (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.X) * invertedDrive)) * scaleFactor;
    		rightSpeed = (-joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) + (joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.X) * invertedDrive)) * scaleFactor;
    	}
    	
    	/*
    	 * the ? : is an in line if statement
    	 * boolean == true ? this executes if the boolean is true : else statement
    	 * this will see if the drive train is to be controlled by rpm with encoders
    	 * or just control by percentage
    	 */
    	left1.set((encoderDrive) ? leftSpeed * maxRPM * invertedDrive: leftSpeed * invertedDrive); // TODO, get the top speed of the motors on the drive train
    	right1.set((encoderDrive) ? rightSpeed * maxRPM * invertedDrive: rightSpeed * invertedDrive);
    	left2.set(left1.getDeviceID());
    	right2.set(right1.getDeviceID());
	}
	
	/*
	 * calculates the math for tank drive
	 * starts off by creating 2 variables to store the to-be calculated speeds
	 * 
	 * the absolute value of the joysticks axises is taken to narrow down the amount of conditions to check
	 * this will check if the joystick is out side a theoretical deazone box on the joystick
	 * 
	 * then the two first speed controllers are set to the corresponding side speeds
	 * the other two follow the first of the side
	 * 
	 * the two variables are also multiplied by the scale factor (given by the slider on the drive joystick)
	 * 
	 * This also contains logic for a Xbox controller, but it's untested, may or may not work
	 */ 
	public void tankDrive(){
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	/*
    	 * if statements to check what type of controller is in use
    	 */
    	if(controlWithXboxController){
    		if(Math.abs(xboxController.getRawAxis(1)) >= deadzone){
	        	leftSpeed = xboxController.getRawAxis(1);
	    	}
	    	
	    	if(Math.abs(xboxController.getRawAxis(5)) >= deadzone){
	    		rightSpeed = -xboxController.getRawAxis(5);
	    	}
	    	
	    	left1.set((encoderDrive) ? leftSpeed * maxRPM * invertedDrive: leftSpeed * invertedDrive); // TODO, get the top speed of the motors on the drive train
	    	right1.set((encoderDrive) ? rightSpeed * maxRPM * invertedDrive: rightSpeed * invertedDrive);
	    	left2.set(left1.getDeviceID());
	    	right2.set(right1.getDeviceID());
    	}
    	else{
	    	if(Math.abs(joyStickLeft.getValue(JoystickMappings.LogitechAttack3_Axis.Y)) >= deadzone){
	        	leftSpeed = joyStickLeft.getValue(JoystickMappings.LogitechAttack3_Axis.Y) * scaleFactor;
	    	}
	    	
	    	if(Math.abs(joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y)) >= deadzone){
	    		rightSpeed = -joyStickRight.getValue(JoystickMappings.LogitechAttack3_Axis.Y) * scaleFactor;
	    	}
	    	
	    	/*
	    	 * see above
	    	 */
	    	left1.set((encoderDrive) ? leftSpeed * maxRPM * invertedDrive: leftSpeed * invertedDrive); // TODO, get the top speed of the motors on the drive train
	    	right1.set((encoderDrive) ? rightSpeed * maxRPM * invertedDrive: rightSpeed * invertedDrive);
	    	left2.set(left1.getDeviceID());
	    	right2.set(right1.getDeviceID());
		}
    }
}
