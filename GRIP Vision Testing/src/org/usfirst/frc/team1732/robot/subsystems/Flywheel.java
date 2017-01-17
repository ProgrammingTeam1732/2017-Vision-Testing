package org.usfirst.frc.team1732.robot.subsystems;

import org.usfirst.frc.team1732.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Flywheel extends Subsystem {

	private final CANTalon		motor			= new CANTalon(RobotMap.FLYWHEEL_MOTER_DEVICE_NUMBER);
	public static final double	FORWARD_SPEED	= 1;
	public static final double	STOP_SPEED		= 0;
	public static final double	REVERSE_SPEED	= 1;

	@Override
	public void initDefaultCommand() {

	}

	public void setForward() {
		motor.set(FORWARD_SPEED);
	}

	public void setStop() {
		motor.set(STOP_SPEED);
	}

	public void setReverse() {
		motor.set(REVERSE_SPEED);
	}
}
