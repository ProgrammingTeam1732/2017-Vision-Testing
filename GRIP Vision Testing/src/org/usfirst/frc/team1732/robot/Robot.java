
package org.usfirst.frc.team1732.robot;

import org.usfirst.frc.team1732.robot.commands.AimTowardsGearPeg;
import org.usfirst.frc.team1732.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1732.robot.subsystems.Flywheel;
import org.usfirst.frc.team1732.robot.vision.GearVision;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public static OI			oi;
	private static Thread		visionThread;
	public static GearVision	gearVision;
	public static DriveTrain	driveTrain;
	public static Flywheel		flywheel;

	@Override
	public void robotInit() {
		flywheel = new Flywheel();
		driveTrain = new DriveTrain();
		gearVision = new GearVision();
		oi = new OI();

		visionThread = new Thread(gearVision);
		visionThread.setDaemon(true);
		visionThread.start();
		SmartDashboard.putData(Scheduler.getInstance());
		SmartDashboard.putData(driveTrain);
		SmartDashboard.putData(new AimTowardsGearPeg());
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
