package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PneumaticGearHandler implements SubSystem {
	private LinearJoystick joyOP;
	
	private Compressor compressor;
	private DoubleSolenoid solenoid;
	
	private boolean buttonPressed;
	
	public PneumaticGearHandler(LinearJoystick joyOP) {
		this.joyOP = joyOP;
		
		compressor = new Compressor(0);
		compressor.setClosedLoopControl(true);
		
		solenoid = new DoubleSolenoid(0, 1);
	}
	
	@Override
	public void update() {
		
		
		if(joyOP.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Bottom_Lower_Front) && !buttonPressed) {
			buttonPressed = true;
			
			if(solenoid.get() == DoubleSolenoid.Value.kReverse) 
				solenoid.set(DoubleSolenoid.Value.kForward);
			else 
				solenoid.set(DoubleSolenoid.Value.kReverse);
		} else if(!joyOP.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Bottom_Lower_Front)) {
			buttonPressed = false;
		}
	}
}
