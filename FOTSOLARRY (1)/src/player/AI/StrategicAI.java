package player.AI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import board.Board;
import board.Move;
import board.Pawn;
import board.Tile;
import board.Wall;
/**
 * This class plays the role of the artificial intelligence which has two levels that is "easy" & "hard"
 * and extends the class Player.
 * @creator  , Larry Fotso
 */
public class StrategicAI extends Computer{

	public static int visitedYellow[] = new int[81];
	public static int visitedRed[] = new int[81];

	public StrategicAI(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves, Pawn pawn) {
		super(board, legalMoves, opponentMoves, pawn);

		
	}

	/**
	 * clears the visited tile list
	 */
	public static void clear() {
		for(int i = 0; i < visitedYellow.length; i++) { 
			visitedYellow[i] = 0;
			visitedRed[i] = 0;
		}
	}
	/**
	 * this method helps to create random moves.
	 * @param board
	 * @return
	 */
	public Move getRandomDes(Board board){
        int[] visitedNodes = (this.pawn.getPawnbelong().isYellow()) ? visitedYellow : visitedRed;
        visitedNodes[this.pawn.getPawnPos()] += 1;
        ArrayList<Move> moves = new ArrayList<Move>(pawn.calculateLegalMove(board));
    	moves.sort(new Comparator<Move>() {
			@Override
			public int compare(Move o1, Move o2) {
				if(o1.getDestinationCoordinate() < 0 || o1.getDestinationCoordinate() > 80) {
					return 1;
				}
				if(o2.getDestinationCoordinate() < 0 || o2.getDestinationCoordinate() > 80) {
					return -1;
				}
				
				int bo1 = visitedNodes[o1.getDestinationCoordinate()];
				int bo2 = visitedNodes[o2.getDestinationCoordinate()];
				if(bo1 > bo2)
					return 1;
				else if(bo1 < bo2) 
					return -1;
				
				if(pawn.getPawnbelong().isRed()) {
		        	if(o1.getDestinationCoordinate() > o2.getDestinationCoordinate())
		        		return -1;
		        	else if (o1.getDestinationCoordinate() < o2.getDestinationCoordinate())
		        		return 1;
		        	else if(new Random().nextBoolean())
		        		return 1;
		        	else 
		        		return  -1;
		        }
		        else {
		        	if(o1.getDestinationCoordinate() < o2.getDestinationCoordinate())
		        		return -1;
		        	else if (o1.getDestinationCoordinate() > o2.getDestinationCoordinate())
		        		return 1;
		        	else if(new Random().nextBoolean())
		        		return 1;
		        	else 
		        		return  -1;
		        }
			}
		});
    	
        return moves.get(0);
    }
	/**
	 * this methode helps to place walls randomly.
	 * @param wall
	 * @return
	 */
    public Tile placeRandomWalls( Wall wall){
    	final ArrayList<Pawn> ActivePawns = getPawns(board);
    	this.pawn = ActivePawns.get(0);
    	Pawn opponent = getPawnOpponent();
    	
    	 int percentage = 0;
    	 int opponentPos = opponent.getPawnPos() / 9;
         if(opponent.getPawnbelong().isRed()) {
         	percentage = 70 - (opponentPos * 40) / 8;
         }
         else {
         	percentage = 30 + (opponentPos * 40) / 8;
         }         
        Random rand = new Random();
        int n = rand.nextInt(100);
        if(this.getNumWalls() > 0) {
        	boolean flag = n < percentage;
        	Tile tile = this.board.getTile(opponent.getPawnPos());
        	
        	if(flag && opponent.getPawnbelong().isYellow() && opponent.getPawnPos() - 9 >= 0) {
        		tile = this.board.getTile(opponent.getPawnPos() - 9);
        	}
            if(flag && this.canPlaceWall(tile, true, wall)) {
                this.placeHorizontalWall(tile,wall);
                lastWallHorizontal = true;
                lastMove = getMove();
                return tile;
            }
            
            else if(this.canPlaceWall(tile, false, wall)) {
                this.placeVerticalWall(tile,wall);
                lastWallHorizontal = false;
                lastMove = getMove();
                return tile;
            }
        }
        lastMove = getRandomDes(board);
        return null;
    }
}
