// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.lib.commands;

import java.util.function.BooleanSupplier;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Constants;
import frc.robot.commands.Drivetrain.PoseAlign;
import frc.robot.subsystems.drivetrain.Drivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PoseAlignSequence extends SequentialCommandGroup {

  /** Creates a new PoseAlignSequence. */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget, double distanceThreshold) {

    PoseAlign alignWithPose = new PoseAlign(drivetrain, finalTarget);

    addCommands(
      AutoBuilder.pathfindToPoseFlipped(pathfindingTarget, Constants.kDrivetrain.PATH_CONSTRAINTS).until(
        () -> alignWithPose.getDistanceFromTarget() <= distanceThreshold),
      alignWithPose
    );

  }
  
  /** Creates a new PoseAlignSequence. */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget, BooleanSupplier shiftCondition) {

    PoseAlign alignWithPose = new PoseAlign(drivetrain, finalTarget);

    addCommands(
      AutoBuilder.pathfindToPoseFlipped(pathfindingTarget, Constants.kDrivetrain.PATH_CONSTRAINTS).until(shiftCondition),
      alignWithPose
    );

  }

  /** Creates a new PoseAlignSequence. */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget) {
    this(
      drivetrain,
      pathfindingTarget, 
      finalTarget, 
      1);
  }

}
