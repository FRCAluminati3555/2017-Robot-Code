package org.usfirst.frc.team3555.robot.control.input;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings.Axis;

/*
 * Class From 2016 Robot Code
 * Joystick that controls with a logarithmic function rather than linear
 * Good for precision control, and makes robot control much better and smoother 
 */
public class ExponentialJoystick extends JoystickBase {
	public ExponentialJoystick(int inputIndex, double deadzone) {
		super(inputIndex, deadzone);
	}

	public double getValue(Axis axis) {
		double value = getRawValue(axis);
		return value < 0 ? -Math.pow(value, 2) : Math.pow(value, 2);
//		double log = Math.log(1 - Math.abs(value));
//		return value < 0 ? Math.max(-1, log) : Math.min(1, -log);
		
	}
}
