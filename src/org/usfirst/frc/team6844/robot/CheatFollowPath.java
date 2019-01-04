package org.usfirst.frc.team6844.robot;

import java.util.function.Function;

import easypath.FollowPath;
import easypath.Path;


public class CheatFollowPath extends FollowPath {

	
	/**
	   * Constructor for the command for the robot to actually follow the path. All math is done in the
	   * execute method. You should provide a Path object and a speed function in the form of a lambda
	   * or method reference, for example:
	   *
	   * <pre>{@code
	   * new FollowPath(myPath, p -> {
	   *     // If we are less than 25% done with the path, drive at 30% speed
	   *     if (p < 0.25) {
	   *         return 0.3;
	   *     }
	   *     // If we are between 25% and 90% done with the path, drive at 80% speed
	   *     else if (0.25 <= p && p < 90) {
	   *         return 0.8;
	   *     }
	   *     // Otherwise drive at 25% speed
	   *     else return 0.25;
	   * });
	   * }</pre>
	   *
	   * @param path the path object the robot should follow
	   * @param speedFunction the function describing the speed the robot should drive along the path,
	   * accepting a double representing the percentage completion of the path and returning a double
	   * representing the speed the robot should drive at
	   */
	  public CheatFollowPath(Path path, Function<Double, Double> speedFunction) {
	    super(path, speedFunction);
	  }

	  /**
	   * Constructor for the command for the robot to actually follow the path. See the other
	   * constructor more info. The difference with this constructor is that it takes in a constant
	   * speed for the robot to drive at along the entire path.
	   *
	   * @param path the path object the robot should follow
	   * @param constantSpeed the constant speed the robot should drive at along the course of the path
	   */
	  public CheatFollowPath(Path path, double constantSpeed) {
	    super(path, constantSpeed);
	  }

	  /**
	   * Constructor for the command for the robot to actually follow the path. All math is done in the
	   * execute method. You should provide a Path object and a speed function in the form of a lambda
	   * or method reference, for example:
	   *
	   * <pre>{@code
	   * new FollowPath(myPath, p -> {
	   *     // If we are less than 25% done with the path, drive at 30% speed
	   *     if (p < 0.25) {
	   *         return 0.3;
	   *     }
	   *     // If we are between 25% and 90% done with the path, drive at 80% speed
	   *     else if (0.25 <= p && p < 90) {
	   *         return 0.8;
	   *     }
	   *     // Otherwise drive at 25% speed
	   *     else return 0.25;
	   * });
	   * }</pre>
	   *
	   * @param path the path object the robot should follow
	   * @param speedFunction the function describing the speed the robot should drive along the path,
	   * accepting a double representing the percentage completion of the path and returning a double
	   * representing the speed the robot should drive at
	   * @param shiftAt the percentage of the path that you want your drive train to shift up at; e.g.
	   * 0.5 if you want to shift halfway through the path
	   */
	  public CheatFollowPath(Path path, Function<Double, Double> speedFunction, double shiftAt) {
	    super(path, speedFunction, shiftAt);
	  }

	  /**
	   * Constructor for the command for the robot to actually follow the path. See the other
	   * constructor more info. The difference with this constructor is that it takes in a constant
	   * speed for the robot to drive at along the entire path.
	   *
	   * @param path the path object the robot should follow
	   * @param constantSpeed the constant speed the robot should drive at along the course of the path
	   * @param shiftAt the percentage of the path that you want your drive train to shift up at; e.g. *
	   * 0.5 if you want to shift halfway through the path
	   */	
	CheatFollowPath(Path path, double constantSpeed, double shiftAt) {
		super(path, constantSpeed, shiftAt);
	}
	
	
	
	public void initialize() {
		super.initialize();
	}
	
	public void execute() {
		super.execute();
	}
	
	public boolean isFinished() {
		return super.isFinished();
	}
	
	public void end() {
		super.end();
	}
	
	public void interrupted() {
		super.interrupted();
	}
	

}







