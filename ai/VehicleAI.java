package ca.ubc.ece.cpen221.mp4.ai;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.DestroyCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.vehicles.copy.Vehicle;

/**
 * A simple vehicle AI
 */
public class VehicleAI {
	protected Vehicle veh;
	protected World world;
	
	
	public VehicleAI() {
	}
	
	/** 
	 * Decides the next action to be taken, given the state of the World and the
	 * vehicle.
	 *
	 * @param world
	 *            the current World
	 * @param animal
	 *            the vehicle waiting for the next action
	 * @return the next action for vehicle
	 */
	public Command getNextAction(World world, Vehicle veh) {
		this.veh = veh;
		this.world = world;
		Command com = null;
		
		//vehicle always tries to speed up
		veh.speedUp();
		
		//vehicle always maintains direction unless a wall is infront of it
		Direction dir = veh.getDirection();
		
		Location targetLocation = new Location(veh.getLocation(), dir);
		if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
			return new MoveCommand(veh, targetLocation);
		}
		
		//if the position is not empty the vehicle attempts to run over the object
		else{
			com = destroyCheck(targetLocation);
		}
		
		if (com != null){
			return com;
		}
		
		//otherwise it must be against a wall so it will turn to a random direction along the wall
		//this slows the vehicle down
		else {
			if (dir == Direction.EAST) {
				int temp = (Math.random() <= 0.5) ? 1 : 2;
				if (temp == 1 && !veh.getLocation().equals(new Location(world.getWidth(), world.getHeight()))){
					dir = Direction.SOUTH;
				}
				else if(!veh.getLocation().equals(new Location(world.getWidth(),0))) {
					dir = Direction.NORTH;
				}
				else {
					dir = Direction.WEST;
				}
				targetLocation = new Location(veh.getLocation(), dir);
				
				com = destroyCheck(targetLocation);
				
				if (com != null){
					return com;
				}
				
				if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
					return new MoveCommand(veh, targetLocation);
				}
				
			}
			
			if (dir == Direction.WEST) {
				int temp = (Math.random() <= 0.5) ? 1 : 2;
				if (temp == 1 && !veh.getLocation().equals(new Location(0, world.getHeight()))){
					dir = Direction.SOUTH;
				}
				else if (!veh.getLocation().equals(new Location(0,0))){
					dir = Direction.NORTH;
				}
				else {
					dir = Direction.EAST;
				}
				targetLocation = new Location(veh.getLocation(), dir);
				
				com = destroyCheck(targetLocation);
				
				if (com != null){
					return com;
				}
				
				if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
					return new MoveCommand(veh, targetLocation);
				}
			}
			
			if (dir == Direction.SOUTH) {
				int temp = (Math.random() <= 0.5) ? 1 : 2;
				if (temp == 1 && !veh.getLocation().equals(new Location(world.getWidth(), world.getHeight()))){
					dir = Direction.EAST;
				}
				else if (!veh.getLocation().equals(new Location(0, world.getHeight()))) {
					dir = Direction.WEST;
				}
				else {
					dir = Direction.NORTH;
				}
				targetLocation = new Location(veh.getLocation(), dir);
				
				com = destroyCheck(targetLocation);
				
				if (com != null){
					return com;
				}
				
				if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
					return new MoveCommand(veh, targetLocation);
				}
			}
			
			if (dir == Direction.NORTH) { 
				int temp = (Math.random() <= 0.5) ? 1 : 2;
				if (temp == 1 && !veh.getLocation().equals(new Location(world.getWidth(), 0))){
					dir = Direction.EAST;
				}
				else if (!veh.getLocation().equals(new Location(0,0))){
					dir = Direction.WEST;
				}
				else {
					dir = Direction.SOUTH;
				}
				targetLocation = new Location(veh.getLocation(), dir);
				
				com = destroyCheck(targetLocation);
				
				if (com != null){
					return com;
				}
				
				if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
					return new MoveCommand(veh, targetLocation);
				}
			}
		}
		return veh.stop();
	}
	
	/**
	 * checks whether or not the vehicle is in a collision 
	 * 
	 * @param targetLocation the location directly in front of the vehicle
	 * @return either a command to destroy the vehicle, the item at the targetLocation or null
	 */
	public Command destroyCheck(Location targetLocation) {
		if (Util.isValidLocation(world, targetLocation) && !Util.isLocationEmpty(world, targetLocation)){
			for (Item i : world.getItems()) { 
				if (i.getLocation().equals(targetLocation)) {
					return new DestroyCommand(veh, i); 
				}
			}
		}
		return null;
	}
}