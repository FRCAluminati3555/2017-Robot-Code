package org.usfirst.frc.team3555.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Joystick;

public class Shooter implements SubSystem{
	/*
	 * fields for the shooter components so that the whole class can use them
	 */
	private CANTalon shooterCANTalon;
	private Joystick joyOP;
	
	/*
	 * represents the rpm of the shooter
	 */
	private double rpm;
	
	/*
	 * Contructor for the Shooter class
	 * Takes in the CANTalon that the shooter will use
	 * Also takes in the joystick that will control the shooter
	 */
	public Shooter(Joystick joyOP, CANTalon shooterCANTalon){
		this.joyOP = joyOP;
		this.shooterCANTalon = shooterCANTalon;
		
		shooterCANTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
		shooterCANTalon.setPID(0, 0, 0);//TODO test for these values!
		shooterCANTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterCANTalon.configEncoderCodesPerRev(0);//TODO get this value
		shooterCANTalon.enableControl();
	}
	
	/*
	 * Update method implemented by the subsystem interface
	 * need to figure out how the user will use the shooter
	 */
	public void update(){
		shooterCANTalon.set(rpm);
	}

	/*
	 * method that will set the rpm of the shooter
	 */
	public void setRPM(double rpm){
		this.rpm = rpm;
	}
	
	/*
	 * method to get the rpm of the shooter
	 */
	public double getRPM(){
		return rpm;
	}
}

