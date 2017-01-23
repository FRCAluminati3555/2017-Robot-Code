package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class MotorGroup {
	private SpeedController speedController1;
	private SpeedController speedController2;

	public MotorGroup(SpeedController speedController1, SpeedController speedController2){
		this.speedController1 = speedController1;
		this.speedController2 = speedController2;
	}

	public void set(double speed){
		speedController1.set(speed);
		speedController2.set(speed);
	}
	
	public SpeedController get(){
		return speedController1;
	}
}
