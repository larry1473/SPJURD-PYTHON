package graphicInterface;

import board.Alliance;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * the class helps us with the game setup
 * @author larry Fotso.
 */
public class GameSetup extends JDialog{
    private Game.PlayerType redPlayerType;
    private Game.PlayerType yellowPlayerType;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";
    private static final String STRATEGICAI_TEXT = "Strategic";

    public GameSetup() {
    	redPlayerType =  Game.PlayerType.STRATEGIC;
        yellowPlayerType = Game.PlayerType.STRATEGIC;
    }
    
    public GameSetup(final JFrame frame, final boolean modal) {
        super(frame,modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton redHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton redComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton redStragegicButton = new JRadioButton(STRATEGICAI_TEXT);
        final JRadioButton yellowHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton yellowComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton yellowStrategicButton = new JRadioButton(STRATEGICAI_TEXT);
        redHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHumanButton);
        redGroup.add(redComputerButton);
        redGroup.add(redStragegicButton);
        redHumanButton.setSelected(true);

        final ButtonGroup yellowGroup = new ButtonGroup();
        yellowGroup.add(yellowHumanButton);
        yellowGroup.add(yellowComputerButton);
        yellowGroup.add(yellowStrategicButton);
        yellowHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("Red"));
        myPanel.add(redHumanButton);
        myPanel.add(redComputerButton);
        myPanel.add(redStragegicButton);
        myPanel.add(new JLabel("yellow"));
        myPanel.add(yellowHumanButton);
        myPanel.add(yellowComputerButton);
        myPanel.add(yellowStrategicButton);


        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                redPlayerType = redComputerButton.isSelected() ? Game.PlayerType.COMPUTER : (redStragegicButton.isSelected()) ? Game.PlayerType.STRATEGIC : Game.PlayerType.HUMAN;
                yellowPlayerType = yellowComputerButton.isSelected() ? Game.PlayerType.COMPUTER : (yellowStrategicButton.isSelected()) ? Game.PlayerType.STRATEGIC :  Game.PlayerType.HUMAN;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }
    /**
     * giving the computer an alliance
     * @param player
     * @return
     */
    boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.RED.RED) {
            return getRedPlayerType() == Game.PlayerType.COMPUTER || getRedPlayerType() == Game.PlayerType.STRATEGIC;
        }
        return getYellowPlayerType() == Game.PlayerType.COMPUTER || getYellowPlayerType() == Game.PlayerType.STRATEGIC;
    }
    boolean isStategicAI(final Player player) {
    	if(isAIPlayer(player)) {
    		if(player.getAlliance() == Alliance.RED.RED) {
                return getRedPlayerType() == Game.PlayerType.STRATEGIC;
            }
            return getYellowPlayerType() == Game.PlayerType.STRATEGIC;
    	}
    	return false;
    }

    Game.PlayerType getRedPlayerType() {
        return this.redPlayerType;
    }

    Game.PlayerType getYellowPlayerType() {
        return this.yellowPlayerType;
    }

}
