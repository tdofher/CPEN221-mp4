package ca.ubc.ece.cpen221.mp4.commands;

import ca.ubc.ece.cpen221.mp4.Food;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;
import ca.ubc.ece.cpen221.mp4.vehicles.copy.Vehicle;

/**
 * An EatCommand is a {@link Command} which represents a {@link LivingItem}
 * eating a {@link Food}.
 */
public final class DestroyCommand implements Command {

	private final Vehicle item;
	private final Item destroy;

	/**
	 * Construct a {@link EatCommand}, where <code> item </code> is the eater
	 * and <code> food </code> is the food. Remember that the food must be
	 * adjacent to the eater, and the eater must have greater strength than the
	 * food.
	 *
	 * @param item
	 *            the eater
	 * @param food
	 *            : the food
	 */
	public DestroyCommand(Vehicle item, Item destroy) {
		this.item = item;
		this.destroy = destroy;
	}

	@Override
	public void execute(World w) throws InvalidCommandException {
		if (item.getStrength() <= destroy.getStrength())
			item.loseEnergy(Integer.MAX_VALUE);
		if (destroy.getLocation().getDistance(item.getLocation()) != 1)
			throw new InvalidCommandException("Invalid EatCommand: Food is not adjacent");

		destroy.loseEnergy(Integer.MAX_VALUE);
	}

}