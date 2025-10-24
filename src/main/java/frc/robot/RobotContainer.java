// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Set;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.Mode;
import frc.robot.Constants.kElevator.Level;
import frc.robot.Constants.kElevator.LevelType;
//import frc.robot.commands.Climber.*;
import frc.robot.commands.Drivetrain.DriveCommand;
import frc.robot.commands.Drivetrain.ResetFieldOrientedHeading;
import frc.robot.commands.Drivetrain.RunDutyCycle;
import frc.robot.commands.Elevator.ManualElevator;
import frc.robot.commands.EndEffector.BargeAlgaeThrow;
import frc.robot.commands.EndEffector.BumpAlgae;
import frc.robot.commands.EndEffector.ClampAlgae;
import frc.robot.commands.EndEffector.IndexCoralSequence;
import frc.robot.commands.EndEffector.IndexSequence;
import frc.robot.commands.EndEffector.IntakeAlgae;
import frc.robot.commands.EndEffector.ManualEndEffector;
import frc.robot.commands.EndEffector.ManualFeedCommand;
import frc.robot.commands.EndEffector.OutakeAlgae;
import frc.robot.commands.EndEffector.ScoreCoral;
import frc.robot.commands.GroundIntake.IndexCoral;
import frc.robot.commands.GroundIntake.IntakeCoral;
import frc.robot.commands.GroundIntake.IntakeCoralSequence;
import frc.robot.commands.GroundIntake.ManualRotateGroundIntake;
import frc.robot.commands.GroundIntake.RotateGroundIntake;
import frc.robot.commands.GroundIntake.SpinPlacementMotors;
import frc.robot.commands.LEDs.CoralLEDs;
import frc.robot.commands.Scoring.AlignAndGetAlgae;
import frc.robot.commands.Scoring.AlignAndGetCoral;
import frc.robot.commands.Scoring.AlignAndScoreCoral;
import frc.robot.commands.Scoring.BargeAlgae;
import frc.robot.commands.Scoring.IntakeAdjustment;
import frc.robot.commands.Scoring.MoveToLevel;
import frc.robot.commands.Scoring.MoveToLevelParallel;
import frc.robot.commands.Scoring.ProcessAlgae;
import frc.robot.commands.Scoring.ResetRelativeEncoders;
import frc.robot.commands.Scoring.SetLevel;
import frc.robot.commands.Scoring.ToAlgae;
import frc.robot.commands.Scoring.ToStow;
import frc.robot.commands.SourceIntake.ManualRotateSourceIntake;
import frc.robot.commands.SourceIntake.RotateSourceIntake;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.EndEffector;
import frc.robot.subsystems.GroundIntake;
import frc.robot.subsystems.LEDs;
import frc.robot.subsystems.SourceIntake;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.RealSwerveModuleIO;
import frc.robot.subsystems.drivetrain.SimSwerveModuleIO;
import frc.robot.subsystems.drivetrain.SwerveModuleIO;
import frc.robot.testing.routines.DrivetrainTest;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  private final XboxController driverController = Button.controller1;
  private final XboxController operatorController = Button.controller2;

  // ==========================
  // Subsystems
  // ==========================

  public final Drivetrain m_drivetrain;
  //public final Climber m_climber;
  public final Elevator m_elevator;
  public final EndEffector m_endEffector;
  public final SourceIntake m_sourceIntake;
  public final LEDs m_leds;
  public final GroundIntake m_groundIntake;

  public final AutoSelector m_autoSelector;
  public final ReefPositionSelector m_reefPositionSelector;
  public final ShuffleboardData m_shuffleboardData;

  // ==========================
  // Commands
  // ==========================

  /* Drivetrain */
  public final DriveCommand m_swerveDriveOpenLoop;
  public final DriveCommand m_swerveDriveClosedLoop;
  public final DriveCommand m_swerveDriveClosedLoopSlowMode;
  public final RunDutyCycle m_setDrivePercentOutput;
  public final ResetFieldOrientedHeading m_resetFieldOrientedHeading;
  public final Command m_sysIDDriveRoutine;
  public final Command m_alignAndScoreCoral;
  public final Command m_alignAndGetAlgae;
  public final Command m_alignAndGetCoral;

  /* Scoring */
  public final SetLevel m_setLevelOne;
  public final SetLevel m_setLevelOneB;
  public final SetLevel m_setLevelTwo;
  public final SetLevel m_setLevelThree;
  public final SetLevel m_setLevelFour;
  public final SetLevel m_setLowerAlgae;
  public final SetLevel m_setUpperAlgae;
  public final MoveToLevel m_moveUpToLevel;
  public final MoveToLevel m_moveDownToLevel;
  public final ToAlgae m_toAlgaeHigher;
  public final ToAlgae m_toAlgaeLower;
  public final ToStow m_elevatorToStow;
  public final ToStow m_elevatorEmergencyStow;
  public final IntakeAdjustment m_intakeAdjustment;
  public final BargeAlgae m_bargeAlgae;
  public final ProcessAlgae m_processAlgae;
  public final MoveToLevelParallel m_moveToLevelParallel;

  public final IndexCoralSequence m_indexCoralSequence;

  /* Climber */
  //public final ManualClimberCommand m_manualClimb;
  //public final SequentialCommandGroup m_climb;

  /* Elevator */
  public final ManualElevator m_manualElevator;

  /* End Effector */
  public final IndexSequence m_indexCoral;
  public final BumpAlgae m_bumpAlgae;
  public final ScoreCoral m_scoreCoral;
  public final ManualEndEffector m_manualEndEffector;
  public final ManualFeedCommand m_manualFeed;

  public final IntakeAlgae m_IntakeAlgae;
  public final OutakeAlgae m_OutakeAlgae;
  public final ClampAlgae m_clampAlgae;

  public final BargeAlgaeThrow m_bargeAlgaeThrow;

  /* Source Intake */
  public final ManualRotateSourceIntake m_manualSourceIntake;
  public final RotateSourceIntake m_goToSourceIntakeAngle1;
  public final RotateSourceIntake m_goToSourceIntakeAngle2;

  /* Ground Intake */
  public final ManualRotateGroundIntake m_manualRotateGroundIntake;
  public final SpinPlacementMotors m_moveIntake;
  public final SpinPlacementMotors m_moveIntakeReversed;
  public final RotateGroundIntake m_goToGroundIntakeAngleIntake;
  public final RotateGroundIntake m_goToGroundIntakeAngleIndex;
  public final RotateGroundIntake m_groundIntakeStow;
  public final IntakeCoral m_intakeCoral;
  public final IndexCoral m_indexCoralGroudIntakeCommand;
  public final IntakeCoralSequence m_intakeCoralSequence;

  /* LEDs */
  public final CoralLEDs m_coralLEDs;

  /* Triggers */
  public final Trigger robotTipping;
  public final Trigger slowModeTrigger;
  public final Trigger algaeIntakeTrigger;

  /* Tests */
  public final DrivetrainTest m_drivetrainTest;
  public final InstantCommand m_antiGravityTest;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    if (RobotBase.isReal()) {
      Constants.ADVANTAGE_KIT_MODE = Mode.REAL;
    }

    // ==========================
    // Subsystems
    // ==========================

    switch (Constants.ADVANTAGE_KIT_MODE) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        m_drivetrain = new Drivetrain(
            new RealSwerveModuleIO(Constants.kDrivetrain.Mod0.CONSTANTS),
            new RealSwerveModuleIO(Constants.kDrivetrain.Mod1.CONSTANTS),
            new RealSwerveModuleIO(Constants.kDrivetrain.Mod2.CONSTANTS),
            new RealSwerveModuleIO(Constants.kDrivetrain.Mod3.CONSTANTS));
        break;
      case SIM:
        m_drivetrain = new Drivetrain(
            new SimSwerveModuleIO(),
            new SimSwerveModuleIO(),
            new SimSwerveModuleIO(),
            new SimSwerveModuleIO());
        break;
      default:
        m_drivetrain = new Drivetrain(
            new SwerveModuleIO() {},
            new SwerveModuleIO() {},
            new SwerveModuleIO() {},
            new SwerveModuleIO() {});
        break;
    }

    m_endEffector = new EndEffector();
    m_elevator = new Elevator();
    m_sourceIntake = new SourceIntake();
    m_groundIntake = new GroundIntake();
    //m_climber = new Climber();
    m_leds = new LEDs();

    m_autoSelector = new AutoSelector(m_drivetrain, m_elevator, m_endEffector, m_sourceIntake);
    m_reefPositionSelector = new ReefPositionSelector();
    m_shuffleboardData = new ShuffleboardData(m_drivetrain, m_endEffector, m_autoSelector);

    // ==========================
    // Commands
    // ==========================

    /* Drivetrain */
    m_swerveDriveOpenLoop = new DriveCommand(m_drivetrain, driverController, true, false);
    m_swerveDriveClosedLoop = new DriveCommand(m_drivetrain, driverController, false, false);
    m_swerveDriveClosedLoopSlowMode = new DriveCommand(m_drivetrain, driverController, false, true);
    m_setDrivePercentOutput = new RunDutyCycle(m_drivetrain, 0.10, 0);
    m_resetFieldOrientedHeading = new ResetFieldOrientedHeading(m_drivetrain);
    m_sysIDDriveRoutine = new DeferredCommand(m_drivetrain::getSysIDDriveRoutine, Set.of(m_drivetrain));

    /* Scoring */
    m_moveUpToLevel = new MoveToLevel(m_endEffector, m_elevator, LevelType.CORAL, false, false);
    m_moveDownToLevel = new MoveToLevel(m_endEffector, m_elevator, LevelType.CORAL, true, false);
    m_setLevelOne = new SetLevel(Level.LEVEL1, LevelType.CORAL);
    m_setLevelOneB = new SetLevel(Level.LEVEL1B, LevelType.CORAL);
    m_setLevelTwo = new SetLevel(Level.LEVEL2, LevelType.CORAL);
    m_setLevelThree = new SetLevel(Level.LEVEL3, LevelType.CORAL);
    m_setLevelFour = new SetLevel(Level.LEVEL4, LevelType.CORAL);
    m_setLowerAlgae = new SetLevel(Level.ALGAE1, LevelType.ALGAE);
    m_setUpperAlgae = new SetLevel(Level.ALGAE2, LevelType.ALGAE);
    m_elevatorToStow = new ToStow(m_endEffector, m_elevator);
    m_elevatorEmergencyStow = new ToStow(m_endEffector, m_elevator);
    m_toAlgaeHigher = new ToAlgae(m_elevator, m_endEffector);
    m_toAlgaeLower = new ToAlgae(m_elevator, m_endEffector);
    m_intakeAdjustment = new IntakeAdjustment(m_endEffector, m_sourceIntake);
    m_alignAndScoreCoral = new DeferredCommand(
      () -> new AlignAndScoreCoral(m_drivetrain, m_elevator, m_endEffector),
      Set.of(m_drivetrain));
    m_alignAndGetCoral = new DeferredCommand(
      () -> new AlignAndGetCoral(m_drivetrain, m_elevator, m_endEffector),
      Set.of(m_drivetrain));
    m_alignAndGetAlgae = new DeferredCommand(
      () -> new AlignAndGetAlgae(m_drivetrain, m_elevator, m_endEffector, driverController),
      Set.of(m_drivetrain));
    m_bargeAlgae = new BargeAlgae(m_endEffector, m_elevator);
    m_processAlgae = new ProcessAlgae(m_endEffector, m_elevator);
    m_moveToLevelParallel = new MoveToLevelParallel(m_elevator, m_endEffector, LevelType.CORAL);

    m_indexCoralSequence = new IndexCoralSequence(m_endEffector, m_elevator, m_groundIntake);
    m_intakeCoralSequence = new IntakeCoralSequence(m_groundIntake);

    /* End Effector */
    m_indexCoral = new IndexSequence(m_endEffector, m_elevator, operatorController);
    m_bumpAlgae = new BumpAlgae(m_endEffector);
    m_scoreCoral = new ScoreCoral(m_endEffector);
    m_bargeAlgaeThrow = new BargeAlgaeThrow(m_endEffector);
    m_manualEndEffector = new ManualEndEffector(m_endEffector, operatorController);
    m_manualFeed = new ManualFeedCommand(m_endEffector);

    m_IntakeAlgae = new IntakeAlgae(m_endEffector);
    m_OutakeAlgae = new OutakeAlgae(m_endEffector);
    m_clampAlgae = new ClampAlgae(m_endEffector);

    /* Elevator */
    m_manualElevator = new ManualElevator(m_elevator, operatorController);

    /* Source Intake */
    m_manualSourceIntake = new ManualRotateSourceIntake(m_sourceIntake, operatorController);
    m_goToSourceIntakeAngle1 = new RotateSourceIntake(m_sourceIntake, 2, Constants.kSourceIntake.INTAKE_ANGLE);
    m_goToSourceIntakeAngle2 = new RotateSourceIntake(m_sourceIntake, 2, Constants.kSourceIntake.CLIMB_ANGLE);

    /* Ground Intake */
    m_manualRotateGroundIntake = new ManualRotateGroundIntake(m_groundIntake, operatorController);
    m_moveIntake = new SpinPlacementMotors(m_groundIntake, true);
    m_moveIntakeReversed = new SpinPlacementMotors(m_groundIntake, false);
    m_goToGroundIntakeAngleIndex = new RotateGroundIntake(m_groundIntake, 2, Constants.kGroundIntake.INDEX_ANGLE);
    m_goToGroundIntakeAngleIntake = new  RotateGroundIntake(m_groundIntake, 2,Constants.kGroundIntake.INTAKE_ANGLE);
    m_groundIntakeStow = new RotateGroundIntake(m_groundIntake, 2, Constants.kGroundIntake.STOW_ANGLE);
    m_intakeCoral = new IntakeCoral(m_groundIntake);
    m_indexCoralGroudIntakeCommand = new IndexCoral(m_groundIntake);

    /* Climber */
  //  m_manualClimb = new ManualClimberCommand(m_climber, Button.controller2);
    // The climb command is created with a WaitCommand before it so that the climber
    // won't immediately activate when the button is pressed.
    // This prevents accidental damage to the climber by running it when there isn't
    // a cage.
   // m_climb = new SequentialCommandGroup(new WaitCommand(0.5), new ClimbCommand(m_climber));

    /* LEDs */
    m_coralLEDs = new CoralLEDs(m_leds, m_endEffector);

    /* Tests */
    m_drivetrainTest = new DrivetrainTest(m_drivetrain);
    m_antiGravityTest = new InstantCommand(() -> m_endEffector.setVoltage(-m_endEffector.normalKG), m_endEffector);

    /* Triggers */
    robotTipping = new Trigger(() -> m_drivetrain.getRoll() > 20 || m_drivetrain.getPitch() > 20);
    slowModeTrigger = new Trigger(() -> Button.cont1_leftBumper.getAsBoolean() && Button.cont1_rightBumper.getAsBoolean());
    algaeIntakeTrigger = new Trigger(() -> Button.cont1_leftTrigger.getAsBoolean() && Button.cont1_rightTrigger.getAsBoolean());

    // Configure the trigger bindings
    configureBindings();

    m_drivetrain.setDefaultCommand(m_swerveDriveClosedLoop);
    // m_climber.setDefaultCommand(m_manualClimb);
    m_endEffector.setDefaultCommand(m_manualEndEffector);
    m_elevator.setDefaultCommand(m_manualElevator);
    m_groundIntake.setDefaultCommand(m_manualRotateGroundIntake);
    //m_sourceIntake.setDefaultCommand(m_manualSourceIntake);
    m_leds.setDefaultCommand(m_coralLEDs);

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // TODO:

    // ================
    // Driver Controls
    // ================

    /* Drivetrain */

    Button.cont1_controlPadUp.onTrue(m_resetFieldOrientedHeading);
    Button.cont1_leftStickClick1.whileTrue(m_swerveDriveClosedLoopSlowMode);

    //Button.controlPadLeft1.whileTrue(m_sysIDDriveRoutine);
    //Button.leftTrigger1.whileTrue(m_alignAndScoreCoral);
    //Button.buttonX.whileTrue(m_alignAndGetAlgae);
    //Button.buttonA.whileTrue(m_alignAndGetCoral);

    
    

    /* Elevator */
    Button.cont1_plus.onTrue(m_elevatorToStow);
    Button.cont1_buttonA.onTrue(new ConditionalCommand(m_setLevelOne.andThen(new MoveToLevelParallel(m_elevator, m_endEffector, LevelType.CORAL)),
    m_processAlgae, () -> (EndEffector.hasCoral() == true)));
    Button.cont1_buttonB.onTrue(new ConditionalCommand(new SequentialCommandGroup(m_setUpperAlgae, m_toAlgaeHigher), 
    m_setLevelTwo.andThen(new MoveToLevelParallel(m_elevator, m_endEffector, LevelType.CORAL)), () -> (EndEffector.hasCoral() == true)));
    Button.cont1_buttonX.onTrue(new ConditionalCommand(new SequentialCommandGroup(m_setLowerAlgae, m_toAlgaeLower), 
    m_setLevelThree.andThen(new MoveToLevelParallel(m_elevator, m_endEffector, LevelType.CORAL)), () -> (EndEffector.hasCoral() == true)));
    Button.cont1_buttonY.onTrue(new ConditionalCommand(m_setLevelFour.andThen(new MoveToLevelParallel(m_elevator, m_endEffector, LevelType.CORAL)), 
    new BargeAlgae(m_endEffector, m_elevator), () -> (EndEffector.hasCoral() == true)));

    Button.cont1_rightStickClick1.whileTrue(m_alignAndGetAlgae);
    Button.cont1_rightStickClick1.onFalse(m_clampAlgae);


    /* Ground Intake */
    Button.cont1_minus.onTrue(m_groundIntakeStow);
    Button.cont1_controlPadLeft.onTrue(m_goToGroundIntakeAngleIndex);
    Button.cont1_controlPadRight.onTrue(m_goToGroundIntakeAngleIntake);

    // TODO TESTING PURPOSES
    Button.cont1_leftTrigger.onTrue(m_intakeCoralSequence);
    Button.cont1_leftBumper.onTrue(m_indexCoralSequence);
    //Button.cont1_leftBumper.onTrue();

    /* Scoring */
    Button.cont1_rightTrigger.onTrue(new ConditionalCommand(m_scoreCoral, m_bargeAlgaeThrow, 
    () -> (EndEffector.hasCoral() == true)));
    //Button.cont1_leftBumper.onTrue(m_alignAndGetAlgae);
    //Button.cont1_rightBumper.onTrue(m_alignAndGetAlgae);

 
    /* TODO */
    //algaeIntakeTrigger.onTrue(null);
    //algaeIntakeTrigger.onFalse(null);
    //Button.cont1_leftTrigger.onTrue(); TODO Stow + Ground Intake
    

    // ==================
    // Operator Controls
    // ==================

    /* End Effector */
    Button.cont2_rightBumper.whileTrue(m_IntakeAlgae);
    Button.cont2_rightTrigger.onTrue(m_OutakeAlgae);

    /* Ground Intake */
    Button.cont2_leftBumper.whileTrue(m_moveIntake);
    Button.cont2_leftTrigger.whileTrue(m_moveIntakeReversed);

    // Todo implement later
    //Button.cont2_minus.onTrue(new InstantCommand(
    //    () -> m_sourceIntake.setEncoderPosition(0), m_sourceIntake));


    /* Elevator */
    
    Button.cont2_rightStickClick.onTrue(m_elevatorToStow);
    Button.cont2_buttonY.onTrue(m_bargeAlgae);


    Button.cont2_plus.onTrue(new InstantCommand(
        () -> m_elevator.setEncoderPosition(0), m_elevator));


    /* Scoring */
    
    //TODO
    //Button.cont2_buttonB.onTrue(m_setLowerAlgae);
    //Button.cont2_buttonX.onTrue(m_setUpperAlgae);

    //Button.cont2_buttonA.onTrue(m_processAlgae);
    

    robotTipping.onTrue(m_elevatorEmergencyStow);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
        new ResetRelativeEncoders(m_endEffector, m_sourceIntake),
        new ParallelCommandGroup(
          new RotateSourceIntake(m_sourceIntake, 2, Constants.kSourceIntake.INTAKE_ANGLE),
          m_autoSelector.getAutoCommand()));
  }

  public Command getTeleopInitCommand() {
    return null;
    // return new SequentialCommandGroup(
    // new RotateSourceIntake(m_sourceIntake, 2,
    // Constants.kSourceIntake.INTAKE_ANGLE));
  }
}
