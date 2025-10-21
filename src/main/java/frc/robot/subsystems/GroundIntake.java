// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.TalonFXPositionalSubsystem;
import frc.lib.config.CTREConfigs;
import frc.robot.Constants;

public class GroundIntake extends TalonFXPositionalSubsystem {

  private final double DEFAULT_POSITION = 0;

  private final TalonFX intakeMotor;

  private final CANrange coralDetector;

  /** Creates a new GroundIntake. */
  public GroundIntake() {
    super(new int[] {Constants.kGroundIntake.ROTATION_MOTOR}, 
    new boolean[] {true}, 
    Constants.kGroundIntake.KP, 
    Constants.kGroundIntake.KI, 
    Constants.kGroundIntake.KD, 
    0.1, 
    Constants.kGroundIntake.SENSOR_TO_MECHANISM_RATIO, 
    GravityTypeValue.Arm_Cosine, 
    Constants.kGroundIntake.POSITION_CONVERSION_FACTOR,
    Constants.kGroundIntake.VELOCITY_CONVERSION_FACTOR,
    Constants.CTRE_CONFIGS.groundIntakeFXConfig);

    intakeMotor = new TalonFX(Constants.kGroundIntake.INTAKE_MOTOR);
    coralDetector = new CANrange(Constants.kGroundIntake.CAN_RANGE_ID);

  }

  public double getDefaultPosition(){
    return DEFAULT_POSITION;
  }

/**
   * Sets the current position as a PID setpoint
   * and automatically applies anti-gravity feedforward
   */
  public void maintainPosition() {
    if (getPositionTargetReference() != getPositions()[0]) {
      setPosition(getPositions()[0]);
    }
  }

  public void resetRelativeEncoder(){
    setEncoderPosition(DEFAULT_POSITION);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
