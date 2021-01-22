package player;

import board.*;

import java.util.Collection;
import java.util.Stack;

/**
 * the Player class serves as a mother class to the classes "Computer", "HumanPLayer"and defines all the necessary methods needed.
 * @cauthor Larry Fotso
 */
public abstract class Player {
    // the player's filed variable.
    protected Board board;
    protected int numWalls = 10;
    protected Collection<Move> legalMoves;
    protected Pawn pawn;
    protected boolean placed = false;
    
    static final int[] redWin = {72, 73, 74, 75, 76, 77, 78, 79, 80};
    static final int[] yelWin = {0, 1, 2, 3, 4, 5, 6, 7, 8};

    public Player(Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves, Pawn pawn) {
        this.board = board;
        this.legalMoves = legalMoves;
        this.pawn = pawn;
        
    }

    public boolean getPlaced(){
        return placed;
    }

    public Pawn getPawn() {
        return pawn;
    }

    /**
     * Allows us to test of the wanted move is legal or not.
     * @return returns true if its is else it returns false.
     */
    public boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move) || pawn.getPawnPos() == move.getDestinationCoordinate();
    }

    /**
     *
     * @return We return the player's active pawns on the board.
     */

    public abstract Collection<Pawn> getActivePawns();

    /**
     *
     * @return we return a collection of the player's legal moves .
     */

    public Collection<Move> getLegalMoves() {

        return this.legalMoves;
    }

    public void setLegalMoves(Collection<Move> legalMoves) {
    	this.legalMoves = legalMoves;
    }
    /**
     *
     * @return the player's alliance.
     */

    public abstract Alliance getAlliance();

    /**
     *
     * @return We the players opponent.
     */

    public abstract Player getOpponent();


    public int getNumWalls() {
        return numWalls;
    }


    /**
     *
     * @param move The move we want to make.
     * @return We return a new transition move.
     */

    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {// test if the wanted move is  not a legal move
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE); // if the condition is true we return a illegal move
        }

        final Board transitionBoard = move.execute(); // we make the move

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);// an return a new transition move.
    }

    /**
     * This method enable to place a wall horizontally.
     * @param tile1 the tile on which we want to place the wall.
     * @param wall the wall to place on the tile.
     */


    public void placeHorizontalWall(Tile tile1, Wall wall) {
    	if(numWalls > 0) {
            if(tile1.getTileCoordinate()-1 >= 0){
                tile1.placeWall(wall, wall.getHorizontal());
                if(tile1.getTileCoordinate() - 1 >= 0) {
	            	Tile tile2 = board.getTile(tile1.getTileCoordinate() - 1);
	                tile2.placeWall(wall, wall.getHorizontal());
                }
                numWalls -= 1;
            }
        }
    }
    
    public void removeHorizontalWall(Tile tile1, Wall wall) {
    	if(numWalls > 0) {
            if(tile1.getTileCoordinate()-1 >= 0){
                tile1.removeWall(wall, wall.getHorizontal());
                if(tile1.getTileCoordinate() - 1 >= 0) {
	            	Tile tile2 = board.getTile(tile1.getTileCoordinate() - 1);
	                tile2.removeWall(wall, wall.getHorizontal());
                }
                numWalls += 1;
            }
        }
    }

    /**
     * This method enable to place a wall vertically.
     * @param tile1 the tile on which we want to place the wall.
     * @param wall the wall to place on the tile.
     */

    public void placeVerticalWall(Tile tile1, Wall wall) {
    	if(numWalls > 0) {
            if(tile1.getTileCoordinate()-1 >= 0){
                tile1.placeWall(wall, wall.getVertical());
                if(tile1.getTileCoordinate() - 9 >= 0) {
	            	Tile tile2 = board.getTile(tile1.getTileCoordinate() - 9);
	                tile2.placeWall(wall, wall.getVertical());
                }
                numWalls -= 1;
            }
        }

    }
	/**
	 * This method enable to place a wall vertically.
	 * @param tile1 the tile on which we want to place the wall.
	 * @param wall the wall to place on the tile.
	 */
    public void removeVerticalWall(Tile tile1, Wall wall) {
    	if(numWalls > 0) {
            if(tile1.getTileCoordinate()-1 >= 0){
                tile1.removeWall(wall, wall.getVertical());
                if(tile1.getTileCoordinate() - 9 >= 0) {
	            	Tile tile2 = board.getTile(tile1.getTileCoordinate() - 9);
	                tile2.removeWall(wall, wall.getVertical());
                }
                numWalls += 1;
            }
        }

    }

	/**
	 * test all the possible conditon to see if the wall can be placed
	 * @param tile
	 * @param horizontal
	 * @param wall
	 * @return
	 */
    public boolean canPlaceWall(Tile tile, boolean horizontal, Wall wall) {
    	if(numWalls <= 0) return false;
    	
    	if(horizontal)  {
    		if(tile.getTileCoordinate() % 9 == 0) {
    			return false;
    		}
    		else if(tile.hasHorizontalWall()) {
    			return false;
    		}
    		else if(tile.getTileCoordinate() - 1 >= 0) {
    			Tile tile2 = board.getTile(tile.getTileCoordinate() - 1);
    			if(tile2.len == 1 && tile2.getWalls()[0] != null) {
    				if(tile2.getTileCoordinate() - 9 >= 0) {
        				Tile tile3 = board.getTile(tile2.getTileCoordinate() - 9);
        				if(tile3.getWalls()[0] != tile2.getWalls()[0]) {
                			return false;
        				}
        			}
        			return false;
        		}
    			else if(tile2.len == 2 && tile2.getWalls()[1] != null) {
    				if(tile2.getTileCoordinate() - 9 >= 0) {
        				Tile tile3 = board.getTile(tile2.getTileCoordinate() - 9);
        				if(tile3.len == 2 && tile3.getWalls()[1] != tile2.getWalls()[1]) {
                			return false;
        				}
        				else if(tile3.len == 1 && tile3.getWalls()[0] != tile2.getWalls()[1]) {
                			return false;
        				}
        			}
        		}
    			else if(tile2.len == 2 && tile2.getWalls()[0] != null && tile2.getWalls()[0].getDirection()) {
    				return false;
        		}
    		}
    	}
    	else {
    		if(tile.getTileCoordinate() <= 8) {
    			return false;
    		}
    		else if(tile.len == 1 && tile.getWalls()[0] != null) {
    			return false;
    		}
    		else if(tile.len == 2 && tile.getWalls()[1] != null) {
    			return false;
    		}
    		else if(tile.getTileCoordinate() - 9 >= 0) {
    			Tile tile2 = board.getTile(tile.getTileCoordinate() - 9);
    			if(tile2.len == 1 && tile2.getWalls()[0] != null) {
        			return false;
        		}
        		else if(tile2.len == 2 && (tile2.getWalls()[1] != null)) {
        			return false;
        		}
        		else if(tile2.len == 2 && (tile2.getWalls()[0] != null)) {
        			if(tile2.getTileCoordinate() - 1 >= 0) {
        				Tile tile3 = board.getTile(tile2.getTileCoordinate() - 1);
        				if(tile3.getWalls()[0] != tile2.getWalls()[0]) {
                			return false;
        				}
        			}
        		}
    		}
    	}
    	
    	return !arePlayersBlocked(tile, horizontal, wall);
    }

	/**
	 * returns if the player is completely blocked.
 	 * @param tile
	 * @param horizontal
	 * @param wall
	 * @return
	 */
    private boolean arePlayersBlocked(Tile tile, boolean horizontal, Wall wall) {
    	boolean flag = true;
    	if(horizontal) {
    		placeHorizontalWall(tile, wall);
    	} else {
    		placeVerticalWall(tile, wall);
    	}
    	for(Pawn pawn : board.getAllPieces()) {
    		Stack<Tile> stack = new Stack<Tile>();
    		stack.add(board.getTile(pawn.getPawnPos()));
    		boolean[] visited = new boolean[81];
    		visited[pawn.getPawnPos()] = true;
        	flag = true;
	        
			while(!stack.isEmpty()) {
				Tile current = stack.pop();
				if(pawn.getPawnbelong().isRed() && isRedOver(current.getTileCoordinate())) {
					flag = false;
					break;
				}
				else if(pawn.getPawnbelong().isYellow() && isYellowOver(current.getTileCoordinate())) {
					flag = false;
					break;
				}
				
				Collection<Move> moves  = pawn.calculateLegalMove(board, current.getTileCoordinate());
				for(Move move : moves) {
					if(move.getDestinationCoordinate() >= 0 && move.getDestinationCoordinate() <= 80 && !visited[move.getDestinationCoordinate()]) {
						Tile t = board.getTile(move.getDestinationCoordinate());
						stack.add(t);
						visited[move.getDestinationCoordinate()] = true;
					}
				}
			}
			if(flag) {
				break;
			}
    	}
    	
    	if(horizontal) {
    		removeHorizontalWall(tile, wall);
    	} else {
    		removeVerticalWall(tile, wall);
    	}
    	
    	return flag;
    }

    /**
     *
     * @return return true if the wall game is over else false.
     */

    public boolean isGameOver() {
        
        for (int i = 0; i < 9; i++) {// loop for zero to nine
            for (Pawn p : getActivePawns()) {// loop through the active pawns on the board.
                if (p.getPawnbelong().isYellow() && p.getPawnPos() == yelWin[i]) {// we test the position of the pawn
                    return true;// is the pawns is in the adversary's side of the return true
                } else if (p.getPawnbelong().isRed() && p.getPawnPos() == redWin[i]) {
                    return true;

                }
            }
        }
        return false; // else we return false an the game ends.

    }

	/**
	 * test if the yellow test has won
	 * @param pos
	 * @return
	 */
	private boolean isYellowOver(int pos) {
    	for (int i = 0; i < 9; i++) {// loop for zero to nine
    		if (pos == yelWin[i]) {// we test the position of the pawn
                return true;// is the pawns is in the adversary's side of the return true
            }
        }
    	return false;
    }

	/**
	 * test if the red player has won.
	 * @param pos
	 * @return
	 */
	private boolean isRedOver(int pos) {
    	for (int i = 0; i < 9; i++) {// loop for zero to nine
    		if (pos == redWin[i]) {// we test the position of the pawn
                return true;// is the pawns is in the adversary's side of the return true
            }
        }
    	return false;
    }

}