package org.usfirst.frc.team3555.robot.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;

public class Climber implements SubSystem{
	/*
	 * Fields for the components that control the climber
	 */
	private Joystick joyOP;
	private CANJaguar climberCANJaguar;
	private double deadzone;
	/*
	 * constructor for the climber class
	 * takes in the joystick and CANJaguar that control it
	 */
	public Climber(Joystick joyOP, CANJaguar climberCANJaguar, double deadzone){
		this.joyOP = joyOP;
		this.climberCANJaguar = climberCANJaguar;
		this.deadzone = deadzone;
	}

	/*
	 * this is the update method implemented by the subsystem interface
	 * this will check if the joystcik is outside the deadzone
	 * if so, it will spin the motor at that percentage, all the way forward is 1
	 * and all the way back is -1, and the middle is 0
	 */
	public void update() {
		if(Math.abs(joyOP.getRawAxis(1)) > deadzone){
			climberCANJaguar.set(joyOP.getRawAxis(1));
		}
	}
}
