package org.usfirst.frc.team6844.robot;

public interface HuskyClass {
	
	public void teleopInit();
	
	public void doTeleop();
	
	public void autoInit();
	
	public void doAuto();
	
	public double[] getInfo();
	
	public void giveInfo(double[] info);
}
