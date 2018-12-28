package org.usfirst.frc.team6844.robot;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;



import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GhostController implements HuskyClass {
	
	private final int SWITCH_DROP_DISTANCE = 1000;
	private final int SCALE_DROP_DISTANCE = 0;
	
	private ArrayList<AutoTask> taskList = new ArrayList<AutoTask>();
	
	private int counter;
	private double mapTargX;
	private double mapTargY;
	private double oldTargX;
	private double oldTargY;
	private int mapUpdate;
	
	private double[] chassisInfo;
	
	private double[] clawInfo;
	private double[] posInfo;
	private double[] teleTargPos;	//experemental
	private double teleTargAngle;
	
	private double[] targPoint = {-1, -1};
	private double oldTime = -1;
	
	private Chassis chassis;
	private PositionTracker tracker;
	
	private HuskyJoy driveJoy;
	private HuskyJoy weaponsJoy;
	
	private SendableChooser<String> firstAutoChooser = new SendableChooser<>();
	private SendableChooser<String> secondAutoChooser = new SendableChooser<>();
	private SendableChooser<Boolean> twoCubeChooser = new SendableChooser<>();
	private Timer timer = new Timer();

	public GhostController(Chassis Ch, Arm A, Claw Cl, ArmExtender AA, Winch W, PositionTracker T,  HuskyJoy DJ, HuskyJoy WJ) {
		chassis = Ch;

		tracker = T;
		driveJoy = DJ;
		weaponsJoy = WJ;
		
		
	}
	
	public void dashInit() {
		firstAutoChooser.addDefault("Switch inside", "sw_in");
		firstAutoChooser.addObject("Switch outside (not center)", "sw_out");
		firstAutoChooser.addObject("Scale outside", "sc_out");
		firstAutoChooser.addObject("Auto run", "auto_run");
		SmartDashboard.putData("First auto", firstAutoChooser);
		
		secondAutoChooser.addDefault("Switch inside", "sw_in");
		secondAutoChooser.addObject("Switch outside (not center)", "sw_out");
		secondAutoChooser.addObject("Scale outside", "sc_out");
		secondAutoChooser.addObject("Auto run", "auto_run");
		SmartDashboard.putData("Second auto", secondAutoChooser);
		
		twoCubeChooser.addDefault("No", false);
		twoCubeChooser.addDefault("Yes", true);
		SmartDashboard.putData("Do a two cube auto?", twoCubeChooser);
		
	}
	
	@Override
	public void teleopInit() {
		teleTargPos = new double[] {0, 0};
		teleTargAngle = 0;
		
		

	}

	@Override
	public void doTeleop() {
		
		posInfo = tracker.getInfo();
		
		SmartDashboard.putNumber("sonar inch", posInfo[3]);
		
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
		targPoint[0] = -1.0d;
		targPoint[1] = -1.0d;
		mapUpdate = 0;
		oldTargX = 0;
		oldTargY = 0;
		oldTime = -1.0;
		SmartDashboard.putNumber("mapUpdate", mapUpdate);
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
			
			switch(taskList.get(counter).getType()){
			
			case easyPath:
				if(driveEasyPath(taskList.get(counter).getInfo())) {
					counter++;
				}
				break;
			
			default:
				chassis.giveInfo(new double[] {0, 0});
				break;
				
			}
		}
//		*/
		
		
		
	}


	private boolean driveEasyPath(double[] info) {
		// TODO Auto-generated method stub
		return false;
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



