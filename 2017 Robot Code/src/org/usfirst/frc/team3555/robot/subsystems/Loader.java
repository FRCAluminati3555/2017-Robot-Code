package org.usfirst.frc.team3555.robot.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Loader implements SubSystem{
	/*
	 * field for the components that will control the loader
	 */
	private Talon loaderTalon;
	private Joystick joyOP;
	
	/*
	 * contructor for the Loader class
	 * takes in a Joystick that will control the loader
	 * takes in a talon the control the loader
	 */
	public Loader(Joystick joyOP, Talon loaderTalon){
		this.joyOP = joyOP;
		this.loaderTalon = loaderTalon;
	}
	
	/*
	 * update method implemented by the SubSystem interface
	 */
	public void update() {
		
	}
}
