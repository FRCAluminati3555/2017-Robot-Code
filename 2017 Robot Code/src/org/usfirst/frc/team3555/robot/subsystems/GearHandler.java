package org.usfirst.frc.team3555.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class GearHandler implements SubSystem{
	/*
	 * CANTalon for the GearHandler
	 */
	private CANTalon gearHandlerCANTalon;
	
	private Joystick joyOP;
	
	/*
	 * An enumeration to represent the different Positions that the gear handler can be in
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
	public GearHandler(Joystick joyOP, CANTalon gearHandlerCANTalon){
		this.joyOP = joyOP;
		this.gearHandlerCANTalon = gearHandlerCANTalon;
		
		gearHandlerCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
		gearHandlerCANTalon.changeControlMode(CANTalon.TalonControlMode.Position);
		gearHandlerCANTalon.setPID(12.8, 0, 0);
		gearHandlerCANTalon.enableControl();
	}
	
	/*
	 * Method to set the position of where the CANTalon needs to go
	 * this takes in a Gear Handler position
	 * this will take the passed in position and assign the field toPosition to the
	 * corresponding value
	 */
	public void setToPosistion(GearHandlerPositions pos){
		if(pos == GearHandlerPositions.UPPER_POS){
			toPosition = 460;
		}
		else if(pos == GearHandlerPositions.MIDDLE_POS){
			toPosition = 600;
		}
		else if(pos == GearHandlerPositions.DOWN_POS){
			toPosition = 720;
		}
	}
	
	/*
	 * This is the update method from the implemented interface
	 * this wil listen for 3 buttons getting pressed, and call the setToPosition to tell where the gearHandler needs to go 
	 * this will make the CANTalon go to the position of the field
	 */
	public void update(){
		if(joyOP.getRawButton(8)){
			setToPosistion(GearHandlerPositions.UPPER_POS);
		}
		else if(joyOP.getRawButton(10)){
			setToPosistion(GearHandlerPositions.MIDDLE_POS);
		}
		else if(joyOP.getRawButton(12)){	
			setToPosistion(GearHandlerPositions.DOWN_POS);
		}
		gearHandlerCANTalon.set(toPosition);
	}
}
