package player;

import board.Alliance;
import board.Board;
import board.Move;
import board.Pawn;

import java.util.Collection;
/**
 * this class represents the yellow player.
 * @author larry Fotso
 */
public class YellowPlayer extends Player{
    protected int numWalls = 10;
    public YellowPlayer(Board board, Collection<Move> yellowDefaultMoves, Collection<Move> redDefaultMoves, Pawn pawn) {
        super(board,yellowDefaultMoves,redDefaultMoves, pawn);
    }
    

    @Override
    public int getNumWalls() {
        return numWalls;
    }

    @Override
    public Collection<Pawn> getActivePawns() {
        return this.board.getYellowPawn();
    }



    @Override
    public Alliance getAlliance() {
        return Alliance.YELLOW;
    }

    @Override
    public Player getOpponent() {
        return this.board.redPlayer();
    }
}
