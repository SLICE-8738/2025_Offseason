// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.lib.commands;

import java.util.function.BooleanSupplier;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.Constants;
import frc.robot.commands.Drivetrain.PoseAlign;
import frc.robot.subsystems.drivetrain.Drivetrain;

/**
 * Automatically drives to the given final target while avoiding field obstacles
 * by pathfinding to the separate pathfinding target using PathplannerLib and then
 * aligning with the given final target using PID.
 */
public class PoseAlignSequence extends SequentialCommandGroup {

  private PoseAlign finalAlign;

  /**
   * Creates a new PoseAlignSequence with a given distance threshold.
   * 
   * @param drivetrain The drivetrain subsystem object
   * @param pathfindingTarget The target pose to pathfind to, ideally a reasonable distance
   *                          from the final target to allow room for the PID alignment to smoothly 
   *                          correct any error
   * @param finalTarget The final target pose to ultimately align with
   * @param distanceThreshold The distance from the final target pose at which to shift from pathfinding
   *                          to PID alignment
   */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget, double distanceThreshold) {

    finalAlign = new PoseAlign(drivetrain, finalTarget);

    addCommands(
      AutoBuilder.pathfindToPoseFlipped(pathfindingTarget, Constants.kDrivetrain.PATH_CONSTRAINTS).until(
        () -> finalAlign.getDistanceFromTarget() <= distanceThreshold),
      finalAlign
    );

  }
  
  /**
   * Creates a new PoseAlignSequence with a given custom shift condition.
   * 
   * @param drivetrain The drivetrain subsystem object
   * @param pathfindingTarget The target pose to pathfind to, ideally a reasonable distance
   *                          from the final target to allow room for the PID alignment to smoothly 
   *                          correct any error
   * @param finalTarget The final target pose to ultimately align with
   * @param shiftCondition A custom boolean supplier that returns true when the sequence should shift 
   *                       from pathfinding to PID alignment
   */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget, BooleanSupplier shiftCondition) {

    finalAlign = new PoseAlign(drivetrain, finalTarget);

    addCommands(
      AutoBuilder.pathfindToPoseFlipped(pathfindingTarget, Constants.kDrivetrain.PATH_CONSTRAINTS).until(shiftCondition),
      finalAlign
    );

  }

  /**
   * Creates a new PoseAlignSequence with a default distance threshold of 1.35 meters,
   * which should be a reasonable starting point for any tuning if deemed necessary.
   * 
   * @param drivetrain The drivetrain subsystem object
   * @param pathfindingTarget The target pose to pathfind to, ideally a reasonable distance
   *                          from the final target to allow room for the PID alignment to smoothly 
   *                          correct any error
   * @param finalTarget The final target pose to ultimately align with
   * @param distanceThreshold The distance from the final target pose at which to switch from pathfinding
   *                          to PID alignment
   */
  public PoseAlignSequence(Drivetrain drivetrain, Pose2d pathfindingTarget, Pose2d finalTarget) {
    this(
      drivetrain,
      pathfindingTarget, 
      finalTarget, 
      1.35);
  }

  /**
   * @return Whether PID (phase 2) alignment
   *         is active in the sequence
   */
  public boolean isFinalAlignActive() {
    return finalAlign.isScheduled();
  }

}
