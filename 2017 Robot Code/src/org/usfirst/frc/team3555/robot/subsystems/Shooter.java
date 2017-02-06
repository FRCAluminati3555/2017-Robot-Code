package org.usfirst.frc.team3555.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class Shooter implements SubSystem{
	/*
	 * fields for the shooter components so that the whole class can use them
	 */
	private CANTalon shooterCANTalon;
	private Joystick joyOP;
	
	/*
	 * Contructor for the Shooter class
	 * Takes in the CANTalon that the shooter will use
	 * Also takes in the joystick that will control the shooter
	 */
	public Shooter(Joystick joyOP, CANTalon shooterCANTalon){
		this.joyOP = joyOP;
		this.shooterCANTalon = shooterCANTalon;
	}
	
	/*
	 * Update method implemented by the subsystem interface
	 * need to figure out how the user will use the shooter with the joystick
	 */
	public void update(){
		
	}
}

