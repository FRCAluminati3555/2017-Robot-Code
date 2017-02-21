package org.usfirst.frc.team3555.robot.subsystems;

import java.io.IOException;
import java.io.PrintWriter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements SubSystem{
	/*
	 * The CANTalon used for the shooter
	 * JoyOP is the operator joystick
	 */
	private CANTalon shooterCANTalon;
	private Joystick joyOP;
	
	/*
	 * This is the servo that will either let the balls throughto the shooter
	 * or block them from going in
	 */
	private Servo feader;
	
	/*
	 * These variables represent the positions of the servo for either blocking or letting balls into the shooter
	 */
	private int feaderPositionUp = 255, feaderPositionDown = 0;//TODO get real values
	
	/*
	 * these represent the rpm that is needed for the each type of shot
	 */
	private double rpmHighGoal = 1000, rpmLowGoal = 500;//TODO get real values
	
	/*
	 * Constructor for the Shooter class
	 * Takes in the CANTalon that the shooter will use
	 * Also takes in the joystick that will control the shooter
	 * This will also take in the Servo that will either block or let balls into the shooter
	 */
	public Shooter(Joystick joyOP, CANTalon shooterCANTalon, Servo feader){
		this.joyOP = joyOP;
		this.shooterCANTalon = shooterCANTalon;
		
		this.feader = feader;
		
		/*
		 * general init of a CANTalon
		 * this will change the CANTalon to speed mode, make it's feedback device a quad encoder
		 * set PID values
		 * set the amount of encoder pulses per revolution of the motor (used to keep track of revolutions)
		 * enables the closed loop to begin
		 */
//		shooterCANTalon.reset();
//		shooterCANTalon.setPosition(0);
//		shooterCANTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
//		shooterCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//		shooterCANTalon.configEncoderCodesPerRev(500);
//		shooterCANTalon.configNominalOutputVoltage(+0.0f, -0.0f);
//        shooterCANTalon.configPeakOutputVoltage(+12.0f, -12.0f);
//		shooterCANTalon.setF(0);
//		shooterCANTalon.setPID(.5, 0, 0); 
//		shooterCANTalon.reverseSensor(true);
//		shooterCANTalon.enableControl();
//		shooterCANTalon.set(1000);
		
		shooterCANTalon.reset();
		shooterCANTalon.setPosition(0);
		shooterCANTalon.changeControlMode(CANTalon.TalonControlMode.Voltage);
		shooterCANTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		shooterCANTalon.configEncoderCodesPerRev(500);
		shooterCANTalon.configNominalOutputVoltage(+0.0f, -0.0f);
        shooterCANTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		shooterCANTalon.setF(0);
		shooterCANTalon.setPID(0, 0, 0); 
		shooterCANTalon.reverseSensor(true);
		shooterCANTalon.enableControl();
		shooterCANTalon.set(5);
		
//		shooterCANTalon.changeControlMode(CANTalon.TalonControlMode.Speed);
//		shooterCANTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//		shooterCANTalon.reverseSensor(true);
//		shooterCANTalon.setEncPosition(0);
//		shooterCANTalon.setF(0);
//		shooterCANTalon.setPID(2.5,0,5);//TODO test for these values!
//		shooterCANTalon.configEncoderCodesPerRev(500);//TODO get this value
//		shooterCANTalon.enableControl();
	}
	
	/*
	 * Update method implemented by the subsystem interface
	 * This will listen for the user pressing either buttons to shoot for the high goal or the low goal
	 */
	public void update(){
//		if(joyOP.getRawButton(10)){
//			shootWithRPM(rpmHighGoal);
//		}
//		else if(joyOP.getRawButton(11)){
//			shootWithRPM(rpmLowGoal);
//		}
//		else{
//			shooterCANTalon.set(0);
//			feader.setRaw(feaderPositionDown);
//		}
//		shooterCANTalon.set(1);
//		if(Math.abs(joyOP.getRawAxis(1)) > .1)
//			shooterCANTalon.set(joyOP.getRawAxis(1));
//		else
//			shooterCANTalon.set(5);
		SmartDashboard.putNumber("Shooter Speed (rpm): ", shooterCANTalon.getSpeed());
		SmartDashboard.putNumber("Enc Values: ", shooterCANTalon.getEncPosition());
		SmartDashboard.putNumber("Talon Pos: ", shooterCANTalon.getPosition());
		SmartDashboard.putNumber("Talon Current: ", shooterCANTalon.getOutputCurrent());
		SmartDashboard.putNumber("Target: ", shooterCANTalon.getSetpoint());
		SmartDashboard.putNumber("Talon Voltage: ", shooterCANTalon.getOutputVoltage());
//		SmartDashboard.putNumber(key, value)
		
	}

	/*
	 * method that will set the rpm of the shooter
	 * and blocks the balls from entering the shooter
	 */
	public void setRPM(double rpm){
		feader.setRaw(feaderPositionDown);
		shooterCANTalon.set(rpm);
	}
	
	/*
	 * method that will spin up the motor to the given rpm
	 * once it is at the rpm, the servo will let the balls through, and they'll start to shoot
	 */
	public void shootWithRPM(double rpm){
		setRPM(rpm);
		
		if(shooterCANTalon.getSpeed() > rpm-10){
			feader.setRaw(feaderPositionUp);
		}
	}
}

