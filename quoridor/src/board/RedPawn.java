package board;

import java.util.List;
/**
 * this pawn is a child of the class pawn and represents the red players pawn.
 * @author larry Fotso
 */
public class RedPawn extends Pawn {


    public RedPawn(pawnType pawnType, int position, Alliance pawnbelong) {
        super(pawnType, position, pawnbelong);
    }




    /**
     *
     * @param move An instance of Move tha we help us create the a new RedPawn and place on a the tile we want it to move on.
     * @return A new RedPawn
     */

    @Override
    public RedPawn movePawn(Move move) {
        return new RedPawn(pawnType.REDPAWN,move.getDestinationCoordinate(),move.getMovedPawn().getPawnbelong());

    }

    @Override

    public String toString(){
        return pawnType.REDPAWN.toString();
    }
}
