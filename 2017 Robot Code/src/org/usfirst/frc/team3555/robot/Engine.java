package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.control.input.ExponentialJoystick;
import org.usfirst.frc.team3555.robot.control.input.LinearJoystick;
import org.usfirst.frc.team3555.robot.subsystems.Climber;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain.DriveModes;
import org.usfirst.frc.team3555.robot.subsystems.GearHandler;
import org.usfirst.frc.team3555.robot.subsystems.Loader;
import org.usfirst.frc.team3555.robot.subsystems.Shooter;
import org.usfirst.frc.team3555.robot.vision.CameraSwitch;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;

/*
 * The function of this class is to hold all of the references to the objects
 * Rather than have the main class(Robot.Java) have everything
 * This make the main class easier to look at, and cleans it up
 */
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
	 * 
	 * The Linear and Exponential Joysticks are just 2 different ways that the joysticks can get controlled
	 * See joystick classes for more info
	 * The difference is that the input of the joysticks will go through a function that will make the output more friendly to the driver
	 * For example, in linear the graph of input to output is just a straight line
	 * But the exponential joystick has a lower slop so that the robot is less sensitive to little movements
	 */
	private LinearJoystick joyOP = new LinearJoystick(0, DEADZONE); 
	private ExponentialJoystick joyLeft = new ExponentialJoystick(1, DEADZONE), joyRight = new ExponentialJoystick(2, DEADZONE);
	private Joystick buttons = new Joystick(3);
	
	/*
	 * Controller object that will look at an xbox controller (other controllers like the xbox controller will work as well, like a ps3 controller)
	 */
	private XboxController controller = new XboxController(3);
	
	private CANTalon shooterCANTalon = new CANTalon(47); 
	
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
	
	private CANTalon gearHandlerCANTalon = new CANTalon(45); 
	private CANTalon climberCANTalon = new CANTalon(46);
	
	/*
	 * this creates a Talon object that will be used for the loader
	 * This and the servo below take in a pwm channel to talk to
	 */
	private Talon loaderTalon = new Talon(0); 
	
	/*
	 * the servo that swished the ammo around in the robot
	 */
	private Servo shooterSwisher = new Servo(1); 
	
	/*
	 * this creates an object of the drive train class 
	 * the first parameter is the deadzone of the joysticks in the drive train
	 * When someone lets go of the joy stick, the joy stick doesn't end up at perfectly 0
	 * If the joy stick doesn't end up at 0, then the motors get instructions to set speed at, for example, .05.
	 * Over time, sending it this instruction breaks the motor, and draws power.
	 * The deadzone is the area of the joy stick that the joy stick won't send the speed controller instructions.
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
	 * it also takes in the talon that controlk
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
	 
	private CameraSwitch cameraSwtich = new CameraSwitch(buttons);
	
	public Engine(){
		/*
		 * starts up the camera switching thread
		 * object name not neccasary because it is not refrenced anywhere else 
		 */
//		new CameraSwitch(joyRight);
		
		/*
		 * starts up the usb camera on port 1
		 */
//		CameraServer.getInstance().startAutomaticCapture();
		
		drive.setDriveMode(DriveModes.ARCADE_DRIVE);
		drive.useEncoderForDrive(false);
	}

	/* 
	 * Acessors to get a reference to a subsystem object
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

	public CameraSwitch getCameraSwtich() {
		return cameraSwtich;
	}

	public Shooter getShooter() {
		return shooter;
	}

	public Climber getClimber() {
		return climber;
	}
}
