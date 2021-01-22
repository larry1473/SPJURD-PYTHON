package board;

import java.awt.*;

/**
 * The class Wall is the representation of the walls in the game.
 * @author larry Fotso
 */
public class Wall  {
    private boolean direction;// the direction we want  to give to the wall.
    private  final  boolean HORIZONTAL = true;
    private  final boolean VERTICAL = false;
    private Tile tile;



    public Wall ( Tile tile,boolean direction) {
        this.direction = direction;
        this.tile = tile;


    }
    /**
     * return the horizontal direction.
     * @return
     */
    public boolean getHorizontal(){
        return HORIZONTAL;
    }
    /**
     * return the vertical direction.
     * @return
     */
    public boolean getVertical(){
        return VERTICAL;
    }






    public  boolean getDirection() {
        return direction;
    }


     @Override
    public int hashCode(){
        int key =  2;
         if(direction == HORIZONTAL){
             key++;
         }
         return key;
    }

    @Override
    public boolean equals(Object wall){
        boolean wallIsWall = wall instanceof Wall;
        return wallIsWall && hashCode() == wall.hashCode();
    }

    @Override
    public  String toString(){
       String result = "" ;
       if(direction){
           result += "H";
       }
       else{
           result += "|";
       }
       return result;

    }


}
