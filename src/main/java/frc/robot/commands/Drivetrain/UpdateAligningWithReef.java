// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Drivetrain;

import frc.lib.LoggedCommand;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class UpdateAligningWithReef extends LoggedCommand {

  private final Drivetrain m_drivetrain;
  private final boolean m_aligningWithReef;

  /** Creates a new UpdateAligningWithReefCommand. */
  public UpdateAligningWithReef(Drivetrain drivetrain, boolean aligningWithReef) {

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);

    m_drivetrain = drivetrain;
    m_aligningWithReef = aligningWithReef;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    m_drivetrain.setAligningWithReef(m_aligningWithReef);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }

}
