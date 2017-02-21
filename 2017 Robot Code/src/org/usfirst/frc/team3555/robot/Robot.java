/*
 * 2017 Robot Code
 * FRC Team 3555, Aluminati
 * Programmed by, Sam Secondo
 */

package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.subsystems.Climber;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain.DriveModes;
import org.usfirst.frc.team3555.robot.subsystems.GearHandler;
import org.usfirst.frc.team3555.robot.subsystems.Loader;
import org.usfirst.frc.team3555.robot.subsystems.Shooter;
import org.usfirst.frc.team3555.robot.vision.CameraSwitch;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends SampleRobot {
	/*
	 * represents the deadzone on all joysticks, so that the motors won't be told to move at 
	 * minute speeds that just drain the motor, and aren't enough to actually move the motor
	 */
	private final double DEADZONE = .08;
	
	/* 
	 * Creates 3 Joystick objects, each taking the port number as a parameter
	 * this port number is the number that the driver station will read as
	 * JoyOP is the operator joystick that will be used for everything except driving
	 * the other two are for driving
	 */
	private Joystick joyOP = new Joystick(0), joyLeft = new Joystick(1), joyRight = new Joystick(2);
	
	/*
	 * this creates a CANTalon for the shooter
	 */
	private CANTalon shooterCANTalon = new CANTalon(0); //TODO get this device id
	/*
	 * These two blocks of code create 4 CANTalons, each taking a CAN Device ID number
	 * this number can be found on the RoboRIO config dashboard
	 * the spelling convention is based on the fact that the speed controllers don't have a specific 
	 * position on the robot, other than for the left side, or the right side
	 * this is because they both just go into the same gearbox
	 * the 2s follow the 1s
	 */
//	private CANTalon driveCANTalonLeft1 = new CANTalon(43), driveCANTalonLeft2 = new CANTalon(41); 
//	private CANTalon driveCANTalonRight1 = new CANTalon(44), driveCANTalonRight2 = new CANTalon(42);
	
	/*
	 * this creates a CANTalon for the gear handler, also taking in a CAN Device ID number
	 */
//	private CANTalon gearHandlerCANTalon = new CANTalon(45); //TODO get this device id
//	
//	private CANTalon climberCANTalon = new CANTalon(47);
	
	/*
	 * this creates a Talon object that will be used for the loader
	 * This and the servo below take in a pwm channel to talk to
	 */
//	private Talon loaderTalon = new Talon(0); 
	
	/*
	 * the servo that prevents the balls from reaching the shooter
	 */
	private Servo shooterFeader = new Servo(1); 
	
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
//	private DriveTrain drive = new DriveTrain(DEADZONE, DriveModes.ARCADE_DRIVE, joyLeft, joyRight, 
//								driveCANTalonLeft1, driveCANTalonLeft2, driveCANTalonRight1, driveCANTalonRight2);
	
	/*
	 * this creates an object of the gear handler class
	 * the first parameter is a CANTalon for controlling the gate
	 */
//	private GearHandler gearHandler = new GearHandler(joyOP, gearHandlerCANTalon);

//	private Loader loader = new Loader(joyOP, loaderTalon);
	
	private Shooter shooter = new Shooter(joyRight, shooterCANTalon,shooterFeader);
	
//	private Climber climber = new Climber(joyOP, climberCANTalon, DEADZONE);
	 
	private CameraSwitch cs;
	
	public void test(){
		LiveWindow.run();
//		shooter.update();
	}
	
	/*
	 * constructor for the robot, does all of the initialization
	 */
	public Robot(){
//		cs = new CameraSwitch(joyRight);
//		drive.setDriveMode(DriveModes.ARCADE_DRIVE);
//		drive.useEncoderForDrive(true);
	}
	
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			/*
			 * each of the subsystems have an update method that will update the subsystem by using the speed controller
			 * or keeping a position, or take in user input and such...
			 */
//			drive.update();
//			gearHandler.update();
			shooter.update();
			
			
			Timer.delay(0.005);
		}
	}
}
