package org.usfirst.frc.team3555.robot.Subsystems;

import org.usfirst.frc.team3555.robot.Input.JoystickMappings;
import org.usfirst.frc.team3555.robot.Input.LinearJoystick;

import com.ctre.CANTalon;

/**
 * <h1>The Climber</h1>
 * 
 * This class represents the winch system that is used to climb the rope at the end of the match
 * 
 * @author Sam Secondo
 */
public class Climber implements SubSystem{
	private CANTalon climberCANTalon;
	private CANTalon climberCANTalon2;
	
	private LinearJoystick joystck;
	
	/**
	 * Constructs an object of the Climber class <br>
	 * Initializes the CANTalons
	 * 
	 * @param joyOP - Operator Joy Stick for controlling the climber 
	 */
	public Climber(LinearJoystick joyOP){
		this.joystck = joyOP;
		
		climberCANTalon = new CANTalon(46);
		climberCANTalon2 = new CANTalon(47);
		
		climberCANTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		climberCANTalon2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		climberCANTalon.set(0);
		climberCANTalon2.set(0);
	}

	/**
	 * Checks to see if the operator joy stick is outside the dead zone and that the top lower left button on the joy stick is pressed. <br>
	 * The button is checked to ensure that the operator is intending to move the climber. <br>
	 * Unlike other joy stick inputs, this does not take the absolute value of the joy stick input because the climber only goes one direction. <br>
	 * <p>
	 * If the input is below the dead zone or the button is not pressed then the CANTAlon will set itself to 0.
	 */
	public void update() {
		if(joystck.isButtonPressed(JoystickMappings.LogitechExtreme3D_Button.Top_Lower_Left)) { 
			climberCANTalon.set(joystck.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y));
			climberCANTalon2.set(joystck.getValue(JoystickMappings.LogitechExtreme3D_Axis.Y));
		} else {
			climberCANTalon.set(0);
			climberCANTalon2.set(0);
		}
	}
}
