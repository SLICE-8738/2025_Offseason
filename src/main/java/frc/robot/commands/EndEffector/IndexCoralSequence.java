// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.EndEffector;

import com.google.flatbuffers.Constants;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.kElevator.Level;
import frc.robot.Constants.kElevator.LevelType;
import frc.robot.Constants.kGroundIntake;
import frc.robot.commands.GroundIntake.IndexCoral;
import frc.robot.commands.GroundIntake.RotateGroundIntake;
import frc.robot.commands.Scoring.SetLevel;
import frc.robot.commands.Scoring.ToStow;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.EndEffector;
import frc.robot.subsystems.GroundIntake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IndexCoralSequence extends SequentialCommandGroup {
  /** Creates a new IndexCoralSequence. */
  public IndexCoralSequence(EndEffector endEffector, Elevator elevator, GroundIntake groundIntake) {
    addRequirements(elevator);
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new ParallelCommandGroup(new ToStow(endEffector, elevator),
    new RotateGroundIntake(groundIntake, 2, frc.robot.Constants.kGroundIntake.INDEX_ANGLE)),
    new ParallelCommandGroup(new IndexCoral(groundIntake).withTimeout(2.5), new IndexAlignCommand(endEffector)));
  }
}
