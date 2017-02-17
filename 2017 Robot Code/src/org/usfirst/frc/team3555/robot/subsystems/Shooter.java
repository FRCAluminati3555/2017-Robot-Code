package org.usfirst.frc.team3555.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;

public class Shooter implements SubSystem{
	/*
	 * fields for the shooter components so that the whole class can use them
	 */
	private CANTalon shooterCANTalon;
	private Joystick joyOP;
	
	private Servo feader;
	
	private int feaderPositionHigh = 255, feaderPositionDown = 0;
	private double rpmHighGoal = 1000, rpmLowGoal = 500;
	
	/*
	 * represents the rpm of the shooter
	 */
	private double rpm;
	
	/*
	 * Contructor for the Shooter class
	 * Takes in the CANTalon that the shooter will use
	 * Also takes in the joystick that will control the shooter
	 */
	public Shooter(Joystick joyOP, CANTalon shooterCANTalon, Servo feader){
		this.joyOP = joyOP;
		this.shooterCANTalon = shooterCANTalon;
		
		this.feader = feader;
		
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
	public void update(){}

	/*
	 * method that will set the rpm of the shooter
	 */
	public void setRPM(double rpm){
		this.rpm = rpm;
		
		feader.setRaw(feaderPositionDown);
		shooterCANTalon.set(rpm);
	}
	
	public void shoot(){
//		if(shooterCANTalon.getS)
	}
	
	/*
	 * method to get the rpm of the shooter
	 */
	public double getRPM(){
		return rpm;
	}
}

