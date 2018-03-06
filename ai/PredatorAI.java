package ca.ubc.ece.cpen221.mp4.ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
//import ca.ubc.ece.cpen221.mp4.commands.MoveAwayFromCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.animals.ArenaAnimal;

public class PredatorAI extends AbstractAI {
	protected Set<Item> surrounding;
	protected ArenaAnimal animal;
	protected ArenaWorld world;

	public PredatorAI() {
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		surrounding = world.searchSurroundings(animal);

		this.animal = animal;
		this.world = world;
		
		AdjacencyListGraph openLocations = null; //createGraph();
		
		Command com = null;
		
		com = dangerCheck(); //each check is ranked by priority
		
		if (com == null){
			com = breedCheck(false);
			if (com == null) {
				com = eatCheck();
				if (com == null) {
					com = coordinate();

					if (com == null){
						com = chaseCheck(openLocations);

						if (com == null) {
							com = search();
						}
					}
				}
			}
		}
		if (com != null) {
			return com;
		}
		return new WaitCommand();
	}

	/**
	 * creates a graph of the open locations within the predator's sight
	 * 
	 * @return the graph created
	 */
	public AdjacencyListGraph createGraph() {
		AdjacencyListGraph openLocations = new AdjacencyListGraph();
		int ymax = world.getHeight();
		int xmax = world.getWidth();

		for (int y = 0; y < ymax; y++) { // adds all open locations within view
											// range to the graph
			for (int x = 0; x < xmax; x++) {
				Location loc = new Location(x, y);
				if (animal.getLocation().getDistance(loc) <= animal.getViewRange()
						&& this.isLocationEmpty(world, animal, loc) && Util.isValidLocation(((World) world), loc)) {
					openLocations.addVertex(loc);
				}
			}
		}
		openLocations.addVertex(this.animal.getLocation());

		for (Location locs : openLocations.getVertices()) { // adds all the edges
			for (Location edgeCheck : openLocations.getVertices()) {
				if (locs.getDistance(edgeCheck) == 1) {
					openLocations.addEdge(locs, edgeCheck);
				}
			}
		}
		return openLocations;
	}

	/**
	 * checks for animals and items that could hurt the predator
	 * 
	 * @return a command to move away from the danger
	 */
	public Command dangerCheck() {
		for (Item i : surrounding) {
			if (i.getStrength() > animal.getStrength()) {
				return new MoveAwayFromCommand(animal, i.getLocation());
			}
		}
		return null;
	}

	/**
	 *
	 * if the predator is at max energy and has an empty adjacent square it will breed
	 * if the predator has the opportunity to eat but has too much energy this is passed as true
	 * and the predator will breed regardless of the other conditions as long as there is an empty space
	 * 
	 * @param noEat true if the predator is adjacent to a meal and has too much energy to eat it
	 * @return the command to breed
	 */
	public Command breedCheck(Boolean noEat) {
		int numMeals = 0;

		for (Item i : surrounding) {
			if (i.getMeatCalories() > 0) {
				numMeals++;
			}
		}

		Location randomEmptyAdjacent = Util.getRandomEmptyAdjacentLocation((World) world, animal.getLocation());

		if ((animal.getEnergy() == animal.getMaxEnergy() && animal.getEnergy() > animal.getMinimumBreedingEnergy() &&
				randomEmptyAdjacent != null && numMeals > 1) ){//|| (noEat && randomEmptyAdjacent != null)){

			return new BreedCommand(animal, randomEmptyAdjacent);
		}
		return null;
	}

	/**
	 * if there is a food source adjacent the predator will eat it
	 * 
	 * @return the command to eat
	 */
	public Command eatCheck() {
		for (Item i : surrounding) {
			if (animal.getLocation().getDistance(i.getLocation()) == 1 && i.getStrength() < animal.getStrength()
					&& i.getMeatCalories() != 0 && animal.getEnergy() < animal.getMaxEnergy() - i.getMeatCalories()) {
				return new EatCommand(animal, i);
			}
			if (animal.getLocation().getDistance(i.getLocation()) == 1 && i.getStrength() < animal.getStrength()
					&& i.getMeatCalories() != 0 && animal.getEnergy() > animal.getMaxEnergy() - i.getMeatCalories()) {
				return breedCheck(true);
			}
		}
		return null;
	}

	/**
	 * if prey is near by the predator will move towards it
	 * 
	 * @return the command to move towards prey
	 */
	public Command chaseCheck( AdjacencyListGraph graph) {
		
		//find best meal
		Set<Item> edible = new HashSet<Item>();
		for (Item i : surrounding) {
			if (i.getStrength() < animal.getStrength() && i.getMeatCalories() != 0) {
				edible.add(i);
			}
		}
		while (!edible.isEmpty()) {
			Item meal = null;
			boolean flag = true;

			for (Item i : edible) {
				if (flag || i.getMeatCalories() < meal.getMeatCalories()) {
					meal = i;
					flag = false;
				}
			}
			/*
			Command shortDis = shortestDistance(meal.getLocation(), graph);
			if (shortDis != null){
				return shortDis;
			}
			*/

			Direction dir = Util.getDirectionTowards(animal.getLocation(), meal.getLocation());

			Location targetLocation = new Location(animal.getLocation(), dir);

			if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
				return new MoveCommand(animal, targetLocation);
			}
			edible.remove(meal);
		}

		return null;
	}

	/**
	 * returns a command to move to the location that should be moved to in order to travel the shortest distance to the item
	 * or null if the item is directly adjacent
	 * 
	 * @param b the location of the object being moved to
	 * @return the command to move towards the item or null if the distance is 1
	 */
	public Command shortestDistance(final Location b, AdjacencyListGraph openLocations) {
		Location a = this.animal.getLocation();

		if (a.getDistance(b) == 1) {
			return null;
		}

		Stack<Location> visited = new Stack<Location>();
		List<Location> adj = new LinkedList<Location>();
		
		adj.addAll(openLocations.getDownstreamNeighbors(a));
		
		Boolean flag = true;
		
		while (adj.size() != 0 && flag){ 		//finds every path from a of length = to the shortest length

			List<Location> tmp = new LinkedList<Location>();

			tmp.addAll(adj);
			adj.clear();

			for (Location currentLoc : tmp) {
				if (!visited.contains(currentLoc)) {
					visited.add(currentLoc);

					if (currentLoc.equals(b)) {
						flag = false;
						break;
					}
					for (Location j : openLocations.getDownstreamNeighbors(currentLoc)) {
						if (!visited.contains(j)) {
							adj.add(j);
						}
					}
				}
			}
			visited.add(new Location(-1,-1));  //flag marking the depth levels
		}
		Location flagLoc = new Location(-1,-1);
		
		Location curr = b;
		while (visited.size() > 1){
			visited.pop();
			
			for (int i = visited.size() - 1 ; i >= 0 ; i--){
				if (visited.get(i).equals(flagLoc)){
					break;
				}
				else {
					visited.pop();
				}
			}
			List<Location> up = openLocations.getUpstreamNeighbors(curr);
			
			for (Location u : up){
				if (visited.contains(u)){
					curr = u;
				}
			}
		}
		
		if (this.isLocationEmpty(world, animal, curr) && Util.isValidLocation(world, curr) && curr != null){
			return new MoveCommand(this.animal, curr);
		}
		return null;
	}

	/**
	 * if no prey is in sight the predator moves away from its own species and
	 * circles grass patches
	 * 
	 * @return the appropriate move command
	 */
	public Command search() {
		for (Item i : surrounding) {
			if (i.getName() == "Fox" && !i.equals(animal)) {
				return new MoveAwayFromCommand(animal, i.getLocation());
			}
		}
		Boolean flag = true;
		int dist = 0;
		Item grass = null;

		for (Item i : surrounding) {
			if ((i.getName() == "grass" && flag) || (i.getLocation().getDistance(animal.getLocation()) < dist)) {
				grass = i;
			}
		}
		if (grass != null && grass.getLocation().getDistance(animal.getLocation()) > 2) {
			Direction dir = Util.getDirectionTowards(animal.getLocation(), grass.getLocation());

			Location targetLocation = new Location(animal.getLocation(), dir);

			if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
				return new MoveCommand(animal, targetLocation);
			}
		} else if (grass != null && grass.getLocation().getDistance(animal.getLocation()) == 1) {
			Direction dir = Util.getDirectionTowards(animal.getLocation(), grass.getLocation());

			if (dir == Direction.EAST) {
				Location targetLocation = new Location(animal.getLocation(), Direction.NORTH);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (dir == Direction.SOUTH) {
				Location targetLocation = new Location(animal.getLocation(), Direction.EAST);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (dir == Direction.WEST) {
				Location targetLocation = new Location(animal.getLocation(), Direction.SOUTH);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (dir == Direction.NORTH) {
				Location targetLocation = new Location(animal.getLocation(), Direction.WEST);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}
		}

		else if (grass != null && grass.getLocation().getDistance(animal.getLocation()) == 2) {

			if (animal.getLocation().getX() > grass.getLocation().getX()
					&& animal.getLocation().getY() > grass.getLocation().getY()) {
				Location targetLocation = new Location(animal.getLocation(), Direction.WEST);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (animal.getLocation().getX() > grass.getLocation().getX()
					&& animal.getLocation().getY() < grass.getLocation().getY()) {
				Location targetLocation = new Location(animal.getLocation(), Direction.SOUTH);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (animal.getLocation().getX() < grass.getLocation().getX()
					&& animal.getLocation().getY() > grass.getLocation().getY()) {
				Location targetLocation = new Location(animal.getLocation(), Direction.NORTH);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}

			if (animal.getLocation().getX() < grass.getLocation().getX()
					&& animal.getLocation().getY() < grass.getLocation().getY()) {
				Location targetLocation = new Location(animal.getLocation(), Direction.EAST);

				if (Util.isValidLocation(world, targetLocation)
						&& this.isLocationEmpty(world, animal, targetLocation)) {
					return new MoveCommand(animal, targetLocation);
				}
			}
		}

		Direction dir = Util.getRandomDirection();
		Location targetLocation = new Location(animal.getLocation(), dir);
		if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
			return new MoveCommand(animal, targetLocation);
		}
		return null;
	}

	public Command coordinate() {
		return null;
	}

}
