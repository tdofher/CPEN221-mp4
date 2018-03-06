package ca.ubc.ece.cpen221.mp4.ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.animals.ArenaAnimal;
import ca.ubc.ece.cpen221.mp4.items.animals.Fox;
import ca.ubc.ece.cpen221.mp4.items.animals.Rabbit;

/**
 * Your Rabbit AI.
 */
public class RabbitAI extends AbstractAI {

	private int closest = 10; // max number; greater than rabbit's view range
	private int temp;
	private boolean foxFound;
	private int HUNGER_THRESHOLD = 7;
	private int POPULATION_THRESHOLD = 20;

	public RabbitAI() {
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		this.foxFound = false; 

		Set<Item> surrounding = world.searchSurroundings(animal);

		Location randomEmptyAdjacent = randomEmptyAdjacent(world, animal);

		// Breeds when energy equal to MAX_ENERGY and greater than
		// MINIMUM_BREEDING_ENERGY
		if (animal.getEnergy() > animal.getMinimumBreedingEnergy() && animal.getEnergy() == animal.getMaxEnergy()
				&& randomEmptyAdjacent != null) {
			return new BreedCommand(animal, randomEmptyAdjacent);
		}

		// Prioritizes eating grass if rabbit hunger is below hunger threshold
		if (animal.getEnergy() <= HUNGER_THRESHOLD) {
			for (Item i : surrounding) {
				if (i.getName() == "grass" && animal.getLocation().getDistance(i.getLocation()) == 1) {
					return new EatCommand(animal, i);
				}
			}
		}

		// Fox avoidance, moves away from a Fox if there is a Fox in its
		// VIEW_RANGE
		for (Item i : surrounding) {
			if (i.getName() == "Fox") {
				return new MoveAwayFromCommand(animal, i.getLocation());
			}
		}

		// Eat Grass if adjacent and the ArenaAnimal's energy is not too close
		// to its MAX_ENERGY
		if (animal.getEnergy() < animal.getMaxEnergy() - 5) {
			for (Item i : surrounding) {
				if (i.getName() == "grass" && animal.getLocation().getDistance(i.getLocation()) == 1) {
					return new EatCommand(animal, i);
				}
			}
		}

		// Searches for and moves toward Grass within its view range
		for (Item i : surrounding) {
			if (i.getName() == "grass") {
				Location grassLocation = i.getLocation();
				Direction dir = Util.getDirectionTowards(animal.getLocation(), grassLocation);
				Location targetLocation = new Location(animal.getLocation(), dir);
				// Tries multiple ways to move toward the Grass
				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
				dir = Util.getDirectionTowards(animal.getLocation(),
						new Location(animal.getLocation().getX(), grassLocation.getY()));
				targetLocation = new Location(animal.getLocation(), dir);
				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}
		}

		// If there is no Grass in view and a Rabbit in view move
		// in opposite direction of the Rabbit
		for (Item i : surrounding) {
			if (i.getName() == "Rabbit" && !i.equals(animal)) {
				return new MoveAwayFromCommand(animal, i.getLocation());
			}
		}

		// If X Location % 2 is 1 move NORTH if Valid and Empty
		if (Math.abs(animal.getLocation().getX() % 2) == 1) {
			Location targetLocation = new Location(animal.getLocation(), Direction.NORTH);
			if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
				return new MoveCommand(animal, targetLocation);
			}
		}
		// If X Location % 2 is 0 move SOUTH if Valid and Empty
		if (Math.abs(animal.getLocation().getX() % 2) == 0) {
			Location targetLocation = new Location(animal.getLocation(), Direction.SOUTH);
			if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
				return new MoveCommand(animal, targetLocation);
			}
		}

		// Move EAST of WEST if Valid and Empty
		Direction dir = Direction.EAST;
		Location targetLocation = new Location(animal.getLocation(), dir);
		if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
			return new MoveCommand(animal, targetLocation);
		}
		dir = Direction.WEST;
		targetLocation = new Location(animal.getLocation(), dir);
		if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
			return new MoveCommand(animal, targetLocation);
		}

		return new WaitCommand();
	}

	/**
	 * Returns a random empty Location in the specified ArenaWorld that is
	 * adjacent to <code>animal</code>, or null if there is no empty adjacent
	 * location. Same as method in Util but takes ArenaWorld and ArenaAnimal as
	 * parameters
	 *
	 * @param world
	 *            the current ArenaWorld
	 * @param animal
	 *            the animal whose location the empty location must be adjacent
	 *            to
	 * 
	 * @return empty adjacent location, or null if none exists
	 */
	private Location randomEmptyAdjacent(ArenaWorld world, ArenaAnimal animal) {
		// All Locations adjacent to ArenaAnimal's Location
		Location locationA = new Location(animal.getLocation().getX() + 1, animal.getLocation().getY());
		Location locationB = new Location(animal.getLocation().getX() - 1, animal.getLocation().getY());
		Location locationC = new Location(animal.getLocation().getX(), animal.getLocation().getY() + 1);
		Location locationD = new Location(animal.getLocation().getX(), animal.getLocation().getY() - 1);
		Location locationE = new Location(animal.getLocation().getX() + 1, animal.getLocation().getY() + 1);
		Location locationF = new Location(animal.getLocation().getX() - 1, animal.getLocation().getY() + 1);
		Location locationG = new Location(animal.getLocation().getX() + 1, animal.getLocation().getY() - 1);
		Location locationH = new Location(animal.getLocation().getX() - 1, animal.getLocation().getY() - 1);
		Location[] adjacentPoints = new Location[] { locationA, locationB, locationC, locationD, locationE, locationF,
				locationG, locationH };

		Set<Location> emptyLocations = new HashSet<Location>();

		for (int index = 0; index < adjacentPoints.length; index++) {
			if (isLocationEmpty(world, animal, adjacentPoints[index])) {
				emptyLocations.add(adjacentPoints[index]);
			}
		}

		if (emptyLocations.size() == 0) {
			return null;
		}

		int randomLoc = new Random().nextInt(emptyLocations.size());
		return (Location) emptyLocations.toArray()[randomLoc];
	}
}
