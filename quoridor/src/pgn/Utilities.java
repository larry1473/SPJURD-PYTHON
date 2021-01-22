package pgn;

import board.*;
/**
 * this helpes in the saving of the game.
 * @author larry Fotso.
 */
public class Utilities {

    private Utilities() {
        throw new RuntimeException("Not Instantiable!");
    }


    public static String createFENFromGame(final Board board) {
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                "0 1";
    }

    public static Board createGameFromFEN(final String fenString) {
        return parseFEN(fenString);
    }

    private static Board parseFEN(final String fenString) {
        final String[] fenPartitions = fenString.trim().split(" ");
        final Board.Builder builder = new Board.Builder();
        final String gameConfiguration = fenPartitions[0];
        final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                .replaceAll("8", "--------")
                .replaceAll("7", "-------")
                .replaceAll("6", "------")
                .replaceAll("5", "-----")
                .replaceAll("4", "----")
                .replaceAll("3", "---")
                .replaceAll("2", "--")
                .replaceAll("1", "-")
                .toCharArray();
        int i = 0;
        while (i < boardTiles.length) {
            switch (boardTiles[i]) {
                case 'r':
                    builder.setPawn(new RedPawn(Pawn.pawnType.REDPAWN, i, Alliance.RED));
                    i++;
                    break;
                case 'Y':
                    builder.setPawn(new YellowPawn(Pawn.pawnType.YELLOWPAWN, i, Alliance.YELLOW));
                    i++;
                    break;
                case '-':
                    i++;
                    break;
                default:
                    throw new RuntimeException("Invalid FEN String " + gameConfiguration);
            }
        }
        builder.setMoveMaker(moveMaker(fenPartitions[1]));
        return builder.build();
    }

    private static Alliance moveMaker(final String moveMakerString) {
        if (moveMakerString.equals("r")) {
            return Alliance.RED;
        } else if (moveMakerString.equals("y")) {
            return Alliance.YELLOW;
        }
        throw new RuntimeException("Invalid FEN String " + moveMakerString);
    }


    private static String calculateBoardText(final Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_OF_TILES; i++) {
            final String tileText = board.getTile(i).getPawn() == null ? "-" :
                    board.getTile(i).getPawn().getPawnbelong().isRed() ? board.getTile(i).getPawn().toString() :
                            board.getTile(i).getPawn().toString().toLowerCase();
            builder.append(tileText);
        }
        builder.insert(8, "/");
        builder.insert(17, "/");
        builder.insert(26, "/");
        builder.insert(35, "/");
        builder.insert(44, "/");
        builder.insert(53, "/");
        builder.insert(62, "/");
        builder.insert(71,"/");
        return builder.toString()
                .replaceAll("---------", "9")
                .replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }

    private static String calculateCurrentPlayerText(final Board board) {
        return board.actualPlayer().toString().substring(0, 1).toLowerCase();
    }


}
