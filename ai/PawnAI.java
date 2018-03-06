package ca.ubc.ece.cpen221.mp4.ai;

import java.util.Set;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.chessPieces.ChessPiece;
import ca.ubc.ece.cpen221.mp4.chessPieces.Queen;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;

/**
 * An artificial intelligence that allows a ChessPiece with the
 * potentialLocations of a Pawn to behave as a Pawn would in a game of chess.
 * Once reaching the end of the world Pawn isDead and a Queen spawns in its
 * place.
 *
 */
public class PawnAI implements ChessAI {

	@Override
	public Command getNextAction(World world, ChessPiece piece) {

		Set<Location> possibleMoves = piece.potentialLocations();

		// When the Pawn reaches the top or bottom of the World, remove the pawn
		// and spawn a new Queen
		if ((piece.getLocation().getY() == 0 || piece.getLocation().getY() == world.getHeight() - 1)
				&& !piece.isDead()) {
			Location loc = piece.getLocation();
			piece.loseEnergy(100);
			world.step();
			promotionPawn(world, loc);
			return new WaitCommand();

		}

		// Move into one of the Pawn's possible Locations if Empty and Valid
		for (Location loc : possibleMoves) {
			if (Util.isLocationEmpty(world, loc)) {
				return new MoveCommand(piece, loc);
			}
		}

		return new WaitCommand();
	}

	/**
	 * Spawns a new Queen in a location that a pawn previously existed
	 * 
	 * @param world
	 *            the World in which the Queen is to be spawned
	 * @param loc
	 *            the Location in which to spawn a new Queen, must be Valid and
	 *            Empty
	 * 
	 */
	private void promotionPawn(World world, Location loc) {
		QueenAI queenAI = new QueenAI();
		Queen queen = new Queen(queenAI, loc);
		world.addItem(queen);
		world.addActor(queen);
	}

}
