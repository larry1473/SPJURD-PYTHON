package board;


import org.carrot2.shaded.guava.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import board.Move.*;
/**
 * this represent a the pawns an calculates all their legal moves.
 * @author larry Fotso
 */

public abstract class Pawn {
	protected  int pawnposition; // The Pawn position on the board.
	protected   Alliance pawnbelong; // to who the pawn belongs.
	protected final int[] pawnPossibleMoves = {-9,-1,1,9}; // All the possible moves of the pawn on the pawn.
	protected Board board;
	protected static pawnType pawnType;
	protected  boolean ISFIRSTMOVE;

	public Pawn(final pawnType pawnType , final int pawnPosition, final Alliance pawnbelong){
		this.pawnType = pawnType;
		this.pawnbelong = pawnbelong;
		this.pawnposition = pawnPosition;
		this.ISFIRSTMOVE = false;

	}
	/**
	 * Return the value of every pawn.
	 * @return
	 */
	public int getPawnValue(){
		return this.pawnType.getPawnValue();
	}
	/**
	 * Return the position of a pawn.
	 * @return
	 */
	public  int getPawnPos() {
		return this.pawnposition;
	}

	/**
	 * The pawn stays in place.
	 * @param board
	 * @return
	 */
	public Move inPlaceMove(final Board board) {
		return new MainMove(board, this, this.pawnposition);
	}
	/**
	 * Calculates all the legal moves of the Pawns.
	 * @param board
	 * @return
	 */
	public Collection<Move> calculateLegalMove(final Board board){
		return calculateLegalMove(board, this.pawnposition);
	}
	
	public Collection<Move> calculateLegalMove(final Board board, int currentPosition) {
		List<Move> legalMoves = new ArrayList<>();
		for(int move : pawnPossibleMoves){// we loop through all the possible moves.
			int nextDestinationCoordinate = currentPosition + move;// Calculates all the next destination coordinate.
			if(isFirstColumnExclusion(currentPosition,move) || isNinthColumnExclusion(currentPosition,move) ){// We test if the move apply to our actual position is out of the board.
				continue;

			}
			if (isvalidTilecoord(nextDestinationCoordinate)) {// we test if the detination where we are trying to go has a valid coordinate.
				Tile pawnNextTile = board.getTile(nextDestinationCoordinate);// the Tile where the pawn will be placed.
				if (!pawnNextTile.isTileOccupied()) {// test if the tile is occupied by another pawn.
					if(board.getTile(nextDestinationCoordinate).len == 1) {// test the number of walls that could placed on a tile.
						if(move == -1 && board.getTile(nextDestinationCoordinate).getWalls()[0] == null){// We test if we are going left an there is no wall on the left.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
						else if(move == 1 && board.getTile(currentPosition).getWalls()[0] == null){// we test if we are going right an there is no wall on the right.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
						else if(move == 9 && board.getTile(currentPosition).getWalls()[0] == null){
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
					}
					else if( board.getTile(nextDestinationCoordinate).len == 2) {
						if(move == -9 && board.getTile(nextDestinationCoordinate).getWalls()[0] == null) {//we test if there is a wall when going up.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
						else if(move == 9 && board.getTile(currentPosition).getWalls()[0] == null){//we test if there wall when going down.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
						else if(move == -1 && board.getTile(nextDestinationCoordinate).getWalls()[1] == null){// we test if there  is a wall when going left.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
						else if(move == 1 && board.getTile(currentPosition).getWalls().length > 1 && board.getTile(currentPosition).getWalls()[1] == null){// we test if there is a wall when going right.
							legalMoves.add(new MainMove(board, this, nextDestinationCoordinate));
						}
					}



				}
				else {// The next tile is not occupied.


					if(move == 1){
						if(board.getTile(nextDestinationCoordinate).len == 2 && board.getTile(nextDestinationCoordinate).getWalls()[1] != null){// we test if there is no wall on the Right of the tile where we want to go.
							if(isvalidTilecoord(nextDestinationCoordinate - 9) &&  board.getTile(nextDestinationCoordinate - 9).getWalls()[0] == null) {// we test is the oblique Tile to our actual position has no wall and add the move.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 9));
							}
							if(isvalidTilecoord(nextDestinationCoordinate + 9) &&  board.getTile(nextDestinationCoordinate).getWalls()[0] == null) {// we test is the oblique Tile to our actual position has no wall and add the move.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 9));
							}
						}
						else if(board.getTile(nextDestinationCoordinate).len == 1 && board.getTile(nextDestinationCoordinate).getWalls()[0] != null){// we test if there is wall on behind the pawn close to use if yes we make an oblique move.
							if(nextDestinationCoordinate - 9 > 0) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 9));
							}
							if(nextDestinationCoordinate + 9 <= 80) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 9));
							}
						}
						else if((currentPosition + 1) % 9 != 8 ){
							legalMoves.add(new MainMove(board,this,nextDestinationCoordinate + 1 ));
						}
					}
					else if(move == -1){// if the move is a left move.
						int thisRow = currentPosition / 9;// actual row
						int nextRow = (nextDestinationCoordinate - 1) / 9;// the next row
						if( nextRow == thisRow && board.getTile(nextDestinationCoordinate).len == 2 && board.getTile(nextDestinationCoordinate - 1).getWalls()[1] != null){
							if(isvalidTilecoord(nextDestinationCoordinate - 9) &&  board.getTile(nextDestinationCoordinate - 9).getWalls()[0] == null) {// we test is the next destination -9 is valid and has no wall then we make an oblique move.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 9));
							}
							if(isvalidTilecoord(nextDestinationCoordinate + 9) &&  board.getTile(nextDestinationCoordinate).getWalls()[0] == null) {// we test is the next destination +9 is valid and has no wall then we make an oblique move.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 9));
							}
						}
						else if( nextRow == thisRow && board.getTile(nextDestinationCoordinate).len == 1 && board.getTile(nextDestinationCoordinate - 1).getWalls()[0] != null){
							if(nextDestinationCoordinate - 9 > 0) {// test if the next destination index - 9 is still contained on the board.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 9));
							}
							if(nextDestinationCoordinate + 9 <= 80) {// test if the next destination + 9 is still in the board.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 9));
							}
						}
						else if((currentPosition - 1) % 9 != 0 ){// test for and edge case.
							legalMoves.add(new MainMove(board,this,nextDestinationCoordinate - 1 ));
						}
					}
					else if(move == 9){
						if(board.getTile(nextDestinationCoordinate).len == 2 && board.getTile(nextDestinationCoordinate).getWalls()[0] != null){
							if(isvalidTilecoord(nextDestinationCoordinate - 1) &&  board.getTile(nextDestinationCoordinate).getWalls()[1] == null) {// we test if destination is valid an if there no wall and then add the move to our moves.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 1));
							}
							if(isvalidTilecoord(nextDestinationCoordinate + 1) &&  board.getTile(nextDestinationCoordinate + 1).getWalls().length > 1 && 
									board.getTile(nextDestinationCoordinate + 1).getWalls()[1] == null) {// we test if destination is valid an if there no wall and then add the move to our moves.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 1));
							}
						}
						else if(board.getTile(nextDestinationCoordinate).len == 1 && board.getTile(nextDestinationCoordinate).getWalls()[0] != null && 
								board.getTile(nextDestinationCoordinate).getWalls()[0].getDirection()){
							if(nextDestinationCoordinate - 1 > 0) {// test is there is a wall on the right of the occupied tile and make oblique moves.
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 1));
							}
							if(nextDestinationCoordinate + 1 <= 80) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 1));
							}
						}
						else if((currentPosition + 9) % 9 <= 8 ){
							legalMoves.add(new MainMove(board,this,nextDestinationCoordinate + 9 ));
						}
					}
					else if(move == -9){
						if( isvalidTilecoord(nextDestinationCoordinate - 9) && board.getTile(nextDestinationCoordinate).len == 2 && board.getTile(nextDestinationCoordinate - 9).getWalls()[0] != null){
							if(isvalidTilecoord(nextDestinationCoordinate - 1) &&  board.getTile(nextDestinationCoordinate).getWalls()[1] == null) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 1));
							}
							if(isvalidTilecoord(nextDestinationCoordinate + 1) &&  board.getTile(nextDestinationCoordinate + 1).getWalls()[1] == null) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 1));
							}
						}
						else if( isvalidTilecoord(nextDestinationCoordinate - 9) && board.getTile(nextDestinationCoordinate).len == 1 && board.getTile(nextDestinationCoordinate - 9).getWalls()[0] != null){
							if(nextDestinationCoordinate - 1 > 0) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate - 1));
							}
							if(nextDestinationCoordinate + 1 <= 80) {
								legalMoves.add(new MainMove(board, this, nextDestinationCoordinate + 1));
							}
						}
						else if((currentPosition - 9) % 9 <= 8 ){
							legalMoves.add(new MainMove(board,this,nextDestinationCoordinate - 9 ));
						}

					}
				}

			}
		}

		return ImmutableList.copyOf(legalMoves);
	}


	private boolean isvalidTilecoord(int coordinate) { // checks the validity of the tile coordinate.
		return coordinate >= 0 && coordinate < BoardUtils.NUM_OF_TILES;

	}

	private boolean isFirstColumnExclusion(final int currentpos, final int  move) {
		BoardUtils boardUtils = new BoardUtils();
		return boardUtils.FIRST_COLUMN[currentpos] && (move == -1);

	}

	private boolean isNinthColumnExclusion(final int currentpos, final int  move) {
		BoardUtils boardUtils = new BoardUtils();
		return boardUtils.NINTH_COLUMN[currentpos] && (move == 1);

	}


	public Pawn.pawnType getPawnType() {
		return this.pawnType;
	}

	@Override
	public boolean equals(final Object other){
		if(this == other)
			return true;
		if(!(other instanceof Pawn))
			return false;
		final Pawn otherPawn = (Pawn) other;
		return pawnposition ==  otherPawn.getPawnPos() && pawnType == otherPawn.getPawnType() && pawnbelong == otherPawn.getPawnbelong()&& ISFIRSTMOVE == otherPawn.isFirstMove();

	}


	@Override
	public int hashCode(){
		int res = pawnType.hashCode();
		res = 31 * res + pawnbelong.hashCode();
		res = 31 * res + pawnposition;
		res = 31 * res + (ISFIRSTMOVE ? 1:0);
		return res;

	}
	/**
	 *
	 * @param move An instance of Move tha we help us create the a new Pawn and place on a the tile we want it to move on.
	 * @return A new Pawn
	 */
	public abstract Pawn movePawn(Move move);



	public Alliance getPawnbelong() {
		return pawnbelong;
	}

	public boolean isFirstMove() {
		return this.ISFIRSTMOVE;
	}

	public enum pawnType{
		REDPAWN(100,"R"),
		YELLOWPAWN(100,"Y");
		private  final String pawnName;
		private final int pawnValue;

		pawnType(final int pawnValue ,final String pawnName){
			this.pawnName = pawnName;
			this.pawnValue = pawnValue;

		}

		@Override
		public String toString(){
			return this.pawnName;
		}

		public int getPawnValue() {
			return pawnValue;
		}
	}


}
