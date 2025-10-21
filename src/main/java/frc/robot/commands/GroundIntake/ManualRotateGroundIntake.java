// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.GroundIntake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.GroundIntake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ManualRotateGroundIntake extends Command {

  private GroundIntake m_GroundIntake;
  private GenericHID m_Controller;

  private double axis;
  private boolean maintaining;

  /** Creates a new ManualRotateGroundIntake. */
  public ManualRotateGroundIntake(GroundIntake intake, GenericHID controller) {
    m_GroundIntake = intake;
    m_Controller = controller;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_GroundIntake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    axis =MathUtil.applyDeadband(-m_Controller.getRawAxis(5),0.3);
    if (axis == 0) {
      if (!maintaining) {
        m_GroundIntake.maintainPosition();
      }
      maintaining = true;
    } 
    else {
      maintaining = false;
      if(m_GroundIntake.getPositions()[0] <= m_GroundIntake.getDefaultPosition() + 3 && axis < 0){
        m_GroundIntake.set(0);
      }
      else if(m_GroundIntake.getPositions()[0] >= 80 && axis > 1){
        m_GroundIntake.set(0);
      }else {
        m_GroundIntake.set(axis * 0.25);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_GroundIntake.set(0);
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
