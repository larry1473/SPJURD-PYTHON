package player;

import board.Alliance;
import board.Board;
import board.Move;
import board.Pawn;

import java.util.Collection;
import java.util.List;
/**
 * this class represents the red player.
 * @author larry Fotso
 */
public class RedPlayer extends Player {
    protected int numWalls = 10;

    public RedPlayer(Board board, Collection<Move> redDefaultMoves, Collection<Move> yellowDefaultMoves, Pawn pawn) {
        super(board,redDefaultMoves,yellowDefaultMoves, pawn);
    }

    @Override
    public int getNumWalls() {
        return numWalls;
    }

    @Override
    public Collection<Pawn> getActivePawns() {

        return this.board.getRedPawn();
    }
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }

    @Override
    public Player getOpponent() {
        return this.board.yellowPlayer();
    }
}
