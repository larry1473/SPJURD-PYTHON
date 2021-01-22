package player;

import board.Board;
import board.Move;

/**
 * This class represent the transition from one to another when a move is made.
 * @creator Cendrick  Razakamaniraka , Larry Fotso
 */
public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus; // This will tell is if the move was made.

    /**
     * returns the move status.
     * @return
     */
    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus){

        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    /**
     * return the board on which we want to transition.
     * @return
     */
    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
