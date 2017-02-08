package org.usfirst.frc.team3555.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class DriveTrain implements SubSystem{
	/*
	 * fields that take the place of the passed in variable so the rest of the class can see them
	 */
	private Joystick joyLeft, joyRight;
	
	/*
	 * represents the deadzone on the joysticks to prevent the motors from moving at .005%
	 */
	private double deadzone;
	
	/*
	 * represents whether or not the encoders are taken into account in the drive train controls
	 * aka controlled by RPM (uses encoders) or just percentage (does not use encoders)
	 */
	private boolean encoderDrive;
	
	/*
	 * allows driver to reverse controls
	 */
	private int invertedDrive;

	/*
	 * CANTalon fields that represent the CANTalons on the drive train
	 * the naming convention is based on the fact that the two sides both have 2 speed controllers that control 1 shaft
	 */
	private CANTalon left1, left2, right1, right2;
	
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
	 * contructor for the Drive Train class
	 */
	public DriveTrain(double deadzone, DriveModes startingControlMode, Joystick joyLeft, Joystick joyRight,
			CANTalon left1, CANTalon left2, CANTalon right1, CANTalon right2, double maxRPM){
		
		/*
		 * sets the fields of the class to the passed in parameters of the constructor
		 */
		this.deadzone = deadzone;
		this.joyLeft = joyLeft;
		this.joyRight = joyRight;
		
		currentControlMode = startingControlMode;
		
		this.left1 = left1;
		this.left2 = left2;
		this.right1 = right1;
		this.right2 = right2;
		
		this.maxRPM = maxRPM;
		
		invertedDrive = 1;
		
		/**
		 * the init of the CANTalons
		 */
		
		/*
		 * starts off by setting one controller of each side to speed mode
		 * that way they are controlled by rpm rather than a percentage of power available 
		 */
		left1.changeControlMode(CANTalon.TalonControlMode.Speed);
		right1.changeControlMode(CANTalon.TalonControlMode.Speed);
		
		/*
		 * sets these controllers to have a Quad Encoder to read from
		 */
		left1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		right1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		
		/*
		 * sets the amount of pulses, from the encoder, that equates to one revolution
		 */
		left1.configEncoderCodesPerRev(10);
		right1.configEncoderCodesPerRev(10);
		
		/*
		 * sets the PID of the two speed controllers
		 * an article explaining PID: http://www.flitetest.com/articles/pid-s-explained-in-simple-terms
		 * it's basically constatns that the controller will use to either speed itself up or lower itself
		 * to keep at a constant rate or position
		 */
		left1.setPID(0, 0, 0); //TODO test for these constants!
		right1.setPID(0, 0, 0);
		
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
	}
	
	/*
	 * this is the update method specified by the subsystem interface
	 * this is the method that will check which drive mode that is chosen, 
	 * then it will do the math of the corresponding drive mode, and use that to control the robot
	 */
	public void update(){
		if(currentControlMode == DriveModes.ARCADE_DRIVE)
			arcadeDrive();
		else if(currentControlMode == DriveModes.TANK_DRIVE)
			tankDrive();
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
	public void invertControls(boolean invert){
		if(invert)
			invertedDrive = -1;
		else
			invertedDrive = 1;
	}
	
	/*
	 * this is a method that will allow the user to change whether or not the encoders are used in the drive controls
	 */
	public void useEncoderForDrive(boolean use){
		encoderDrive = use;
		
		if(use){
			left1.changeControlMode(CANTalon.TalonControlMode.Speed);
			right1.changeControlMode(CANTalon.TalonControlMode.Speed);
			
			left1.enableControl();
			right1.enableControl();
		}
		else{
			left1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			right1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			
			left1.enableControl();
			right1.enableControl();
		}
	}
	
	/*
	 * calculates the math for arcade drive
	 * starts off by creating 2 variables to store the to-be calculated speed
	 * 
	 * the absolute value of the joystick axises is taken to narrow down the amount of conditions to check
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
	 */ 
	public void arcadeDrive(){
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(joyRight.getRawAxis(1)) >= deadzone || Math.abs(joyRight.getRawAxis(0)) >= deadzone){ 
    		leftSpeed = joyRight.getRawAxis(1) - joyRight.getRawAxis(0);
    		rightSpeed = -joyRight.getRawAxis(1) - joyRight.getRawAxis(0);
    	}
    	
    	left1.set((encoderDrive) ? leftSpeed * maxRPM * invertedDrive: leftSpeed * invertedDrive); // TODO, get the top speed of the motors on the drive train
    	right1.set((encoderDrive) ? rightSpeed * maxRPM * invertedDrive: rightSpeed * invertedDrive);
    	left2.set(left1.getDeviceID());
    	right2.set(right1.getDeviceID());
	}
	
	/*
	 * tank drive starts off with two variables to store the speed value
	 * it will then check if the joysticks are outside the deadzone
	 * if they are, the corresponding value is set to that amount
	 */
	public void tankDrive(){
		double leftSpeed = 0;
    	double rightSpeed = 0;
    	
    	if(Math.abs(joyLeft.getRawAxis(1)) >= deadzone){
        	leftSpeed = joyLeft.getRawAxis(1);
    	}
    	
    	if(Math.abs(joyRight.getRawAxis(1)) >= deadzone){
    		rightSpeed = -joyRight.getRawAxis(1);
    	}
    	
    	left1.set(leftSpeed); // TODO, get the top speed of the motors on the drive train
    	right1.set(rightSpeed);
    	left2.set(left1.getDeviceID());
    	right2.set(right1.getDeviceID());
    }
}
