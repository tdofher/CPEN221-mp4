package ca.ubc.ece.cpen221.mp4.ai;

import java.util.Random;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.chessPieces.ChessPiece;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;

/**
 * An artificial intelligence that allows a ChessPiece with the
 * potentialLocations of a Knight to behave as a Knight in the game of chess.
 *
 */
public class KnightAI implements ChessAI {

	@Override
	public Command getNextAction(World world, ChessPiece piece) {

		Set<Location> possibleMoves = piece.potentialLocations();

		// Took random set idea from
		// http://stackoverflow.com/questions/124671/picking-a-random-element-from-a-set
		int randomMove = new Random().nextInt(possibleMoves.size());
		int index = 0;
		// Checks the Location at position randomMove in the set of
		// possibleMoves and if Valid and Empty moves there
		for (Location loc : possibleMoves) {
			if (index == randomMove && Util.isLocationEmpty(world, loc))
				return new MoveCommand(piece, loc);
			index += 1;
		}

		// Checks whether each Location in the set of possible moves is Empty
		// and Valid and moves there if so moves to that Location
		for (Location loc : possibleMoves) {
			if (Util.isLocationEmpty(world, loc)) {
				return new MoveCommand(piece, loc);
			}
		}

		return new WaitCommand();
	}

}
