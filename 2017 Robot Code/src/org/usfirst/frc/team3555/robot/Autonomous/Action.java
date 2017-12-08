package org.usfirst.frc.team3555.robot.Autonomous;

public class Action {
	private ActionUpdate actionUpdate; 
	private Action[] actions; 
	private boolean complete;
	
	public Action(ActionUpdate actionUpdate) {
		this.actionUpdate = actionUpdate;
	}
	
	public Action(Action... actions) {
		this.actions = actions;
	}
	
	public boolean update() {
		if(!complete) {
			if(actions != null) {
				for(Action a : actions) 
					complete = a.update();
			} else {
				complete = actionUpdate.update();
			}
		}
		return complete;
	}
	
	public boolean isComplete() { return complete; }
}
