package org.usfirst.frc.team3555.robot.subsystems;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings;
import org.usfirst.frc.team3555.robot.control.input.LinearJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearHandler implements SubSystem{
	private CANTalon gearHandlerCANTalon;
	
	private LinearJoystick joystick;
	
	/*
	 * An enumeration to represent the different positions that the gear handler can be in
	 */
	public static enum GearHandlerPositions{
		UPPER_POS, MIDDLE_POS, DOWN_POS
	}
	
	/*
	 * Variable that represents where the gear handler is going
	 */
	private double toPosition;
	
	/*
	 * Constructor for the Gear Handler class, takes in 1 CANTalon to control, 
	 * sets the field CANTalon to the passed in talon
	 * Init of the talon
	 * sets it to have an analog potentiometer
	 * sets the talon to position mode
	 * sets the pid
	 * enables control to the CANTalon
	 */
	public GearHandler(LinearJoystick joyOP, CANTalon gearHandlerCANTalon){
		this.joystick = joyOP;
		this.gearHandlerCANTalon = gearHandlerCANTalon;
		
//		SmartDashboard.putString("Gear Handler Position: ", "Middle");
		
		gearHandlerCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		gearHandlerCANTalon.changeControlMode(CANTalon.TalonControlMode.Position);
		gearHandlerCANTalon.setPID(12.8, 0, 0);
		gearHandlerCANTalon.setInverted(false);
		gearHandlerCANTalon.enableControl();
		
//		setToPosistion(GearHandlerPositions.DOWN_POS);
	}
	
	/*
	 * Method to set the position of where the CANTalon needs to go
	 * this takes in a Gear Handler position
	 * this will take the passed in position and assign the field toPosition to the 
	 * corresponding value
	 */
	public void setToPosistion(GearHandlerPositions pos){
		if(pos == GearHandlerPositions.UPPER_POS){
			toPosition = 260;//460;
		}
		else if(pos == GearHandlerPositions.MIDDLE_POS){
			toPosition = 410;//460;
		}
		else if(pos == GearHandlerPositions.DOWN_POS){
			toPosition = 470;//450
		}
		
		if(toPosition != 0)
			gearHandlerCANTalon.set(toPosition);
	}
	
	/*
	 * This is the update method from the implemented interface
	 * this wil listen for 3 buttons getting pressed, and call the setToPosition to tell where the gearHandler needs to go 
	 * this will make the CANTalon go to the position of the field
	 */
	public void update(){
		if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_7)){
			setToPosistion(GearHandlerPositions.UPPER_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Upper");
		}
		else if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_9)){
			setToPosistion(GearHandlerPositions.MIDDLE_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Middle");
		}
		else if(joystick.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Button_11)){	
			setToPosistion(GearHandlerPositions.DOWN_POS);
			SmartDashboard.putString("Gear Handler Position: ", "Down");
		}
		if(toPosition > 200)
			gearHandlerCANTalon.set(toPosition);
		
		SmartDashboard.putNumber("Gear Handler Pot Pos: ", gearHandlerCANTalon.getPosition());
		
	}
}
