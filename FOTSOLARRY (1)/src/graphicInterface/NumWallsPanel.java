package graphicInterface;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
/**
 * this panel is used to represent the player numbers of wall.
 * @author larry Fotso.
 */
public class NumWallsPanel extends JPanel {
    private final JPanel northPanel;
    private final  JPanel southPanel;
    private static final  Dimension DIM = new Dimension(40,80);
    private static final EtchedBorder border_Size = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");


    public NumWallsPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(border_Size);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        this.setPreferredSize(DIM);
    }

    public void redo(final Game.MoveHistory moveHistory){
        southPanel.removeAll();
        northPanel.removeAll();
    }


}
