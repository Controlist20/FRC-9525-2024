// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PWMClimber extends SubsystemBase {

  PWMSparkMax m_climbMotor;
  private double m_currentHeight;

  /** Creates a new Climber. */
  public PWMClimber() {
    m_climbMotor = new PWMSparkMax(kclimberID);
    m_currentHeight = 7; // Initialize the current height
  }

  public Command ascend() {
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          setClimbSpeed(kclimberAscendSpeed);
        },
        // When the command stops, stop the wheels
        () -> {
          stop();
        }
      );
  }

  public Command ascend2() {
    return this.startEnd(
        () -> {
          setClimbSpeed(kclimberAscendSpeed);
        },
        () -> {
          stop();
        }
      ); //.until(m_currentHeight <= kclimbMaxHeight); // Interrupt when max height reached
  }

  public Command ascend3() {
    return new CommandBase() {
      @Override
      public void initialize() {
        setClimbSpeed(kclimberAscendSpeed);
      }

      @Override
      public void execute() {
        if (m_currentHeight >= kclimberMaxHeight) {
          stop();
        }
        updateHeight(kclimberAscendSpeed);
      }

      @Override
      public boolean isFinished() {
        return m_currentHeight >= kclimberMaxHeight;
      }

      @Override
      public void end(boolean interrupted) {
        stop();
      }
    };
  }

  public Command descend() {
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          setClimbSpeed(kclimberDescendSpeed);
        },
        // When the command stops, stop the wheels
        () -> {
          stop();
        }
      );
  }

  public Command descend2() {
    return this.startEnd(
        () -> {
          while (m_currentHeight > kclimbMinHeight) {
            setClimbSpeed(kclimberDescendSpeed);
            // Update height while descending
            updateHeight(kclimberDescendSpeed);
          }
          stop();
        },
        () -> {
          stop();
        }
      );
  }

  public Command descend2() {
    return new CommandBase() {
      @Override
      public void initialize() {
        setClimbSpeed(kclimberDescendSpeed);
      }

      @Override
      public void execute() {
        if (m_currentHeight <= kclimberMinHeight) {
          stop();
        }
        updateHeight(kclimberDescendSpeed);
      }

      @Override
      public boolean isFinished() {
        return m_currentHeight <= kclimberMinHeight;
      }

      @Override
      public void end(boolean interrupted) {
        stop();
      }
    };
  }

  /**
   * An accessor method to set the speed (technically the output percentage) of the climb motor
   */
  public void setClimbSpeed(double speed) {
    m_climbMotor.set(speed);
    updateHeight(speed); // Update height while climbing or descending
  }

  // Update the current height based on climb speed
  private void updateHeight(double speed) {
    // Assuming the climb speed is in meters per second and time interval is in seconds
    double timeInterval = 0.1; // Example time interval (in seconds)
    double climbSpeed = speed; // Example climb speed (in meters per second)

    // Update the current height based on climb speed and time interval
    m_currentHeight += climbSpeed * timeInterval;
  }

  // A helper method to stop climb motor. You could skip having a method like this and call the
  // individual accessors with speed = 0 instead
  public void stop() {
    m_climbMotor.set(0);
  }
}
