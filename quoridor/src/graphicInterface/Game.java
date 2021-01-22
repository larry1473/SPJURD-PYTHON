package graphicInterface;

import board.*;
import org.carrot2.shaded.guava.common.collect.Lists;
import pgn.Utilities;
import player.AI.Computer;
import player.AI.StrategicAI;
import player.MoveStrategy;
import player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class Game extends Observable  {
    private  JFrame gameFrame;
    private   GameHistoryPanel gameHistoryPanel;
    private NumWallsPanel numWallsPanel;
    private BoardDirection boardDirection;
    private static Board gameBoard;
    private final static Dimension frame_Dimension= new Dimension(900,900);
    private final Dimension bord_Dimension = new Dimension(400,350);
    private final Dimension TilePanel_Dimension = new Dimension(50,50);
    private final Dimension horizontalWall_Dimension = new Dimension(50,12);
    private final Dimension verticalWall_Dimension  = new Dimension(12,50);
    
    private  BoardPanel  boardPanel;
    private MoveHistory moveHistory;
    private final String PawnIconPath = "images/pawns/";
    private Tile sourceTile;
    private Tile destinationTile;
    private Pawn humanMovedPawn;
    private boolean highLight;

    private static final Game GAME = new Game();
    private GameSetup gameSetup;
    private Move AiMove;
    private JLabel high;
    private JLabel pawnAssign;
    /**
     * this class helps in representing all the logic done in the project.
     *@author larry Fotso.
     */
    public  Game(){
        this.numWallsPanel = new NumWallsPanel();
		this.gameFrame = new JFrame("Quoridor");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar jMBar = populateMenuBar();
        this.gameFrame.setJMenuBar(jMBar);
        this.gameFrame.setSize(frame_Dimension);
        this.gameBoard = Board.defaultBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.numWallsPanel = new NumWallsPanel();
        this.boardPanel = new BoardPanel();
        this.moveHistory = new MoveHistory();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        boardPanel.setBackground(Color.gray);
        this.boardDirection = BoardDirection.NORMAL;
        this.highLight = true;
        this.gameFrame.add(this.numWallsPanel,BorderLayout.WEST);// Adding the number of walls panels to the left.
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);// Adding the game to  the center.
        this.gameFrame.add(this.gameHistoryPanel,BorderLayout.EAST); // Adding the the move history to right.
        this.gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * enables us to show the frame and all the components on it.
     */
    public  void show(){
        Game.get().getMoveHistory().clear();
        Game.get().getGameHistoryPanel().redo(gameBoard,Game.get().getMoveHistory());
        Game.get().getNumWallsPanel().redo(Game.get().getMoveHistory());
        Game.get().getBoardPanel().drawBoard(Game.get().getGameBoard());


    }
    /**
     * Return the game frame.
     * @return
     */
    private JFrame getGameFrame() {
        return this.gameFrame;
    }
    /**
     * Return the an instance of game.
     * @return
     */
    public static Game get(){
        return GAME;
    }
    /**
     * returns the game setup.
     * @return
     */
    public   GameSetup getGameSetup() {
        return this.gameSetup;
    }

    /**
     * return the logic board.
     * @return
     */
    private Board getGameBoard(){
        return this.gameBoard;
    }

    /**
     * it updates the computer's move;
     * @param move
     */
    private void updateGameBoard(final Board board){
        this.gameBoard = board;
    }
    private  void updateAiMove( final Move move){
        this.AiMove = move;
        
    }

    /**
     * clear every thing from the board and reset the postions of the pawns
     */
    public void reset() {
    	JOptionPane.showMessageDialog(null, "The " + Game.get().getGameBoard().actualPlayer().getAlliance().toString() + " won ");
    	
    	gameBoard= Board.defaultBoard();//Game.get().getGameBoard().actualPlayer().placeHorizontalWall(tilePanel.getTile(),wall);
        Game.get().getMoveHistory().clear();
        Game.get().getGameHistoryPanel().redo(gameBoard, Game.get().getMoveHistory());
        Game.get().getNumWallsPanel().redo(Game.get().getMoveHistory());
        gameSetup= new GameSetup(getGameFrame(), true);
        Board.clearBoard();
        
        StrategicAI.clear();
        for(int i = 0; i < BoardUtils.NUM_OF_TILES; i++) {
        	TilePanel t1 = Game.get().boardPanel.boardTiles.get(i);
        	if(t1.hWall != null) {
        		t1.hWall.removeWall();
        	}
        	if(t1.vWall != null) {
        		t1.vWall.removeWall();
        	}
        }
        Game.get().getGameHistoryPanel().validate();
        Game.get().getBoardPanel().drawBoard(gameBoard);
    }
    private MoveHistory getMoveHistory(){
        return this.moveHistory;
    }
    
    private GameHistoryPanel getGameHistoryPanel(){
        return  this.gameHistoryPanel;
    }
    private  NumWallsPanel getNumWallsPanel(){
        return this.numWallsPanel;
    }
    
    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }
    
    private void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }
    /**
     * creating the menu bar and adding all the components.
     * @return
     */
    private JMenuBar populateMenuBar() {
        final JMenuBar jMBar = new JMenuBar();
        jMBar.add(createFileMenu());
        jMBar.add(createPreferredMenu());
        jMBar.add(option());
        return jMBar;
    }
    /**
     * creating file jmenu and adding listeners.
     * @return
     */
    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("file");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        final JMenuItem openPGN = new JMenuItem("Load PGN File", KeyEvent.VK_O);
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int option = chooser.showOpenDialog(Game.get().getGameFrame());
                if (option == JFileChooser.APPROVE_OPTION) {
                   // loadPGNFile(chooser.getSelectedFile());
                }
            }


        });
        fileMenu.add(openPGN);
        final JMenuItem openFEN = new JMenuItem("Load FEN File", KeyEvent.VK_F);
        openFEN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String fenString = JOptionPane.showInputDialog("Input FEN");
                if(fenString != null) {
                    //undoAllMoves();
                    gameBoard= Utilities.createGameFromFEN(fenString);
                    Game.get().getBoardPanel().drawBoard(gameBoard);
                }
            }
        });
        fileMenu.add(openFEN);
        final JMenuItem saveToPGN = new JMenuItem("Save Game", KeyEvent.VK_S);
        saveToPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileFilter() {
                    @Override
                    public String getDescription() {
                        return ".pgn";
                    }
                    @Override
                    public boolean accept(final File file) {
                        return file.isDirectory() || file.getName().toLowerCase().endsWith("pgn");
                    }
                });
                final int option = chooser.showSaveDialog(Game.get().getGameFrame());
                if (option == JFileChooser.APPROVE_OPTION) {
                    savePGNFile(chooser.getSelectedFile());
                }
            }
        });
        fileMenu.add(saveToPGN);

        final  JMenuItem closeMenu = new JMenuItem("Exit", KeyEvent.VK_X);
        final JMenuItem restart = new JMenuItem("restart",KeyEvent.VK_N);
        closeMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.get().reset();
            }
        });
        fileMenu.add(restart);
        fileMenu.add(closeMenu);
        return fileMenu;
    }
    /**
     * helps in saving the game.
     * @param pgnFile
     */
    private static void savePGNFile(final File pgnFile) {
        try {
            writeGameToPGNFile(pgnFile, Game.get().getMoveHistory());
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * writes the state of the game to the file.
     * @param pgnFile
     * @param moveHistory
     * @throws IOException
     */
    public static void writeGameToPGNFile(final File pgnFile, final MoveHistory moveHistory) throws IOException {
        final StringBuilder builder = new StringBuilder();
        builder.append(calculateEventString()).append("\n");
        builder.append(calculateDateString()).append("\n");
        builder.append(calculatePlyCountString(moveHistory)).append("\n");
        for(final Move move : moveHistory.getMoves()) {
            builder.append(move.toString()).append(" ");
        }
        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pgnFile, true)))) {
            writer.write(builder.toString());
        }
    }

    private static String calculateEventString() {
        return "[Event \"" +"Black Widow Game"+ "\"]";
    }

    private static String calculateDateString() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return "[Date \"" + dateFormat.format(new Date()) + "\"]";
    }

    private static String calculatePlyCountString(final MoveHistory moveHistory) {
        return "[PlyCount \"" +moveHistory.size() + "\"]";
    }
    /**
     * creating and adding listeners the preferences j menu .
     * @return
     */
    private JMenu createPreferredMenu(){
        final JMenu  preferredMenu = new JMenu("prefrences");
        final JMenuItem flipBoard = new JMenuItem("flip board");
        flipBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(gameBoard);
            }
        });
        preferredMenu.add(flipBoard);

        preferredMenu.addSeparator();

        final JCheckBoxMenuItem highlighter = new JCheckBoxMenuItem(" highLight moves ", false);
        highlighter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highLight = highlighter.isSelected();
            }
        });
        preferredMenu.add(highlighter);
        return preferredMenu;
    }
    /**
     * creating the option j menu bar and adding listeners.
     * @return
     */
    private JMenu option(){
        JMenu optionsMenu =  new JMenu("options");
        JMenuItem setgame = new JMenuItem("Setup Game ");

        setgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Game.get().getGameSetup().promptUser();
               Game.get().setupUpdate(Game.get().getGameSetup());
            }
        });
        optionsMenu.add(setgame);
        return  optionsMenu;
    }

    private void setupUpdate(GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);

    }
    /**
     * this class helpers to watch the game state.
     * @author larry Fotso.
     */
    private static class TableGameAIWatcher implements Observer{

        @Override
        public void update(Observable o, Object arg) {
            if(Game.get().getGameSetup().isAIPlayer(Game.get().getGameBoard().actualPlayer()) && ! Game.get().getGameBoard().actualPlayer().isGameOver()){
                AIThink think  = new AIThink();
                think.execute();
                
            }
            if(Game.get().getGameBoard().actualPlayer().isGameOver()) {
                Game.get().reset();
            }

        }
    }
    /**
     * this class helpers us to link the logical AI to the graphic.
     * @author larry Fotso.
     */
    private static class AIThink extends SwingWorker<Move, String>{
    	
        private AIThink(){
            
        }

        @Override
        /**
         * helps us to move and place the wall
         */
        protected Move doInBackground() throws Exception {
            Random rand = new Random();
            Computer ai = null;
            if(GAME.gameSetup.isStategicAI(gameBoard.actualPlayer())){
                ai = new StrategicAI(Game.get().getGameBoard(),gameBoard.actualPlayer().getLegalMoves(),gameBoard.actualPlayer().getOpponent().getLegalMoves(), gameBoard.actualPlayer().getPawn()); 
            } else {
            	ai = new Computer(Game.get().getGameBoard(),gameBoard.actualPlayer().getLegalMoves(),gameBoard.actualPlayer().getOpponent().getLegalMoves(), gameBoard.actualPlayer().getPawn());
            }
            
            int opponentPos = gameBoard.actualPlayer().getOpponent().getPawn().getPawnPos() / 9;
            int percentage = 0;
            if(gameBoard.actualPlayer().getOpponent().getPawn().getPawnbelong().isRed()) {
            	percentage = 70 - (opponentPos * 40) / 8;
            }
            else {
            	percentage = 30 + (opponentPos * 40) / 8;
            }
            int num = rand.nextInt(100);
            if(num < percentage){
                final Move bestMove = ai.execute(Game.get().getGameBoard());
                return bestMove;
            }
            else{
                 Tile tile = ai.placeRandomWalls(Game.get().getGameBoard().getTile(Game.get().getBoardPanel().tilePanel.tileId).getWall());
                 if(tile != null) {
                	 if(ai.lastWallHorizontal)
                		 Game.get().getBoardPanel().boardTiles.get(tile.getTileCoordinate()).hWall.placeWallComputer();
                	 else
                		 Game.get().getBoardPanel().boardTiles.get(tile.getTileCoordinate()).vWall.placeWallComputer();
                 }
                 return ai.lastMove;
            }

        }


        @Override 
        public void done(){
            try {

                final Move bestMove  = get();
                if(BoardUtils.isGameOver(Game.get().getGameBoard())){
                    Game.get().reset();
                    return;
                }
                Game.get().updateAiMove(bestMove);
                Game.get().updateGameBoard(Game.get().getGameBoard().actualPlayer().makeMove(bestMove).getTransitionBoard()); // After the Ai makes the move.
                Game.get().getMoveHistory().addMove(bestMove);
                Game.get().getGameHistoryPanel().redo(Game.get().getGameBoard(), Game.get().getMoveHistory());
                Game.get().getNumWallsPanel();
                Game.get().getBoardPanel().drawBoard(Game.get().gameBoard);
                Game.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * this class represents the graphic board.
     * @author larry Fotso
     */

    private class BoardPanel extends JPanel{
        private final List<TilePanel> boardTiles;
        private TilePanel tilePanel;

        BoardPanel(){
            super(new GridLayout(9,9));
            this.boardTiles = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUM_OF_TILES; i++){
                if(i != 80 ) {
                    if (80 - i > 8) {
                        if ((i+1) % 9 == 0 && i != 0) {
                            tilePanel = new TilePanel(new Tile(i,1,2),i);
                            this.boardTiles.add(tilePanel);
                        }
                        else {
                            tilePanel = new TilePanel(new Tile(i,2,2),i);
                            this.boardTiles.add(tilePanel);
                        }

                    }
                    else {
                        tilePanel = new TilePanel(new Tile(i,1,1),i);
                        this.boardTiles.add(tilePanel);

                    }


                }
                else{

                    tilePanel = new TilePanel(new Tile(i,2,0),i);
                    this.boardTiles.add(tilePanel);
                }
                 //TilePanel tilePanel = new TilePanel(i);
                //this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            for(int i = 1; i < BoardUtils.NUM_OF_TILES; i++) {
            	TilePanel t1 = this.boardTiles.get(i);
            	if(t1.hWall != null) {
            		t1.hWall.setTop(this.boardTiles.get(i - 1).hWall);
            	}
            	if(t1.vWall != null && i >= 9) {
            		t1.vWall.setLeft(this.boardTiles.get(i - 9).vWall);
            	}
            }
            setPreferredSize(bord_Dimension);
            validate();

        }
        /**
         * this method draws the graphic board.
         * @param gameBoard
         */
        public void drawBoard(Board gameBoard) {
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(gameBoard);
                add(tilePanel);

            }
            validate();
            repaint();
        }
    }

    /**
     * this class represents the graphical tile.
     * @author larry Fotso.
     */
    private class TilePanel extends JPanel{
        private int tileId;
        private Tile tile;

        private HorizontalWallPanel hWall;
        private VerticalWallPanel vWall;

        TilePanel(Tile tile, int tileId){
            super(new BorderLayout(0,0));
            this.tile = tile;
            this.tileId = tileId;
            this.vWall = new VerticalWallPanel(this);
            this.hWall = new HorizontalWallPanel(this);
            setPreferredSize(TilePanel_Dimension);
            assignTileColor();
            assignPawnToTile(gameBoard);
            initComponent();


            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(BoardUtils.isGameOver(Game.get().getGameBoard())) {
                        Game.get().reset();
                        return;
                    }



                    if(isRightMouseButton(e)){ // all is set to null when we right click
                        sourceTile = null;
                        humanMovedPawn = null;
                        destinationTile = null;
                    }
                    else if(isLeftMouseButton(e)){
                        if(sourceTile == null){// test if the initial tile is null if true we give a tile.

                            sourceTile = gameBoard.getTile(tileId);
                            humanMovedPawn = sourceTile.getPawn();
                            if(humanMovedPawn == null){
                                sourceTile = null;
                            }
                        }
                        else{

                            destinationTile = gameBoard.getTile(tileId);
                            final Move move = Move.FactorMove.makeMoves(gameBoard,sourceTile.getTileCoordinate(),destinationTile.getTileCoordinate());
                            final MoveTransition transition = gameBoard.actualPlayer().makeMove(move);// test is the game is over.

                            if(transition.getMoveStatus().isDone()){// test is the game is over.
                                gameBoard = transition.getTransitionBoard();
                                moveHistory.addMove(move);

                            }
                            // setting very thing to null after the move is done.
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPawn = null;

                        }
                        // update the graphics
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override

                            public void run() {
                                gameHistoryPanel.redo(gameBoard,moveHistory);
                                numWallsPanel.redo(moveHistory);
                                if(gameSetup.isAIPlayer(gameBoard.actualPlayer())){
                                    Game.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(gameBoard);
                            }
                        });

                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {


                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //setBackground(Color.darkGray);


                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //setBackground(Color.white);

                }
            });
            validate();




        }




        /**
         * this method places the pawn image on the tile where the pawn is suppose to be .
         * @param board
         */
        private void assignPawnToTile(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    
                    final BufferedImage image =
                            ImageIO.read(new File(PawnIconPath + board.getTile(this.tileId).getPawn().getPawnbelong().toString().substring(0,1) + board.getTile(this.tileId).getPawn().toString() + ".gif" ));
                    add(new JLabel(new ImageIcon(image)),BorderLayout.CENTER); //Todo specify another parameter

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * draws the all the different wall on the tiles.
         */
        private void initComponent(){
            if(tile.getDirections().length == 2){
                drawBoth();
            }
            else if(tile.getDirections().length == 1){
                if(tile.getDirections()[0] == Direction.RIGHT ){
                    drawRightWall1();
                }
                else
                    drawDownWall1();
            }
            else if( tile.getDirections().length == 0){

            }

        }

        /**
         * gives the tile panels it's color .
         */
        private void assignTileColor() {
            setBackground(Color.white);
        }

        /**
         * draws the graphic tile.
         * @param gameBoard
         */
        public void drawTile(Board gameBoard) {
            assignTileColor();
            assignPawnToTile(gameBoard);
            highlightLegalsMoves(gameBoard);
            initComponent();
            validate();
            repaint();




        }

        /**
         * high lights the possible pawn moves.
         * @param board
         */
        private void highlightLegalsMoves(final Board board){
            if(highLight){
                for(final Move move : pawnLegalMoves(board)){
                    if(move.getDestinationCoordinate() == this.tileId){
                        try{
                            this.add(new JLabel(new ImageIcon(ImageIO.read(new File("images/highlight/purple_square.png")))),BorderLayout.CENTER);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        /**
         * gets the pawn legal moves.
         * @param board
         * @return
         */
        private Collection<Move> pawnLegalMoves(final Board board){
            if(humanMovedPawn != null && humanMovedPawn.getPawnbelong() == board.actualPlayer().getAlliance()){
                return humanMovedPawn.calculateLegalMove(board);
            }
            return Collections.emptyList();
        }

        /**
         * returns the tile
         * @return
         */
        public Tile getTile() {
            return this.tile;
        }




        /**
         * draws a wall on the right and down.
         */

        private void drawBoth(){
            hWall.setPreferredSize(horizontalWall_Dimension);
            vWall.setPreferredSize(verticalWall_Dimension);


           this.add(hWall,BorderLayout.SOUTH);
           this.add(vWall,BorderLayout.EAST);




        }



        /**
         * draws a wall on the right.
         */
       private void drawRightWall1(){
            vWall.setPreferredSize(verticalWall_Dimension);
            this.add(vWall,BorderLayout.EAST);


        }


        /**
         * draws a wall down.
         */
        private void drawDownWall1(){
            hWall.setPreferredSize(horizontalWall_Dimension);
            this.add(hWall,BorderLayout.SOUTH);

        }




    }
    /**
     * helps to store the move history in the game.
     * @author larry Fotso
     */
    public static class MoveHistory{
        private final List<Move> moves;

        MoveHistory(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }

        public int size (){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }



    }


    /**
     * represents the horizontal wall.
     * @author larry Fotso
     */
    private class HorizontalWallPanel extends JPanel{
        Wall wall;
        private HorizontalWallPanel top;
        private TilePanel tilePanel;
        boolean isPlaced = false;
        
        HorizontalWallPanel(TilePanel tilePanel) {
            super(new BorderLayout());
            this.tilePanel = tilePanel;
            this.wall = new Wall(this.tilePanel.getTile(),true);
            this.setBackground(Color.gray);
            this.setPreferredSize(horizontalWall_Dimension);



            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if(isLeftMouseButton(e)) {
                        Tile tile = gameBoard.getTile(tilePanel.tileId);
                        if(Game.get().getGameBoard().actualPlayer().canPlaceWall(tile, true, wall)) {
	                        Game.get().getGameBoard().actualPlayer().placeHorizontalWall(tile,wall);
	                        ArrayList<Pawn> pawn  = (ArrayList<Pawn>) Game.get().getGameBoard().getAllPieces();
	                        Board.Builder builder = new Board.Builder();
	                        int t = pawn.get(0).getPawnPos();
	                        int w = pawn.get(1).getPawnPos();
	                        builder.setPawn(pawn.get(0));
	                        builder.setPawn(pawn.get(1));
	                        builder.setMoveMaker(Game.get().getGameBoard().actualPlayer().getOpponent().getAlliance());
	                        gameBoard = builder.build();
	                        placeWall();
	                        if(top != null) top.placeWall();
	                        Game.get().moveMadeUpdate(PlayerType.HUMAN);
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                	if(!HorizontalWallPanel.this.isPlaced) {
                		setBackground(Color.yellow);
                	}
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	if(!HorizontalWallPanel.this.isPlaced) {
                		setBackground(Color.gray);
                	}
                }
            });
            validate();

        }
        public void setTop(HorizontalWallPanel top) {
        	this.top = top;
        }
        public void placeWall() {
        	setBackground(Color.yellow);
            HorizontalWallPanel.this.isPlaced = true;
        }
        public void removeWall() {
        	setBackground(Color.gray);
            HorizontalWallPanel.this.isPlaced = false;
        }
        public void placeWallComputer() {
        	placeWall();
        	if(top != null) top.placeWall();
        }
    }
    /**
     * represents the vertical wall.
     * @author larry Fotso larry.
     */
    private class VerticalWallPanel extends JPanel{
        private TilePanel tilePanel;
        private VerticalWallPanel left;
        private Wall wall;
        boolean isPlaced = false;
        
        VerticalWallPanel(TilePanel tilePanel) {
            super(new BorderLayout());

            this.tilePanel = tilePanel;
            this.setVisible(true);
            this.setPreferredSize(verticalWall_Dimension);
            this.setBackground(Color.gray);
            this.wall = new Wall(tilePanel.tile,false);


            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(isLeftMouseButton(e)) {
                        Tile tile = gameBoard.getTile(tilePanel.tileId);
	                    if(Game.get().getGameBoard().actualPlayer().canPlaceWall(tile, false, wall)) {
	                        Game.get().getGameBoard().actualPlayer().placeVerticalWall(tile, wall);
	                        
	                        Board.Builder builder = new Board.Builder();
	                        ArrayList<Pawn> pawn  = (ArrayList<Pawn>) Game.get().getGameBoard().getAllPieces();
	                        int t = pawn.get(0).getPawnPos();
	                        int w = pawn.get(1).getPawnPos();
	                        builder.setPawn(pawn.get(0));
	                        builder.setPawn(pawn.get(1));
	                        builder.setMoveMaker(Game.get().getGameBoard().actualPlayer().getOpponent().getAlliance());
	                        gameBoard = builder.build();
	                       
	                        placeWall();
	                        if(left != null) left.placeWall();
	                        Game.get().moveMadeUpdate(PlayerType.HUMAN);
                        }
                    }
                }
            

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                	if(!VerticalWallPanel.this.isPlaced) {
                		setBackground(Color.yellow);
                	}
                }

                @Override
                public void mouseExited(MouseEvent e) {
                	if(!VerticalWallPanel.this.isPlaced) {
                		setBackground(Color.gray);
                	}
                }
            });
            validate();

        }
        public void setLeft(VerticalWallPanel left) {
        	this.left = left;
        }
        public void removeWall() {
        	setBackground(Color.gray);
        	VerticalWallPanel.this.isPlaced = false;
        }
        public void placeWall() {
        	setBackground(Color.yellow);
        	VerticalWallPanel.this.isPlaced = true;
        }
        public void placeWallComputer() {
        	placeWall();
        	if(left != null) left.placeWall();
        }
    }
    /**
     * helps us to have different board direction.
     */
    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTile){
                return boardTile;
            }

            @Override
             BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTile){
                return Lists.reverse(boardTile);
            }
            @Override
            public BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(final List<TilePanel> boardTile);

         abstract BoardDirection opposite();

    }

    public enum PlayerType {
        HUMAN,
        COMPUTER,
        STRATEGIC;
    }




}
