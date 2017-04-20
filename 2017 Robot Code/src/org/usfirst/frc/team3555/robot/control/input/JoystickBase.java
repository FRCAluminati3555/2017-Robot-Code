package org.usfirst.frc.team3555.robot.control.input;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings.Axis;
import org.usfirst.frc.team3555.robot.control.input.JoystickMappings.Button;

import edu.wpi.first.wpilibj.Joystick;

/*
 * Class From 2016 Robot Code
 * This abstract class is used to standardize the different types of joysticks that could be made
 * For example the LinearJoystick and LogarithmicJoystick
 */
public abstract class JoystickBase {
	private Joystick joystick;
	private double deadzone;
	
	public JoystickBase(int inputIndex, double deadzone) {
		joystick = new Joystick(inputIndex);
		this.deadzone = deadzone;
	}
	
	public abstract double getValue(Axis axis);
	
	public final double getRawValue(Axis axis) { 
		double rawValue = joystick.getRawAxis(axis.getIndex());
		return Math.abs(rawValue) < deadzone ? 0 : rawValue;
	}
	
	public boolean isButtonPressed(Button button) { 
		return joystick.getRawButton(button.getIndex());
	}

	public boolean isPOVPressed(int angle) { return joystick.getPOV() == angle; }
	public int getPOV() { return joystick.getPOV(); }
	public Joystick getJoystick() { return joystick; }
}
