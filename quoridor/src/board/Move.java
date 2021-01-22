package board;

import graphicInterface.Game;
/**
 * This class manages all the different moves of the game.
 * @author larry fotso.
 */

public  abstract class Move {
    protected final Board board;
    protected Pawn movedPawn;
    protected final int destinationCoordinate;
    protected final boolean ISFIRSTMOVE;
    public static final Move NULL_MOVE = new NullMove();

    public  Move(final Board board, final Pawn movedPawn, final int destinationCoordinate){
        this.board = board;
        this.movedPawn = movedPawn;
        this.destinationCoordinate = destinationCoordinate;
        this.ISFIRSTMOVE = movedPawn.isFirstMove();

    }

    private Move(final Board board ,final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPawn = null;
        this.ISFIRSTMOVE = false;
    }

    @Override
    public String toString(){
        return "o_o_o";

    }



    public Pawn getMovedPawn(){
        return movedPawn;
    }
    /**
     * Returns the actual coordinate of a pawn.
     * @return
     */
    private int getActualCoord() {
        return this.getMovedPawn().getPawnPos();
    }
    /**
     * Return the coordinate of where
     * @return
     */
    public int getDestinationCoordinate(){
        return destinationCoordinate;
    }

    @Override
    public int hashCode(){
        int prime = 1;
        int res = 1;
        res = prime * res + this.destinationCoordinate;
        res = prime * res + this.movedPawn.hashCode();
        res = prime * res + this.movedPawn.getPawnPos();
        return res;
    }
    @Override
    public boolean equals(Object other){
       if(this == other) return true;
       if(!(other instanceof  Move)) return false;
       Move otherMove = (Move) other;
       return getActualCoord() == otherMove.getActualCoord() && getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPawn().equals(otherMove.getMovedPawn());
    }


    // Not sure if imma use this method
    public boolean isAttack(){
        return false;
    }

    // Not sure about this method too.
    public Pawn getAttackPawn(){
        return null;
    }


    /**
     * We are using the Board builder to materialize a new board to return from execute.
     * In this method we will loop through all the Pawns on the board and if the Pawns are not the moved Pawn then it will just place on the board.
     * @return A new Board with the moved pawn.
     */
    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        
        for (Pawn p : this.board.actualPlayer().getActivePawns()){
            if(!this.movedPawn.equals(p)){
                builder.setPawn(p);
            }
            else{
                builder.setPawn(this.movedPawn.movePawn(this));
            }
        }
        for(Pawn pawn : this.board.actualPlayer().getOpponent().getActivePawns()){
            builder.setPawn(pawn);
        }
        //builder.setPawn(this.movedPawn.movePawn(this));// This will move the actual Pawn that we want to move.
        // Sets the moveMaker to the nextPlayer.
        builder.setMoveMaker(this.board.actualPlayer().getOpponent().getAlliance());


        return builder.build();
    }

    /**
     * @MainMove Class will take care of the normal move from one tile to and other and has as a mother the class Move.
     */

    public static final class MainMove extends Move{

        public MainMove(Board board, Pawn pawn, int destinationCoordinate) {
            super(board, pawn, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object o){
            return this == o || o instanceof MainMove && super.equals(o);
        }

        @Override
        public String toString(){
            return movedPawn.getPawnbelong().toString() + BoardUtils.getPositionAtCoord(this.destinationCoordinate);
        }


    }

    /**
     * this class takes care of the null Moves
     * @author larry Fotso
     */

    public static final class NullMove extends Move{
        public NullMove(){
            super(null ,  -1);

        }
        @Override
        public Board execute(){
            throw new RuntimeException("the null move cannot be executed ");
        }
    }

    public  static class FactorMove{

        private FactorMove(){

            throw new RuntimeException("Not instantiable ");
        }

        /**
         * Enables us to make a move.
         * @param board
         * @param actualCoord
         * @param nextCoord
         * @return
         */

        public static Move makeMoves(Board board, int actualCoord, int nextCoord){
            for(Move move : board.getAllLegalMoves()){
                if(move.getActualCoord() == actualCoord  && move.getDestinationCoordinate() == nextCoord) {
                    return move;
                }
            }
            return NULL_MOVE;
        }


    }



}
