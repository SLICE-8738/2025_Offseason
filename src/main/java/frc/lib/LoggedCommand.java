// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.lib;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * Command superclass for logging whether a command
 * is running to AdvantageKit
 */
public class LoggedCommand extends Command {

  /** Creates a new LoggedCommand. */
  public LoggedCommand() {}

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    for (Subsystem subsystem : getRequirements()) {
      Logger.recordOutput("Commands/" + subsystem.getName() + "/" + getName() + " Running", true);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    for (Subsystem subsystem : getRequirements()) {
      Logger.recordOutput("Commands/" + subsystem.getName() + "/" + getName() + " Running", false);
    }  
  }

}
