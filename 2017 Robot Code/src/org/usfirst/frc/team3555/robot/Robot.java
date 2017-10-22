package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * <h1>FRC Team 3555 2017 Robot Code</h1>
 * 
 * {@code Robot} is the main class for the robot code. <br>
 * 
 * In the operator control loop, each of the {@link org.usfirst.frc.team3555.robot.Subsystems.SubSystem Sub Systems} update every iteration for input. <br>
 * A {@link edu.wpi.first.wpilibj.Timer Timer}  delay is also included to give the motors time to update. <br>
 * The {@link org.usfirst.frc.team3555.robot.Engine Engine} class provides the references to all the different subsystems. <br>
 * 
 * Extends {@link edu.wpi.first.wpilibj.SampleRobot Sample Robot}. 
 * 
 * @author Sam Secondo
 * 
 * @see edu.wpi.first.wpilibj.SampleRobot
 * @see edu.wpi.first.wpilibj.Timer
 * @see org.usfirst.frc.team3555.robot.Engine
 * @see org.usfirst.frc.team3555.robot.Subsystems.SubSystem
 */
public class Robot extends SampleRobot {
	
	/** Hold all the references to the sub systems */
	private Engine engine;
	
	/**
	 * Constructs an object of the {@code Robot} class. <br>
	 * Initializes the {@code Engine} object.
	 * 
	 * @see org.usfirst.frc.team3555.robot.Engine 
	 */
	public Robot() {
		engine = new Engine();
	}
	
	/** Drives the robot autonomously */
	public void autonomous() {
		engine.getDrive().autoDrive(100, 100, 6);
	}
	
	/** Updates all the sub systems */
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			engine.update();
			
			Timer.delay(0.005);
		}
	}
}