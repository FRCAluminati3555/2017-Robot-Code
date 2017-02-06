package org.usfirst.frc.team3555.robot.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;

public class Climber implements SubSystem{
	/*
	 * Fields for the components that control the climber
	 */
	private Joystick joyOP;
	private CANJaguar climberCANJaguar;
	
	/*
	 * constructor for the climber class
	 * takes in the joystick and CANJaguar that control it
	 */
	public Climber(Joystick joyOP, CANJaguar climberCANJaguar){
		this.joyOP = joyOP;
		this.climberCANJaguar = climberCANJaguar;
	}

	public void update() {
		
	}
}
