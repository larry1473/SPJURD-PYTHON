package board;

import java.util.List;
/**
 * this pawn is a child of the class pawn and represents the yellow player's pawn.
 * @author larry Fotso
 */
public class YellowPawn extends Pawn {

    public YellowPawn(pawnType yellowpawn, int position, Alliance pawnbelong) {
        super(yellowpawn,position, pawnbelong);
    }

    /**
     *
     * @param move An instance of Move tha we help us create the a new YellowPawn and place on a the tile we want it to move on.
     * @return A new YellowPawn
     */
    @Override
    public YellowPawn movePawn(Move move) {
        return new YellowPawn(pawnType.YELLOWPAWN,move.getDestinationCoordinate(),move.getMovedPawn().getPawnbelong());
    }

    @Override

    public String toString(){
        return pawnType.YELLOWPAWN.toString();
    }
}
