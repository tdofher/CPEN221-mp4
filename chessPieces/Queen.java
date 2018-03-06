package ca.ubc.ece.cpen221.mp4.chessPieces;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.ai.ChessAI;
import ca.ubc.ece.cpen221.mp4.commands.Command;

/**
 * The Queen implementation of ChessPiece is assigned a random colour and can
 * move in any direction
 *
 */
public class Queen implements ChessPiece {
	// Arbitrary energy
	private static final int INITIAL_ENERGY = 40;
	// Arbitrary strength
	private static final int STRENGTH = 500;
	// Increased Queen's speed over other chess pieces to simulate rapid
	// movement in any direction
	private static final int COOLDOWN = 1;
	// Although this is irrelevant to chess pieces there are some instances
	// where the Queen moves two spaces in Manhattan Coordinates
	private static final int MOVING_RANGE = 2;
	private static final ImageIcon queenImage = Util.loadImage("queen.gif");

	// Creates a fixed random int to determine the piece's colour
	private final int random = new Random().nextInt(10);
	// Recommended QueenAI
	private final ChessAI ai;

	private Location location;
	private int energy;

	public Queen(ChessAI queenAI, Location initialLocation) {
		ai = queenAI;
		location = initialLocation;
		energy = INITIAL_ENERGY;
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}

	@Override
	public int getMovingRange() {
		return MOVING_RANGE;
	}

	@Override
	public ImageIcon getImage() {
		return queenImage;
	}

	@Override
	public String getName() {
		return "queen";
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public int getStrength() {
		return STRENGTH;
	}

	@Override
	public void loseEnergy(int energyLost) {
		energy -= energyLost;
	}

	@Override
	public boolean isDead() {
		return energy <= 0;
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
	public Command getNextAction(World world) {
		Command nextAction = ai.getNextAction(world, this);
		return nextAction;
	}

	/**
	 * The set of all potential locations that a queen could move in one step
	 * 
	 * @return locations a set of all the possible locations a Queen could move
	 *         if not obstructed
	 */
	@Override
	public Set<Location> potentialLocations() {

		Set<Location> locations = new HashSet<Location>();

		// The queen can move to any adjacent square, including diagonals.
		// Technically it can move any number of squares in any direction but
		// this is factored in by the Queen's increased speed
		int index = 1;
		locations.add(new Location(this.location.getX(), this.location.getY() + index));
		locations.add(new Location(this.location.getX(), this.location.getY() - index));
		locations.add(new Location(this.location.getX() + index, this.location.getY() - index));
		locations.add(new Location(this.location.getX() + index, this.location.getY() + index));
		locations.add(new Location(this.location.getX() - index, this.location.getY() - index));
		locations.add(new Location(this.location.getX() - index, this.location.getY() + index));
		locations.add(new Location(this.location.getX() + index, this.location.getY()));
		locations.add(new Location(this.location.getX() - index, this.location.getY()));

		return locations;
	}

	@Override
	public String colour() {

		if (this.random % 2 == 1) {
			return "white";
		}
		return "black";
	}
}
