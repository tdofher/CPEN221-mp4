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
 * The {@link Bear} is an {@link ArenaAnimal} that uses the {@link PredatorAI}.
 */
public class Bear implements ArenaAnimal {
	// Constant attributes of the Bear implementation of ArenaAnimal
	private static final int INITIAL_ENERGY = 20;
	private static final int MAX_ENERGY = 200;
	private static final int STRENGTH = 400;
	private static final int VIEW_RANGE = 5;
	private static final int MIN_BREEDING_ENERGY = 100;
	private static final int COOLDOWN = 5;
	private static final int MOVING_RANGE = 1;
	private static final ImageIcon bearImage = Util.loadImage("bear.gif");

	private final AI ai;

	private Location location;
	private int energy;

	/**
	 * Constructs a new Bear at a certain initialLocation
	 * 
	 * @param initialLocation
	 *            the location in which the Bear is created
	 */
	public Bear(Location initialLocation) {

		this.ai = new PredatorAI();
		this.location = initialLocation;

		this.energy = INITIAL_ENERGY;
	}

	@Override
	public LivingItem breed() {
		Bear child = new Bear(location);
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
		return bearImage;
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
		return energy;
	}

	@Override
	public int getMinimumBreedingEnergy() {
		return MIN_BREEDING_ENERGY;
	}

	@Override
	public int getMovingRange() {
		return MOVING_RANGE;
	}

	@Override
	public String getName() {
		return "Bear";
	}

	@Override
	public Command getNextAction(World world) {
		Command nextAction = ai.getNextAction(world, this);
		this.energy--;
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
