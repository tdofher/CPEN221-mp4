package ca.ubc.ece.cpen221.mp4.vehicles.copy;

import javax.swing.ImageIcon;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.ai.VehicleAI;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;

public class Truck implements Vehicle {
	private final static int INITIAL_SPEED = 7;
	private final static int MAX_SPEED = 1;
	private final static int STRENGTH = 2000;
	private int COOLDOWN; 
	private static final ImageIcon truckImage = Util.loadImage("trucks.gif");
	protected Direction direc;
	private final VehicleAI ai;

	protected Location location;
	private int energy;
	
	public Truck(VehicleAI vAI, Location initialLocation) {
		this.ai = vAI;
		this.location = initialLocation;
		this.COOLDOWN = INITIAL_SPEED;
		this.energy = STRENGTH - COOLDOWN * 5;  //cooldown is higher at low speeds therefore the strength/momentum is less
		COOLDOWN = 1 /(COOLDOWN + 1);
		this.direc = Util.getRandomDirection();
	}
	
	@Override
	public void moveTo(Location targetLocation) {
		if (Util.getDirectionTowards(this.location, targetLocation).equals(this.direc)){
		this.location = targetLocation;	
		}
		else {
			this.slowDown();
			this.turn(Util.getDirectionTowards(this.location, targetLocation));
		}
	}

	@Override
	public int getMovingRange() {
		return 1;
	}

	@Override
	public ImageIcon getImage() {
		return Truck.truckImage;
	}

	@Override
	public String getName() {
		return "truck";
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public int getStrength() {
		this.energy = STRENGTH - COOLDOWN * 5;
		return this.energy; //strength is higher at fast speeds
	}

	@Override
	public void loseEnergy(int energy) {
		this.energy = 0;
	}

	@Override
	public boolean isDead() {
		if (energy > 0){		//in this case dead refers to crashed
			return false;
		}
		return true;
	}

	@Override
	public int getPlantCalories() {
		return 0;
	}

	@Override
	public int getMeatCalories() {
		return 0;
	}
	
	@Override
	public int getCoolDownPeriod() {
		return COOLDOWN;
	}
	
	@Override
	public void speedUp() {
		if (COOLDOWN > MAX_SPEED){
			COOLDOWN -= 1;
		}
	}

	@Override
	public void slowDown() {
		if (COOLDOWN < INITIAL_SPEED){
			COOLDOWN += 1;
		}
	}

	@Override
	public Command stop() {
		COOLDOWN = INITIAL_SPEED;
		return new WaitCommand();
	}
	
	@Override
	public void turn(Direction dir) {
		this.COOLDOWN = 4;
		
		Location targetLocation = new Location(this.location, dir);
		this.direc = dir;
		this.moveTo(targetLocation);
	}
	
	@Override
	public Command getNextAction(World world) {
		Command nextAction = ai.getNextAction(world, this);
		return nextAction;
	}

	@Override
	public Direction getDirection() {
		return this.direc;
	}
}