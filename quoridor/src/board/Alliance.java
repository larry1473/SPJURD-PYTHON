package board;

import player.Player;
import player.RedPlayer;
import player.YellowPlayer;

/**
 * this enumeration represent the different teams of the games
 * @author larry Fotso
 */
public enum Alliance {
     RED{
         @Override
         public boolean isRed() {
             return true;
         }

         @Override
         public boolean isYellow() {
             return false;
         }


         @Override
         public Player choosePlayer(RedPlayer redPlayer, YellowPlayer yellowPlayer) {
             return redPlayer;
         }
     },
    YELLOW {
        @Override
        public boolean isRed() {
            return false;
        }

        @Override
        public boolean isYellow() {
            return true;
        }



        @Override
        public Player choosePlayer(RedPlayer redPlayer, YellowPlayer yellowPlayer) {
            return yellowPlayer;
        }
    };

    public abstract boolean isRed();// Helps to know which team is red.

    public abstract boolean isYellow(); // Helps to know which team is yellow.


    public abstract Player choosePlayer(RedPlayer redPlayer, YellowPlayer yellowPlayer); // Chooses between the two players.
}
