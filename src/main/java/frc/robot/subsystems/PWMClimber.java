// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PWMClimber extends SubsystemBase {
  PWMSparkMax m_climbMotor;
  DigitalInput bottomSwitch;
  DigitalInput topSwitch;
  public double m_currentHeight;

  /** Creates a new Climber. */
  public PWMClimber() {
    m_climbMotor = new PWMSparkMax(kclimberID);
    m_currentHeight = 10; // Initialize the current height
    topSwitch = new DigitalInput(1); // Tells the climber to stop ascending
    bottomSwitch = new DigitalInput(2); // Tells the climber to stop descending
  }

  public Command ascend() {
    // Climber will ascend the robot while the top switch is false
    return this.startEnd(
        () -> {
          // Start ascending until the top switch turns TRUE
          while (!topSwitch.get()) {
            setClimbSpeed(kclimberAscendSpeed);
          }
          stop();
        },
        () -> {
          stop();
        });
  }

  public Command descend() {
    return this.startEnd(
        () -> {
          while (bottomSwitch.get()) {
            setClimbSpeed(kclimberDescendSpeed);
          }
          stop();
        },
        () -> {
          stop();
        });
  }
  // public Command ascend() {
  // return this.startEnd(
  // // Starts as
  // () -> {
  // setClimbSpeed(kclimberAscendSpeed);
  // },
  // // When the command stops, stop the wheels
  // () -> {
  // stop();
  // });
  // }

  // public Command descend() {
  // return this.startEnd(
  // // When the command is initialized, set the wheels to the intake speed values
  // () -> {
  // setClimbSpeed(kclimberDescendSpeed);
  // },
  // // When the command stops, stop the wheels
  // () -> {
  // stop();
  // });
  // }


  /**
   * An accessor method to set the speed (technically the output percentage) of
   * the climb motor
   */
  public void setClimbSpeed(double speed) {
    m_climbMotor.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Switch Top", this.topSwitch.get());
    SmartDashboard.putBoolean("Switch Bottom", this.bottomSwitch.get());
  }

  // A helper method to stop climb motor. You could skip having a method like this
  // and call the
  // individual accessors with speed = 0 instead
  public void stop() {
    m_climbMotor.set(0);
  }
}
