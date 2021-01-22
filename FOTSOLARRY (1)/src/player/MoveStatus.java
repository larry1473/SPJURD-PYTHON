package player;
import board.Board;

/**
 * this emumuration is to represent the different move status.
 * @author larry Fotso.
 */
public enum MoveStatus {
    DONE{
        @Override
        public boolean isDone(){
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    };
    public abstract boolean isDone();

}
