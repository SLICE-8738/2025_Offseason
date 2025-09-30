// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import com.pathplanner.lib.util.FlippingUtil;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.commands.LoggedCommand;
import frc.lib.config.JoystickFilterConfig;
import frc.lib.math.PolarJoystickFilter;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class PartialPoseAlign extends LoggedCommand {

  private final Drivetrain m_drivetrain;
  private final XboxController m_driverController;

  private final PolarJoystickFilter translationFilter;

  private final Pose2d m_targetPose;

  private final PIDController yController, rotationController;
  
  private Transform2d targetRelativePosition = new Transform2d();

  /**
   * Automatically aligns along robot-relative Y and rotational axes 
   * to the given pose using PID while using manual input from a controller
   * for the robot-relative X axis.
   * 
   * @param drivetrain The drivetrain subsystem instance passed in
   *                   from RobotContainer.
   * @param driverController The controller object for the driver controller
   * @param targetPose The pose to align to (must be for blue alliance)
   * @param automaticallyFlip Whether the given pose should automatically be
   *                          flipped to the red alliance side when red alliance
   *                          is selected in DriverStation
   */
  public PartialPoseAlign(Drivetrain drivetrain, XboxController driverController, Pose2d targetPose) {

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);

    m_drivetrain = drivetrain;

    m_driverController = driverController;

    translationFilter = new PolarJoystickFilter(new JoystickFilterConfig(
      0.07,
      0.5,
      Constants.OperatorConstants.DRIVE_EXPONENT,
      Constants.OperatorConstants.DRIVE_EXPONENT_PERCENT));

    m_targetPose = (DriverStation.getAlliance().get() == Alliance.Blue || !automaticallyFlip) ? targetPose : FlippingUtil.flipFieldPose(targetPose);

    yController = new PIDController(5.5, 0, 0.55);
    rotationController = new PIDController(6, 0, 0);

    yController.setSetpoint(0);
    yController.setTolerance(0.04);

    rotationController.setSetpoint(m_targetPose.getRotation().getDegrees());
    rotationController.enableContinuousInput(0, 360);
    rotationController.setTolerance(2);
        
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    super.initialize();
    m_drivetrain.addField2dPose(m_targetPose, "Auto Align Target Pose");

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    targetRelativePosition = m_drivetrain.getPose().minus(m_targetPose);

    double translationX = translationFilter.filter(-m_driverController.getRawAxis(1), 0)[0];
    double translationY = yController.calculate(targetRelativePosition.getY());
    double rotation = rotationController.calculate(m_drivetrain.getPose().getRotation().getDegrees());

    m_drivetrain.drive(
      new Transform2d(translationX, translationY, Rotation2d.fromDegrees(rotation)), 
      false, 
      false);

      SmartDashboard.putNumber("Auto Align Y Error", yController.getError());
      SmartDashboard.putNumber("Auto Align Rotation Error", rotationController.getError());

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

    m_drivetrain.drive(
      new Transform2d(), 
      false,
      false);
    super.end(interrupted);
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return DriverStation.isTeleopEnabled() ? false : yController.atSetpoint() && rotationController.atSetpoint();
  }

  public double getDistanceFromTarget() {
    return m_drivetrain.getPose().getTranslation().getDistance(m_targetPose.getTranslation());
  }

}
