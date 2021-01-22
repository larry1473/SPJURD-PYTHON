package board;

import org.carrot2.shaded.guava.common.collect.ImmutableList;
import org.carrot2.shaded.guava.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * this class contains all the necessary stuff the board needs
 * @author Larry Fotso
 */

public class BoardUtils {
    public final boolean[] FIRST_COLUMN  = initColumn(0);
    public final boolean[] NINTH_COLUMN = initColumn(8);

    public static final String[]  ALGEBRIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORD = initializePositionToCoord();

    public static  final int NUM_OF_TILES = 81;
    public static final int NUM_TILES_PER_ROW = 9;
    public static final int NUM_COLS = 9;
    /**
     * get all the position and changes it to an algebraic notion.
     * @return
     */
    private static Map<String, Integer> initializePositionToCoord() {
        final Map<String, Integer> positionCoord = new HashMap<>();

        for(int i = 0; i< NUM_OF_TILES; i++){
            positionCoord.put(ALGEBRIC_NOTATION[1], i);
        }
        return ImmutableMap.copyOf(positionCoord);
    }

    /**
     * Initial the board in an algebraic notation.
     * @return
     */
    private static String[] initializeAlgebraicNotation() {// Initial the board in an algebraic notation.
        return new String[]{
                "a9", "b9", "c9", "d9", "e9", "f9", "g9", "h9", "i9",
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", "i8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", "i7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", "i6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1",
        };


    }



    public static int  getCoordAtPosition(final String position) {
        return POSITION_TO_COORD.get(position);
    }
    public static String getPositionAtCoord(final int coord){
        return ALGEBRIC_NOTATION[coord];
    }


    /**
     * tests the edge cases exceptions.
     * @param i
     * @return
     */

    private boolean[] initColumn(int i) {
        final boolean[] column = new boolean[NUM_OF_TILES];
        do{
            column[i] = true;
            i += NUM_TILES_PER_ROW;
        }
        while (i < NUM_OF_TILES);
        return column;
    }

    /**
     * Test if the game is over.
     * @param board
     * @return
     */
    public static boolean isGameOver(Board board){
        return board.actualPlayer().isGameOver();
    }






}
