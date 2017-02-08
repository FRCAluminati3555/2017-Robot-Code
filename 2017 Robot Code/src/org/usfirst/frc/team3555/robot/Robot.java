/*
 * 2017 Robot Code
 * FRC Team 3555, Aluminati
 * Programmed by, Sam Secondo
 */

package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3555.robot.subsystems.DriveTrain.DriveModes;
import org.usfirst.frc.team3555.robot.subsystems.GearHandler;
import org.usfirst.frc.team3555.robot.subsystems.Loader;
import org.usfirst.frc.team3555.robot.subsystems.Shooter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
	/* 
	 * Creates 3 Joystick objects, each taking the port number as a parameter
	 * this port number is the number that the driver station will read as
	 * JoyOP is the operator joystick that will be used for everything except driving
	 * the other two are for driving
	 */
	private Joystick joyOP = new Joystick(0), joyLeft = new Joystick(1), joyRight = new Joystick(2);
	
	/*
	 * These two blocks of code create 4 CANTalons, each taking a CAN Device ID number
	 * this number can be found on the RoboRIO config dashboard
	 * the spelling convention is based on the fact that the speed controllers don't have a specific 
	 * position on the robot, other than for the left side, or the right side
	 * this is because they both just go into the same gearbox
	 */
	private CANTalon driveCANTalonLeft1 = new CANTalon(0), driveCANTalonLeft2 = new CANTalon(0); // TODO get the IDs of these CANTalons
	private CANTalon driveCANTalonRight1 = new CANTalon(0), driveCANTalonRight2 = new CANTalon(0);
	
	/*
	 * this creates a CANTalon for the gear handler, also taking in a CAN Device ID number
	 */
	private CANTalon gearHandlerCANTalon = new CANTalon(0); //TODO get this device id
	
	/*
	 * this creates a CANTalon for the shooter
	 */
	private CANTalon shooterCANTalon = new CANTalon(0); //TODO get this device id
	
	/*
	 * this creates a Talon object that will be used for the loader
	 */
	private Talon loaderTalon = new Talon(0); //TODO get this PWM port
	
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
	private DriveTrain drive = new DriveTrain(.05, DriveModes.ARCADE_DRIVE, joyLeft, joyRight, 
								driveCANTalonLeft1, driveCANTalonLeft2, driveCANTalonRight1, driveCANTalonRight2, 0);
	
	/*
	 * this creates an object of the gear handler class
	 * the first parameter is a CANTalon for controlling the gate
	 */
	private GearHandler gearHandler = new GearHandler(joyOP, gearHandlerCANTalon);

	private Loader loader = new Loader(joyOP, loaderTalon);
	
	private Shooter shooter = new Shooter(joyOP, shooterCANTalon);
	
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			/*
			 * each of the subsystems have an update method that will update the subsystem by using the speed controller
			 * or keeping a position, or take in user input and such...
			 */
			drive.update();
			gearHandler.update();
			shooter.update();
			
			Timer.delay(0.005);
		}
	}
}
