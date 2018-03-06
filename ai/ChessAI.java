package ca.ubc.ece.cpen221.mp4.ai;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.chessPieces.ChessPiece;
import ca.ubc.ece.cpen221.mp4.commands.Command;

/**
 * The AI interface for all ChessPieces. Each ChessPiece possess a unique
 * implementation of this interface
 *
 */
public interface ChessAI {
	/**
	 * Returns the next Command that the ChessPiece will perform based on its
	 * unique behavior
	 * 
	 * @param world
	 *            the World in which the ChessPiece exists
	 * @param piece
	 *            a chessPiece, preferably matched to its corresponding AI
	 * @return the next Command for the piece to perform
	 */
	public Command getNextAction(World world, ChessPiece piece);
}
