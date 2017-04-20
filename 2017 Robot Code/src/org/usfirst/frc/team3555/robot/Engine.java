package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.control.input.LinearJoystick;
import org.usfirst.frc.team3555.robot.control.input.ExponentialJoystick;
import org.usfirst.frc.team3555.robot.subsystems.*;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain.DriveModes;
import org.usfirst.frc.team3555.robot.vision.CameraSwitch;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;

public class Engine {
	
	/*
	 * When someone lets go of the joystick, it does not always come back to excatly 0
	 * Meaning that the Rio will send the speed controller a very small amount of power, and that's not good
	 * So, the deadzone is a theoretical box (looking at it in a 2D manner) that the 
	 */
	private final double DEADZONE = .02;
	
	/* 
	 * Creates 3 Joystick objects, each taking the port number as a parameter
	 * this port number is the number that the driver station will read as
	 * JoyOP is the operator joystick that will be used for everything except driving
	 * the other two are for driving
	 */
	private LinearJoystick joyOP = new LinearJoystick(0, DEADZONE); 
	private ExponentialJoystick joyLeft = new ExponentialJoystick(1, DEADZONE), joyRight = new ExponentialJoystick(2, DEADZONE);
	
	/*
	 * Controller object that will look at an xbox controller (other controllers like the xbox controller will work as well, like a ps3 controller)
	 */
	private XboxController controller = new XboxController(3);
	
	/*
	 * this creates a CANTalon for the shooter
	 */
	private CANTalon shooterCANTalon = new CANTalon(47); //TODO get this device id
	
	/*
	 * These two blocks of code create 4 CANTalons, each taking a CAN Device ID number
	 * this number can be found on the RoboRIO config dashboard
	 * the spelling convention is based on the fact that the speed controllers don't have a specific 
	 * position on the robot, other than for the left side, or the right side
	 * this is because they both just go into the same gearbox
	 * the 2s follow the 1s
	 */
	private CANTalon driveCANTalonLeft1 = new CANTalon(43), driveCANTalonLeft2 = new CANTalon(41); 
	private CANTalon driveCANTalonRight1 = new CANTalon(44), driveCANTalonRight2 = new CANTalon(42);
	
	/*
	 * this creates a CANTalon for the gear handler, also taking in a CAN Device ID number
	 */
	private CANTalon gearHandlerCANTalon = new CANTalon(45); 
	
	private CANTalon climberCANTalon = new CANTalon(46);
	
	/*
	 * this creates a Talon object that will be used for the loader
	 * This and the servo below take in a pwm channel to talk to
	 */
	private Talon loaderTalon = new Talon(0); 
	
	/*
	 * the servo that prevents the balls from reaching the shooter
	 */
	private Servo shooterSwisher = new Servo(1); 
	
	/*
	 * this creates an object of the drive train class 
	 * the first parameter is the deadzone of the joysticks in the drive train
	 * When someone lets go of the joy stick, the joy stick doesn't end up at perfectly 0
	 * If the joy stick doesn't end up at 0, then the motors get instructions to set speed at, for example, .05.
	 * Over time, sending it this instruction breaks the motor, and draws power.
	 * The dead zone is the area of the joy stick that the joy stick won't send the speed controller instructions.
	 * 
	 * The second parameter is the starting state of the drive train
	 * this is a static enumeration in the drive train class that is used to switch between different modes of drive controls
	 * 
	 * The next two parameters are the joysticks that the object will use to control the robot
	 * 
	 * the next 4 are the speed controllers that the robot will use for the robot
	 * in this case, the 4 made as a field are passed in
	 */
	private DriveTrain drive = new DriveTrain(DEADZONE, DriveModes.ARCADE_DRIVE, joyLeft, joyRight, controller,
								driveCANTalonLeft1, driveCANTalonLeft2, driveCANTalonRight1, driveCANTalonRight2);
	
	/*
	 * Creates an object that represents the gear handler
	 * takes in a joystick (operator joystick), and a talon srx to control the gear handler
	 * No deadzone because this is based on buttons not an axis
	 */
	private GearHandler gearHandler = new GearHandler(joyOP, gearHandlerCANTalon);

	/*
	 * Creates an object that represents the loader
	 * takes in a joystick (operator joystick), and a talon sr to control the loader
	 * Deadzone is not taken in because this deadzone will be different from the other deadzone
	 * because it is on the slider of the operator joystick
	 * so the deadzone is only inside this class
	 */
	private Loader loader = new Loader(joyOP, loaderTalon);
	
	/*
	 * Creates an object that represents the shooter
	 * takes in a joystick (operator joystick), and a talon srx to control the shooter
	 * also a servo to move the balls around the storage area
	 * and the deadzone of the joystick
	 */
	private Shooter shooter = new Shooter(joyOP, shooterCANTalon, shooterSwisher, DEADZONE);
	
	/*
	 * Creates an object that represents the loader
	 * takes in a joystick (operator joystick), and a talon sr to control the loader
	 * also the deadzone of the joystick
	 */
	private Climber climber = new Climber(joyOP, climberCANTalon, DEADZONE);
	 
	public Engine(){
		/*
		 * starts up the camera switching thread
		 * object name not neccasary because it is not refrenced anywhere else 
		 */
//		new CameraSwitch(joyRight);
		
		/*
		 * starts up the usb camera on port 1
		 */
		CameraServer.getInstance().startAutomaticCapture();
		
		drive.setDriveMode(DriveModes.ARCADE_DRIVE);
		drive.useEncoderForDrive(false);
	}

	/* 
	 * Accessors to get a reference to a subsystem object
	 */
	public DriveTrain getDrive() {
		return drive;
	}

	public GearHandler getGearHandler() {
		return gearHandler;
	}

	public Loader getLoader() {
		return loader;
	}

	public Shooter getShooter() {
		return shooter;
	}

	public Climber getClimber() {
		return climber;
	}
}
