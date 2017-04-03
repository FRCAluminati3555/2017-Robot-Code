package org.usfirst.frc.team3555.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Loader implements SubSystem{
	/*
	 * field for the components that will control the loader
	 */
	private Talon loaderTalon;
	private Joystick joyOP;
	
	private double loaderDeadzone;
	
	/*
	 * contructor for the Loader class
	 * takes in a Joystick that will control the loader
	 * takes in a talon the control the loader
	 */
	public Loader(Joystick joyOP, Talon loaderTalon){
		this.joyOP = joyOP;
		this.loaderTalon = loaderTalon;
		
		loaderDeadzone = .9;
	}

	/*
	 * update method implemented by the SubSystem interface
	 * checks to see if the slider is passed the loading deadzone
	 * if so, then the motor will spin at the slider's percentage
	 * 1 being the top, and -1 being the bottom
	 */
	public void update() {
		if(joyOP.getRawAxis(3) < -loaderDeadzone){
			loaderTalon.set(joyOP.getRawAxis(3) * -1);
		}
		else{
			loaderTalon.set(joyOP.getRawAxis(3));
		}
		SmartDashboard.putNumber("JoyOP Slider", joyOP.getRawAxis(3));
	}
}
