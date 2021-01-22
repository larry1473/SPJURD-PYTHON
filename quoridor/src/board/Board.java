package board;



import org.carrot2.shaded.guava.common.collect.ImmutableList;
import org.carrot2.shaded.guava.common.collect.Iterables;
import player.Player;
import player.RedPlayer;
import player.YellowPlayer;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class main aim is to create the object board where the game will take place and on will all the different moves and walls will be placed on.
 * @author  Larry Fotso
 */

public class Board {
    private final List<Tile> gameBoard; // the list of tiles that will constitute the board
    private  Collection<Pawn> redPawn;
    private  Collection<Pawn> yellowPawn;

    private  RedPlayer redPlayer;
    private  YellowPlayer yellowPlayer;
    private  Player actualPlayer;

    //private static Tile  tile;

    private final int walls = 20;


    /**
     * The board constructor.
     * @param builder the board builder
     */

    public Board(Builder builder){
        this.gameBoard = makeGameBoard(builder);
        this.redPawn = calculateActivePawns(this.gameBoard, Alliance.RED);
        this.yellowPawn = calculateActivePawns(this.gameBoard, Alliance.YELLOW);

        final Collection<Move> redDefaultMoves = calculateLegalMoves(this.redPawn);
        final Collection<Move> yellowDefaultMoves = calculateLegalMoves(this.yellowPawn);

        this.redPlayer =  new RedPlayer(this,redDefaultMoves,yellowDefaultMoves, (Pawn)this.redPawn.toArray()[0]);
        this.yellowPlayer  = new YellowPlayer(this,yellowDefaultMoves,redDefaultMoves, (Pawn)this.yellowPawn.toArray()[0]);

        this.actualPlayer = builder.moverMaker.choosePlayer(this.redPlayer,this.yellowPlayer);

    }

    public void updateMoveYellow() {
    	final Collection<Move> yellowDefaultMoves = calculateLegalMoves(this.yellowPawn);
    	this.yellowPlayer.setLegalMoves(yellowDefaultMoves);
    }

    
    public boolean hasHorizontalWall(int pos) {
    	if(pos >= 0 && pos <= 80) {
    		return getTile(pos).hasHorizontalWall();
    	}
    	return false;
    }
    public boolean hasVerticalWall(int pos) {
    	if(pos >= 0 && pos <= 80) {
    		return getTile(pos).hasVerticalWall();
    	}
    	return false;
    }

    public Player actualPlayer(){ // Returns the actual player.
        return this.actualPlayer;
    }

    public Collection<Pawn> getRedPawn(){// Returns the redpawn.
        return this.redPawn;
    }

    public Collection<Pawn> getYellowPawn(){// Returns the YellowPawn.
        return this.yellowPawn;
    }

    public Player redPlayer(){// Returns the redPlayer.
        return this.redPlayer;
    }

    public Player yellowPlayer(){// Returns the YellowPlayer.
        return this.yellowPlayer;
    }

    /**
     * Gets all the pawns on the board.
     * @param gameBoard The board we are playing on.
     * @param alliance the pawns teams.
     * @return A Collections of Pawns.
     */

    private Collection<Pawn> calculateActivePawns(List<Tile> gameBoard, Alliance alliance) {
        final List<Pawn> activePawns = new ArrayList<>();
        for(final Tile tile : gameBoard){
            if(tile.isTileOccupied()){
                final Pawn pawn = tile.getPawn();
                if(pawn.getPawnbelong() == alliance){
                    activePawns.add(pawn);
                }
            }
        }
        return ImmutableList.copyOf(activePawns);
    }

    /**
     * Calculates the possible moves of all the pawns.
     * @param pawns the pawns we want to calculate their legal moves.
     * @return all the legal moves.
     */

    private Collection<Move> calculateLegalMoves(Collection<Pawn> pawns) {
        final  List<Move> legalMoves = new ArrayList<>();
        for(final Pawn pawn : pawns) {
            legalMoves.addAll(pawn.calculateLegalMove(this));
        }
        return ImmutableList.copyOf(legalMoves);

    }
    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_OF_TILES; i++ ){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if((i + 1)%BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");

            }
        }
        return builder.toString();
    }

    public static void clearBoard() {
    	Tile.EMPTY_TILES_CACHE = Tile.createAllPossibleTiles();
    }
    // This method populates a list of tiles numbered from 0-81 that will represent our game board.
    private static List<Tile>makeGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_OF_TILES];
        for( int i = 0; i < BoardUtils.NUM_OF_TILES; i++ ){ // we loop through  and map a pawn to the tiles ID
            if(i != 80 ) {
                if (80 - i > 8) {
                    if ((i+1) % 9 == 0 && i != 0) {
                        tiles[i] = Tile.createTile(i, builder.boardconfiguration.get(i), 1, 2);
                    } else {
                        tiles[i] = Tile.createTile(i,builder.boardconfiguration.get(i), 2, 2);
                    }

                } else {
                    tiles[i] = Tile.createTile(i, builder.boardconfiguration.get(i), 1, 1);

                }


            }
            else{
                tiles[i] = Tile.createTile(i, builder.boardconfiguration.get(i), 2, 0);
            }



        }
        return ImmutableList.copyOf(tiles);
    }

    /**
     * the method will help us to create a default board when the game starts with the pawns placed on it.
     * @return the default board
     */
    public static Board defaultBoard(){
        final Builder builder = new Builder();
        // Default positioning of the RED pawn.
        builder.setPawn(new RedPawn(Pawn.pawnType.REDPAWN,4,Alliance.RED));
        //Default positioning of the RED pawn.
        builder.setPawn(new YellowPawn(Pawn.pawnType.YELLOWPAWN, 76,Alliance.YELLOW));
        builder.setMoveMaker(Alliance.YELLOW);

        return builder.build();
    }


    /**
     *
     * @param coordinate the tile coordinate.
     * @return the tile with that tile coordinate.
     */
    public Tile getTile(int coordinate) {
        return gameBoard.get(coordinate);
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.redPlayer.getLegalMoves(),this.yellowPlayer.getLegalMoves()));// Concatenate both players legalMoves.
    }

    /**
     *
     * @return All the pawns on the board.
     */
    public Collection<Pawn> getAllPieces() {
        return Stream.concat(this.redPawn.stream(),
                this.yellowPawn.stream()).collect(Collectors.toList());
    } 
    /**
     * This create the board.
     * @author  Larry Fotso
     */
    public static  class Builder{
        Map<Integer, Pawn> boardconfiguration; // This variable will help us to map the tileID to pawn on the tile.
        Alliance moverMaker; // Helps us to keep track of the person who has to move.

        public Builder(){
            this.boardconfiguration = new HashMap<>();

        }

        /**
         *
         * @param pawn the pawn we want to add on the builder
         * @return an instance of builder to where is was called from.
         */
        public Builder setPawn(final Pawn pawn ){
            this.boardconfiguration.put(pawn.getPawnPos(), pawn);
            return this;
        }

        /**
         *
         * @param moveMaker the alliance of the person who has to play
         * @return an instance of builder with a changed movermaker.
         */
        public Builder setMoveMaker(final Alliance moveMaker){
            this.moverMaker = moveMaker;
            return this;
        }
        public Board build(){ // This method builds the board we want.
            return new Board(this);

        }

    }
}
