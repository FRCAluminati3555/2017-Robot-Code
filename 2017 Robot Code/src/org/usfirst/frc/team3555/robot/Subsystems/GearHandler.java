package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class Not Used
 * Gear Handler replaced with the Pneumatics edition 
 * 
 * @author Sam S.
 */
public class GearHandler implements SubSystem{
	public static enum GearHandlerPositions{
		UPPER_POS, MIDDLE_POS, DOWN_POS
	}
	
	private CANTalon gearHandlerCANTalon;
	private LinearJoystick joystick;
	
	private double toPosition;

	public GearHandler(LinearJoystick joyOP){
		this.joystick = joyOP;
		
		gearHandlerCANTalon = new CANTalon(45);
		gearHandlerCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		gearHandlerCANTalon.changeControlMode(CANTalon.TalonControlMode.Position);
		gearHandlerCANTalon.setPID(12.8, 0, 0);
		gearHandlerCANTalon.setInverted(false);
		gearHandlerCANTalon.enableControl();
	}
	
	public void setToPosistion(GearHandlerPositions pos) {
		if(pos == GearHandlerPositions.UPPER_POS) 
			toPosition = 260;
		else if(pos == GearHandlerPositions.MIDDLE_POS) 
			toPosition = 410;
		else if(pos == GearHandlerPositions.DOWN_POS) 
			toPosition = 470;
		
		gearHandlerCANTalon.set(toPosition);
	}
	
	public void update(){
		if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_7)) {
			setToPosistion(GearHandlerPositions.UPPER_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Upper");
		} else if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_9)) {
			setToPosistion(GearHandlerPositions.MIDDLE_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Middle");
		} else if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_11)) {	
			setToPosistion(GearHandlerPositions.DOWN_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Down");
		}
		
		SmartDashboard.putNumber("Gear Handler Pot Pos: ", gearHandlerCANTalon.getPosition());
	}
}
