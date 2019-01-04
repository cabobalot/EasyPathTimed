package org.usfirst.frc.team6844.robot;


import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Drivetrain extends Subsystem {

	Spark sparkLeft1, sparkLeft2, sparkRight1, sparkRight2;
	ADXRS450_Gyro gyro;
	Encoder encoderLeft, encoderRight;

	public static final double TICKS_PER_INCH = 2048 / (6 * Math.PI);
	public static final double DISTANCE_PER_PULSE = 1 / TICKS_PER_INCH;

	private boolean reversed = false;

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

	public void setLeftRightSpeeds(double left, double right) {
		sparkLeft1.set(left);
		sparkLeft2.set(left);

		sparkRight1.set(right);
		sparkRight2.set(right);
	}
	
	public double getTotalDistance() {
		return (encoderLeft.getDistance() + encoderRight.getDistance()) / 2.0d;
	}
	
	public void reset() {
		resetEncoders();
		resetGyro();
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


	public void resetEncoders() {
		encoderLeft.reset();
		encoderRight.reset();
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
