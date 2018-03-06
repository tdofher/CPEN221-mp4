package ca.ubc.ece.cpen221.mp4.chessPieces;

import java.util.Set;

import ca.ubc.ece.cpen221.mp4.Actor;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.items.MoveableItem;

/**
 * 
 * A ChessPiece represents a MoveableItem that can move to any one of a set of
 * potential locations in the World provided that location is a valid location.
 * A {@link ChessPiece} also possesses a colour which for some pieces determines
 * their direction of movement and image.
 *
 */
public interface ChessPiece extends MoveableItem, Actor {

	/**
	 * The set of all locations that would be valid for the ChessPiece to move
	 * provided that they are Valid and Empty. Does not check if locations are
	 * Valid and Empty.
	 * 
	 * @return the set of possible locations for the chess piece to move
	 */
	Set<Location> potentialLocations();

	/**
	 * Returns what colour a ChessPiece is, either "black" or "white"
	 * 
	 * @return the colour of the chess piece
	 */
	String colour();

}
