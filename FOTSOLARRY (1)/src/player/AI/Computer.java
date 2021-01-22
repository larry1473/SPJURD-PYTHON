package player.AI;

import board.*;
import player.MoveStrategy;
import player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Stack;

/**
 * This class plays the role of the artificial intelligence which has two levels that is "easy" & "hard"
 * and extends the class Player.
 * @author Larry Fotso
 */
public class Computer extends Player implements MoveStrategy {

	public boolean lastWallHorizontal = false;
	public Move lastMove;
	
    public Computer(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves, Pawn pawn) {
        super(board, legalMoves, opponentMoves, pawn);
    }
    /**
     * get all the pawns on the board.
     * @param board
     * @return
     */
    public ArrayList<Pawn> getPawns(Board board){
        final ArrayList<Pawn> pawns = new ArrayList<>();
        for(Pawn pawn : board.actualPlayer().getActivePawns()){
            if(pawn.calculateLegalMove(board).isEmpty()){
                continue;
            }
            pawns.add(pawn);
        }
        return pawns;
    }

    @Override
    public Move execute(Board board) {
        return getRandomDes(board);
    }
    /**
     * this method helps to create random moves.
     * @param board
     * @return
     */
    public Move getRandomDes(Board board){
        Object[] res = pawn.calculateLegalMove(board).toArray();
        Random ran = new Random();
        int num = ran.nextInt(res.length);
        return (Move) res[num];

    }

    /**
     * gets an in place move.
     * @return
     */
    public Move getMove() {
    	final ArrayList<Pawn> ActivePawns = getPawns(board);
        this.pawn = ActivePawns.get(0);
        return this.pawn.inPlaceMove(board);
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
        Random rand = new Random();
        int n = rand.nextInt(3);
        if(this.getNumWalls() > 0) {
        	Tile tile = this.board.getTile(opponent.getPawnPos());
        	boolean flag = n == 1;
        	
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




    @Override
    public Collection<Pawn> getActivePawns() {
        if(this.getOpponent().getAlliance().isRed())
            return this.board.getYellowPawn();
        return this.board.getRedPawn();
    }

    @Override
    public Alliance getAlliance() {
        if(this.getOpponent().getAlliance().isRed())
            return Alliance.YELLOW;
        return Alliance.RED;
    }

    @Override
    public Player getOpponent() {
       if(this.pawn.getPawnbelong().isRed())
           return this.board.yellowPlayer();
       return this.board.redPlayer();
    }
    public Pawn getPawnOpponent() {
    	if(this.pawn.getPawnbelong().isRed())
            return (Pawn) this.board.getYellowPawn().toArray()[0];
        return (Pawn) this.board.getRedPawn().toArray()[0];
    }

}
