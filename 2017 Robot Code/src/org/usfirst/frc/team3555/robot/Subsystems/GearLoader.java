package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Talon;

public class GearLoader implements SubSystem {
	private LinearJoystick joyOP;
//	private CANTalon loaderTalon;
	private Talon loaderTalon;
	
//	private boolean running;
	
	public GearLoader(LinearJoystick joyOP) {
		this.joyOP = joyOP;
		
		loaderTalon = new Talon(0);
		
//		loaderTalon.changeControlMode(CANTalon.TalonControlMode.Current);
//		loaderTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		loaderTalon.set(0);
		
//		loaderTalon.enableControl();
	}
	
	@Override
	public void update() {
		if(joyOP.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Top_Lower_Right)) 
			loaderTalon.set(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y));
		else 
			loaderTalon.set(0);
		
//		if(joyOP.wasButtonJustReleased(JoystickMappings.LogitechExtreme3D_Button.Top_Lower_Right)) {
//			running = !running;
//			
//			if(running)
//				loaderTalon.set(1);
//			else
//				loaderTalon.set(0);
//		}
	}
}
