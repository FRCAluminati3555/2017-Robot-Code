package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/*
 * Reserved project for random testing
 */
public class Robot extends SampleRobot {
//	private Talon talon = new Talon(7);
	private CANTalon talonLeft1 = new CANTalon(43);//43 41
	private CANTalon talonLeft2 = new CANTalon(41);
	private CANTalon talonRight1 = new CANTalon(44);
	private CANTalon talonRight2 = new CANTalon(42);
											  //44 42
	
//	private CANTalon talon = new CANTalon(45);
//	private XboxController joy = new XboxController(3);
	private Joystick joy = new Joystick(0);
//	UsbCamera camera = new UsbCamera("cam0", 0);
//	DigitalInput s = new DigitalInput(0);
	
	public void test(){
//		LiveWindow.addActuator("CANTALONS", "SRX", cantalon);
////		LiveWindow.
//		LiveWindow.run();
	}
	
	public Robot(){
//		SmartDashboard.putBoolean("Switch: ", s.get());
//		CameraServer.getInstance().addCamera(camera);
//		CameraServer.getInstance().startAutomaticCapture(camera);
//		talon.reset();
//		talon.setPosition(0);
//		talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talonLeft1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talonLeft2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talonRight1.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talonRight2.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
////		talon.changeControlMode(CANTalon.TalonControlMode.Position);
//		talon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//		talon.setFeedbackDevice(CANTalon.FeedbackDevice.AnalogPot);
//		talon.configEncoderCodesPerRev(500);
//		talon.configNominalOutputVoltage(+0.0f, -0.0f);
//        talon.configPeakOutputVoltage(+12.0f, -12.0f);
//		talon.setF(0);
//		talon.setPID(12.8, 0, 0); // position mode on the gear handler
////		talon.setPID(.6, .0001, .15); // this is for the position mode
//		talon.setPID(.2, .00062, .03); // this is for the speed mode
//		
//		talon.reverseSensor(false);
//		talon.enableControl();
//		talon.set(4000);
//		talon.enableControl();
		talonLeft1.enableControl();
		talonLeft2.enableControl();
		talonRight1.enableControl();
//		talonRight2.enableControl();
	}
	
	public void operatorControl() {
		while(isOperatorControl() && isEnabled()) {
//			SmartDashboard.putNumber("Slider Thing: ", joy.getRawAxis(2));
			
			/*
			 * left rear
			 */
			if(joy.getRawButton(5)){
				talonLeft1.set(.5);
				talonLeft2.set(0);
				talonRight1.set(0);
				talonRight2.set(0);
			}
			
			/*
			 * Left front
			 */
			else if(joy.getRawButton(3)){
				talonLeft1.set(0);
				talonLeft2.set(.5);
				talonRight1.set(0);
				talonRight2.set(0);
			}
			/*
			 * Right rear
			 */
			else if(joy.getRawButton(6)){
				talonLeft1.set(0);
				talonLeft2.set(0);
				talonRight1.set(.5);
				talonRight2.set(0);
			}
			/*
			 * Right Front
			 */
			else if(joy.getRawButton(4)){
				talonLeft1.set(0);
				talonLeft2.set(0);
				talonRight1.set(0);
				talonRight2.set(.5);
			}
			else{
				talonLeft1.set(0);
				talonLeft2.set(0);
				talonRight1.set(0);
				talonRight2.set(0);
			}
			
			SmartDashboard.putNumber("Current Right Front", talonRight2.getOutputCurrent());
			SmartDashboard.putNumber("Current Right Rear", talonRight1.getOutputCurrent());
			SmartDashboard.putNumber("Current Left Front", talonLeft2.getOutputCurrent());
			SmartDashboard.putNumber("Current Left Rear", talonLeft1.getOutputCurrent());
//			talon.set(.5);
//			SmartDashboard.putBoolean("Switch: ", s.get());
//			
//			if(s.get()){
//				CameraServer.getInstance().removeCamera("cam0");
//				CameraServer.getInstance().startAutomaticCapture(null);
//			}
//			else{
//				CameraServer.getInstance().addCamera(camera);
//				CameraServer.getInstance().startAutomaticCapture(camera);
//			}
//			if(Math.abs(joy.getRawAxis(1)) > .08){
//				talon.set(joy.getRawAxis(1));
//			}
//			else{
//				talon.set(0);
//			}
//			
//			if(Math.abs(joy.getRawAxis(5)) > .08){
//				talon.set(joy.getRawAxis(5));
//			}
//			else{
//				talon.set(0);
//			}
//			
//			SmartDashboard.putNumber("Slider: ", joy1.getRawAxis(2));
//			if(Math.abs(joy.getRawAxis(1)) > .1)
//				talon.set(joy.getRawAxis(1));
//			else
				
//			if(joy.getRawButton(9))
//				talon.set(460);
//			if(joy.getRawButton(10))
//				talon.set(600);
//			if(joy.getRawButton(11))
//				talon.set(720);
//			talon.set(1); //for position mode, tells it to turn once (test for pid constants) the constants above seems to work well
//			talon.set(100); //for speed mode, tells it to turn at 100 rpm (test for pid constants) the constants above seems to work well 
			
//			SmartDashboard.putNumber("Talon Pot: ", talon.getAnalogInPosition());
//			SmartDashboard.putNumber("Talon Speed: ", talon.getSpeed());
////			SmartDashboard.putNumber("Talon Encoder Pos: ", talon.getEncPosition());
//			SmartDashboard.putNumber("Talon rpm: ", talon.getSpeed());
//			SmartDashboard.putNumber("Talon Voltage: ", talon.getOutputVoltage());
//			SmartDashboard.putNumber("Talon current: ", talon.getOutputCurrent());
			
			Timer.delay(0.005); 
		}
//		talon.setPosition(0);
	}
}
