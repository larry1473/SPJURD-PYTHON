package player;

import board.Board;
import board.Move;

/**
 * @author larry Fotso
 */
public interface MoveStrategy {

    public Move execute(Board board);
}
