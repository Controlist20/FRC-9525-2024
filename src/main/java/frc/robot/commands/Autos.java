// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.LauncherConstants;
import frc.robot.subsystems.PWMDrivetrain;
import frc.robot.subsystems.PWMLauncher;

import static frc.robot.Constants.AutoConstants.*;

// import frc.robot.subsystems.CANDrivetrain;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static Command exampleAuto(PWMDrivetrain drivetrain) {
    /**
     * RunCommand is a helper class that creates a command from a single method, in this case we
     * pass it the arcadeDrive method to drive straight back at half power. We modify that command
     * with the .withTimeout(1) decorator to timeout after 1 second, and use the .andThen decorator
     * to stop the drivetrain after the first command times out
     */
    return new RunCommand(() -> drivetrain.arcadeDrive(kAutoDrivePower, 0), drivetrain)
        .withTimeout(kAutoTimeout)
        .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain));
  }

    /** Example static factory for an autonomous command. */
    public static Command simpleAuto(PWMDrivetrain drivetrain) {
      return new RunCommand(() -> drivetrain.arcadeDrive(kAutoDrivePower, 0), drivetrain)
          .withTimeout(kAutoTimeout)
          .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain));
    }

  public static Command shootAndSitAuto(PWMDrivetrain drivetrain, PWMLauncher launcher) {

    return Commands.sequence(
      // Drive forward 
      // new RunCommand(() -> drivetrain.arcadeDrive(AutoConstants.kAutoDrivePower, 0), drivetrain)
      //   .withTimeout(kAutoTimeout)
      //   .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)),
  
      // Launch the note
      new PrepareLaunch(launcher)
              .withTimeout(LauncherConstants.kLauncherDelay)
              .andThen(new LaunchNote(launcher))
              .withTimeout(LauncherConstants.kLauncherRunDuration)
              .andThen(() -> launcher.stop())
              .handleInterrupt(() -> launcher.stop())

      // // Drive backward 
      // new RunCommand(() -> drivetrain.arcadeDrive(-AutoConstants.kAutoDrivePower, 0), drivetrain)
      // .withTimeout(kAutoTimeout)
      // .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain))
      );
  }

  public static Command shootAndDriveDiagonalBackwardAuto(PWMDrivetrain drivetrain, PWMLauncher launcher) {

    return Commands.sequence(
      // // Drive forward 
      // new RunCommand(() -> drivetrain.arcadeDrive(AutoConstants.kAutoDrivePower, 0), drivetrain)
      //   .withTimeout(kAutoTimeout)
      //   .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)),
  
      // Launch the note
      new PrepareLaunch(launcher)
              .withTimeout(LauncherConstants.kLauncherDelay)
              .andThen(new LaunchNote(launcher))
              .withTimeout(LauncherConstants.kLauncherRunDuration)
              .andThen(() -> launcher.stop())
              .handleInterrupt(() -> launcher.stop()),

      // // Drive backward 
      new RunCommand(() -> drivetrain.arcadeDrive(-0.7, 0), drivetrain)
      .withTimeout(1.9*kAutoTimeout)
      // .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0.75, 0), drivetrain))

      );
  }

    public static Command shootAndDriveStraightBackwardAuto(PWMDrivetrain drivetrain, PWMLauncher launcher) {

    return Commands.sequence(
      // // Drive forward 
      // new RunCommand(() -> drivetrain.arcadeDrive(AutoConstants.kAutoDrivePower, 0), drivetrain)
      //   .withTimeout(kAutoTimeout)
      //   .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain)),
  
      // Launch the note
      new PrepareLaunch(launcher)
              .withTimeout(LauncherConstants.kLauncherDelay)
              .andThen(new LaunchNote(launcher))
              .withTimeout(LauncherConstants.kLauncherRunDuration)
              .andThen(() -> launcher.stop())
              .handleInterrupt(() -> launcher.stop()),

      // // Drive backward 
      new RunCommand(() -> drivetrain.arcadeDrive(-0.65, 0), drivetrain)
      .withTimeout(1.3*kAutoTimeout)
      // .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0.75, 0), drivetrain))

      );

  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
