package org.usfirst.frc.team3555.robot.Input;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings.Axis;
import org.usfirst.frc.team3555.robot.Input.JoystickMappings.Button;

import edu.wpi.first.wpilibj.Joystick;

/*
 * Class From 2016 Robot Code
 * This abstract class is used to standardize the different types of joysticks that could be made
 * For example the LinearJoystick and ExponentialJoystick 
 */
public abstract class JoystickBase {
	private Joystick joystick;
	private double deadzone;
	
	private boolean[] buttons, justPressed, cantPress;
	
	public JoystickBase(int inputIndex, double deadzone) {
		joystick = new Joystick(inputIndex);
		this.deadzone = deadzone;
		
		buttons = new boolean[joystick.getButtonCount()];
		justPressed = new boolean[buttons.length];
		cantPress = new boolean[buttons.length];
	}
	
	public abstract double getValue(Axis axis);

	/**
	 * Used to update the state of the buttons and check if a button was just released
	 * This places all the boolean logic here, instead of in a sub system
	 * Must be called every iteration throughout the operator control to keep up to date with real time
	 */
	public final void updateButtons() {
		for(int i = 0; i < buttons.length; i++) 
			buttons[i] = joystick.getRawButton(i+1);
		
		for(int i = 0;i < buttons.length; i++) {
			if(cantPress[i] && !buttons[i]) {
				cantPress[i] = false;
			} else if(justPressed[i]) {
				cantPress[i] = true;
				justPressed[i] = false;
			}
			
			if(!cantPress[i] && buttons[i]) {
				justPressed[i] = true;
			}
		}
	}
	
	public final double getRawValue(Axis axis) { 
		double rawValue = joystick.getRawAxis(axis.getIndex());
		return Math.abs(rawValue) < deadzone ? 0 : rawValue;
	}
	
	public boolean isButtonPressed(Button button) { 
		return joystick.getRawButton(button.getIndex());
	}
	
	public boolean wasButtonJustReleased(Button button) {
		return justPressed[button.getIndex()];
	}

	public boolean isPOVPressed(int angle) { return joystick.getPOV() == angle; }
	public int getPOV() { return joystick.getPOV(); }
	public Joystick getJoystick() { return joystick; }
}
