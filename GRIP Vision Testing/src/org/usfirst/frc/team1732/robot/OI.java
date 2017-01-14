package org.usfirst.frc.team1732.robot;

import org.usfirst.frc.team1732.robot.commands.AimTowardsGearPeg;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private final Joystick controller = new Joystick(0);
	// makes it easier to switch around buttons and functions
	private final Button button1 = new JoystickButton(controller, 1), button2 = new JoystickButton(controller, 2),
			button3 = new JoystickButton(controller, 3), button4 = new JoystickButton(controller, 4),
			button5 = new JoystickButton(controller, 5), button6 = new JoystickButton(controller, 6),
			button7 = new JoystickButton(controller, 7), button8 = new JoystickButton(controller, 8),
			button9 = new JoystickButton(controller, 9), button10 = new JoystickButton(controller, 10),
			button11 = new JoystickButton(controller, 11), button12 = new JoystickButton(controller, 12);

	private final Button	ballIntakeForward	= button1;
	private final Button	ballIntakeReverse	= button2;
	private final Trigger	ballIntakeStop		= new Trigger() {
													@Override
													public boolean get() {
														return !(ballIntakeForward.get() || ballIntakeReverse.get());
													}
												};

	private final Button	feederForward	= button3;
	private final Button	feederReverse	= button4;
	private final Trigger	feederStop		= new Trigger() {
												@Override
												public boolean get() {
													return !(feederForward.get() || feederReverse.get());
												}
											};

	private final Button	flywheelForward	= button5;
	private final Button	flywheelReverse	= button6;
	private final Trigger	flywheelStop	= new Trigger() {
												@Override
												public boolean get() {
													return !(flywheelForward.get() || flywheelReverse.get());
												}
											};

	private final Button	gearIntakeForward	= button7;
	private final Button	gearIntakeReverse	= button8;
	private final Trigger	gearIntakeStop		= new Trigger() {
													@Override
													public boolean get() {
														return !(gearIntakeForward.get() || gearIntakeReverse.get());
													}
												};

	private final Button climber = button9;

	private final Button otherShooter = button10;

	private final Button driveWithCamera = button11;

	public OI() {
		// ballIntakeForward.whenPressed(new BallIntakeSetForward());
		// ballIntakeReverse.whenPressed(new BallIntakeSetReverse());
		// ballIntakeStop.whenActive(new BallIntakeSetStop());
		//
		// feederForward.whenPressed(new FeederSetForward());
		// feederReverse.whenPressed(new FeederSetReverse());
		// feederStop.whenActive(new FeederSetStop());
		//
		// flywheelForward.whenPressed(new FlywheelSetForward());
		// flywheelReverse.whenPressed(new FlywheelSetReverse());
		// flywheelStop.whenActive(new FlywheelSetStop());
		//
		// gearIntakeForward.whenPressed(new GearIntakeSetForward());
		// gearIntakeReverse.whenPressed(new GearIntakeSetReverse());
		// gearIntakeStop.whenActive(new GearIntakeSetStop());
		//
		// climber.whenPressed(new ClimberSetUp());
		// climber.whenReleased(new ClimberSetStop());
		//
		// otherShooter.whenPressed(new OtherShooterShoot());
		// otherShooter.whenReleased(new OtherShooterReset());
		driveWithCamera.whenPressed(new AimTowardsGearPeg());
	}

	public double getLeftSpeed() {
		return controller.getRawAxis(RobotMap.LEFT_JOYSTICK_AXIS)
				* Math.abs(controller.getRawAxis(RobotMap.LEFT_JOYSTICK_AXIS));
	}

	public double getRightSpeed() {
		return controller.getRawAxis(RobotMap.RIGHT_JOYSTICK_AXIS)
				* Math.abs(controller.getRawAxis(RobotMap.RIGHT_JOYSTICK_AXIS));
	}
}