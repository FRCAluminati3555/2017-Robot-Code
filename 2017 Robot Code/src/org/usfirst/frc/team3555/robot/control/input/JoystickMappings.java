package org.usfirst.frc.team3555.robot.control.input;

/*
 * Class From 2016 Robot Code 
 * This class in essence makes the other code more readable
 * This is because instead of saying joystick.getRawButton()
 * The buttons are accessed through an enum that describes the button's location
 * Rather than just it's number
 */
public class JoystickMappings {
	public static interface Axis { public int getIndex(); }
	public static interface Button { public int getIndex(); }
	
	public static enum LogitechAttack3_Axis implements Axis {
		X(0), Y(1), Slider(2);
		
		private int index;
		private LogitechAttack3_Axis(int index) { 
			this.index = index;
		}
		
		public int getIndex() { return index; }
	}
	
	public static enum LogitechAttack3_Button implements Button {
		Trigger(1), 
		Top_Left(4), Top_Right(5), Top_Center(3), Top_Lower(2),
		
		Bottom_Left_Front(6), Bottom_Left_Back(7),
		Button_6(6), Button_7(7),
		
		Bottom_Back_Left(8), Botttom_Back_Right(9),
		Button_8(8), Button_9(9),
		
		Bottom_Right_Front(11), Bottom_Right_Back(10),
		Button_11(11), Button_10(10);
		
		private int index;
		private LogitechAttack3_Button(int index) { 
			this.index = index;
		}
		
		public int getIndex() { return index; }
	}
	
	public static enum LogitechExtreme3D_Axis implements Axis {
		X(0), Y(1), Rotation(2), Slider(3);
		
		private int index;
		private LogitechExtreme3D_Axis(int index) { 
			this.index = index;
		}
		
		public int getIndex() { return index; }
	}
	
	public static enum LogitechExtreme3D_Button implements Button {
		Trigger(1), Thumb(2), 
		Top_Upper_Left(5),  Top_Lower_Left(3),    
		Top_Upper_Right(6), Top_Lower_Right(4),
		
		Bottom_Lower_Front(7),  Bottom_Upper_Front(8), 
		Bottom_Lower_Middle(9), Bottom_Upper_Middle(10),
		Bottom_Lower_Back(11),  Bottom_Upper_Back(12),
		
		Button_7(7),   Button_8(8), 
		Button_9(9),   Button_10(10),
		Button_11(11), Button_12(12);
		
		private int index;
		private LogitechExtreme3D_Button(int index) { 
			this.index = index;
		}

		public int getIndex() { return index; }
	}
	
	public static enum XBox360_Axis implements Axis {
		Left_Vertical(0), Left_Horizontal(1),
		Right_Vertical(3), Right_Horizontal(4),
		Right_Trigger(2);
		
		private int index;
		private XBox360_Axis(int index) { 
			this.index = index;
		}
		
		public int getIndex() { return index; }
	}
	
	public static enum XBox360_Button implements Button {
		A(1), B(2), X(3), Y(4), Back(7), Start(8), 
		Left_Bumper(5), Right_Bumper(6),
		Left_Stick(9), Right_Stick(10);
		
		private int index;
		private XBox360_Button(int index) { 
			this.index = index;
		}
		
		public int getIndex() { return index; }
	}
}
