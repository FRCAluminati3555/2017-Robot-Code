package org.usfirst.frc.team3555.robot.Autonomous;

import java.util.ArrayDeque;

/**
 * This is a queue that holds a set of {@link Action} to be done.
 * Adding actions will add to the queue, but only one action will be checked (the first will execute, when it has completed, the next will move up and execute)
 * However, an action can store more than 1 action, allowing for these to be completed in sets in succession 
 * 
 * @author Sam S.
 */
public class ActionQueue {
	private ArrayDeque<Action> queue;
	
	public ActionQueue() {
		queue = new ArrayDeque<>();
	}
	
	/**
	 * Updates the first action, and will remove it from the queue if it is complete, and will move to the next action
	 * @see Action
	 */
	public void update() {
		Action currentAction = queue.peek();
		
		if(currentAction != null && currentAction.update()) 
			queue.remove();
	}
	
	/**
	 * Adds this action to the end of the queue
	 * NOTE: These actions are completed one by one in succession. 
	 * This means that if an action is added to the end, the rest that are in the front all need to execute before this one does
	 * 
	 * @param a - Action to be added
	 */
	public void add(Action a) { queue.addLast(a); }
}
