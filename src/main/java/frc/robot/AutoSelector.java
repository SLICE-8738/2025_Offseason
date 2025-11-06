// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;

import frc.robot.Constants.kField.CoralStationPosition;
import frc.robot.Constants.kField.ReefPosition;
import frc.robot.Constants.kElevator.Level;
import frc.robot.commands.Scoring.AlignAndGetCoralAutonomous;
import frc.robot.commands.Scoring.AlignAndScoreCoralAutonomous;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.EndEffector;
import frc.robot.subsystems.SourceIntake;
import frc.robot.subsystems.drivetrain.Drivetrain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.pathfinding.Pathfinding;

/**
 * This class primarily manages the creation and updating of the autonomous mode
 * and starting position {@link SendableChooser sendable choosers} on Shuffleboard.
 */
public class AutoSelector {

    public enum Routine {

        SCORE_1_CORAL_L4("Score 1 Coral L4"),
        TEST_PATH("Test Path"),
        AUTO_BUILDER("Auto Builder"),
        SCORE_4_CORAL_L4_FRONT_LEFT("Score 4 Coral L4 Front Left"),
        SCORE_4_CORAL_L4_FRONT_RIGHT("Score 4 Coral L4 Front Right");

        public final String name;

        Routine(String name) {

            this.name = name;

        }

    }

    public final SendableChooser<Routine> routineChooser;

    private Optional<PathPlannerAuto> autoCommand = Optional.empty();

    private final Map<String, Pose2d> autoPoses = new HashMap<String, Pose2d>();

    public AutoSelector(Drivetrain drivetrain, Elevator elevator, EndEffector endEffector) {

        routineChooser = new SendableChooser<Routine>();

        routineChooser.setDefaultOption(Routine.SCORE_1_CORAL_L4.name, Routine.SCORE_1_CORAL_L4);

        for (int i = 1; i < Routine.values().length; i++) {

            Routine mode = Routine.values()[i];
            routineChooser.addOption(mode.name, mode);

        }

        routineChooser.onChange(routine -> updateAutoCommand(routine));

        AutoBuilder.configure(
            drivetrain::getPose,
            drivetrain::resetOdometry,
            drivetrain::getChassisSpeeds,
            drivetrain::runChassisSpeeds,
            new PPHolonomicDriveController(
                new PIDConstants(Constants.kDrivetrain.TRANSLATION_KP),
                new PIDConstants(Constants.kDrivetrain.ROTATION_KP)),
            new RobotConfig(
                Constants.kDrivetrain.MASS,
                Constants.kDrivetrain.MOMENT_OF_INERTIA,
                new ModuleConfig(
                    Constants.kDrivetrain.WHEEL_DIAMETER / 2,
                    Constants.kDrivetrain.MAX_LINEAR_VELOCITY,
                    Constants.kDrivetrain.WHEEL_COEFFICIENT_OF_FRICTION,
                    DCMotor.getKrakenX60(1).withReduction(Constants.kDrivetrain.DRIVE_GEAR_RATIO),
                    Constants.kDrivetrain.DRIVE_STATOR_CURRENT_LIMIT,
                    1),
                Constants.kDrivetrain.kSwerveKinematics.getModules()),
            () -> DriverStation.getAlliance().get() == Alliance.Red,
            drivetrain);

        Pathfinding.ensureInitialized();

        /* Reef Positions */
        for (ReefPosition position : ReefPosition.values()) {

            for (Level level : new Level[] {Level.LEVEL1, Level.LEVEL2, Level.LEVEL3}) {

                NamedCommands.registerCommand(
                    "Score Coral " + position.name + " " + level.name,
                    new AlignAndScoreCoralAutonomous(drivetrain, elevator, endEffector, position, level));

                autoPoses.put("Score Coral " + position.name + " " + level.name, position.pathfindingTarget);

            }

            ReefPosition level2Position;

            switch (position.reefSide) {
                
                case LEFT:
                    level2Position = ReefPosition.BACK_MIDDLE_LEFT_BRANCH;
                    break;
                case RIGHT:
                    level2Position = ReefPosition.BACK_MIDDLE_RIGHT_BRANCH;
                    break;
                default:
                    level2Position = position;
                    break;

            }

            NamedCommands.registerCommand(
                "Score Coral " + position.name + " Level 4",
                new ConditionalCommand(
                    new AlignAndScoreCoralAutonomous(drivetrain, elevator, endEffector, position, Level.LEVEL4),
                    new AlignAndScoreCoralAutonomous(
                        drivetrain, 
                        elevator, 
                        endEffector, 
                        level2Position, 
                        Level.LEVEL2),
                    () -> DriverStation.getMatchTime() >= 3));

            autoPoses.put("Score Coral " + position.name + " Level 4", position.pathfindingTarget);

        }

        /* Coral Station Positions */
        for (CoralStationPosition position : CoralStationPosition.values()) {
            
                NamedCommands.registerCommand(
                    "Get Coral " + position.name, 
                    new AlignAndGetCoralAutonomous(drivetrain, elevator, endEffector, position));

                autoPoses.put("Get Coral " + position.name, position.pathfindingTarget);

        }

    }

    private void updateAutoCommand(Routine routine) {

        try {

            System.out.println("Auto selection changed to " + routine.name);
            autoCommand = Optional.of(new PathPlannerAuto(routine.name));

        }
        catch (Exception e) {

            DriverStation.reportError(e.getMessage(), false);   
            e.printStackTrace();         
            autoCommand = Optional.empty();

        }

    }

    public Command getAutoCommand() {

        return autoCommand.get();

    }

    public String getRoutine() {

        return routineChooser.getSelected().name;

    }

}