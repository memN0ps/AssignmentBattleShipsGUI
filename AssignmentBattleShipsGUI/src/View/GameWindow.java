package View;

import Controller.*;
import Model.Game;
import Model.Player;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 *  creates the Jframe
 *
 */
public class GameWindow extends JFrame {

    // Private fields
    private PlayerDisplay[] allPlayerDisplays;
    private JFrame form;
    private AttackPhaseHandler myAttackPhaseHandler;
    private DeploymentHandler myDeployer;
    private Game myGame;
    private boolean isComplete = false;


    public GameWindow(StartWindow startWindow) {

        this.myGame = new Game(startWindow.playerName, "Computer");
        this.myDeployer = new DeploymentHandler(this);
        this.myAttackPhaseHandler = new AttackPhaseHandler(this);

        constructGUI();
        startGUI();
    }

    /**
     * 
     * @return the panel display for computer
     */
    public PlayerDisplay getComputerDisplay() {
        return allPlayerDisplays[StartWindow.COMPUTER_INDEX];
    }

    /**
     * 
     * @return player 2
     */
    public Player getComputerPlayer() {
        return myGame.getPlayer2();
    }

    /**
     * 
     * @return the default background for jframe
     */
    public Color getDefaultBackgroundColour() {
        return form.getBackground();
    }

    /**
     * displays the players panels
     * @param player
     * @return the player display and computer display panel
     */
    public PlayerDisplay getDisplayFor(Player player) {
        return (allPlayerDisplays[0].getPlayer() == player) ? this.allPlayerDisplays[0] : this.allPlayerDisplays[1];
    }

    /**
     * displays the enemy panel of the current player
     * @param player
     * @return 
     */
    public PlayerDisplay getEnemyDisplayFor(Player player) {
        return (allPlayerDisplays[0].getPlayer() == player) ? this.allPlayerDisplays[1] : this.allPlayerDisplays[0];
    }

    /**
     * 
     * @return player 1s display panel
     */
    public PlayerDisplay getHumanDisplay() {
        return allPlayerDisplays[StartWindow.HUMAN_INDEX];
    }

    /**
     * 
     * @return player 1
     */
    public Player getHumanPlayer() {
        return myGame.getPlayer1();
    }

    /**
     * 
     * @return the current game
     */
    public Game getGame() {
        return myGame;
    }

    /**
     * 
     * @return true if game has finished
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * contructs JFrame and panels used to make battleApp
     */
    private void constructGUI() {
        final GameWindow gameWindow = this;

        JPanel displayGridsPanel = new JPanel();

        PlayerDisplay humanDisplay, computerDisplay;
        allPlayerDisplays = new PlayerDisplay[2];

        allPlayerDisplays[StartWindow.HUMAN_INDEX] = humanDisplay = new PlayerDisplay(this, this.getHumanPlayer());
        allPlayerDisplays[StartWindow.COMPUTER_INDEX] = computerDisplay = new PlayerDisplay(this, this.getComputerPlayer());

        displayGridsPanel.setLayout(new GridLayout(1, 2));
        displayGridsPanel.add(humanDisplay.getComponent());
        displayGridsPanel.add(computerDisplay.getComponent());

        humanDisplay.setListener(this.myDeployer);
        computerDisplay.setListener(this.myAttackPhaseHandler);

        JPanel buttonsPanel = new JPanel();

        JButton bnAutoDeploy, bnAutoTarget, bnQuit;

        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(bnAutoDeploy = gameWindow.myDeployer.bnAutoDeploy = new JButton("Auto-deploy"));
        buttonsPanel.add(bnAutoTarget = gameWindow.myAttackPhaseHandler.bnAutoTarget = new JButton("Auto-target"));
        buttonsPanel.add(bnQuit = new JButton("Quit"));

        bnAutoDeploy.setEnabled(false);
        bnAutoTarget.setEnabled(false);

        bnAutoDeploy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameWindow.myDeployer.onAutoDeploy();
            }
        });

        bnAutoTarget.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameWindow.myAttackPhaseHandler.onAutoTarget();
            }
        });

        bnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameWindow.onQuit();
            }
        });


        form = new JFrame("BattleApp");

        form.setLayout(new BorderLayout());
        form.add(displayGridsPanel, BorderLayout.CENTER);
        form.add(buttonsPanel, BorderLayout.SOUTH);

        form.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        form.pack();
        form.setResizable(false);
        form.setLocationRelativeTo(null);
        form.setVisible(true);
    }

    /**
     * starts the attackphase handler if deployment is complete
     * @param handler 
     */
    public void onDeployComplete(DeploymentHandler handler) {
        myAttackPhaseHandler.startGUI();
    }

    /**
     * player quits the game closes
     */
    public void onQuit() {
        form.dispose();
    }

    /**
     * final methods before BattleApp closes
     * @param winningPlayer 
     */
    public void onWon(Player winningPlayer) {
        isComplete = true;
        if (!winningPlayer.isComputer()) {
            myGame.startConnection();
        }
        form.dispose();

    }

    /**
     * checks to see if player 1 or player 2 and returns the other player
     * @param player
     * @return 
     */
    public Player otherPlayer(Player player) {
        Player otherPlayerTest;
        if ((otherPlayerTest = myGame.getPlayer1()) != player) {
            return otherPlayerTest;
        }
        return myGame.getPlayer2();
    }

    /**
     * starts the GUI for deploying ships
     */
    public void startGUI() {
        for (PlayerDisplay playerDisplay : this.allPlayerDisplays) {
            playerDisplay.status(playerDisplay.getPlayer().getName());
        }

        myDeployer.startGUI();
    }

    /**
     * redraws the panel
     */
    public void redraw() {
        for (PlayerDisplay playerDisplay : this.allPlayerDisplays) {
            playerDisplay.redraw();
        }
        myDeployer.drawOverlay();
    }
}
