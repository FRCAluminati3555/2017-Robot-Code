package org.usfirst.frc.team3555.robot.subsystems;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings;
import org.usfirst.frc.team3555.robot.control.input.LinearJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Loader implements SubSystem{
	/*
	 * field for the components that will control the loader
	 */
	private Talon loaderTalon;
	private LinearJoystick joyOP;
	
	private double loaderDeadzone;
	
	/*
	 * contructor for the Loader class
	 * takes in a Joystick that will control the loader
	 * takes in a talon the control the loader
	 */
	public Loader(LinearJoystick joyOP, Talon loaderTalon){
		this.joyOP = joyOP;
		this.loaderTalon = loaderTalon;
		
		loaderDeadzone = .85;
	}

	/*
	 * update method implemented by the SubSystem interface
	 * checks to see if the slider is passed the loading deadzone
	 * if so, then the motor will spin at the slider's percentage
	 * 1 being the top, and -1 being the bottom
	 */
	public void update() {
		if(Math.abs(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider)) > loaderDeadzone){
			loaderTalon.set(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider) * -1);
		}
		else{
			loaderTalon.set(0);
		}
		SmartDashboard.putNumber("Loader: ", joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider));
	}
}
