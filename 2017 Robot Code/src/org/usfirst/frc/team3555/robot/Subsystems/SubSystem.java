package org.usfirst.frc.team3555.robot.Subsystems;

/**
 * <h1>Sub System</h1>
 * 
 * The {@code SubSystem} interface standardizes all of the subsystems to include the update method. 
 * The update method is called every iteration through the operator control loop to receive input and update the speed on that system's controllers.
 *
 * @author Sam Secondo
 */
public interface SubSystem {
	void update();
}
