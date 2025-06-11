// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.EndEffector;

import frc.lib.LoggedCommand;
import frc.robot.subsystems.EndEffector;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MotorIntakeAlgae extends LoggedCommand {
  /** Creates a new EndEffectorCommand. */

  EndEffector endEffector;
  private static boolean running;

  public MotorIntakeAlgae(EndEffector endEffector) {
    this.endEffector = endEffector;
    addRequirements(endEffector);
    
  }
  
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
    endEffector.maintainPosition();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    endEffector.setPlacementMotor(0.2);
    running = true;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    endEffector.setPlacementMotor(0);
    running = false;
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public static boolean isRunning(){
    return running;
  }
  // && middleSensor == true
}
