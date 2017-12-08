package org.usfirst.frc.team3555.robot.Autonomous;

import java.util.ArrayDeque;

/**
 * This is a queue that holds a set of {@link Action} to be done
 * 
 * @author Sam S.
 */
public class ActionQueue {
	private ArrayDeque<Action> queue;
	
	public ActionQueue() {
		queue = new ArrayDeque<>();
	}
	
	public void update() {
		Action currentAction = queue.peek();
		
		if(currentAction != null) {
			if(currentAction.update())
				queue.remove();
		}
	}
	
	public void add(Action a) { queue.addLast(a); }
}
