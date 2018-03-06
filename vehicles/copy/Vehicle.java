package ca.ubc.ece.cpen221.mp4.vehicles.copy;

import ca.ubc.ece.cpen221.mp4.Actor;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.items.MoveableItem;

public interface Vehicle extends MoveableItem, Actor{
	
	/**
	 * increases the speed of the vehicle by an amount determined by the
	 * vehicle type
	 */
	void speedUp();
	
	/**
	 * decreases the speed of the vehicle by an amount determined by the
	 * vehicle type
	 */
	void slowDown();
	
	/**
	 * stops the vehicles motion
	 * @return a WaitCommand 
	 */
	Command stop();
	
	/**
	 * turns the vehicle in the direction 'dir' and decreases the speed
	 * of the vehicle to a speed dependent on the vehicle type
	 * 
	 * @param dir the direction turned towards
	 */
	void turn(Direction dir);
	
	/**
	 * retrieves the next action the vehicle is to take as determined by the 
	 * chosen AI
	 * 
	 * @param world the world the vehicle is a part of
	 * @return the command indicating what action to take
	 */
	Command getNextAction(World world);
	
	/**
	 * retrieves the vehicles cooldown period
	 * 
	 * @return the vehicles cooldown period
	 */
	int getCoolDownPeriod();
	
	/**
	 * retrieves the direction that the vehicle is currently moving
	 * 
	 * @return the vehicles direction of travel
	 */
	Direction getDirection();
}
