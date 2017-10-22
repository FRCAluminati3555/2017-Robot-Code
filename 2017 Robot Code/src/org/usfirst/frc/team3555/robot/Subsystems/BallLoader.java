package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BallLoader implements SubSystem{
	private LinearJoystick joyOP;
	private Talon loaderTalon;
	
	private double loaderDeadzone;
	
	public BallLoader(LinearJoystick joyOP){
		this.joyOP = joyOP;
		loaderTalon = new Talon(0); 
		
		loaderDeadzone = .85;
	}

	public void update() {
		if(Math.abs(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider)) > loaderDeadzone)
			loaderTalon.set(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider) * -1);
		else
			loaderTalon.set(0);

		SmartDashboard.putNumber("Loader: ", joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Slider));
	}
}
