package org.usfirst.frc.team6844.robot;

import java.util.ArrayList;

import easypath.EasyPath;
import easypath.EasyPathConfig;
import easypath.PathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GhostController implements HuskyClass {
	
	private ArrayList<AutoTask> taskList = new ArrayList<AutoTask>();
	
	private int counter;
	
	private double[] chassisInfo;
	
	private double[] posInfo;
	private double[] teleTargPos;	//experemental
	
	private int taskIteration = -1;
	
	
	
	private Timer timer = new Timer();
	
	private Drivetrain dt = new Drivetrain();
	
	EasyPathConfig config = new EasyPathConfig(
	        dt, // the subsystem itself
	        dt::setLeftRightSpeeds, // function to set left/right speeds
	        // function to give EasyPath the length driven
	        dt::getTotalDistance,
	        dt::getHeading, // function to give EasyPath the heading of your robot
	        dt::reset, // function to reset your encoders to 0
	        0.07 // kP value for P loop
	    );
	
	// This drives 36 inches in a straight line, driving at 25% speed the first 50% of the path,
    // and 75% speed in the remainder.
    // x is the percentage completion of the path, between 0 and 1.
	CheatFollowPath m_autonomousCommand = new CheatFollowPath(
	        PathUtil.createStraightPath(36.0), x -> {
	          if (x < 0.5) return 0.25;
	          else return 0.75;
	        });
	
	CheatFollowPath[] customCommands = {m_autonomousCommand};
	
	
	
	public GhostController() {

		
		EasyPath.configure(config);
		
		
		
	}
	
	
	@Override
	public void teleopInit() {

	}

	@Override
	public void doTeleop() {
		
			//normal drive (slider)
//		chassis.giveInfo(new double[] {-driveJoy.getSliderScaled(1), driveJoy.getSliderScaled(2)});
		
			//non slider drive
		/*
		if(weaponsJoy.getRawButton(9) || driveJoy.getRawButton(9)) {
			chassis.giveInfo(new double[] {-1.0, 0.0});
			arm.setAntiFall(true);
		}
		else {
			chassis.giveInfo(new double[] {-driveJoy.getDeadAxis(1, 0.1, 0.1), driveJoy.getRawAxis(2)});
			arm.setAntiFall(false);
		}
		
		
			//climb
		if (weaponsJoy.getRawButton(1)) {
			double[] actInfo = actuator.getInfo();
			double[] winchInfo = winch.getInfo();
			
			
			//(((-getRawAxis(3) + 1) / 4) + 0.5)
//			winch.giveInfo(new double[] {-(((-weaponsJoy.getRawAxis(3) + 1) / 4) + 0.5)});
			winch.giveInfo(new double[] {-1});
			actuator.setCliming(true);
			//actuator.giveInfo(winchInfo);
		}
		else if (weaponsJoy.getRawButton(7)) {
			winch.giveInfo(new double[] {(((-weaponsJoy.getRawAxis(3) + 1) / 4) + 0.5)});
		}
		else {
			winch.giveInfo(new double[] {0});
			actuator.setCliming(false);
			actuator.giveArmAngle(arm.getInfo()[0]);
		}
		
		
		
		
		/*	//angle accel turn
		teleTargAngle += joy.getSliderScaled(2) * 5;
		
		SmartDashboard.putNumber("joy", joy.getSliderScaled(2));
		
		chassis.giveInfo(new double[] {-joy.getSliderScaled(1),
				angleAccel(posInfo[2], teleTargAngle)});
		*/
		
		
		//fake mecanum
		/*
		teleTargPos[0] += (joy.getSliderScaled(0) / 10);
		teleTargPos[1] += (-joy.getSliderScaled(1) / 10);
		if(driveTo(teleTargPos)) {
			pointAt(0);
		}
//		*/
		
		
		
	}

	@Override
	public void autoInit() {
		String gameInfo = DriverStation.getInstance().getGameSpecificMessage();
		taskList.clear();
		
		
		taskList.add(new AutoTask(TaskType.easyPath, new double[] {0}));
		
		
		
		
		taskList.add(new AutoTask(TaskType.stop, new double[] {0}));
		
		counter = 0;
		taskIteration = -1;
		timer.reset();
		timer.start();

	}

	@Override
	public void doAuto() {
		//25' 3.5"
		//left -3452.0
		//right -3464.0
		
		//102' 10"
		//right -14032.0
		//left -13976.0
		
		/*		calibrate encoders
		posInfo = tracker.getInfo();
		if ((posInfo[1]-1) < 90) { // 25 3.5
			chassis.giveInfo(new double[] {0.7, angleAccel(posInfo[2], 0)});
			
		}
		else if ((posInfo[1]-1) < 100) {
			chassis.giveInfo(new double[] {0.5, angleAccel(posInfo[2], 0)});
		}
		else {
			chassis.giveInfo(new double[] {0, 0});
		}
		*/
		
		
//		driveTo(new double[] {13, 6.5}, false);
//		SmartDashboard.putBoolean("at targ?", pointAt(45));
		
//		/*				//main stuff
		
		if(counter < taskList.size()) {
			
			SmartDashboard.putNumber("auto item #", counter);
			
			taskIteration++;
			
			switch(taskList.get(counter).getType()){
			
			case easyPath:
				if(driveEasyPath(customCommands[(int) taskList.get(counter).getInfo()[0]])) {
					counter++;
					taskIteration = -1;
				}
				break;
			
			case stop:
				dt.setLeftRightSpeeds(0, 0);
				taskIteration = -1;
				break;
			
			default:
				dt.setLeftRightSpeeds(0, 0);
				taskIteration = -1;
				break;
				
			}
			
			
		}
//		*/
		
		
		
	}


	private boolean driveEasyPath(CheatFollowPath command) {
		
		if (taskIteration < 1) {
			command.initialize();;
		}
		else {
			command.execute();
		}
		
		if (command.isFinished()) {
			command.end();
			return true;
		}
		else {
			return false;
		}
		
	}

	
	
	
	
	@Override
	public double[] getInfo() {
		
		return null;
	}

	@Override
	public void giveInfo(double[] info) {
		// TODO Auto-generated method stub
		
	}
	

	
}



