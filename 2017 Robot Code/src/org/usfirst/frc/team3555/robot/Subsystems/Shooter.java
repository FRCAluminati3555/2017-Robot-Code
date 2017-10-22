package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements SubSystem{
	private LinearJoystick joyOP;
	private CANTalon shooterCANTalon;
	
	private double deadzone;
	
	public Shooter(LinearJoystick joyOP){
		this.joyOP = joyOP;
		
		shooterCANTalon = new CANTalon(47);
		
		shooterCANTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		shooterCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		shooterCANTalon.configEncoderCodesPerRev(500);
		shooterCANTalon.setPID(0, 0, 0); 
		shooterCANTalon.reverseSensor(true);
		shooterCANTalon.enableControl();
	}
	
	public void update(){
		if(Math.abs(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y)) >= deadzone && joyOP.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Trigger))
			shooterCANTalon.set(joyOP.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y) * -1);
		else
			shooterCANTalon.set(0);
		
		SmartDashboard.putNumber("Shooter Speed (rpm): ", shooterCANTalon.getSpeed());
		SmartDashboard.putNumber("Enc Values: ", shooterCANTalon.getEncPosition());
		SmartDashboard.putNumber("Talon Pos: ", shooterCANTalon.getPosition());
		SmartDashboard.putNumber("Talon Current: ", shooterCANTalon.getOutputCurrent());
		SmartDashboard.putNumber("Target: ", shooterCANTalon.getSetpoint());
		SmartDashboard.putNumber("Talon Voltage: ", shooterCANTalon.getOutputVoltage());
	}
}

