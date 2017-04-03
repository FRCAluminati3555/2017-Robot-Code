/*
 * 2017 Robot Code
 * FRC Team 3555, Aluminati
 * Programmed by, Sam Secondo
 */

package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.subsystems.GearHandler.GearHandlerPositions;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends SampleRobot {
	/*
	 * The engine class hold references to all of the objects
	 */
	private Engine engine;
	
	/*
	 *	this is an object of the widget on the smart dashboard to choose an autonomous
	 *	not used currently  
	 */
//	private SendableChooser<Object> autoChooser = new SendableChooser<>();

	/*
	 * This is the test method, just starts up the live window for the robot, can do real time testing
	 * hence the live part in the name
	 * 
	 */
	public void test(){
		LiveWindow.run();
	}
	
	/*
	 * constructor for the robot, creates an object of the engine class that contains the subsystem objects
	 * this will also set the different autonomouses that can be chosen in the dashboard
	 */
	public Robot(){
		/*
		 * the engine class just takes all the fields away from this class
		 * making the Robot class more clean
		 */
		engine = new Engine();
		engine.getDrive().invertControls();
//		autoChooser.addDefault("Just Forward Auto", new JustForwardGearAutonomous());
//		autoChooser.addObject("Turn Right Gear Auto", new TurnRightGearAutonomous());
//		autoChooser.addObject("Turn Left Gear Auto", new TurnLeftGearAutonomous());
	}
	
	public void autonomous(){
//		Autonomous auto = (Autonomous) autoChooser.getSelected();
//		auto.start();
		engine.getGearHandler().setToPosistion(GearHandlerPositions.UPPER_POS);
		Timer.delay(.25);
		engine.getDrive().autoDrive(.52, -.5, 3.75);
	}
	
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			/*
			 * each of the subsystems have an update method that will update the subsystem by using the speed controller
			 * or keeping a position, or take in user input and such...
			 */
			engine.getDrive().update();
			engine.getGearHandler().update();
			engine.getShooter().update();
			engine.getClimber().update();
			engine.getLoader().update();
			
			Timer.delay(0.005);
		}
	}
}
