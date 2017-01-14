package org.usfirst.frc.team1732.robot.commands;

import static org.usfirst.frc.team1732.robot.Robot.driveTrain;

import org.usfirst.frc.team1732.robot.Robot;
import org.usfirst.frc.team1732.robot.vision.GearVision;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AimTowardsGearPeg extends Command {

	public AimTowardsGearPeg() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		super("Aim Towards Gear Peg");
		requires(driveTrain);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double angle = Robot.gearVision.gearPeg.getHorizontalAngle(	GearVision.HORIZONTAL_FIELD_OF_VIEW,
																	GearVision.WIDTH);
		if (angle < -10)
			driveTrain.tankDrive(0.2, -0.2);
		else if (angle > 10)
			driveTrain.tankDrive(-0.2, 0.2);
		else
			driveTrain.tankDrive(0, 0);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.gearVision.gearPeg.getHorizontalAngle(GearVision.HORIZONTAL_FIELD_OF_VIEW,
																	GearVision.WIDTH)) < 10;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		driveTrain.tankDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}
