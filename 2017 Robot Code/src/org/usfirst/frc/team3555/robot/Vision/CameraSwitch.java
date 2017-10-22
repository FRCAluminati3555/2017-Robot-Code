package org.usfirst.frc.team3555.robot.Vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;

public class CameraSwitch {
	
	private Joystick buttons;
	
	private UsbCamera camera1;
	private UsbCamera camera2;
	private VideoSink server;
	
	public CameraSwitch(Joystick buttons){
		this.buttons = buttons;
		
		camera1 = CameraServer.getInstance().startAutomaticCapture(0);
		camera2 = CameraServer.getInstance().startAutomaticCapture(1); 
		
		camera1.setResolution(160, 120);
		camera2.setResolution(160, 120);
		
		server = CameraServer.getInstance().getServer();
	}
	
	public void update(){
		if(buttons.getRawButton(1)){
			server.setSource(camera2);
		}
		else if(!buttons.getRawButton(1)){
			server.setSource(camera1);
		}
	}
}
