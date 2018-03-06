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

/*
 * A Pawn implementation of a ChessPiece is assigned a random colour with potential 
 * Location straight in the + or - Y direction depending on that colour
 */
public class Pawn implements ChessPiece {
	// Arbitrary INITIAL_ENERGY
	private static final int INITIAL_ENERGY = 40;
	// Arbitrary STRENGTH
	private static final int STRENGTH = 50;
	// Slower COOLDOWN time
	private static final int COOLDOWN = 4;
	// Typically only moves one but may be changed later to move diagonally to
	// capture pieces
	private static final int MOVING_RANGE = 2;
	private static final ImageIcon pawnImage = Util.loadImage("pawn.gif");

	// Creates a fixed random int to determine the piece's colour
	private final int random = new Random().nextInt(10);
	// Recommended PawnAI
	private final ChessAI ai;

	private Location location;
	private int energy;

	public Pawn(ChessAI pawnAI, Location initialLocation) {
		ai = pawnAI;
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
		return pawnImage;
	}

	@Override
	public String getName() {
		return "pawn";
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
	 * The potential movement locations of a pawn are straight in either the +Y
	 * of -Y directions (NORTH of SOUTH). A Pawn of colour "black" will move
	 * NORTH and a "white" Pawn will move SOUTH.
	 * 
	 * @return locations a set of all the possible locations a pawn could move
	 *         if Locations are Valid and Empty
	 */
	@Override
	public Set<Location> potentialLocations() {

		Set<Location> locations = new HashSet<Location>();
		if (this.colour() == "black") {
			locations.add(new Location(location.getX(), location.getY() + 1));
		} else {
			locations.add(new Location(location.getX(), location.getY() - 1));
		}
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
