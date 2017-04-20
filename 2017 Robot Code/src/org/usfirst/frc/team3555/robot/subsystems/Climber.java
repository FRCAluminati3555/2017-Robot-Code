package org.usfirst.frc.team3555.robot.subsystems;

import org.usfirst.frc.team3555.robot.control.input.JoystickMappings;
import org.usfirst.frc.team3555.robot.control.input.LinearJoystick;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;

public class Climber implements SubSystem{
	/*
	 * Operator joystick that controls the climber motor
	 * CANTalon that will control the motor
	 */
	private LinearJoystick joystck;
	private CANTalon climberCANTalon;
	
	/*
	 * joystick deadzone to limit the motor being told to move at incredibly small values that just drain battery
	 */
	private double deadzone;
	
	/*
	 * this represents how much current means that the robot is at the top
	 * if it is this value then that must mean we are at the top of the rope pushing against the plate
	 */
//	private double atTopCurrent = 4;//TODO get this value
	
	/*
	 * boolean that tells whether or not the robot is at the top of the rope
	 */
//	private boolean atTop;
	
	/*
	 * array that represents the limit switches at the top of the robot
	 * these will say whether or not we are at the top of the robot
	 */
//	private DigitalInput[] limitSwitches;
	
	/*
	 * constructor for the climber class
	 * takes in the joystick and CANTalon that control it
	 * also takes in the deadzone for the joystick
	 * the deadzone is passed in so that all the joysticks have the same deadzone
	 * this also takes in the limit switches that are on the robot
	 * the DigitalInput... means that some number of this object will be passed in
	 * that could be either just 1 limit switch, or multiple
	 * it is treated like an array, but it can be passed in as just 1 when creating the object of the climber   
	 */
	public Climber(LinearJoystick joyOP, CANTalon climberCANTalon, double deadzone, DigitalInput...digitalInputs){
		this.joystck = joyOP;
		this.climberCANTalon = climberCANTalon;
		this.deadzone = deadzone;
		
//		limitSwitches = digitalInputs;
		
		/*
		 * Sets the CANTalon to percent mode
		 */
		climberCANTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		climberCANTalon.enableControl();
	}

	/*
	 * this is the update method implemented by the subsystem interface
	 * this will check if the joystick is outside the deadzone
	 * if so, it will spin the motor at that percentage, all the way forward is 1
	 * and all the way back is -1, and the middle is 0
	 * However, to do this a button must be pressed while moveing the joystick 
	 * axis 1 is the y axis (in a 2D perspective of a joystick)
	 * this will check that the trigger is being pressed as well to prevent the climber from being activated
	 * accidently
	 * 
	 * this will also check whether or not the output current of the CANTalon is greater than or equal to
	 * the current that means we're at the top, however
	 * at least one of the limit switches must also be in a closed state to make the robot stop
	 * that way the two truths keep each other in check, meaning that if a limit switch is pressed by something
	 * other than the plate, nothing will happen
	 * also, if the limit switch is on, and the current isn't at the top, that mean it is accidently getting pressed
	 * or we're touching the plate, but haven't yet pushed the plate all the way up
	 */
	public void update() {
//		if(climberCANTalon.getOutputCurrent() >= atTopCurrent){
//			for(int i = 0; i < limitSwitches.length; i++){ // steps through the array of limitswitches
//				if(limitSwitches[i].get()){//TODO check how the switches are wired for value of pressed
//					atTop = true;
//					climberCANTalon.set(0);
//					break;
//				}
//			}
//		}
		
		if(joystck.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y) > deadzone && joystck.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Top_Lower_Left)){// && atTop == false){
			climberCANTalon.set(joystck.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y));
		}
		else{
			climberCANTalon.set(0);
		}
	}
}
