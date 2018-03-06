package ca.ubc.ece.cpen221.mp4.items.animals;

import javax.swing.ImageIcon;

import ca.ubc.ece.cpen221.mp4.Food;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.ai.ArenaAnimalAI;
import ca.ubc.ece.cpen221.mp4.ai.PredatorAI;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

/**
 *The {@link Tiger} is an {@link ArenaAnimal} that uses the {@link PredatorAI}.
 */
public class Tiger implements ArenaAnimal {
	// Constant attributes of the Tiger implementation of Animal
	private static final int INITIAL_ENERGY = 20;
	private static final int MAX_ENERGY = 200;
	private static final int STRENGTH = 400;
	private static final int VIEW_RANGE = 5;
	private static final int MIN_BREEDING_ENERGY = 100;
	private static final int COOLDOWN = 2;
	private static final int MOVING_RANGE = 1;
	private static final ImageIcon tigerImage = Util.loadImage("tiger.gif");

	private final AI ai;

	private Location location;
	private int energy;

	/**
	 * Constructs a new Tiger at a certain initialLocation
	 * 
	 * @param initialLocation
	 *            the location in which the Tiger is created
	 */
	public Tiger(Location initialLocation) {
		// Tiger uses the PredatorAI
		this.ai = new PredatorAI();
		this.location = initialLocation;

		this.energy = INITIAL_ENERGY;
	}

	@Override
	public LivingItem breed() {
		Tiger child = new Tiger(location);
		child.energy = energy / 2;
		this.energy = energy / 2;
		return child;
	}

	@Override
	public void eat(Food food) {
		energy = Math.min(MAX_ENERGY, energy + food.getMeatCalories());
	}

	@Override
	public int getCoolDownPeriod() {
		return COOLDOWN;
	}

	@Override
	public int getEnergy() {
		return energy;
	}

	@Override
	public ImageIcon getImage() {
		return tigerImage;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public int getMaxEnergy() {
		return MAX_ENERGY;
	}

	@Override
	public int getMeatCalories() {
		// The amount of meat calories it provides is equal to its current
		// energy.
		return energy;
	}

	@Override
	public int getMinimumBreedingEnergy() {
		return MIN_BREEDING_ENERGY;
	}

	@Override
	public int getMovingRange() {
		return MOVING_RANGE; // Can only move to adjacent locations.
	}

	@Override
	public String getName() {
		return "Tiger";
	}

	@Override
	public Command getNextAction(World world) {
		Command nextAction = ai.getNextAction(world, this);
		this.energy--; // Loses 1 energy regardless of action.
		return nextAction;
	}

	@Override
	public int getPlantCalories() {
		return 0;
	}

	@Override
	public int getStrength() {
		return STRENGTH;
	}

	@Override
	public int getViewRange() {
		return VIEW_RANGE;
	}

	@Override
	public boolean isDead() {
		return energy <= 0;
	}

	@Override
	public void loseEnergy(int energyLoss) {
		this.energy = this.energy - energyLoss;
	}

	@Override
	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}
}
