// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.LauncherConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.LaunchNote;
import frc.robot.commands.PrepareLaunch;
import frc.robot.subsystems.PWMClimber;
import frc.robot.subsystems.PWMDrivetrain;
import frc.robot.subsystems.PWMLauncher;

// import frc.robot.subsystems.CANDrivetrain;
// import frc.robot.subsystems.CANLauncher;

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

  // The robot's subsystems are defined here.
  private final PWMDrivetrain m_drivetrain = new PWMDrivetrain();
  // private final CANDrivetrain m_drivetrain = new CANDrivetrain();
  private final PWMLauncher m_launcher = new PWMLauncher();
  // private final CANLauncher m_launcher = new CANLauncher();

  private final PWMClimber m_climber = new PWMClimber();

  /*
   * The gamepad provided in the KOP shows up like an XBox controller if the mode
   * switch is set to X mode using the
   * switch on the top.
   */
  private final CommandXboxController m_driverController = new CommandXboxController(
      OperatorConstants.kDriverControllerPort);

  private final Command m_shootAndSitAuto = Autos.shootAndSitAuto(
      m_drivetrain,
      m_launcher);

  private final Command m_shootAndDriveDiagonalBackwardAuto = Autos.shootAndDriveDiagonalBackwardAuto(
      m_drivetrain,
      m_launcher);

  private final Command m_shootAndDriveStraightBackwardAuto = Autos.shootAndDriveStraightBackwardAuto(
      m_drivetrain,
      m_launcher);
  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Shoot and Sit Auto", m_shootAndSitAuto);
    m_chooser.setDefaultOption(
        "Shoot and Drive Diagonal Backward Auto",
        m_shootAndDriveDiagonalBackwardAuto);
    m_chooser.setDefaultOption(
        "Shoot and Drive Straight Backward Auto",
        m_shootAndDriveStraightBackwardAuto);

    SmartDashboard.putData("Auto choices", m_chooser);

    // Put subsystems to dashboard.
    Shuffleboard.getTab("Drivetrain").add(m_drivetrain);
    Shuffleboard.getTab("Launcher").add(m_launcher);
    Shuffleboard.getTab("Climber").add(m_climber);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * accessed via the
   * named factory methods in the Command* classes in
   * edu.wpi.first.wpilibj2.command.button (shown
   * below) or via the Trigger constructor for arbitary conditions
   */
  private void configureBindings() {
    // 1 - DRIVETRAIN CONTROLS
    // Set the default command for the drivetrain to drive using the joysticks
    m_drivetrain.setDefaultCommand(
        new RunCommand(
            () -> m_drivetrain.arcadeDrive(
                -m_driverController.getLeftY(),
                -m_driverController.getRightX()),
            m_drivetrain));

    // 2 - INTAKE CONFIGURATION
    // Set up a binding to run the intake command while the operator is pressing and
    // holding the
    // left Bumper
    m_driverController.leftBumper().whileTrue(m_launcher.getIntakeCommand());

    // 3 - LAUNCH CONFIGURATION
    /*
     * Create an inline sequence to run when the operator presses and holds the A
     * (green) button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command
     */
    m_driverController
        .a()
        .onTrue(
            new PrepareLaunch(m_launcher)
                .withTimeout(LauncherConstants.kLauncherDelay)
                .andThen(new LaunchNote(m_launcher))
                .withTimeout(LauncherConstants.kLauncherRunDuration)
                .andThen(() -> m_launcher.stop())
                .handleInterrupt(() -> m_launcher.stop()));

    // // ******* TRY THIS APPROACH *************
    // Define the command sequence for launching
    // Command launchSequence = new PrepareLaunch(m_launcher)
    // .withTimeout(LauncherConstants.kLauncherDelay)
    // .andThen(new LaunchNote(m_launcher))
    // .withTimeout(LauncherConstants.kLauncherRunDuration)
    // .andThen(() -> m_launcher.stop())
    // .handleInterrupt(() -> m_launcher.stop());

    // m_driverController.a().whenPressed(launchSequence);

    // 4 - CLIMB CONFIGURATION
    m_driverController
        .y()
        .whileTrue(m_climber.ascend().handleInterrupt(() -> m_climber.stop()));
    m_driverController
        .b()
        .whileTrue(m_climber.descend().handleInterrupt(() -> m_climber.stop()));

    // 5 - SLOW LAUNCH CONFIGURATION
    // // Set up a binding to change the speed of the intake command when the
    // operator presses the X button
    // m_driverController
    // .x()
    // .onTrue(
    // new PrepareSlowLaunch(m_launcher)
    // .withTimeout(LauncherConstants.kLauncherDelay)
    // .andThen(new SlowLaunchNote(m_launcher))
    // .withTimeout(LauncherConstants.kLauncherRunDuration)
    // .andThen(() -> m_launcher.stop())
    // .handleInterrupt(() -> m_launcher.stop())
    // );

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // return Autos.exampleAuto(m_drivetrain);
    return m_chooser.getSelected();
  }
}
