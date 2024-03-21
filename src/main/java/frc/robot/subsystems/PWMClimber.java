// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.ClimberConstants.*;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PWMClimber extends SubsystemBase {
  PWMSparkMax m_climbMotor;
  private double m_currentHeight;

  /** Creates a new Climber. */
  public PWMClimber() {
    m_climbMotor = new PWMSparkMax(kclimberID);
    m_currentHeight = 7; // Initialize the current height

  }

  /**
   * This method is an example of the 'subsystem factory' style of command creation. A method inside
   * the subsytem is created to return an instance of a command. This works for commands that
   * operate on only that subsystem, a similar approach can be done in RobotContainer for commands
   * that need to span subsystems. The Subsystem class has helper methods, such as the startEnd
   * method used here, to create these commands.
   */
  public Command ascend() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
        // When the command is initialized, set the wheels to the intake speed values
        () -> {
          setClimbSpeed(kclimberAscendSpeed);
        },
        // When the command stops, stop the wheels
        () -> {
          stop();
        });
  }

public Command descend() {
    // The startEnd helper method takes a method to call when the command is initialized and one to
    // call when it ends
    return this.startEnd(
    // When the command is initialized, set the wheels to the intake speed values
    () -> {
        setClimbSpeed(kclimberDescendSpeed);
    },
    // When the command stops, stop the wheels
    () -> {
        stop();
    });
  }


  public Command ascend2() {
    return this.startEnd(
        () -> {
            setClimbSpeed(kclimberAscendSpeed);
        },
        () -> {
            stop();
        }
    );//.until(m_currentHeight <= kclimbMaxHeight); // Interrupt when max height reached
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
