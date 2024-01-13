/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7245.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


public class Robot extends IterativeRobot {
	Spark Left = new Spark(1);
	Spark Right = new Spark(0);
	Spark elevLift = new Spark(2);
	DigitalInput limitSwitch = new DigitalInput(3);
	long autonTime;
	
	DifferentialDrive drive = new DifferentialDrive(Left, Right);
	Joystick joystick = new Joystick(1);
	//XboxController controller= new XboxController(0);
	JoystickButton button1 = new JoystickButton(joystick,1);
	JoystickButton button2 = new JoystickButton(joystick,2);
	JoystickButton button7 = new JoystickButton(joystick,7);
	JoystickButton button8 = new JoystickButton(joystick,8);
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	DifferentialDrive intakeDrive = new DifferentialDrive(new Spark(3), new Spark(4));

	
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		autonTime = System.currentTimeMillis();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		drive.setSafetyEnabled(false);
		intakeDrive.setSafetyEnabled(false);
	}

	@Override
	public void autonomousPeriodic() {
		/*switch (m_autoSelected) {
			case kCustomAuto:	
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}*/
		
		if(System.currentTimeMillis()-autonTime < 4000)
		{
			elevLift.set(0.9);
		}
		else {
			elevLift.set(0);
		}
		if(System.currentTimeMillis()-autonTime < 4500 && System.currentTimeMillis()-autonTime > 1000)
		{
			drive.tankDrive(0.7, 0.6);
		}
		else {
			
			drive.stopMotor();
			
		}
		
		if (DriverStation.getInstance().getGameSpecificMessage().toLowerCase().charAt(0) == 'l' 
				&& System.currentTimeMillis()-autonTime > 4500
				&& System.currentTimeMillis()-autonTime < 6000)
		{
			
				intakeDrive.tankDrive(1, 1);
			
		}
		else {
			intakeDrive.tankDrive(-0.8,-0.8);
		}
		
		
	}

	public double circleMap(double input) {
		
		double k = (1.95 - 0.35)/1.3;
		double R = Math.pow(1 + Math.pow((1-k),4), 0.25);
		
		return Math.signum(input)*(k - Math.sqrt(Math.pow(R, 2) - Math.pow(input, 2)));
	}
	
	public double cubicMap(double input) {
		
		double k = 1.29996410157;
		double R = -1.00891701994;
		
		return Math.signum(input)*(k + Math.pow(Math.pow(R, 3) + Math.pow(Math.abs(input),
				3),1/3));
	}
	@Override
	public void teleopPeriodic() {
		
			
//		drive.arcadeDrive()-joystick.getY(), -(joystick.getTwist());
		drive.arcadeDrive(-(joystick.getY()), joystick.getTwist());
		System.out.println(joystick.getY());
		if(button7.get())
		{
			elevLift.set(0.8);
		}
		else if(button8.get())
		{
			elevLift.set(-0.8);
		}
		else
		{
			elevLift.set(0);
		}
		
		if(button1.get())
		{
			intakeDrive.tankDrive(1,1);
		}
		/*else if(joystick.getPOV() == 90)
		{
			intakeDrive.tankDrive(1,-1);
		}*/
		else if(button2.get())
		{
			intakeDrive.tankDrive(-1, -1);
		}
		/*else if(joystick.getPOV() == 270)
		{
			intakeDrive.tankDrive(-1, 1);
		}*/
		else 
		{
			intakeDrive.stopMotor();
		}
		
	}

	@Override
	public void testPeriodic() {
	}
}
