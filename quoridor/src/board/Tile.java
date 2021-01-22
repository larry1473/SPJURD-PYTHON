package board;

import org.carrot2.shaded.guava.common.collect.ImmutableMap;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.Map;
/**
 * this class represents the tiles on the board.
 * @author larry Fotso
 */
public  class Tile {
	protected  int tileCoordinate;
	public final int x;
	public final int len;
	private Wall[] walls;
	private Direction[] directions;
	private boolean dir;
	private Wall wall;
	private Pawn pawnOnTile;


	public static Map<Integer, Tile> EMPTY_TILES_CACHE = createAllPossibleTiles();

	public Tile(final int tileCoordinate, int len,int x){
		this.tileCoordinate = tileCoordinate;
		this.len = len;
		this.x = x;
		createSpace(len,x);
		this.wall = new Wall(this,true);
	}
	public Tile(int tileCoordinate, int len,int x, Pawn pawn) {
		this(tileCoordinate, len, x);
		this.pawnOnTile = pawn;
	}
	/**
	 * Return the wall we wan to place.
	 * @return
	 */

	public Wall getWall(){
		return this.wall;
	}
	public Wall[] getWalls() {
		return walls;
	}
	public void setPawn(Pawn pawn) {
		pawnOnTile = pawn;
	}
	/**
	 * creates the different wall spaces on the tiles.
	 * @param len
	 * @param x
	 */
	private void createSpace(int len , int x){
		if(x != 0) {
			if (len == 1) {
				walls = new Wall[1];
				directions = new Direction[1];
				if (x == 1) {
					directions[0] = Direction.RIGHT;
				}
				else {
					directions[0] = Direction.DOWN;
				}
			} else if (len == 2) {
				walls = new Wall[2];
				directions = new Direction[2];
				directions[0] = Direction.DOWN;
				directions[1] = Direction.RIGHT;

			}
		}
		else{
			directions = new Direction[0];
			walls = new Wall[0];
		}

	}
	/**
	 * Returns the board of directions.
	 * @return
	 */

	public Direction[] getDirections() {
		return directions;
	}
	/**
	 * it places a given wall given a direction.
	 * @param wall
	 * @param dir
	 * @return
	 */

	public boolean placeWall(Wall wall,boolean dir){
		boolean placed = false;
		if (directions.length == 0){
			return placed;
		}
		else {
			if (dir == wall.getHorizontal()) { // if true

				for (int i = 0; i < directions.length; i++) {
					if (directions[i] == Direction.DOWN) {
						if (walls[i] == null) {
							walls[i] = wall;
							placed = true;
						}
					}
				}

			}
			else {

				for (int i = 0; i < directions.length; i++) {
					if (directions[i] == Direction.RIGHT) {
						if (walls[i] == null) {
							walls[i] = wall;
							
							placed = true;
						}

					}
				}
			}

		}


		return placed;
	}

	/**
	 * Removes a placed wall.
	 * @param wall
	 * @param dir
	 * @return
	 */
	public boolean removeWall(Wall wall, boolean dir){
		boolean placed = false;
		if (directions.length == 0){
			return placed;
		}
		else {
			if (dir == wall.getHorizontal()) { // if true

				for (int i = 0; i < directions.length; i++) {
					if (directions[i] == Direction.DOWN) {
						if (walls[i] != null) {
							walls[i] = null;
							placed = false;
						}
					}
				}

			}
			else {

				for (int i = 0; i < directions.length; i++) {
					if (directions[i] == Direction.RIGHT) {
						if (walls[i] != null) {
							walls[i] = null;
							System.out.println(System.identityHashCode(this));
							placed = false;
						}

					}
				}
			}

		}


		return placed;
	}

	/**
	 * checkes,if the tile has a horizonatal wall.
	 * @return
	 */
	public boolean hasHorizontalWall() {
		if(walls.length > 0 && walls[0] != null && walls[0].getDirection())
			return true;
		else if( walls.length > 1 && walls[0] != null  && walls[1].getDirection())
			return true;
		else 
			return false;
	}
	/**
	 * checkes,if the tile has a vertical wall.
	 * @return
	 */
	public boolean hasVerticalWall() {
		if(walls.length > 0 && walls[0] != null  && !walls[0].getDirection())
			return true;
		else if(walls.length > 1 && walls[0] != null  && !walls[1].getDirection())
			return true;
		else 
			return false;
	}


	public int getTileCoordinate(){
		return this.tileCoordinate;
	}


	/**
	 * create all tiles on the board.
	 * @param tileCoordinate tile position.
	 * @param pawn the pawn is the tile is occupied.
	 * @param len length of the list of wall.
	 * @param x
	 * @return
	 */
	public static Tile createTile(final int tileCoordinate,  final Pawn pawn,int len, int x ){

		Tile t = EMPTY_TILES_CACHE.get(tileCoordinate);
		t.setPawn(pawn);
		return t;
	}
	/**
	 * creates all the possible tiles.
	 * @return
	 */

	public static Map<Integer, Tile> createAllPossibleTiles(){
		final Map<Integer, Tile> TileMap = new HashMap<>();

		for(int i = 0; i < BoardUtils.NUM_OF_TILES; i++){
			if(i != 80){
				if(80 - i > 8){
					if ((i+1) % 9 == 0 && i != 0) {
						TileMap.put(i, new Tile(i,1,2) );
					}
					else {
						TileMap.put(i, new Tile(i,2,2) );
					}
				}
				else{
					TileMap.put(i, new Tile(i,1,1) );

				}

			}
			else{
				TileMap.put(i, new Tile(i,2,0) );
			}

		}
		return ImmutableMap.copyOf(TileMap);

	}


	/**
	 * this method tests if a tile is occupied.
	 * @return boolean if the tile is occupied or not.
	 */
	public boolean isTileOccupied() {
		return pawnOnTile != null;
	}

	public Pawn getPawn() {
		return this.pawnOnTile;
	}

	@Override
	public  String toString(){
		if(pawnOnTile != null)
			return getPawn().getPawnbelong().isRed()? getPawn().toString().toLowerCase() : getPawn().toString().toLowerCase();
			else
				return "-";
	}


}
