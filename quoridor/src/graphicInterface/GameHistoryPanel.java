package graphicInterface;

import board.Board;
import board.Move;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * this class represents the move history
 * @author larry Fotso.
 */
public class GameHistoryPanel extends JPanel {
    private final DataModel model ;
    private final JScrollPane scrollPane;
    private static final Dimension DIM = new Dimension(150,400);
    public GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(DIM);
        this.add(scrollPane,BorderLayout.CENTER);
        this.setVisible(true);
    }
    /**
     * building up the move log.
     * @param board
     * @param moveHistory
     */
    public void redo(final Board board , final Game.MoveHistory moveHistory){
        int actualRow = 0;
        this.model.clear();
        
        for(final Move move : moveHistory.getMoves()){
        	if(move.getMovedPawn() != null) {
	            final String moveText = move.toString();
	            if(move.getMovedPawn().getPawnbelong().isRed()){
	                this.model.setValueAt(moveText, actualRow,0);
	            }
	            else if(move.getMovedPawn().getPawnbelong().isYellow()){
	                this.model.setValueAt(moveText,actualRow,1);
	                actualRow++;
	            }
        	}
        }
        if(moveHistory.getMoves().size() > 0){
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();

            if(lastMove.getMovedPawn() != null) {
	            if(lastMove.getMovedPawn().getPawnbelong().isRed()){
	                this.model.setValueAt(moveText,actualRow, 0);
	            }
	            else if(lastMove.getMovedPawn().getPawnbelong().isYellow()){
	                this.model.setValueAt(moveText,actualRow-1,1);
	            }
            }
        }
        final JScrollBar vert = scrollPane.getVerticalScrollBar();
        vert.setValue(vert.getMaximum());// if the number of moves exceeds the scroll panel it will auto advance dow to the last moves

    }
    /**
     * representing the move log.
     */
    private static class DataModel extends DefaultTableModel{
        private  final List<Row> values;
        private  static final String[] Names = {"RED", "YELLOW"};

        private DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount(){
            if(this.values == null) return 0;
            return this.values.size();
        }

        @Override
        public int getColumnCount(){
            return Names.length;
        }

        @Override
        public Object getValueAt(final int row , final int column ){
            final Row actualRow = this.values.get(row);
            if(column == 0){
                return  actualRow.getRedMoves();
            }
            else if(column == 1){
                return actualRow.getYellowMoves();
            }
            return null;
        }
        @Override
        /**
         * changing the value of a give row.
         */
        public void setValueAt(final Object value, final int row ,final  int column){
            final Row actualRow;

            if(this.values.size() <= row){
                actualRow = new Row();
                this.values.add(actualRow);
            }
            else {
                actualRow = this.values.get(row);
            }
            if(column == 0){
                actualRow.setRedMoves((String) value);
                fireTableRowsInserted(row,row);
            }
            else if(column == 1){
                actualRow.setYellowMoves((String)value);
                fireTableCellUpdated(row,column);
            }
        }
        @Override
        public Class<?> getColumnClass(final int col){
            return Move.class;
        }
        @Override
        public String getColumnName(final int col){
            return Names[col];
        }

    }
    /**
     * this class represents the row in move log $
     * @author larry Fotso
     */
    private static class Row {
        private String redMoves;
        private String yellowMoves;

        Row(){

        }

        public String getRedMoves() {
            return redMoves;
        }

        public String getYellowMoves() {
            return yellowMoves;
        }

        public void setRedMoves(String redMoves) {
            this.redMoves = redMoves;
        }

        public void setYellowMoves(String yellowMoves) {
            this.yellowMoves = yellowMoves;
        }
    }



}
