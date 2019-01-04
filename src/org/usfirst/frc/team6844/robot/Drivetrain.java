package org.usfirst.frc.team6844.robot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Spark;

public class Drivetrain {

	Spark sparkLeft1, sparkLeft2, sparkRight1, sparkRight2;
	ADXRS450_Gyro gyro;
	Encoder encoderLeft, encoderRight;

	public static final double TICKS_PER_INCH = 2048 / (6 * Math.PI);
	public static final double DISTANCE_PER_PULSE = 1 / TICKS_PER_INCH;

	private boolean reversed = false;
	private double driveScalingFactor = 1;
	private double left, right;

	public Drivetrain() {
		super();

		sparkLeft1 = new Spark(0);
		sparkLeft2 = new Spark(1);
		sparkRight1 = new Spark(2);
		sparkRight2 = new Spark(3);

		sparkLeft1.setInverted(true);
		sparkLeft2.setInverted(true);

		gyro = new ADXRS450_Gyro(Port.kOnboardCS0);

		encoderLeft = new Encoder(2, 3);
		encoderRight = new Encoder(4, 5);

		encoderLeft.setDistancePerPulse(DISTANCE_PER_PULSE);
		encoderRight.setDistancePerPulse(DISTANCE_PER_PULSE);
	}

	public void tankDrive(double left, double right) {
		this.left = left;
		this.right = right;
	}

	public void arcadeDrive(double speed, double turn) {
		if (reversed) {
			turn = - turn;
		}

		tankDrive(speed - turn, speed + turn);
	}

	public void arcadeDrive(double speed, double turn, boolean squareInputs) {
		arcadeDrive(sign(speed) * Math.pow(speed, 2), sign(turn) * Math.pow(turn, 2));
	}

	private double sign(double magnitude) {
		if (magnitude > 0) {
			return 1;
		} else if (magnitude < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public void update() {
		left *= driveScalingFactor;
		right *= driveScalingFactor;

		sparkLeft1.set(left);
		sparkLeft2.set(left);

		sparkRight1.set(right);
		sparkRight2.set(right);
	}

	public void resetGyro() {

		gyro.reset();
	}

	public void calibrateGyro() {

		gyro.calibrate();
	}

	public double getHeading() {
		return gyro.getAngle();
	}

	public void reverseDriveDirection() {

		reversed = !reversed;

		sparkLeft1.setInverted(!sparkLeft1.getInverted());
		sparkLeft2.setInverted(!sparkLeft2.getInverted());

		sparkRight1.setInverted(!sparkRight1.getInverted());
		sparkRight2.setInverted(!sparkRight2.getInverted());
	}

	public void setScalingFactor(double scale) {
		driveScalingFactor = scale;
	}

	public double getScalingFactor() {
		return driveScalingFactor;
	}

	public int getLeftEncoder() {
		return encoderLeft.get();
	}

	public int getRightEncoder() {
		return encoderRight.get();
	}

	public double getLeftEncoderDistance() {
		return encoderLeft.getDistance();
	}

	public double getRightEncoderDistance() {
		return encoderRight.getDistance();
	}

	public void resetEncoders() {
		encoderLeft.reset();
		encoderRight.reset();
	}

	public void nerfSpeed() {
		if (driveScalingFactor == 1) {

			driveScalingFactor = .5;
		} else if (driveScalingFactor == .5){

			driveScalingFactor = 1;
		}
	}


	public Set<String> getStateLogFields() {
		Set<String> fields = new HashSet<>();

		fields.add("left");
		fields.add("right");
		fields.add("driveScalingFactor");

		fields.add("sparkLeft1PWM");
		fields.add("sparkLeft2PWM");
		fields.add("sparkRight1PWM");
		fields.add("sparkRight2PWM");

		fields.add("gyroHeading");

		fields.add("encoderLeftTicks");
		fields.add("encoderRightTicks");
		fields.add("encoderLeftDistance");
		fields.add("encoderRightDistance");

		return fields;
	}


	public Map<String, Object> logState() {
		Map<String, Object> state = new HashMap<>();

		state.put("left", left);
		state.put("right", right);
		state.put("driveScalingFactor", driveScalingFactor);

		state.put("sparkLeft1PWM", sparkLeft1.get());
		state.put("sparkLeft2PWM", sparkLeft2.get());
		state.put("sparkRight1PWM", sparkRight1.get());
		state.put("sparkRight2PWM", sparkRight2.get());

		state.put("gyroHeading", gyro.getAngle());
		state.put("encoderLeftTicks", encoderLeft.get());
		state.put("encoderRightTicks", encoderRight.get());
		state.put("encoderLeftDistance", encoderLeft.getDistance());
		state.put("encoderRightDistance", encoderRight.getDistance());

		return state;
	}
}
