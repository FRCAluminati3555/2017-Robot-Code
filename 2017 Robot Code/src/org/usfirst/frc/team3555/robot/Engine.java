package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.Input.ExponentialJoystick;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;
import org.usfirst.frc.team3555.robot.Subsystems.Climber;
import org.usfirst.frc.team3555.robot.Subsystems.DriveTrain;
import org.usfirst.frc.team3555.robot.Subsystems.GearLoader;
import org.usfirst.frc.team3555.robot.Subsystems.PneumaticGearHandler;
import org.usfirst.frc.team3555.robot.Vision.CameraSwitch;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

/**
 * <h1>Engine</h1>
 * 
 * 
 * 
 * @author Sam
 */
public class Engine {
	/** 
	 * The deadzone represents a percentage of area, on the joy stick, that won't be sent as data to the motor controller. <br>
	 * This is because when you let go of the joy stick it doesn't always land perfectly back at 0,0. <br>
	 * Therefore, a percentage is allocated to represent a zone that won't be used such that the motors do not get small 
	 * 		instructions that don't make the motor move, but rather just grind the motor up. <br>
	 */
	private final double DEADZONE = .02;
	
	/**
	 * Input objects for the joy sticks
	 * 
	 * @see org.usfirst.frc.team3555.robot.Input
	 */
	private LinearJoystick joyOP = new LinearJoystick(0, DEADZONE); 
	private ExponentialJoystick joyLeft = new ExponentialJoystick(1, DEADZONE), joyRight = new ExponentialJoystick(2, DEADZONE);
	private Joystick buttons = new Joystick(3);

	/**
	 * Sub System objects
	 * 
	 * @see org.usfirst.frc.team3555.robot.Subsystems
	 */
//	private CameraSwitch cameraSwtich;
	
	private DriveTrain drive;
	private PneumaticGearHandler pneummaticGearHandler;
	private GearLoader gearLoader;
//	private GearHandler gearHandler;
//	private BallLoader ballLoader;
//	private Shooter shooter;
	private Climber climber;
	
	/** 
	 * Constructs an object of the Engine class
	 * Initializes all the sub systems
	 */
	public Engine() {
		joyOP.updateButtons();
		joyLeft.updateButtons();
		joyRight.updateButtons();
		
//		cameraSwtich = new CameraSwitch(buttons);
		CameraServer.getInstance().startAutomaticCapture();
		
		drive = new DriveTrain(joyLeft, joyRight);
		pneummaticGearHandler = new PneumaticGearHandler(joyOP);
		gearLoader = new GearLoader(joyOP);
//		gearHandler = new GearHandler(joyOP);
//		ballLoader = new BallLoader(joyOP);
//		shooter = new Shooter(joyOP);
		climber = new Climber(joyOP);
	}
	
	public void update() {
		drive.update();
		pneummaticGearHandler.update();
		gearLoader.update();
//		gearHandler.update();
//		ballLoader.update();
//		shooter.update();
		climber.update();
	}

	/** Getters to the sub system objects */
	public DriveTrain getDrive(){return drive;}
//	public PneumaticGearHandler getPneumaticGearHandler() { return pneummaticGearHandler; }
//	public GearHandler getGearHandler(){return gearHandler;}
//	public BallLoader getLoader(){return ballLoader;}
	public GearLoader getGearLoader() { return gearLoader; }
//	public CameraSwitch getCameraSwtich(){return cameraSwtich;}
	public Climber getClimber(){return climber;}
}
