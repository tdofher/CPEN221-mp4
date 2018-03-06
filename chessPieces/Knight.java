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
 * A Knight implementation of a ChessPieces has potential locations in a
 * distance 3 L shape around its current location. It is assigned a random
 * colour "black" or "white"
 *
 */
public class Knight implements ChessPiece {
	// Arbitrary
	private static final int INITIAL_ENERGY = 40;
	// Arbitrary
	private static final int STRENGTH = 200;
	// Slower COOLDOWN time
	private static final int COOLDOWN = 4;
	// Knight moves 3 spaces in its L pattern
	private static final int MOVING_RANGE = 3;
	private static final ImageIcon knightImage = Util.loadImage("knight.gif");

	// Recommended KnightAI
	private final ChessAI ai;

	private Location location;
	private int energy;
	// Generate a random number to determine colour
	private final int random = new Random().nextInt(10);

	public Knight(ChessAI knightAI, Location initialLocation) {
		ai = knightAI;
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
		return knightImage;
	}

	@Override
	public String getName() {
		return "knight";
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
	 * Knight moves in a distance 3 "L" shape in any direction. A set of all the
	 * locations a Knight could move if they are Valid and Empty
	 * 
	 * @return a set of all the possible Locations a knight could move if
	 *         Locations are Valid and Empty
	 */
	@Override
	public Set<Location> potentialLocations() {
		Set<Location> locations = new HashSet<Location>();
		locations.add(new Location(location.getX() + 2, location.getY() + 1));
		locations.add(new Location(location.getX() + 2, location.getY() - 1));
		locations.add(new Location(location.getX() - 2, location.getY() + 1));
		locations.add(new Location(location.getX() - 2, location.getY() - 1));
		locations.add(new Location(location.getX() + 1, location.getY() + 2));
		locations.add(new Location(location.getX() - 1, location.getY() + 2));
		locations.add(new Location(location.getX() + 1, location.getY() - 2));
		locations.add(new Location(location.getX() - 1, location.getY() - 2));
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
