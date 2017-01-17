
package org.usfirst.frc.team1732.robot;

import java.awt.Rectangle;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	// SerialPort serial = new SerialPort(9600, SerialPort.Port.kUSB);
	Joystick controller = new Joystick(0);
	CANTalon leftTalon1 = new CANTalon(23);
	CANTalon leftTalon2 = new CANTalon(22);
	CANTalon leftTalon3 = new CANTalon(21);
	CANTalon rightTalon1 = new CANTalon(11);
	CANTalon rightTalon2 = new CANTalon(13);
	CANTalon rightTalon3 = new CANTalon(14);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		leftTalon3.setInverted(true);
		rightTalon1.setInverted(true);
		rightTalon2.setInverted(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	private Arduino board1;
	public void autonomousInit() {
		board1 = new Arduino();
		/*
		 * for(;;){ String[] data = board1.getData(); for(int i = 0; i <
		 * data.length; i++) System.out.print(data[i] + " ");
		 * System.out.println(); edu.wpi.first.wpilibj.Timer.delay(0.125); }
		 */
		//for (;;) {
		//	String s = board1.getData();
			/*for(String ss : s)
			SmartDashboard.putString("board", SmartDashboard.getString("board", "") + ss);
			int pixels = Integer.parseInt(s[11]);
			double speed = distanceToBoiler(4, Integer.parseInt(s[7]) + pixels / 2,
					getTheta(Integer.parseInt(s[7]), Integer.parseInt(s[7]) + pixels / 2));
			speed = speed / 120;
			leftTalon1.set(speed);
			leftTalon2.set(speed);
			leftTalon3.set(speed);
			rightTalon1.set(speed);
			rightTalon2.set(speed);
			rightTalon3.set(speed);*/
		//	System.out.print(s);
		
	}

	public static final int horizontalFieldOfView = 75;
	public static final int camreaWidth = 640;

	public double getTheta(int x, int objectWidthInPixels) {
		int centerx = x + objectWidthInPixels / 2;
		return ((centerx - (camreaWidth / 2)) / camreaWidth) * horizontalFieldOfView;
	}

	public double distanceToBoiler(int objectWidthInInches, int objectWidthInPixels, double thetaInDegrees) {
		int width = camreaWidth * objectWidthInInches / objectWidthInPixels;
		return width / 2 / Math.tan(Math.toRadians(thetaInDegrees / 2));
	}

	/**
	 * This function is called periodically during autonomous
	 */
	private String total = "";
	private boolean found = false;
	public void autonomousPeriodic() {
		String s = board1.getData();
		//String total
		System.out.println("----------");
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(found && c != '^') total += c;
			if(c == '$') found = true;
			else if(c == '^') {
				found = false;
				System.out.println(total);
				total = "";
			}
			//System.out.println(s);
		}
		System.out.println("**********");
		//total += s;
		//if(total.contains("\n")){
		//	System.out.println(total);
		//	total = "";
		//}
		//for(String ss : s){
		//String[] arr = s.split(" ");
		//System.out.println(arr.length);
		//System.out.println(s);
		//System.out.println(s[0]);
		
	//	SmartDashboard.putString("board", SmartDashboard.getString("board", "") + ss);
		/*int pixels = Integer.parseInt(s[11]);
		double speed = distanceToBoiler(4, Integer.parseInt(s[7]) + pixels / 2,
				getTheta(Integer.parseInt(s[7]), Integer.parseInt(s[7]) + pixels / 2));
		speed = speed / 120;
		leftTalon1.set(speed);
		leftTalon2.set(speed);
		leftTalon3.set(speed);
		rightTalon1.set(speed);
		rightTalon2.set(speed);
		rightTalon3.set(speed);*/
		//}
		//System.out.print(s);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		// System.out.println(serial.readString());
		/*
		 * double c1 = controller.getRawAxis(3)/4; double c2 =
		 * controller.getRawAxis(2)/4; if (c1 < 0) { rightTalon1.set(c1 - c2);
		 * rightTalon2.set(c1 - c2); rightTalon3.set(c1 - c2); leftTalon1.set(c1
		 * + c2); leftTalon2.set(c1 + c2); leftTalon3.set(c1 + c2); }else{
		 * rightTalon1.set(c1 + c2); rightTalon2.set(c1 + c2);
		 * rightTalon3.set(c1 + c2); leftTalon1.set(c1 - c2); leftTalon2.set(c1
		 * - c2); leftTalon3.set(c1 - c2); }
		 * SmartDashboard.putNumber("Right Talon 1 Speed", rightTalon1.get());
		 * SmartDashboard.putNumber("Right Talon 1 Voltage",
		 * rightTalon1.getOutputVoltage());
		 * SmartDashboard.putNumber("Right Talon 2 Voltage",
		 * rightTalon2.getOutputVoltage());
		 * SmartDashboard.putNumber("Right Talon 3 Voltage",
		 * rightTalon3.getOutputVoltage());
		 * SmartDashboard.putNumber("Left Talon 1 Voltage",
		 * leftTalon1.getOutputVoltage());
		 * SmartDashboard.putNumber("Left Talon 2 Voltage",
		 * leftTalon2.getOutputVoltage());
		 * SmartDashboard.putNumber("Left Talon 3 Voltage",
		 * leftTalon3.getOutputVoltage());
		 */

		leftTalon1.set(controller.getRawAxis(3) / 2);
		leftTalon2.set(controller.getRawAxis(3) / 2);
		leftTalon3.set(controller.getRawAxis(3) / 2);
		rightTalon1.set(controller.getRawAxis(1) / 2);
		rightTalon2.set(controller.getRawAxis(1) / 2);
		rightTalon3.set(controller.getRawAxis(1) / 2);

	}
	public Rectangle[] parseString(String regex){
		String[] ss = regex.split(" ");
		Rectangle[] total = new Rectangle[Integer.parseInt(ss[2].substring(0, ss[2].length() -1))];
		return null;
	}
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
