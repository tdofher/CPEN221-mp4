package ca.ubc.ece.cpen221.mp4.ai;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.InvalidCommandException;
import ca.ubc.ece.cpen221.mp4.items.MoveableItem;

public final class MoveAwayFromCommand implements Command {
	private final MoveableItem item;
	private final Location danger;

	public MoveAwayFromCommand(MoveableItem item, Location danger) {
		this.item = item;
		this.danger = danger;
	}
	
	/**
	 * moves 'item' away from 'danger'
	 */
	@Override
	public void execute(World world) throws InvalidCommandException {
		if (item.isDead()) {
			return;
		}
		Direction dir = Util.getDirectionTowards(danger, item.getLocation());

		Location targetLocation = new Location(item.getLocation(), dir);

		if (Util.isLocationEmpty(world, targetLocation) && Util.isValidLocation(world, targetLocation)) {
			item.moveTo(targetLocation);
		} else {
			dir = Util.getDirectionTowards(new Location(item.getLocation().getX(), danger.getY()), item.getLocation());
			targetLocation = new Location(item.getLocation(), dir);
			if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) {
				item.moveTo(targetLocation);
			}
		}
	}
}