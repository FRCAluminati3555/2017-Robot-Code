package org.usfirst.frc.team3555.robot.Input;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings.Axis;

/*
 * Class From 2016 Robot Code
 * This type of joystick is what the normal joystick controls like
 * i.e. the joysticks value directly corresponds to the percentage of voltage the motors get
 */
public class LinearJoystick extends JoystickBase {
	public LinearJoystick(int inputIndex, double deadzone) {
		super(inputIndex, deadzone);
	}

	public double getValue(Axis axis) {
		return getRawValue(axis);
	}
}
